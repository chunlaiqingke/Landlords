package com.handsome.landlords.client.javafx;

import com.alibaba.fastjson.JSONArray;
import com.handsome.landlords.client.javafx.ui.view.room.four.Room4PController;
import com.handsome.landlords.print.SimplePrinter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import com.handsome.landlords.utils.StreamUtils;
import com.handsome.landlords.client.javafx.event.IndexEvent;
import com.handsome.landlords.client.javafx.event.LobbyEvent;
import com.handsome.landlords.client.javafx.event.LoginEvent;
import com.handsome.landlords.client.javafx.event.RoomEvent;
import com.handsome.landlords.client.javafx.ui.UIService;
import com.handsome.landlords.client.javafx.ui.view.index.IndexController;
import com.handsome.landlords.client.javafx.ui.view.index.IndexMethod;
import com.handsome.landlords.client.javafx.ui.view.lobby.LobbyController;
import com.handsome.landlords.client.javafx.ui.view.login.LoginController;
import com.handsome.landlords.client.javafx.ui.view.room.RoomController;
import com.handsome.landlords.client.javafx.util.BeanUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;


public class SimpleClient extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        UIService uiService = new UIService();
        IndexMethod indexMethod = new IndexController(new IndexEvent());
        uiService.addMethods(
                indexMethod,
                new LoginController(new LoginEvent()),
                new LobbyController(new LobbyEvent()),
                new RoomController(new RoomEvent()),
                new Room4PController()
        );
        uiService.getMethod(IndexController.METHOD_NAME).doShow();

        NettyClient nettyClient = new NettyClient(uiService);
        BeanUtil.addBean("nettyClient", nettyClient);

        try {
            List<String> remoteServerAddressList = fetchRemoteServerAddresses();
            Platform.runLater(() -> indexMethod.generateRemoteServerAddressOptions(remoteServerAddressList));
        } catch (IOException e) {
            SimplePrinter.printNotice("获取远程服务器列表失败" + e.getMessage());
            Platform.runLater(() -> indexMethod.setFetchRemoteServerAddressErrorTips());
        }
    }

    private List<String> fetchRemoteServerAddresses() throws IOException {
        String serverInfo = StreamUtils.convertToString(
                new URL("https://raw.githubusercontent.com/ainilili/ratel/master/serverlist.json"));
        return JSONArray.parseArray(serverInfo, String.class);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
