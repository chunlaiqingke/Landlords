package com.handsome.landlords.client.javafx.listener.four;


import com.alibaba.fastjson.JSONObject;
import com.handsome.landlords.client.javafx.entity.CurrentRoomInfo;
import com.handsome.landlords.client.javafx.entity.CurrentRoomInfo4P;
import com.handsome.landlords.client.javafx.listener.AbstractClientListener;
import com.handsome.landlords.client.javafx.ui.view.room.RoomController;
import com.handsome.landlords.client.javafx.ui.view.room.RoomMethod;
import com.handsome.landlords.client.javafx.ui.view.room.four.Room4PController;
import com.handsome.landlords.client.javafx.util.BeanUtil;
import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.enums.ClientType;
import io.netty.channel.Channel;
import javafx.application.Platform;

public class Client4PGameOverListener extends AbstractClientListener {

    public Client4PGameOverListener() {
        super(ClientEventCode.CODE_4P_GAME_OVER);
    }

    @Override
    public void handle(Channel channel, String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        String winnerName = jsonObject.getString("winnerNickname");
        ClientType clientType = jsonObject.getObject("winnerType", ClientType.class);

        CurrentRoomInfo4P currentRoomInfo4P = BeanUtil.getBean("currentRoomInfo4P");
        currentRoomInfo4P.setGameOver(true);

        RoomMethod roomMethod = (RoomMethod) uiService.getMethod(Room4PController.METHOD_NAME);
        Platform.runLater(() -> roomMethod.gameOver(winnerName, clientType));
    }
}
