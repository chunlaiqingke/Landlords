package com.handsome.landlords.client.javafx.ui.view.room.four;

import com.handsome.landlords.client.javafx.entity.CurrentRoomInfo;
import com.handsome.landlords.client.javafx.entity.CurrentRoomInfo4P;
import com.handsome.landlords.client.javafx.event.Room4PEvent;
import com.handsome.landlords.client.javafx.ui.view.EventRegister;
import com.handsome.landlords.client.javafx.ui.view.room.four.Room4PController;
import com.handsome.landlords.client.javafx.util.BeanUtil;
import com.handsome.landlords.entity.Poker;
import javafx.scene.control.Button;

import java.util.List;

public class Room4PEventRegister implements EventRegister {

    private Room4PController uiController;

    private Room4PEvent room4PEvent;

    public Room4PEventRegister(Room4PController uiController, Room4PEvent room4PEvent){
        this.uiController = uiController;
        this.room4PEvent = room4PEvent;
        registerEvent();
    }

    @Override
    public void registerEvent() {
        robLandlord();
        notRobLandlord();
        submitPokers();
        passRound();
        back2Lobby();
        hint();
    }

    private void robLandlord() {
        uiController.$("robButton", Button.class).setOnAction(e -> {
            uiController.hideRobButtons();

            room4PEvent.robLandlord();
        });
    }

    private void notRobLandlord() {
        uiController.$("notRobButton", Button.class).setOnAction(e -> {
            uiController.hideRobButtons();

            room4PEvent.notRobLandlord();
        });
    }

    private void submitPokers() {
        uiController.$("submitButton", Button.class).setOnAction(e -> {
            CurrentRoomInfo4P currentRoomInfo4P = BeanUtil.getBean("currentRoomInfo4P");
            List<Poker> checkedPokers = currentRoomInfo4P.pollCheckedPokers();

            if (checkedPokers.isEmpty()) {
                return;
            }

            // 执行对应的事件
            room4PEvent.submitPokers(checkedPokers);
        });
    }

    private void passRound() {
        uiController.$("passButton", Button.class).setOnAction(e -> {
            room4PEvent.passRound();
        });
    }

    private void hint(){
        uiController.$("hintButton", Button.class).setOnAction(e -> {
            room4PEvent.hint();
        });
    }

    private void back2Lobby() {
        uiController.$("quitButton", Button.class).setOnAction(e -> room4PEvent.exit());
        uiController.$("backLobbyButton", Button.class).setOnAction(e -> room4PEvent.gameOverExit());
    }
}
