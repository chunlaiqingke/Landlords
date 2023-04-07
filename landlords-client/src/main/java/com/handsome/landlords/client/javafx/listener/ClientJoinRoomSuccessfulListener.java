package com.handsome.landlords.client.javafx.listener;


import io.netty.channel.Channel;
import javafx.application.Platform;
import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.client.javafx.ui.view.Method;
import com.handsome.landlords.client.javafx.ui.view.lobby.LobbyController;
import com.handsome.landlords.client.javafx.ui.view.room.RoomController;
import com.handsome.landlords.client.javafx.ui.view.room.RoomMethod;

/**
 * 兼容4P
 */
public class ClientJoinRoomSuccessfulListener extends AbstractClientListener {

    public ClientJoinRoomSuccessfulListener() {
        super(ClientEventCode.CODE_ROOM_JOIN_SUCCESS);
    }

    @Override
    public void handle(Channel channel, String json) {

        Method lobbyMethod = uiService.getMethod(LobbyController.METHOD_NAME);
        RoomMethod roomMethod = (RoomMethod) uiService.getMethod(RoomController.METHOD_NAME);

        Platform.runLater(() -> {
            lobbyMethod.doClose();
            roomMethod.joinRoom();
        });
    }
}
