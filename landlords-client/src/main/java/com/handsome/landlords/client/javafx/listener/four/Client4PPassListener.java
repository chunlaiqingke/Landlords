package com.handsome.landlords.client.javafx.listener.four;


import com.alibaba.fastjson.JSONObject;
import com.handsome.landlords.channel.ChannelUtils;
import com.handsome.landlords.client.javafx.NettyClient;
import com.handsome.landlords.client.javafx.entity.CurrentRoomInfo;
import com.handsome.landlords.client.javafx.entity.CurrentRoomInfo4P;
import com.handsome.landlords.client.javafx.listener.AbstractClientListener;
import com.handsome.landlords.client.javafx.ui.view.room.RoomController;
import com.handsome.landlords.client.javafx.ui.view.room.RoomMethod;
import com.handsome.landlords.client.javafx.ui.view.room.four.Room4PController;
import com.handsome.landlords.client.javafx.util.BeanUtil;
import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.enums.ServerEventCode;
import io.netty.channel.Channel;
import javafx.application.Platform;

import java.util.Collections;

public class Client4PPassListener extends AbstractClientListener {

    public Client4PPassListener() {
        super(ClientEventCode.CODE_4P_GAME_POKER_PLAY_PASS);
    }

    @Override
    public void handle(Channel channel, String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        RoomMethod method = (RoomMethod) uiService.getMethod(Room4PController.METHOD_NAME);
        CurrentRoomInfo4P currentRoomInfo4P = BeanUtil.getBean("currentRoomInfo4P");

        // 更新当前玩家和出牌信息
        String clientNickname = jsonObject.getString("clientNickname");
        currentRoomInfo4P.setRecentPlayerName(clientNickname);
        currentRoomInfo4P.setRecentPokers(Collections.emptyList());

        // 视图更新
        String nextClientNickname = jsonObject.getString("nextClientNickname");
        Platform.runLater(() -> {
            method.play(nextClientNickname);
            method.showMessage(clientNickname, "不出");
        });

        // 如果下一个出牌的是本玩家进行出牌重定向
        int turnClientId = jsonObject.getIntValue("nextClientId");
        NettyClient nettyClient = BeanUtil.getBean("nettyClient");
        if (turnClientId == nettyClient.getId()) {
            ChannelUtils.pushToServer(channel, ServerEventCode.CODE_4P_GAME_POKER_PLAY_REDIRECT, null);
        }
    }
}
