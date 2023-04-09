package com.handsome.landlords.client.javafx.ui.view.index;


import com.handsome.landlords.print.SimplePrinter;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import com.handsome.landlords.client.javafx.ui.event.IIndexEvent;
import com.handsome.landlords.client.javafx.ui.view.EventRegister;
import com.handsome.landlords.client.javafx.ui.view.UIObject;

public class IndexEventRegister implements EventRegister {

    private UIObject uiObject;
    private IIndexEvent indexEvent;

    public IndexEventRegister(UIObject uiObject, IIndexEvent indexEvent) {
        this.uiObject = uiObject;
        this.indexEvent = indexEvent;

        registerEvent();
    }

    @Override
    public void registerEvent() {
        connectServer();
    }

    private void connectServer() {
        uiObject.$("connectButton", Button.class).setOnAction(e -> {
            String host = uiObject.$("host", TextField.class).getText().trim();
            int port = Integer.parseInt(uiObject.$("port", TextField.class).getText().trim());

            try {
                indexEvent.connect(host, port);
            } catch (Exception ex) {
                SimplePrinter.printNotice(String.format("连接netty服务端(%s:%d)失败", host, port));
                Platform.runLater(() -> ((IndexMethod) uiObject).setConnectServerErrorTips());
            }
        });
    }
}
