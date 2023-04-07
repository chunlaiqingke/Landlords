package com.handsome.landlords.client.javafx.ui.view.lobby;

import javafx.scene.layout.Pane;
import com.handsome.landlords.client.javafx.ui.event.ILobbyEvent;
import com.handsome.landlords.client.javafx.ui.view.EventRegister;
import com.handsome.landlords.client.javafx.ui.view.UIObject;

public class LobbyModalEventRegister implements EventRegister {

    private UIObject uiObject;
    private ILobbyEvent lobbyEvent;

    public LobbyModalEventRegister(UIObject uiObject, ILobbyEvent lobbyEvent) {
        this.uiObject = uiObject;
        this.lobbyEvent = lobbyEvent;

        registerEvent();
    }

    @Override
    public void registerEvent() {
        toggleToPVEModalMenu();
        createPVPRoom();
        createPVP4PRoom();
        createPVERoom();
    }

    private void toggleToPVEModalMenu() {
        uiObject.$("pveModalPane",  Pane.class).setOnMouseClicked(e ->
                ((LobbyController.LobbyModalController) uiObject).toggleToPVEModalMenu());
        uiObject.$("pveModalPane",  Pane.class)
                .lookup("Button")
                .setOnMouseClicked(e -> ((LobbyController.LobbyModalController) uiObject).toggleToPVEModalMenu());
    }

    private void createPVPRoom() {
        uiObject.$("pvpModalPane", Pane.class).setOnMouseClicked(e -> {
            lobbyEvent.createPVPRoom();
            uiObject.close();
        });
        uiObject.$("pvpModalPane", Pane.class)
                .lookup("Button")
                .setOnMouseClicked(e -> {
                    lobbyEvent.createPVPRoom();
                    uiObject.close();
                });
    }

    private void createPVP4PRoom() {
        uiObject.$("pvp4pModalPane", Pane.class).setOnMouseClicked(e -> {
            lobbyEvent.createPVP4PRoom();
            uiObject.close();
        });
        uiObject.$("pvp4pModalPane", Pane.class)
                .lookup("Button")
                .setOnMouseClicked(e -> {
                    lobbyEvent.createPVP4PRoom();
                    uiObject.close();
                });
    }

    private static final int SIMPLE_MODAL = 1;
    private static final int NORMAL_MODAL = 2;
    private static final int DIFFICULT_MODAL = 3;

    private void createPVERoom() {
        uiObject.$("simpleModalPane", Pane.class).setOnMouseClicked(e -> {
            lobbyEvent.createPVERoom(SIMPLE_MODAL);
            uiObject.close();
        });
        uiObject.$("simpleModalPane", Pane.class)
                .lookup("Button")
                .setOnMouseClicked(e -> {
                    lobbyEvent.createPVERoom(SIMPLE_MODAL);
                    uiObject.close();
                });

        uiObject.$("normalModalPane", Pane.class).setOnMouseClicked(e -> {
            lobbyEvent.createPVERoom(NORMAL_MODAL);
            uiObject.close();
        });
        uiObject.$("normalModalPane", Pane.class)
                .lookup("Button")
                .setOnMouseClicked(e -> {
                    lobbyEvent.createPVERoom(NORMAL_MODAL);
                    uiObject.close();
                });

        uiObject.$("difficultModalPane", Pane.class).setOnMouseClicked(e -> {
            lobbyEvent.createPVERoom(DIFFICULT_MODAL);
            uiObject.close();
        });
        uiObject.$("difficultModalPane", Pane.class)
                .lookup("Button")
                .setOnMouseClicked(e -> {
                    lobbyEvent.createPVERoom(DIFFICULT_MODAL);
                    uiObject.close();
                });
    }
}
