package com.handsome.landlords.server.event;

import org.nico.noson.Noson;
import com.handsome.landlords.channel.ChannelUtils;
import com.handsome.landlords.entity.ClientSide;
import com.handsome.landlords.entity.Room;
import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.helper.MapHelper;
import com.handsome.landlords.server.ServerContains;

import java.util.HashMap;
import java.util.Map;

public class ServerEventListener_CODE_GAME_WATCH implements ServerEventListener {

    @Override
    public void call(ClientSide clientSide, String data) {
        Room room = ServerContains.getRoom(Integer.parseInt(data));

        if (room == null) {
            String result = MapHelper.newInstance()
                    .put("roomId", data)
                    .json();

            ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_ROOM_JOIN_FAIL_BY_INEXIST, result);
        } else {
            // 将用户加入到房间中的观战者列表中
            clientSide.setRoomId(room.getId());
            room.getWatcherList().add(clientSide);

            Map<String, String> map = new HashMap<>(16);
            map.put("owner", room.getRoomOwner());
            map.put("status", room.getStatus().toString());
            ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_GAME_WATCH_SUCCESSFUL, Noson.reversal(map));
        }
    }
}
