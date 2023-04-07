package com.handsome.landlords.client.javafx.listener;

import io.netty.channel.Channel;
import javafx.application.Platform;
import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.client.javafx.ui.view.Method;
import com.handsome.landlords.client.javafx.ui.view.lobby.LobbyController;
import com.handsome.landlords.client.javafx.ui.view.room.RoomController;
import com.handsome.landlords.client.javafx.ui.view.room.RoomMethod;
import com.handsome.landlords.client.javafx.ui.view.util.AlertUtils;

public class ClientKickListener extends AbstractClientListener {

    public ClientKickListener() {
        super(ClientEventCode.CODE_CLIENT_KICK);
    }

    @Override
    public void handle(Channel channel, String json) {
        Method lobbyMethod = uiService.getMethod(LobbyController.METHOD_NAME);
        RoomMethod roomMethod = (RoomController) uiService.getMethod(RoomController.METHOD_NAME);

        // 防止出现多次触发 CODE_CLIENT_KICK 事件后导致页面显示错误的情况
        if (roomMethod.isShow()) {
            Platform.runLater(() -> {
                AlertUtils.warn("您已经退出房间", "您因长时间未操作，请出已被房间");

                roomMethod.doClose();
                lobbyMethod.doShow();
            });
        }
    }
}
