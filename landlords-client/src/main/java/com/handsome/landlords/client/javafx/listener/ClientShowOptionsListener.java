package com.handsome.landlords.client.javafx.listener;


import io.netty.channel.Channel;
import javafx.application.Platform;
import com.handsome.landlords.channel.ChannelUtils;
import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.enums.ServerEventCode;
import com.handsome.landlords.client.javafx.ui.view.Method;
import com.handsome.landlords.client.javafx.ui.view.lobby.LobbyController;
import com.handsome.landlords.client.javafx.ui.view.login.LoginController;

public class ClientShowOptionsListener extends AbstractClientListener {

    public ClientShowOptionsListener() {
        super(ClientEventCode.CODE_SHOW_OPTIONS);
    }

    @Override
    public void handle(Channel channel, String json) {
        Method loginMethod = uiService.getMethod(LoginController.METHOD_NAME);
        Method lobbyMethod = uiService.getMethod(LobbyController.METHOD_NAME);

        Platform.runLater(() -> {
            loginMethod.doClose();
            lobbyMethod.doShow();
        });

        ChannelUtils.pushToServer(channel, ServerEventCode.CODE_GET_ROOMS, null);
    }
}
