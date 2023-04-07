package com.handsome.landlords.client.javafx.listener.four;


import com.alibaba.fastjson.JSONObject;
import com.handsome.landlords.channel.ChannelUtils;
import com.handsome.landlords.client.javafx.listener.AbstractClientListener;
import com.handsome.landlords.client.javafx.ui.view.room.RoomController;
import com.handsome.landlords.client.javafx.ui.view.room.four.Room4PController;
import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.enums.ServerEventCode;
import io.netty.channel.Channel;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class Client4PPokerMismatchListener extends AbstractClientListener {

    public Client4PPokerMismatchListener() {
        super(ClientEventCode.CODE_4P_GAME_POKER_PLAY_MISMATCH);
    }

    @Override
    public void handle(Channel channel, String json) {
        // 出牌不匹配，不允许出牌，即简单的不响应用户操作即可
        JSONObject jsonObject = JSONObject.parseObject(json);
        Room4PController roomController = (Room4PController) uiService.getMethod(Room4PController.METHOD_NAME);
        Platform.runLater(() -> {
            Label tips = ((Label) roomController.$("playerPane", Pane.class).lookup(".error-tips"));
            tips.setVisible(true);
            tips.setText("您需要出" + jsonObject.getIntValue("preCount") + "张牌");
            roomController.delayHidden(tips, 2);
        });

        ChannelUtils.pushToServer(channel, ServerEventCode.CODE_4P_GAME_POKER_PLAY_REDIRECT, null);
    }
}
