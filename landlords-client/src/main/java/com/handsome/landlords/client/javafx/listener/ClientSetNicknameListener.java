package com.handsome.landlords.client.javafx.listener;

import io.netty.channel.Channel;
import javafx.application.Platform;
import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.client.javafx.ui.view.Method;
import com.handsome.landlords.client.javafx.ui.view.index.IndexController;
import com.handsome.landlords.client.javafx.ui.view.login.LoginController;

public class ClientSetNicknameListener extends AbstractClientListener {

    public ClientSetNicknameListener() {
        super(ClientEventCode.CODE_CLIENT_NICKNAME_SET);
    }

    @Override
    public void handle(Channel channel, String json) {
        Method indexMethod = uiService.getMethod(IndexController.METHOD_NAME);
        Method loginMethod = uiService.getMethod(LoginController.METHOD_NAME);

        Platform.runLater(() -> {
            indexMethod.doClose();
            loginMethod.doShow();
        });
    }
}
