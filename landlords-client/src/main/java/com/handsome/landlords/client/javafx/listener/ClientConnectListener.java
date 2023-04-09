package com.handsome.landlords.client.javafx.listener;

import com.handsome.landlords.print.SimplePrinter;
import io.netty.channel.Channel;
import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.client.javafx.NettyClient;
import com.handsome.landlords.client.javafx.util.BeanUtil;


public class ClientConnectListener extends AbstractClientListener {

    public ClientConnectListener() {
        super(ClientEventCode.CODE_CLIENT_CONNECT);
    }

    @Override
    public void handle(Channel channel, String json) {
        NettyClient nettyClient = BeanUtil.getBean("nettyClient");
        nettyClient.setId(Integer.parseInt(json));

        SimplePrinter.printNotice("当前客户端被分配的id为 " + nettyClient.getId());
    }
}
