package com.handsome.landlords.client.javafx.listener.four;

import com.alibaba.fastjson.JSONObject;
import com.handsome.landlords.client.javafx.listener.AbstractClientListener;
import com.handsome.landlords.client.javafx.ui.view.room.four.Room4PController;
import com.handsome.landlords.enums.ClientEventCode;
import io.netty.channel.Channel;
import javafx.application.Platform;

public class Client4PChangeTrusteeListener extends AbstractClientListener {
    public Client4PChangeTrusteeListener() {
        super(ClientEventCode.CODE_4P_CHANGE_TRUSTEE);
    }

    @Override
    public void handle(Channel channel, String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        String nickname = jsonObject.getString("nickname");
        String state = jsonObject.getString("status");
        Room4PController room4PController = (Room4PController)uiService.getMethod(Room4PController.METHOD_NAME);

        Platform.runLater(() -> {
            if("True".equalsIgnoreCase(state)){
                room4PController.showTrustee(nickname);
            } else if("False".equalsIgnoreCase(state)) {
                room4PController.hideTrustee(nickname);
            }
        });
    }
}
