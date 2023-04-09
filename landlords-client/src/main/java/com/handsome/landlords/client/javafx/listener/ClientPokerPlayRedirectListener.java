package com.handsome.landlords.client.javafx.listener;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.client.javafx.NettyClient;
import com.handsome.landlords.client.javafx.util.BeanUtil;

public class ClientPokerPlayRedirectListener extends AbstractClientListener {

    public ClientPokerPlayRedirectListener() {
        super(ClientEventCode.CODE_GAME_POKER_PLAY_REDIRECT);
    }

    @Override
    public void handle(Channel channel, String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        NettyClient nettyClient = BeanUtil.getBean("nettyClient");
        int sellClientId = jsonObject.getIntValue("sellClientId");

        // 通知下一个玩家出牌
        if (sellClientId == nettyClient.getId()) {
            ClientListenerUtils.getListener(ClientEventCode.CODE_GAME_POKER_PLAY).handle(channel, json);
        }
    }
}
