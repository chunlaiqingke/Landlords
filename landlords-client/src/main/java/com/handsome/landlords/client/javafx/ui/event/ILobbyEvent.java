package com.handsome.landlords.client.javafx.ui.event;


import com.handsome.landlords.client.javafx.entity.RoomInfo;

public interface ILobbyEvent {

    void selectPVPModal();

    void selectPVEModal();

    void createPVPRoom();

    void createPVP4PRoom();

    void createPVERoom(int modal);

    void showRooms();

    void joinRoom(RoomInfo roomId);
}
