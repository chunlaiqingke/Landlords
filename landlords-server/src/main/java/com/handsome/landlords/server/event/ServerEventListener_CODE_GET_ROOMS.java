package com.handsome.landlords.server.event;

import com.handsome.landlords.enums.RoomType;
import org.nico.noson.Noson;
import com.handsome.landlords.channel.ChannelUtils;
import com.handsome.landlords.entity.ClientSide;
import com.handsome.landlords.entity.Room;
import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.helper.MapHelper;
import com.handsome.landlords.server.ServerContains;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ServerEventListener_CODE_GET_ROOMS implements ServerEventListener {

	@Override
	public void call(ClientSide clientSide, String data) {
		List<Map<String, Object>> roomList = new ArrayList<>(ServerContains.getRoomMap().size());
		for (Entry<Integer, Room> entry : ServerContains.getRoomMap().entrySet()) {
			Room room = entry.getValue();
			roomList.add(MapHelper.newInstance()
					.put("roomId", room.getId())
					.put("roomOwner", room.getRoomOwner())
					.put("roomClientCount", room.getClientSideList().size())
					.put("roomType", room.getType())
					.put("roomMaxSize", room.getType() == RoomType.PVP4P ? 4 : 3)
					.map());
		}
		ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_SHOW_ROOMS, Noson.reversal(roomList));
	}

}
