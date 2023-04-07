package com.handsome.landlords.client.javafx.listener;

import io.netty.channel.Channel;
import javafx.application.Platform;
import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.client.javafx.entity.CurrentRoomInfo;
import com.handsome.landlords.client.javafx.entity.User;
import com.handsome.landlords.client.javafx.ui.view.lobby.LobbyController;
import com.handsome.landlords.client.javafx.ui.view.lobby.LobbyMethod;
import com.handsome.landlords.client.javafx.ui.view.room.RoomController;
import com.handsome.landlords.client.javafx.ui.view.room.RoomMethod;
import com.handsome.landlords.client.javafx.util.BeanUtil;

public class ClientExitListener extends AbstractClientListener {

    public ClientExitListener() {
        super(ClientEventCode.CODE_CLIENT_EXIT);
    }

    @Override
    public void handle(Channel channel, String json) {
        RoomMethod roomMethod = (RoomMethod) uiService.getMethod(RoomController.METHOD_NAME);
        LobbyMethod lobbyMethod = (LobbyMethod) uiService.getMethod(LobbyController.METHOD_NAME);

        User user = BeanUtil.getBean("user");
        user.exitRoom();

        CurrentRoomInfo currentRoomInfo = BeanUtil.getBean("currentRoomInfo");
        // 游戏结束后不直接退出，由用户点击退出按钮进行退出
        if (currentRoomInfo != null && currentRoomInfo.isGameOver()) {
            currentRoomInfo.setGameOver(false);
            return;
        }

        Platform.runLater(() -> {
            roomMethod.doClose();
            lobbyMethod.doShow();
        });
    }
}
