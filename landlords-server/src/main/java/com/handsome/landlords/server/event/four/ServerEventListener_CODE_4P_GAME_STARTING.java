package com.handsome.landlords.server.event.four;

import com.handsome.landlords.channel.ChannelUtils;
import com.handsome.landlords.entity.ClientSide;
import com.handsome.landlords.entity.Poker;
import com.handsome.landlords.entity.Room;
import com.handsome.landlords.enums.*;
import com.handsome.landlords.helper.MapHelper;
import com.handsome.landlords.helper.PokerHelper;
import com.handsome.landlords.helper.PokerHelper4P;
import com.handsome.landlords.server.ServerContains;
import com.handsome.landlords.server.event.ServerEventListener;
import com.handsome.landlords.server.robot.RobotEventListener;
import com.handsome.landlords.utils.LastCardsUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ServerEventListener_CODE_4P_GAME_STARTING implements ServerEventListener {

	@Override
	public void call(ClientSide clientSide, String data) {

		Room room = ServerContains.getRoom(clientSide.getRoomId());

		LinkedList<ClientSide> roomClientList = room.getClientSideList();

		// Send the points of poker
		List<List<Poker>> pokersList = PokerHelper4P.distributePoker();
		//设置玩家的牌
		int cursor = 0;
		for (ClientSide client : roomClientList) {
			client.setPokers(pokersList.get(cursor++));
		}
		//地主的8张底牌
		room.setLandlordPokers(pokersList.get(4));

		// Push information about the robber, 随机抢地主的人
		int startGrabIndex = (int) (Math.random() * 4);
		ClientSide startGrabClient = roomClientList.get(startGrabIndex);
		room.setCurrentSellClient(startGrabClient.getId());

		// Push start game messages
		room.setStatus(RoomStatus.STARTING);
		room.setCreateTime(System.currentTimeMillis());
		room.setLastFlushTime(System.currentTimeMillis());

		// Record the first speaker
		room.setFirstSellClient(startGrabClient.getId());
		List<List<Poker>> otherPokers = new ArrayList<>();
		for (ClientSide client : roomClientList) {
			client.setType(ClientType.PEASANT);
			client.setStatus(ClientStatus.PLAYING);
			for(ClientSide otherClient : roomClientList){
				if(otherClient.getId() != client.getId()){
					otherPokers.add(otherClient.getPokers());
				}
			}
			String lastCards = LastCardsUtils.getLastCards(otherPokers);
			otherPokers = new ArrayList<>();
			String result = MapHelper.newInstance()
					.put("roomId", room.getId())
					.put("roomOwner", room.getRoomOwner())
					.put("roomClientCount", room.getClientSideList().size())
					.put("nextClientNickname", startGrabClient.getNickname())
					.put("nextClientId", startGrabClient.getId())
					.put("pokers", client.getPokers())
					.put("lastPokers",lastCards)
					.put("clientOrderList", roomClientList)
					.json();

			if (client.getRole() == ClientRole.PLAYER) {
				ChannelUtils.pushToClient(client.getChannel(), ClientEventCode.CODE_4P_GAME_STARTING, result);
			} else {
				if (startGrabClient.getId() == client.getId()) {
					RobotEventListener.get(ClientEventCode.CODE_4P_GAME_LANDLORD_ELECT).call(client, result);
				}
			}

		}

//		notifyWatcherGameStart(room);
	}


	/**
	 * 通知房间内的观战人员游戏开始
	 *
	 * @param room	房间
	 */
	private void notifyWatcherGameStart(Room room) {
		String result = MapHelper.newInstance()
				.put("player1", room.getClientSideList().getFirst().getNickname())
				.put("player2", room.getClientSideList().getFirst().getNext().getNickname())
				.put("player3", room.getClientSideList().getLast().getNickname())
				.json();
		for (ClientSide clientSide : room.getWatcherList()) {
			ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_GAME_STARTING, result);
		}
	}

}
