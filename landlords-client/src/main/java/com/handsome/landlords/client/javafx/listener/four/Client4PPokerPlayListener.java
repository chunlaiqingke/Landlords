package com.handsome.landlords.client.javafx.listener.four;

import com.handsome.landlords.client.javafx.entity.User;
import com.handsome.landlords.client.javafx.listener.AbstractClientListener;
import com.handsome.landlords.client.javafx.ui.view.room.RoomController;
import com.handsome.landlords.client.javafx.ui.view.room.RoomMethod;
import com.handsome.landlords.client.javafx.ui.view.room.four.Room4PController;
import com.handsome.landlords.client.javafx.util.BeanUtil;
import com.handsome.landlords.enums.ClientEventCode;
import io.netty.channel.Channel;
import javafx.application.Platform;

public class Client4PPokerPlayListener extends AbstractClientListener {

    public Client4PPokerPlayListener() {
        super(ClientEventCode.CODE_4P_GAME_POKER_PLAY);
    }

    @Override
    public void handle(Channel channel, String json) {
        User user = BeanUtil.getBean("user");
        RoomMethod roomMethod = (RoomMethod) uiService.getMethod(Room4PController.METHOD_NAME);

        Platform.runLater(() -> roomMethod.play(user.getNickname()));
    }
}
