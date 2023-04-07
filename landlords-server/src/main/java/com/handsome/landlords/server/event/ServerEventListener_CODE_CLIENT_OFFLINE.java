package com.handsome.landlords.server.event;

import com.handsome.landlords.channel.ChannelUtils;
import com.handsome.landlords.entity.ClientSide;
import com.handsome.landlords.entity.Room;
import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.enums.ClientRole;
import com.handsome.landlords.helper.MapHelper;
import com.handsome.landlords.server.ServerContains;

public class ServerEventListener_CODE_CLIENT_OFFLINE implements ServerEventListener {

	@Override
	public void call(ClientSide clientSide, String data) {

		Room room = ServerContains.getRoom(clientSide.getRoomId());

		if (room == null) {
			ServerContains.CLIENT_SIDE_MAP.remove(clientSide.getId());
			return;
		}

		String result = MapHelper.newInstance()
				.put("roomId", room.getId())
				.put("exitClientId", clientSide.getId())
				.put("exitClientNickname", clientSide.getNickname())
				.json();
		for (ClientSide client : room.getClientSideList()) {
			if (client.getRole() != ClientRole.PLAYER) {
				continue;
			}
			if (client.getId() != clientSide.getId()) {
				ChannelUtils.pushToClient(client.getChannel(), ClientEventCode.CODE_CLIENT_EXIT, result);
				client.init();
			}
		}
		ServerContains.removeRoom(room.getId());
	}
}
