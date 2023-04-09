package com.handsome.landlords.client.javafx.listener.four;

import com.alibaba.fastjson.JSONObject;
import com.handsome.landlords.client.javafx.NettyClient;
import com.handsome.landlords.client.javafx.listener.AbstractClientListener;
import com.handsome.landlords.client.javafx.listener.ClientListenerUtils;
import com.handsome.landlords.client.javafx.util.BeanUtil;
import com.handsome.landlords.enums.ClientEventCode;
import io.netty.channel.Channel;

public class Client4PPokerPlayRedirectListener extends AbstractClientListener {
    public Client4PPokerPlayRedirectListener() {
        super(ClientEventCode.CODE_4P_GAME_POKER_PLAY_REDIRECT);
    }

    @Override
    public void handle(Channel channel, String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        NettyClient nettyClient = BeanUtil.getBean("nettyClient");
        int sellClientId = jsonObject.getIntValue("sellClientId");

        // 通知下一个玩家出牌
        if (sellClientId == nettyClient.getId()) {
            ClientListenerUtils.getListener(ClientEventCode.CODE_4P_GAME_POKER_PLAY).handle(channel, json);
        }
    }
}
