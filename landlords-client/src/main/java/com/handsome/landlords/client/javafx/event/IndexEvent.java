package com.handsome.landlords.client.javafx.event;

import com.handsome.landlords.client.javafx.NettyClient;
import com.handsome.landlords.client.javafx.ui.event.IIndexEvent;
import com.handsome.landlords.client.javafx.util.BeanUtil;

public class IndexEvent implements IIndexEvent {

    @Override
    public void connect(String host, int port) throws Exception {
        NettyClient nettyClient = BeanUtil.getBean("nettyClient");

        nettyClient.start(host, port);
    }
}
