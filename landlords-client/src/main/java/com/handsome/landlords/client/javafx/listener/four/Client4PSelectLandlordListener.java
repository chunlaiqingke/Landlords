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
import io.netty.channel.Channel;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client4PSelectLandlordListener extends AbstractClientListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(Client4PSelectLandlordListener.class);

    public Client4PSelectLandlordListener() {
        super(ClientEventCode.CODE_4P_GAME_LANDLORD_ELECT);
    }

    @Override
    public void handle(Channel channel, String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);

        CurrentRoomInfo4P currentRoomInfo4P = BeanUtil.getBean("currentRoomInfo4P");

        //谁先抢地主
        String nextRobLandlordUserNickname = jsonObject.getString("nextClientNickname");

        String prevLandlordUserNickname;
        if(currentRoomInfo4P.getPrevPlayerName().equals(nextRobLandlordUserNickname)) {
            prevLandlordUserNickname = currentRoomInfo4P.getCrossPlayerName();
        } else if (currentRoomInfo4P.getNextPlayerName().equals(nextRobLandlordUserNickname)) {
            prevLandlordUserNickname = currentRoomInfo4P.getPlayer().getNickname();
        } else if (currentRoomInfo4P.getCrossPlayerName().equals(nextRobLandlordUserNickname)) {
            prevLandlordUserNickname = currentRoomInfo4P.getNextPlayerName();
        } else {
            prevLandlordUserNickname = currentRoomInfo4P.getPrevPlayerName();
        }

        RoomMethod method = (RoomMethod) uiService.getMethod(Room4PController.METHOD_NAME);
        Platform.runLater(() -> {
            method.clearTime(prevLandlordUserNickname);
            method.robLandlord(nextRobLandlordUserNickname);
        });
    }
}
