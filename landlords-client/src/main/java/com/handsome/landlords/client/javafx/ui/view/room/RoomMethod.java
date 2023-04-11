package com.handsome.landlords.client.javafx.ui.view.room;


import com.handsome.landlords.entity.Poker;
import com.handsome.landlords.entity.PokerSell4P;
import com.handsome.landlords.enums.ClientType;
import com.handsome.landlords.client.javafx.ui.view.Method;

import java.util.List;

public interface RoomMethod extends Method {
    boolean isShow();

    void joinRoom();

    void startGame(List<Poker> pokers);

    void gameOver(String winnerName, ClientType winnerType);

    void showPokers(String playerName, List<Poker> pokers);

    void showMessage(String playerName, String message);

    void play(String playerName);

    void robLandlord(String playerName);

    void clearTime(String playerName);

    void refreshPlayPokers(List<Poker> pokers);

    void refreshPrevPlayerPokers(int pokerCount);

    void refreshNextPlayerPokers(int pokerCount);

    void showRobButtons();

    void hideRobButtons();

    void showSurplusPokers(List<Poker> surplusPokers);

    void setLandLord(String landlordName);

    void showPokerPlayButtons();

    void hidePokerPlayButtons();

    void checkPokers(List<Poker> pokers, PokerSell4P hintPokerSell);

    void clearCheckedPokers(List<Poker> pokers);
}
