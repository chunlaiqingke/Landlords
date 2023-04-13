package com.handsome.landlords.client.javafx.listener.four;

import com.handsome.landlords.client.javafx.entity.CurrentRoomInfo4P;
import com.handsome.landlords.client.javafx.entity.User;
import com.handsome.landlords.client.javafx.listener.AbstractClientListener;
import com.handsome.landlords.client.javafx.listener.ClientListenerUtils;
import com.handsome.landlords.client.javafx.ui.view.room.four.Room4PController;
import com.handsome.landlords.client.javafx.util.BeanUtil;
import com.handsome.landlords.entity.Poker;
import com.handsome.landlords.entity.PokerSell4P;
import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.enums.SellType4P;
import io.netty.channel.Channel;
import javafx.application.Platform;

import java.util.List;

public class Client4PHintAutoPokerPlayListener extends AbstractClientListener {
    public Client4PHintAutoPokerPlayListener() {
        super(ClientEventCode.CODE_4P_HINT_AUTO_POKER_PLAY);
    }

    @Override
    public void handle(Channel channel, String json) {
        Room4PController room4PController = (Room4PController) uiService.getMethod(Room4PController.METHOD_NAME);

        User user = BeanUtil.getBean("user");
        CurrentRoomInfo4P currentRoomInfo4P = BeanUtil.getBean("currentRoomInfo4P");
        List<Poker> recentPokers = currentRoomInfo4P.getRecentPokers();
        PokerSell4P hintPokerSell = user.hint(recentPokers);
        if(!user.isTrusted()) {
            ClientListenerUtils.getListener(ClientEventCode.CODE_4P_GAME_POKER_PLAY).handle(channel, json);
        } else {
            Platform.runLater(() -> {
                //pass
                if(hintPokerSell.getSellType4P() == SellType4P.ILLEGAL) {
                    room4PController.hintPass(user.getNickname());
                } else {
                    room4PController.checkPokers(user.getPokers(), hintPokerSell);
                    room4PController.hintSubmit(user.getNickname());
                }
            });
        }
    }
}
