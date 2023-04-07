package com.handsome.landlords.server.event;

import org.nico.noson.Noson;
import com.handsome.landlords.channel.ChannelUtils;
import com.handsome.landlords.entity.ClientSide;
import com.handsome.landlords.entity.Room;
import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.enums.ClientStatus;
import com.handsome.landlords.enums.RoomStatus;
import com.handsome.landlords.enums.RoomType;
import com.handsome.landlords.server.ServerContains;

public class ServerEventListener_CODE_ROOM_CREATE implements ServerEventListener {

	@Override
	public void call(ClientSide clientSide, String data) {

		Room room = new Room(ServerContains.getServerId());
		room.setStatus(RoomStatus.WAIT);
		room.setType(RoomType.PVP);
		room.setRoomOwner(clientSide.getNickname());
		room.getClientSideMap().put(clientSide.getId(), clientSide);
		room.getClientSideList().add(clientSide);
		room.setCurrentSellClient(clientSide.getId());
		room.setCreateTime(System.currentTimeMillis());
		room.setLastFlushTime(System.currentTimeMillis());

		clientSide.setRoomId(room.getId());
		ServerContains.addRoom(room);

		clientSide.setStatus(ClientStatus.NO_READY);

		ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_ROOM_CREATE_SUCCESS, Noson.reversal(room));
	}
}
