package com.handsome.landlords.client.javafx.listener.four;

import com.handsome.landlords.client.javafx.listener.AbstractClientListener;
import io.netty.channel.Channel;
import javafx.application.Platform;
import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.client.javafx.ui.view.Method;
import com.handsome.landlords.client.javafx.ui.view.lobby.LobbyController;
import com.handsome.landlords.client.javafx.ui.view.room.four.Room4PController;
import com.handsome.landlords.client.javafx.ui.view.room.RoomMethod;

public class Client4PRoomCreateSuccessfulListener extends AbstractClientListener {
    public Client4PRoomCreateSuccessfulListener() {
        super(ClientEventCode.CODE_4P_ROOM_CREATE_SUCCESS);
    }

    @Override
    public void handle(Channel channel, String json) {
        Method lobbyMethod = uiService.getMethod(LobbyController.METHOD_NAME);
        RoomMethod roomMethod = (RoomMethod) uiService.getMethod(Room4PController.METHOD_NAME);

        Platform.runLater(() -> {
            lobbyMethod.doClose();
            roomMethod.joinRoom();
        });
    }
}
