package com.handsome.landlords.client.javafx.ui.view.lobby;


import com.handsome.landlords.client.javafx.entity.RoomInfo;
import com.handsome.landlords.client.javafx.ui.view.Method;

import java.util.List;

public interface LobbyMethod extends Method {

    void showRoomList(List<RoomInfo> roomInfoList);

    void joinRoomFail(String message, String commentMessage);

    void popupCreateModal();
}
