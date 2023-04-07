package com.handsome.landlords.client.javafx.listener.four;

import com.alibaba.fastjson.JSONObject;
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
import io.netty.channel.Channel;
import javafx.application.Platform;

import java.util.List;

public class Client4PShowPokersListener extends AbstractClientListener {

    public Client4PShowPokersListener() {
        super(ClientEventCode.CODE_4P_SHOW_POKERS);
    }

    @Override
    public void handle(Channel channel, String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        String clientNickname = jsonObject.getString("clientNickname");
        List<Poker> sellPokerList = jsonObject.getJSONArray("pokers").toJavaList(Poker.class);

        // 更新当前玩家和出牌信息
        CurrentRoomInfo4P currentRoomInfo4P = BeanUtil.getBean("currentRoomInfo4P");
        User user = currentRoomInfo4P.getPlayer();

        if (user.getNickname().equals(clientNickname)) {
            user.removePokers(sellPokerList);
        } else if (currentRoomInfo4P.getPrevPlayerName().equals(clientNickname)) {
            currentRoomInfo4P.setPrevPlayerSurplusPokerCount(currentRoomInfo4P.getPrevPlayerSurplusPokerCount() - sellPokerList.size());
        } else if (currentRoomInfo4P.getNextPlayerName().equals(clientNickname)) {
            currentRoomInfo4P.setNextPlayerSurplusPokerCount(currentRoomInfo4P.getNextPlayerSurplusPokerCount() - sellPokerList.size());
        } else if (currentRoomInfo4P.getCrossPlayerName().equals(clientNickname)) {
            currentRoomInfo4P.setCrossPlayerSurplusPokerCount(currentRoomInfo4P.getCrossPlayerSurplusPokerCount() - sellPokerList.size());
        }

        currentRoomInfo4P.setRecentPlayerName(clientNickname);
        currentRoomInfo4P.setRecentPokers(sellPokerList);

        // 视图更新
        String nextPlayerName = jsonObject.getString("sellClinetNickname");
        RoomMethod method = (RoomMethod) uiService.getMethod(Room4PController.METHOD_NAME);
        Platform.runLater(() -> {
            method.showPokers(clientNickname, sellPokerList);
            if (nextPlayerName != null) {
                method.play(nextPlayerName);
            }
        });
    }
}
