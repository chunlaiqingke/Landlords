package com.handsome.landlords.server.event.four;


import com.handsome.landlords.channel.ChannelUtils;
import com.handsome.landlords.entity.ClientSide;
import com.handsome.landlords.entity.Room;
import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.enums.ClientRole;
import com.handsome.landlords.enums.ClientType;
import com.handsome.landlords.enums.ServerEventCode;
import com.handsome.landlords.helper.MapHelper;
import com.handsome.landlords.helper.PokerHelper;
import com.handsome.landlords.helper.PokerHelper4P;
import com.handsome.landlords.server.ServerContains;
import com.handsome.landlords.server.event.ServerEventListener;
import com.handsome.landlords.server.robot.RobotEventListener;

public class ServerEventListener_CODE_4P_GAME_LANDLORD_ELECT implements ServerEventListener {

	@Override
	public void call(ClientSide clientSide, String data) {

		Room room = ServerContains.getRoom(clientSide.getRoomId());

		if (room == null) {
			return;
		}
		if (Boolean.parseBoolean(data)) {
			confirmLandlord(clientSide, room);
			return;
		}
		if (clientSide.getNext().getId() == room.getFirstSellClient()) {
			for (ClientSide client : room.getClientSideList()) {
				if (client.getRole() == ClientRole.PLAYER) {
					ChannelUtils.pushToClient(client.getChannel(), ClientEventCode.CODE_GAME_LANDLORD_CYCLE, null);
				}
			}
			ServerEventListener.get(ServerEventCode.CODE_4P_GAME_STARTING).call(clientSide, null);
			return;
		}
		ClientSide turnClientSide = clientSide.getNext();
		room.setCurrentSellClient(turnClientSide.getId());
		String result = MapHelper.newInstance()
				.put("roomId", room.getId())
				.put("roomOwner", room.getRoomOwner())
				.put("roomClientCount", room.getClientSideList().size())
				.put("preClientNickname", clientSide.getNickname())
				.put("nextClientNickname", turnClientSide.getNickname())
				.put("nextClientId", turnClientSide.getId())
				.json();

		for (ClientSide client : room.getClientSideList()) {
			if (client.getRole() == ClientRole.PLAYER) {
				ChannelUtils.pushToClient(client.getChannel(), ClientEventCode.CODE_4P_GAME_LANDLORD_ELECT, result);
				continue;
			}
		}
//		notifyWatcherRobLandlord(room, clientSide);
	}

	public void confirmLandlord(ClientSide clientSide, Room room) {
		clientSide.getPokers().addAll(room.getLandlordPokers());
		PokerHelper4P.sortPoker(clientSide.getPokers());

		int currentClientId = clientSide.getId();
		room.setLandlordId(currentClientId);
		room.setLastSellClient(currentClientId);
		room.setCurrentSellClient(currentClientId);
		clientSide.setType(ClientType.LANDLORD);

		for (ClientSide client : room.getClientSideList()) {
			String result = MapHelper.newInstance()
					.put("roomId", room.getId())
					.put("roomOwner", room.getRoomOwner())
					.put("roomClientCount", room.getClientSideList().size())
					.put("landlordNickname", clientSide.getNickname())
					.put("landlordId", clientSide.getId())
					.put("additionalPokers", room.getLandlordPokers())
					.json();

			if (client.getRole() == ClientRole.PLAYER) {
				ChannelUtils.pushToClient(client.getChannel(), ClientEventCode.CODE_4P_GAME_LANDLORD_CONFIRM, result);
				continue;
			}
		}

//		notifyWatcherConfirmLandlord(room, clientSide);
	}

	/**
	 * 通知房间内的观战人员谁是地主
	 *
	 * @param room	房间
	 * @param landlord 地主
	 */
	private void notifyWatcherConfirmLandlord(Room room, ClientSide landlord) {
		String json = MapHelper.newInstance()
							.put("landlord", landlord.getNickname())
							.put("additionalPokers", room.getLandlordPokers())
							.json();

		for (ClientSide watcher : room.getWatcherList()) {
			ChannelUtils.pushToClient(watcher.getChannel(), ClientEventCode.CODE_GAME_LANDLORD_CONFIRM, json);
		}
	}

	/**
	 * 通知房间内的观战人员抢地主情况
	 *
	 * @param room	房间
	 */
	private void notifyWatcherRobLandlord(Room room, ClientSide player) {
		for (ClientSide watcher : room.getWatcherList()) {
			ChannelUtils.pushToClient(watcher.getChannel(), ClientEventCode.CODE_GAME_LANDLORD_ELECT, player.getNickname());
		}
	}
}
