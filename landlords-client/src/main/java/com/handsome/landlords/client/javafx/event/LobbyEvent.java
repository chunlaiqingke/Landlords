package com.handsome.landlords.client.javafx.event;


import com.handsome.landlords.client.javafx.entity.RoomInfo;
import com.handsome.landlords.enums.RoomType;
import io.netty.channel.Channel;
import com.handsome.landlords.channel.ChannelUtils;
import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.enums.ServerEventCode;
import com.handsome.landlords.client.javafx.listener.ClientListener;
import com.handsome.landlords.client.javafx.listener.ClientListenerUtils;
import com.handsome.landlords.client.javafx.ui.event.ILobbyEvent;
import com.handsome.landlords.client.javafx.util.BeanUtil;


public class LobbyEvent implements ILobbyEvent {

    @Override
    public void selectPVPModal() {
        ClientListener listener = ClientListenerUtils.getListener(ClientEventCode.CODE_SHOW_OPTIONS_PVP);

        listener.handle(BeanUtil.getBean("channel"), ".");
    }

    @Override
    public void selectPVEModal() {
        ClientListener listener = ClientListenerUtils.getListener(ClientEventCode.CODE_SHOW_OPTIONS_PVE);

        listener.handle(BeanUtil.getBean("channel"), ".");
    }

    @Override
    public void createPVPRoom() {
        Channel channel = BeanUtil.getBean("channel");

        ChannelUtils.pushToServer(channel, ServerEventCode.CODE_ROOM_CREATE, null);
    }

    @Override
    public void createPVP4PRoom() {
        Channel channel = BeanUtil.getBean("channel");
        ChannelUtils.pushToServer(channel, ServerEventCode.CODE_4P_ROOM_CREATE, null);
    }

    @Override
    public void createPVERoom(int modal) {
        Channel channel = BeanUtil.getBean("channel");

        ChannelUtils.pushToServer(channel, ServerEventCode.CODE_ROOM_CREATE_PVE, String.valueOf(modal));
    }

    @Override
    public void showRooms() {
        Channel channel = BeanUtil.getBean("channel");

        ChannelUtils.pushToServer(channel, ServerEventCode.CODE_GET_ROOMS, null);
    }

    @Override
    public void joinRoom(RoomInfo room) {
        Channel channel = BeanUtil.getBean("channel");
        if(RoomType.valueOf(room.getRoomType()) == RoomType.PVP4P) {
            ChannelUtils.pushToServer(channel, ServerEventCode.CODE_4P_ROOM_JOIN, String.valueOf(room.getRoomId()));
        } else {
            ChannelUtils.pushToServer(channel, ServerEventCode.CODE_ROOM_JOIN, String.valueOf(room.getRoomId()));
        }
    }
}
