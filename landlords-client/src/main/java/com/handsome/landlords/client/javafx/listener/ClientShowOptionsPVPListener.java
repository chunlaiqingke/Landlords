package com.handsome.landlords.client.javafx.listener;

import io.netty.channel.Channel;
import com.handsome.landlords.enums.ClientEventCode;


public class ClientShowOptionsPVPListener extends AbstractClientListener {

    public ClientShowOptionsPVPListener() {
        super(ClientEventCode.CODE_SHOW_OPTIONS_PVP);
    }

    @Override
    public void handle(Channel channel, String json) {}
}
