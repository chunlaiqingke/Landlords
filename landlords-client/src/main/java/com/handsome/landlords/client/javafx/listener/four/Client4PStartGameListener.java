package com.handsome.landlords.client.javafx.listener.four;

import com.alibaba.fastjson.JSONObject;
import com.handsome.landlords.client.javafx.entity.CurrentRoomInfo4P;
import com.handsome.landlords.client.javafx.entity.User;
import com.handsome.landlords.client.javafx.listener.AbstractClientListener;
import com.handsome.landlords.client.javafx.listener.ClientListenerUtils;
import com.handsome.landlords.client.javafx.ui.view.Method;
import com.handsome.landlords.client.javafx.ui.view.lobby.LobbyController;
import com.handsome.landlords.client.javafx.ui.view.room.four.Room4PController;
import com.handsome.landlords.client.javafx.ui.view.room.RoomMethod;
import com.handsome.landlords.client.javafx.ui.view.util.AlertUtils;
import com.handsome.landlords.client.javafx.util.BeanUtil;
import com.handsome.landlords.entity.ClientSide;
import com.handsome.landlords.entity.Poker;
import com.handsome.landlords.enums.ClientEventCode;
import io.netty.channel.Channel;
import javafx.application.Platform;

import java.util.List;

public class Client4PStartGameListener extends AbstractClientListener {

    public Client4PStartGameListener() {
        super(ClientEventCode.CODE_4P_GAME_STARTING);
    }

    @Override
    public void handle(Channel channel, String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);

        // 设置用户信息和当前对局房间信息
        User user = BeanUtil.getBean("user");
        user.addPokers(jsonObject.getJSONArray("pokers").toJavaList(Poker.class));
        user.joinRoom(jsonObject.getIntValue("roomId"));

        CurrentRoomInfo4P currentRoomInfo4P = new CurrentRoomInfo4P(jsonObject.getIntValue("roomId"),
                jsonObject.getString("roomOwner"));
        currentRoomInfo4P.setGameOver(false);
        currentRoomInfo4P.setPlayer(user);
        currentRoomInfo4P.setPrevPlayerSurplusPokerCount(25);
        currentRoomInfo4P.setNextPlayerSurplusPokerCount(25);
        currentRoomInfo4P.setCrossPlayerSurplusPokerCount(25);
        BeanUtil.addBean("currentRoomInfo4P", currentRoomInfo4P);

        // 计算出玩家的顺序
        List<ClientSide> clientOrderList;
        try {
            clientOrderList = jsonObject.getJSONArray("clientOrderList").toJavaList(ClientSide.class);
        } catch (Exception e) {
            // 服务器不支持提醒
            Platform.runLater(() ->
                    AlertUtils.error("服务器版本过低", "服务器版本过低，不支持当前客户端!\n请连接至最新版本的服务端进行游戏。")
            );
            return;
        }

        ClientSide clientSide = clientOrderList.stream().filter(c -> user.getNickname().equals(c.getNickname())).findFirst().get();
        currentRoomInfo4P.setPrevPlayerName(clientSide.getPre().getNickname());
        currentRoomInfo4P.setNextPlayerName(clientSide.getNext().getNickname());
        currentRoomInfo4P.setCrossPlayerName(clientSide.getNext().getNext().getNickname());

        // 更新试图
        Method lobbyMethod = uiService.getMethod(LobbyController.METHOD_NAME);
        RoomMethod roomMethod = (RoomMethod) uiService.getMethod(Room4PController.METHOD_NAME);

        Platform.runLater(() -> {
            // 客户端加入房间时，可能没有触发 joinRoomSuccessful 的事件
            // 导致视图未正常切换，判断视图是否切换，否则进行试图切换
            if (!roomMethod.isShow()) {
                lobbyMethod.doClose();
                roomMethod.joinRoom();
            }

            roomMethod.startGame(user.getPokers());
        });

        // 触发抢地主(CODE_GAME_LANDLORD_ELECT)事件
        ClientListenerUtils.getListener(ClientEventCode.CODE_4P_GAME_LANDLORD_ELECT).handle(channel, json);
    }
}
