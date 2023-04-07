package com.handsome.landlords.client.javafx.listener;

import io.netty.channel.Channel;
import javafx.application.Platform;
import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.client.javafx.entity.User;
import com.handsome.landlords.client.javafx.ui.view.room.RoomController;
import com.handsome.landlords.client.javafx.ui.view.room.RoomMethod;
import com.handsome.landlords.client.javafx.util.BeanUtil;

public class ClientPokerPlayListener extends AbstractClientListener {

    public ClientPokerPlayListener() {
        super(ClientEventCode.CODE_GAME_POKER_PLAY);
    }

    @Override
    public void handle(Channel channel, String json) {
        User user = BeanUtil.getBean("user");
        RoomMethod roomMethod = (RoomMethod) uiService.getMethod(RoomController.METHOD_NAME);

        Platform.runLater(() -> roomMethod.play(user.getNickname()));
    }
}
