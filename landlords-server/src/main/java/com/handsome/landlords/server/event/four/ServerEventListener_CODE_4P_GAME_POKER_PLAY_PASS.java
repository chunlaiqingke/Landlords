package com.handsome.landlords.server.event.four;

import com.handsome.landlords.channel.ChannelUtils;
import com.handsome.landlords.entity.ClientSide;
import com.handsome.landlords.entity.Room;
import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.enums.ClientRole;
import com.handsome.landlords.helper.MapHelper;
import com.handsome.landlords.server.ServerContains;
import com.handsome.landlords.server.event.ServerEventListener;
import com.handsome.landlords.server.robot.RobotEventListener;

public class ServerEventListener_CODE_4P_GAME_POKER_PLAY_PASS implements ServerEventListener {

	@Override
	public void call(ClientSide clientSide, String data) {
		Room room = ServerContains.getRoom(clientSide.getRoomId());

		if(room != null) {
			if(room.getCurrentSellClient() == clientSide.getId()) {
				if(clientSide.getId() != room.getLastSellClient()) {
					ClientSide turnClient = clientSide.getNext();

					room.setCurrentSellClient(turnClient.getId());

					for(ClientSide client: room.getClientSideList()) {
						String result = MapHelper.newInstance()
								.put("clientId", clientSide.getId())
								.put("clientNickname", clientSide.getNickname())
								.put("nextClientId", turnClient.getId())
								.put("nextClientNickname", turnClient.getNickname())
								.json();
						if(client.getRole() == ClientRole.PLAYER) {
							ChannelUtils.pushToClient(client.getChannel(), ClientEventCode.CODE_4P_GAME_POKER_PLAY_PASS, result);
						}else {
//							if(client.getId() == turnClient.getId()) {
//								RobotEventListener.get(ClientEventCode.CODE_GAME_POKER_PLAY).call(turnClient, data);
//							}
						}
					}

//					notifyWatcherPlayPass(room, clientSide);
				}else {
					ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_4P_GAME_POKER_PLAY_CANT_PASS, null);
				}
			}else {
				ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_4P_GAME_POKER_PLAY_ORDER_ERROR, null);
			}
		}else {
//			ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_ROOM_PLAY_FAIL_BY_INEXIST, null);
		}
	}

	/**
	 * 通知观战者玩家不出牌
	 *
	 * @param room	房间
	 * @param player	不出牌的玩家
	 */
	private void notifyWatcherPlayPass(Room room, ClientSide player) {
		for (ClientSide watcher : room.getWatcherList()) {
			ChannelUtils.pushToClient(watcher.getChannel(), ClientEventCode.CODE_GAME_POKER_PLAY_PASS, player.getNickname());
		}
	}
}
