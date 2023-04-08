package com.handsome.landlords.server.event.four;

import com.handsome.landlords.channel.ChannelUtils;
import com.handsome.landlords.entity.ClientSide;
import com.handsome.landlords.entity.Room;
import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.enums.ClientRole;
import com.handsome.landlords.enums.RoomType;
import com.handsome.landlords.helper.MapHelper;
import com.handsome.landlords.server.ServerContains;
import com.handsome.landlords.server.event.ServerEventListener;

/**
 * 兼容4P
 */
public class ServerEventListener_CODE_4P_CLIENT_EXIT implements ServerEventListener {

	private static final Object locked = new Object();

	@Override
	public void call(ClientSide clientSide, String data) {
		synchronized (locked){
			Room room = ServerContains.getRoom(clientSide.getRoomId());
			if (room == null) {
				return;
			}
			String result = MapHelper.newInstance()
					.put("roomId", room.getId())
					.put("exitClientId", clientSide.getId())
					.put("exitClientNickname", clientSide.getNickname())
					.json();
			for (ClientSide client : room.getClientSideList()) {
				if (client.getRole() == ClientRole.PLAYER) {
					ChannelUtils.pushToClient(client.getChannel(), ClientEventCode.CODE_4P_CLIENT_EXIT, result);
					client.init();
				}
			}

			notifyWatcherClientExit(room, clientSide);
			ServerContains.removeRoom(room.getId());
		}
	}

	/**
	 * 通知所有观战者玩家退出游戏
	 *
	 * @param room 房间
	 * @param player 退出游戏玩家
	 */
	private void notifyWatcherClientExit(Room room, ClientSide player) {
		for (ClientSide watcher : room.getWatcherList()) {
			ChannelUtils.pushToClient(watcher.getChannel(), ClientEventCode.CODE_CLIENT_EXIT, player.getNickname());
		}
	}
}
