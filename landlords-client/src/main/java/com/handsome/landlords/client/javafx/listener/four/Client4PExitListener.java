package com.handsome.landlords.client.javafx.listener.four;

import com.handsome.landlords.client.javafx.entity.CurrentRoomInfo;
import com.handsome.landlords.client.javafx.entity.CurrentRoomInfo4P;
import com.handsome.landlords.client.javafx.entity.User;
import com.handsome.landlords.client.javafx.listener.AbstractClientListener;
import com.handsome.landlords.client.javafx.ui.view.lobby.LobbyController;
import com.handsome.landlords.client.javafx.ui.view.lobby.LobbyMethod;
import com.handsome.landlords.client.javafx.ui.view.room.RoomController;
import com.handsome.landlords.client.javafx.ui.view.room.RoomMethod;
import com.handsome.landlords.client.javafx.ui.view.room.four.Room4PController;
import com.handsome.landlords.client.javafx.util.BeanUtil;
import com.handsome.landlords.enums.ClientEventCode;
import io.netty.channel.Channel;
import javafx.application.Platform;

public class Client4PExitListener extends AbstractClientListener {

    public Client4PExitListener() {
        super(ClientEventCode.CODE_4P_CLIENT_EXIT);
    }

    @Override
    public void handle(Channel channel, String json) {
        RoomMethod roomMethod = (RoomMethod) uiService.getMethod(Room4PController.METHOD_NAME);
        LobbyMethod lobbyMethod = (LobbyMethod) uiService.getMethod(LobbyController.METHOD_NAME);

        User user = BeanUtil.getBean("user");
        user.exitRoom();

        CurrentRoomInfo4P currentRoomInfo4P = BeanUtil.getBean("currentRoomInfo4P");
        // 游戏结束后不直接退出，由用户点击退出按钮进行退出
        if (currentRoomInfo4P != null && currentRoomInfo4P.isGameOver()) {
            currentRoomInfo4P.setGameOver(false);
            return;
        }

        Platform.runLater(() -> {
            roomMethod.doClose();
            lobbyMethod.doShow();
        });
    }
}
