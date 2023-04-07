package com.handsome.landlords.client.javafx.ui.view.index;

import com.handsome.landlords.client.javafx.ui.view.Method;

import java.util.List;

public interface IndexMethod extends Method {
    void generateRemoteServerAddressOptions(List<String> remoteServerAddressList);

    void setFetchRemoteServerAddressErrorTips();

    void setConnectServerErrorTips();
}
