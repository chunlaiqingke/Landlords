package com.handsome.landlords.client.javafx.listener;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import javafx.application.Platform;
import com.handsome.landlords.entity.Poker;
import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.client.javafx.entity.CurrentRoomInfo;
import com.handsome.landlords.client.javafx.entity.User;
import com.handsome.landlords.client.javafx.ui.view.room.RoomController;
import com.handsome.landlords.client.javafx.ui.view.room.RoomMethod;
import com.handsome.landlords.client.javafx.util.BeanUtil;

import java.util.List;

public class ClientShowPokersListener extends AbstractClientListener {

    public ClientShowPokersListener() {
        super(ClientEventCode.CODE_SHOW_POKERS);
    }

    @Override
    public void handle(Channel channel, String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        String clientNickname = jsonObject.getString("clientNickname");
        List<Poker> sellPokerList = jsonObject.getJSONArray("pokers").toJavaList(Poker.class);

        // 更新当前玩家和出牌信息
        CurrentRoomInfo currentRoomInfo = BeanUtil.getBean("currentRoomInfo");
        User user = currentRoomInfo.getPlayer();

        if (user.getNickname().equals(clientNickname)) {
            user.removePokers(sellPokerList);
        } else if (currentRoomInfo.getPrevPlayerName().equals(clientNickname)) {
            currentRoomInfo.setPrevPlayerSurplusPokerCount(currentRoomInfo.getPrevPlayerSurplusPokerCount() - sellPokerList.size());
        } else if (currentRoomInfo.getNextPlayerName().equals(clientNickname)) {
            currentRoomInfo.setNextPlayerSurplusPokerCount(currentRoomInfo.getNextPlayerSurplusPokerCount() - sellPokerList.size());
        }

        currentRoomInfo.setRecentPlayerName(clientNickname);
        currentRoomInfo.setRecentPokers(sellPokerList);

        // 视图更新
        String nextPlayerName = jsonObject.getString("sellClinetNickname");
        RoomMethod method = (RoomMethod) uiService.getMethod(RoomController.METHOD_NAME);
        Platform.runLater(() -> {
            method.showPokers(clientNickname, sellPokerList);
            if (nextPlayerName != null) {
                method.play(nextPlayerName);
            }
        });
    }
}
