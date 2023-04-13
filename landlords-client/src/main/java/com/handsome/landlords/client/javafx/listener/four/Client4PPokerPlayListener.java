package com.handsome.landlords.client.javafx.listener.four;

import com.handsome.landlords.client.javafx.entity.User;
import com.handsome.landlords.client.javafx.listener.AbstractClientListener;
import com.handsome.landlords.client.javafx.listener.ClientListenerUtils;
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
        Room4PController room4PController = (Room4PController) uiService.getMethod(Room4PController.METHOD_NAME);

        if(user.isTrusted()) {
            ClientListenerUtils.getListener(ClientEventCode.CODE_4P_HINT_AUTO_POKER_PLAY).handle(channel, json);
        } else {
            Platform.runLater(() -> {
                room4PController.refreshPlayPokers(user.getPokers());
                room4PController.play(user.getNickname());
            });
        }
    }
}
