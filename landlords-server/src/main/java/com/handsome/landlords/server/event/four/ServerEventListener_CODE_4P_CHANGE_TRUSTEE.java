package com.handsome.landlords.server.event.four;

import com.handsome.landlords.channel.ChannelUtils;
import com.handsome.landlords.entity.ClientSide;
import com.handsome.landlords.entity.Room;
import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.enums.ClientRole;
import com.handsome.landlords.helper.MapHelper;
import com.handsome.landlords.server.ServerContains;
import com.handsome.landlords.server.event.ServerEventListener;

public class ServerEventListener_CODE_4P_CHANGE_TRUSTEE implements ServerEventListener {

    @Override
    public void call(ClientSide client, String data) {
        Room room = ServerContains.getRoom(client.getRoomId());
        String nickname = client.getNickname();
        String result = MapHelper.newInstance()
                .put("nickname", nickname)
                .put("status", data)
                .json();
        for (ClientSide clientSide : room.getClientSideList()) {
            if (clientSide.getRole() == ClientRole.PLAYER) {
                ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_4P_CHANGE_TRUSTEE, result);
            }
        }
    }
}
