package com.handsome.landlords.client.javafx.event;


import io.netty.channel.Channel;
import com.handsome.landlords.channel.ChannelUtils;
import com.handsome.landlords.enums.ServerEventCode;
import com.handsome.landlords.client.javafx.NettyClient;
import com.handsome.landlords.client.javafx.entity.User;
import com.handsome.landlords.client.javafx.ui.event.ILoginEvent;
import com.handsome.landlords.client.javafx.util.BeanUtil;

public class LoginEvent implements ILoginEvent {

    @Override
    public void setNickname(String nickname) {
        NettyClient nettyClient = BeanUtil.getBean("nettyClient");
        nettyClient.setUsername(nickname);

        User user = new User(nickname);
        BeanUtil.addBean("user", user);

        Channel channel = BeanUtil.getBean("channel");

        ChannelUtils.pushToServer(channel, ServerEventCode.CODE_CLIENT_NICKNAME_SET, nickname);
    }
}
