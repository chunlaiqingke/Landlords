package com.handsome.landlords.client.javafx.ui.event;

import com.handsome.landlords.entity.Poker;

import java.util.List;

public interface IRoomEvent {
    void robLandlord();

    void notRobLandlord();

    void submitPokers(List<Poker> pokerList);

    void passRound();

    void exit();

    void gameOverExit();
}
