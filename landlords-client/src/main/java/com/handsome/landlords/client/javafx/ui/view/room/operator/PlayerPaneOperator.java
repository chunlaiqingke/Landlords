package com.handsome.landlords.client.javafx.ui.view.room.operator;

import com.handsome.landlords.entity.Poker;

import java.util.List;

public interface PlayerPaneOperator {

    void robLandlord();

    void clearTimer();

    void showPokers(List<Poker> pokers);

    void showMessage(String message);

    void play();

    void clear();

    void hintSubmit();

    void hintPass();

    void showTrustee();

    void hideTrustee();
}
