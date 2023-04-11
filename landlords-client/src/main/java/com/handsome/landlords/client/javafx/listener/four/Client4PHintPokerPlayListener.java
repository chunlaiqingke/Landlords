package com.handsome.landlords.client.javafx.listener.four;

import com.handsome.landlords.client.javafx.entity.CurrentRoomInfo4P;
import com.handsome.landlords.client.javafx.entity.User;
import com.handsome.landlords.client.javafx.listener.AbstractClientListener;
import com.handsome.landlords.client.javafx.ui.view.room.RoomMethod;
import com.handsome.landlords.client.javafx.ui.view.room.four.Room4PController;
import com.handsome.landlords.client.javafx.util.BeanUtil;
import com.handsome.landlords.entity.Poker;
import com.handsome.landlords.entity.PokerSell4P;
import com.handsome.landlords.enums.ClientEventCode;
import io.netty.channel.Channel;
import javafx.application.Platform;

import java.util.List;

public class Client4PHintPokerPlayListener extends AbstractClientListener {


    public Client4PHintPokerPlayListener() {
        super(ClientEventCode.CODE_4P_HINT_POKER_PLAY);
    }

    @Override
    public void handle(Channel channel, String json) {
        RoomMethod roomMethod = (RoomMethod) uiService.getMethod(Room4PController.METHOD_NAME);

        User user = BeanUtil.getBean("user");
        CurrentRoomInfo4P currentRoomInfo4P = BeanUtil.getBean("currentRoomInfo4P");
        List<Poker> recentPokers = currentRoomInfo4P.getRecentPokers();
        PokerSell4P hintPokerSell = user.hint(recentPokers);
        Platform.runLater(() -> {
            roomMethod.checkPokers(user.getPokers(), hintPokerSell);
        });
    }
}
