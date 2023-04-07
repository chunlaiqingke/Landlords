package com.handsome.landlords.client.javafx.listener;


import com.alibaba.fastjson.JSONArray;
import io.netty.channel.Channel;
import javafx.application.Platform;
import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.client.javafx.entity.RoomInfo;
import com.handsome.landlords.client.javafx.ui.view.lobby.LobbyController;
import com.handsome.landlords.client.javafx.ui.view.lobby.LobbyMethod;

import java.util.List;

public class ClientShowRoomsListner extends AbstractClientListener {

    public ClientShowRoomsListner() {
        super(ClientEventCode.CODE_SHOW_ROOMS);
    }

    @Override
    public void handle(Channel channel, String json) {
        List<RoomInfo> rooms = JSONArray.parseArray(json, RoomInfo.class);

        LobbyMethod method = (LobbyMethod) uiService.getMethod(LobbyController.METHOD_NAME);
        Platform.runLater(() -> method.showRoomList(rooms));
    }
}
