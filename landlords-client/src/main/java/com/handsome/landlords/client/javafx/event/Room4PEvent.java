package com.handsome.landlords.client.javafx.event;

import com.alibaba.fastjson.JSONObject;
import com.handsome.landlords.channel.ChannelUtils;
import com.handsome.landlords.client.javafx.listener.ClientListenerUtils;
import com.handsome.landlords.client.javafx.ui.event.IRoom4PEvent;
import com.handsome.landlords.client.javafx.ui.event.IRoomEvent;
import com.handsome.landlords.client.javafx.util.BeanUtil;
import com.handsome.landlords.entity.Poker;
import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.enums.ServerEventCode;
import io.netty.channel.Channel;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Room4PEvent implements IRoomEvent,IRoom4PEvent {
    @Override
    public void robLandlord() {
        Channel channel = BeanUtil.getBean("channel");

        ChannelUtils.pushToServer(channel, ServerEventCode.CODE_4P_GAME_LANDLORD_ELECT, "TRUE");
    }

    @Override
    public void notRobLandlord() {
        Channel channel = BeanUtil.getBean("channel");

        ChannelUtils.pushToServer(channel, ServerEventCode.CODE_4P_GAME_LANDLORD_ELECT, "FALSE");
    }

    @Override
    public void submitPokers(List<Poker> pokerList) {
        Channel channel = BeanUtil.getBean("channel");

        String[] chars = pokerList.stream()
                .sorted(Comparator.comparingInt(poker -> poker.getLevel().getLevel()))
                .map(p -> {
                    String name = p.getLevel().getName();
                    // 10 实际出牌值为 0
                    return name.length() > 1 ? name.substring(1, 2) : name;
                })
                .collect(Collectors.toList())
                .toArray(new String[] {});
        ChannelUtils.pushToServer(channel, ServerEventCode.CODE_4P_GAME_POKER_PLAY, JSONObject.toJSONString(chars));
    }

    @Override
    public void passRound() {
        Channel channel = BeanUtil.getBean("channel");

        ChannelUtils.pushToServer(channel, ServerEventCode.CODE_4P_GAME_POKER_PLAY_PASS, null);
    }

    @Override
    public void exit() {
        Channel channel = BeanUtil.getBean("channel");

        ChannelUtils.pushToServer(channel, ServerEventCode.CODE_CLIENT_EXIT, null);
    }

    @Override
    public void gameOverExit() {
        ClientListenerUtils.getListener(ClientEventCode.CODE_4P_CLIENT_EXIT).handle(BeanUtil.getBean("channel"), null);
    }
}
