package com.handsome.landlords.client.javafx.ui.view.room;


import javafx.scene.control.Button;
import com.handsome.landlords.entity.Poker;
import com.handsome.landlords.client.javafx.entity.CurrentRoomInfo;
import com.handsome.landlords.client.javafx.ui.event.IRoomEvent;
import com.handsome.landlords.client.javafx.ui.view.EventRegister;
import com.handsome.landlords.client.javafx.ui.view.UIObject;
import com.handsome.landlords.client.javafx.util.BeanUtil;

import java.util.List;

public class RoomEventRegister implements EventRegister {

    private UIObject uiObject;
    private IRoomEvent roomEvent;

    public RoomEventRegister(UIObject uiObject, IRoomEvent roomEvent) {
        this.uiObject = uiObject;
        this.roomEvent = roomEvent;

        registerEvent();
    }

    @Override
    public void registerEvent() {
        robLandlord();
        notRobLandlord();
        submitPokers();
        passRound();
        back2Lobby();
    }

    private void robLandlord() {
        uiObject.$("robButton", Button.class).setOnAction(e -> {
            RoomController roomController = (RoomController) uiObject;
            roomController.hideRobButtons();

            roomEvent.robLandlord();
        });
    }

    private void notRobLandlord() {
        uiObject.$("notRobButton", Button.class).setOnAction(e -> {
            RoomController roomController = (RoomController) uiObject;
            roomController.hideRobButtons();

            roomEvent.notRobLandlord();
        });
    }

    private void submitPokers() {
        uiObject.$("submitButton", Button.class).setOnAction(e -> {
            CurrentRoomInfo currentRoomInfo = BeanUtil.getBean("currentRoomInfo");
            List<Poker> checkedPokers = currentRoomInfo.pollCheckedPokers();

            if (checkedPokers.isEmpty()) {
                return;
            }

            // 执行对应的事件
            roomEvent.submitPokers(checkedPokers);
        });
    }

    private void passRound() {
        uiObject.$("passButton", Button.class).setOnAction(e -> {
            roomEvent.passRound();
        });
    }

    private void back2Lobby() {
        uiObject.$("quitButton", Button.class).setOnAction(e -> roomEvent.exit());
        uiObject.$("backLobbyButton", Button.class).setOnAction(e -> roomEvent.gameOverExit());
    }
}
