package com.handsome.landlords.client.javafx.listener;

import io.netty.channel.Channel;
import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.client.javafx.ui.UIService;


public interface ClientListener {
    void handle(Channel channel, String json);

    ClientEventCode getCode();

    void setUIService(UIService uiService);
}
