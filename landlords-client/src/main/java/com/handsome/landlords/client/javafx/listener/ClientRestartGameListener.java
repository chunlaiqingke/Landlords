package com.handsome.landlords.client.javafx.listener;

import io.netty.channel.Channel;
import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.client.javafx.entity.User;
import com.handsome.landlords.client.javafx.util.BeanUtil;

public class ClientRestartGameListener extends AbstractClientListener {

    public ClientRestartGameListener() {
        super(ClientEventCode.CODE_GAME_LANDLORD_CYCLE);
    }

    @Override
    public void handle(Channel channel, String json) {
        // 无人抢地主，重新发牌抢地主，清理当前牌
        User user = BeanUtil.getBean("user");
        user.clearPokers();
    }
}
