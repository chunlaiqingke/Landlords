package com.handsome.landlords.client.javafx.listener;


import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import javafx.application.Platform;
import com.handsome.landlords.channel.ChannelUtils;
import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.enums.ServerEventCode;
import com.handsome.landlords.client.javafx.NettyClient;
import com.handsome.landlords.client.javafx.entity.CurrentRoomInfo;
import com.handsome.landlords.client.javafx.ui.view.room.RoomController;
import com.handsome.landlords.client.javafx.ui.view.room.RoomMethod;
import com.handsome.landlords.client.javafx.util.BeanUtil;

import java.util.Collections;

public class ClientPassListener extends AbstractClientListener {

    public ClientPassListener() {
        super(ClientEventCode.CODE_GAME_POKER_PLAY_PASS);
    }

    @Override
    public void handle(Channel channel, String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        RoomMethod method = (RoomMethod) uiService.getMethod(RoomController.METHOD_NAME);
        CurrentRoomInfo currentRoomInfo = BeanUtil.getBean("currentRoomInfo");

        // 更新当前玩家和出牌信息
        String clientNickname = jsonObject.getString("clientNickname");
        currentRoomInfo.setRecentPlayerName(clientNickname);
        currentRoomInfo.setRecentPokers(Collections.emptyList());

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
            ChannelUtils.pushToServer(channel, ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT, null);
        }
    }
}
