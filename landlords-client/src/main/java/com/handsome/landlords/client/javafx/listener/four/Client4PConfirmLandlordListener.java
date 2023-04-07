package com.handsome.landlords.client.javafx.listener.four;

import com.alibaba.fastjson.JSONObject;
import com.handsome.landlords.channel.ChannelUtils;
import com.handsome.landlords.client.javafx.entity.CurrentRoomInfo;
import com.handsome.landlords.client.javafx.entity.CurrentRoomInfo4P;
import com.handsome.landlords.client.javafx.entity.User;
import com.handsome.landlords.client.javafx.listener.AbstractClientListener;
import com.handsome.landlords.client.javafx.ui.view.room.RoomController;
import com.handsome.landlords.client.javafx.ui.view.room.RoomMethod;
import com.handsome.landlords.client.javafx.ui.view.room.four.Room4PController;
import com.handsome.landlords.client.javafx.util.BeanUtil;
import com.handsome.landlords.entity.Poker;
import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.enums.ServerEventCode;
import io.netty.channel.Channel;
import javafx.application.Platform;

import java.util.List;

public class Client4PConfirmLandlordListener extends AbstractClientListener {

    public Client4PConfirmLandlordListener() {
        super(ClientEventCode.CODE_4P_GAME_LANDLORD_CONFIRM);
    }

    @Override
    public void handle(Channel channel, String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        String landlordName = jsonObject.getString("landlordNickname");
        List<Poker> surplusPokerList = jsonObject.getJSONArray("additionalPokers").toJavaList(Poker.class);

        // 给地主发底牌
        User user = BeanUtil.getBean("user");
        if (landlordName.equals(user.getNickname())) {
            user.addPokers(surplusPokerList);
        }

        // 设置玩家的角色
        CurrentRoomInfo4P currentRoomInfo4P = BeanUtil.getBean("currentRoomInfo4P");
        currentRoomInfo4P.setLandlord(landlordName);

        // 视图更新
        RoomMethod method = (RoomMethod) uiService.getMethod(Room4PController.METHOD_NAME);

        Platform.runLater(() -> {
            method.clearTime(landlordName);
            method.showSurplusPokers(surplusPokerList);
            method.hideRobButtons();
            method.setLandLord(landlordName);
        });

        // 重定向玩家
        ChannelUtils.pushToServer(channel, ServerEventCode.CODE_4P_GAME_POKER_PLAY_REDIRECT, null);
    }
}
