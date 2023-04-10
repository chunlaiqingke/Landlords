package com.handsome.landlords.client.javafx.ui.view.room.four;

import com.handsome.landlords.client.javafx.entity.CurrentRoomInfo;
import com.handsome.landlords.client.javafx.entity.CurrentRoomInfo4P;
import com.handsome.landlords.client.javafx.entity.User;
import com.handsome.landlords.client.javafx.event.Room4PEvent;
import com.handsome.landlords.client.javafx.ui.view.room.PokerPane;
import com.handsome.landlords.client.javafx.ui.view.room.RoomController;
import com.handsome.landlords.client.javafx.ui.view.room.ShowPokerPane;
import com.handsome.landlords.client.javafx.ui.view.room.SurplusPokerPane;
import com.handsome.landlords.client.javafx.ui.view.room.operator.PlayerPaneOperator;
import com.handsome.landlords.client.javafx.ui.view.util.CountDownTask;
import com.handsome.landlords.client.javafx.util.BeanUtil;
import com.handsome.landlords.helper.BombHelper;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import com.handsome.landlords.entity.Poker;
import com.handsome.landlords.enums.ClientType;
import com.handsome.landlords.client.javafx.ui.view.UIObject;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;

public class Room4PController extends UIObject implements Room4PMethod {

    public static final String METHOD_NAME = "room4P";

    private final String RESOURCE_NAME = "view/room4p.fxml";

    private Room4PEventRegister room4PEventRegister;

    private PlayerPaneOperator prevPlayerPaneOperator;
    private PlayerPaneOperator nextPlayerPaneOperator;
    private PlayerPaneOperator crossPlayerPaneOperator;
    private PlayerPaneOperator playerPaneOperator;

    public Room4PController() throws IOException {
        super();
        root = FXMLLoader.load(getClass().getClassLoader().getResource(RESOURCE_NAME));
        setScene(new Scene(root));
        registerEvent();

        prevPlayerPaneOperator = new PrevPlayerPaneOperator();
        nextPlayerPaneOperator = new NextPlayerPaneOperator();
        crossPlayerPaneOperator = new CrossPlayerPaneOperator();
        playerPaneOperator = new CurrentPlayerPaneOperator();
    }

    @Override
    public void registerEvent() {
        room4PEventRegister = new Room4PEventRegister(this, new Room4PEvent());
    }

    @Override
    public String getName() {
        return METHOD_NAME;
    }

    @Override
    public void doShow() {
        super.show();
    }

    @Override
    public void doClose() {
        super.close();
    }

    @Override
    public boolean isShow() {
        return super.isShowing();
    }

    @Override
    public void joinRoom() {
        doShow();
    }

    @Override
    public void startGame(List<Poker> pokers) {
        // 1，组件状态改变（遮蔽罩隐藏，游戏面板可用状态改变）
        // 2，组件内容填充
        // 3，元素隐藏
        // 4，牌初始化（己方和对方）
        $("waitingPane", Pane.class).setVisible(false);
        $("playingPane", Pane.class).setDisable(false);

        Button robButton = $("robButton", Button.class);
        robButton.setText("抢地主");
        robButton.setVisible(false);

        Button notRobButton = $("notRobButton", Button.class);
        notRobButton.setText("不抢");
        notRobButton.setVisible(false);

        $("prevPlayerPane", Pane.class).lookup(".tips").setVisible(false);
        $("midPlayerPane", Pane.class).lookup(".tips").setVisible(false);
        $("nextPlayerPane", Pane.class).lookup(".tips").setVisible(false);
        $("quitButton", Button.class).setText("退出");

        // 设置玩家名称
        CurrentRoomInfo4P currentRoomInfo4p = BeanUtil.getBean("currentRoomInfo4P");

        $("prevPlayerNickname", Label.class).setText(currentRoomInfo4p.getPrevPlayerName());
        $("midPlayerNickname", Label.class).setText(currentRoomInfo4p.getCrossPlayerName());
        $("nextPlayerNickname", Label.class).setText(currentRoomInfo4p.getNextPlayerName());
        $("playerNickname", Label.class).setText(currentRoomInfo4p.getPlayer().getNickname());

        initPokers(pokers);
    }

    private static final int PER_PLAYER_DEFAULT_POKER_COUNT = 25;
    private static final int SURPLUS_POKER_COUNT = 8;

    private void initPokers(List<Poker> pokers) {
        // 己方牌pane
        refreshPlayPokers(pokers);

        // 上下游牌pane
        $("prevPlayerPokersPane", Pane.class).setVisible(true);
        $("nextPlayerPokersPane", Pane.class).setVisible(true);
        $("midPlayerPokersPane", Pane.class).setVisible(true);
        refreshPrevPlayerPokers(PER_PLAYER_DEFAULT_POKER_COUNT);
        refreshNextPlayerPokers(PER_PLAYER_DEFAULT_POKER_COUNT);
        refreshCrossPlayerPokers(PER_PLAYER_DEFAULT_POKER_COUNT);

        // 底牌
        Pane surplusPokersPane = $("surplusPokersPane", Pane.class);

        surplusPokersPane.getChildren().clear();
        for (int n = 0; n < SURPLUS_POKER_COUNT; n++) {
            surplusPokersPane.getChildren().add(new SurplusPokerPane4P(n).getPane());
        }
    }

    @Override
    public void gameOver(String winnerName, ClientType winnerType) {
        $("playingPane", Pane.class).setDisable(true);
        Pane gameOverPane = $("gameOverPane", Pane.class);
        gameOverPane.setVisible(true);
        Text text = (Text) gameOverPane.lookup("#winnerInfo");
        text.setText(String.format("游戏结束，%s胜利", ClientType.LANDLORD.equals(winnerType) ? "地主" : "农民"));
    }

    @Override
    public void showPokers(String playerName, List<Poker> pokers) {
        getPlayerPaneOperatorByPlayerName(playerName).showPokers(pokers);
    }

    @Override
    public void showMessage(String playerName, String message) {
        getPlayerPaneOperatorByPlayerName(playerName).showMessage(message);
    }

    @Override
    public void play(String playerName) {
        getPlayerPaneOperatorByPlayerName(playerName).play();
    }

    @Override
    public void robLandlord(String playerName) {
        getPlayerPaneOperatorByPlayerName(playerName).robLandlord();
    }

    @Override
    public void clearTime(String playerName) {
        getPlayerPaneOperatorByPlayerName(playerName).clearTimer();
    }

    @Override
    public void refreshPlayPokers(List<Poker> pokers) {
        final int pokersPaneWidth = 870;
        final int pokerPaneWidth = 110;

        // 可能之前有牌，先清理再填充
        Pane pokersPane = $("pokersPane", Pane.class);
        pokersPane.getChildren().clear();

        if (pokers == null || pokers.isEmpty()) {
            return;
        }

        int size = pokers.size();
        // 第一张牌的x轴偏移量
        int firstPokerPaneOffsetX = ((pokersPaneWidth - pokerPaneWidth) - PokerPane4P.MARGIN_LEFT * (size -1)) / 2;

        List<Integer> where = BombHelper.where(pokers);
        for (int i = 0; i < size; i++) {
            boolean isBomb = where.contains(i);
            pokersPane.getChildren().add(new PokerPane4P(i, firstPokerPaneOffsetX, pokers.get(i), isBomb).getPane());
        }
    }

    @Override
    public void refreshPrevPlayerPokers(int pokerCount) {
        Pane prevPlayerPokersPane = $("prevPlayerPokersPane", Pane.class);
        ((Label) prevPlayerPokersPane.lookup(".pokerCount")).setText(String.valueOf(pokerCount));
    }

    @Override
    public void refreshNextPlayerPokers(int pokerCount) {
        Pane nextPlayerPokersPane = $("nextPlayerPokersPane", Pane.class);
        ((Label) nextPlayerPokersPane.lookup(".pokerCount")).setText(String.valueOf(pokerCount));
    }

    @Override
    public void refreshCrossPlayerPokers(int pokerCount) {
        Pane midPlayerPokersPane = $("midPlayerPokersPane", Pane.class);
        ((Label) midPlayerPokersPane.lookup(".pokerCount")).setText(String.valueOf(pokerCount));
    }

    @Override
    public void showRobButtons() {
        $("robButton", Button.class).setVisible(true);
        $("notRobButton", Button.class).setVisible(true);
    }

    @Override
    public void hideRobButtons() {
        $("robButton", Button.class).setVisible(false);
        $("notRobButton", Button.class).setVisible(false);
    }

    @Override
    public void showSurplusPokers(List<Poker> surplusPokers) {
        Pane surplusPokersPane = $("surplusPokersPane", Pane.class);

        surplusPokersPane.getChildren().clear();
        for (int n = 0, size = surplusPokers.size(); n < size; n++) {
            Pane surplusPokerPane = new SurplusPokerPane4P(n, 0, surplusPokers.get(n)).getPane();
            surplusPokerPane.setLayoutX(n * (SurplusPokerPane4P.MARGIN_LEFT));
            surplusPokerPane.setLayoutY(0);

            surplusPokersPane.getChildren().add(surplusPokerPane);
        }
    }

    @Override
    public void setLandLord(String landlordName) {
        // 1，为地主加底牌（重新洗牌）
        CurrentRoomInfo4P currentRoomInfo4P = BeanUtil.getBean("currentRoomInfo4P");
        if (ClientType.LANDLORD.equals(currentRoomInfo4P.getPrevPlayerRole())) {
            currentRoomInfo4P.setPrevPlayerSurplusPokerCount(PER_PLAYER_DEFAULT_POKER_COUNT + SURPLUS_POKER_COUNT);
            currentRoomInfo4P.setNextPlayerSurplusPokerCount(PER_PLAYER_DEFAULT_POKER_COUNT);
            currentRoomInfo4P.setCrossPlayerSurplusPokerCount(PER_PLAYER_DEFAULT_POKER_COUNT);
            refreshPrevPlayerPokers(PER_PLAYER_DEFAULT_POKER_COUNT + SURPLUS_POKER_COUNT);
        } else if (ClientType.LANDLORD.equals(currentRoomInfo4P.getNextPlayerRole())) {
            currentRoomInfo4P.setNextPlayerSurplusPokerCount(PER_PLAYER_DEFAULT_POKER_COUNT + SURPLUS_POKER_COUNT);
            currentRoomInfo4P.setPrevPlayerSurplusPokerCount(PER_PLAYER_DEFAULT_POKER_COUNT);
            currentRoomInfo4P.setCrossPlayerSurplusPokerCount(PER_PLAYER_DEFAULT_POKER_COUNT);
            refreshNextPlayerPokers(PER_PLAYER_DEFAULT_POKER_COUNT + SURPLUS_POKER_COUNT);
        } else if (ClientType.LANDLORD.equals(currentRoomInfo4P.getCrossPlayerRole())) {
            currentRoomInfo4P.setCrossPlayerSurplusPokerCount(PER_PLAYER_DEFAULT_POKER_COUNT + SURPLUS_POKER_COUNT);
            currentRoomInfo4P.setNextPlayerSurplusPokerCount(PER_PLAYER_DEFAULT_POKER_COUNT);
            currentRoomInfo4P.setPrevPlayerSurplusPokerCount(PER_PLAYER_DEFAULT_POKER_COUNT);
            refreshCrossPlayerPokers(PER_PLAYER_DEFAULT_POKER_COUNT + SURPLUS_POKER_COUNT);
        } else {
            User user = BeanUtil.getBean("user");
            refreshPlayPokers(user.getPokers());
        }

        // 2，显示每个人的角色（地主|农民）和姓名
        $("prevPlayerRole", Label.class).setText(ClientType.LANDLORD.equals(currentRoomInfo4P.getPrevPlayerRole()) ? "地主" : "农民");
        $("nextPlayerRole", Label.class).setText(ClientType.LANDLORD.equals(currentRoomInfo4P.getNextPlayerRole()) ? "地主" : "农民");
        $("midPlayerRole", Label.class).setText(ClientType.LANDLORD.equals(currentRoomInfo4P.getCrossPlayerRole()) ? "地主" : "农民");
        $("playerRole", Label.class).setText(ClientType.LANDLORD.equals(currentRoomInfo4P.getPlayer().getRole()) ? "地主" : "农民");
    }

    @Override
    public void showPokerPlayButtons() {
        $("submitButton", Button.class).setVisible(true);
        $("passButton", Button.class).setVisible(true);
    }

    @Override
    public void hidePokerPlayButtons() {
        $("submitButton", Button.class).setVisible(false);
        $("passButton", Button.class).setVisible(false);
    }

    private PlayerPaneOperator getPlayerPaneOperatorByPlayerName(String playerName) {
        CurrentRoomInfo4P currentRoomInfo4P = BeanUtil.getBean("currentRoomInfo4P");

        if (playerName.equals(currentRoomInfo4P.getPrevPlayerName())) {
            return prevPlayerPaneOperator;
        } else if (playerName.equals(currentRoomInfo4P.getNextPlayerName())) {
            return nextPlayerPaneOperator;
        } else if (playerName.equals(currentRoomInfo4P.getCrossPlayerName())) {
            return crossPlayerPaneOperator;
        } else if (playerName.equals(currentRoomInfo4P.getPlayer().getNickname())) {
            return playerPaneOperator;
        }

        throw new IllegalStateException("当前房间没有 " + playerName + " 用户");
    }


    private abstract class AbstractPlayerPaneOperator implements PlayerPaneOperator {
        protected Pane playerShowPane;
        protected Label timer;
        protected Label tips;
        protected Pane playerShowPokersPane;

        protected Pane playerPokersPane;

        protected CountDownTask.CountDownFuture future;

        AbstractPlayerPaneOperator(String parentPaneId) {
            this.playerShowPane = $(parentPaneId, Pane.class);
            this.timer = (Label) playerShowPane.lookup(".timer");
        }

        @Override
        public synchronized void showMessage(String message) {
            clearTimer();

            playerShowPokersPane.getChildren().clear();

            tips.setText(message);
            tips.setVisible(true);
        }

        @Override
        public synchronized void clearTimer() {
            if (future != null && !future.isDone()) {
                future.cancel();
            }
        }

        @Override
        public synchronized void play() {
            playerShowPokersPane.getChildren().clear();

            tips.setVisible(false);

            if (future == null || future.isDone()) {
                CountDownTask task = new CountDownTask(timer, 60,
                        node -> Platform.runLater(() -> hidePokerPlayButtons()),
                        surplusTime -> Platform.runLater(() -> timer.setText(surplusTime.toString())));

                future = task.start();
            }
        }

        @Override
        public synchronized void robLandlord() {
            if (future == null || future.isDone()) {
                CountDownTask task = new CountDownTask(timer, 60,
                        node -> {},
                        surplusTime -> Platform.runLater(() -> timer.setText(surplusTime.toString())));

                future = task.start();
            }
        }

        @Override
        public synchronized void showPokers(List<Poker> pokers) {
            clearTimer();

            tips.setVisible(false);

            renderPokers(pokers);
            refreshPlayerPokers(pokers);
        }

        @Override
        public void clear() {
            clearTimer();

            playerShowPokersPane.getChildren().clear();

            tips.setText("");
            tips.setVisible(false);
        }

        protected abstract void renderPokers(List<Poker> pokers);
        protected abstract void refreshPlayerPokers(List<Poker> pokers);
    }

    private class PrevPlayerPaneOperator extends AbstractPlayerPaneOperator {

        PrevPlayerPaneOperator() {
            super("prevPlayerShowPane");

            this.tips = (Label) playerShowPane.lookup(".tips");
            this.playerShowPokersPane = (Pane) playerShowPane.lookup("#prevPlayerShowPokersPane");

            this.playerPokersPane = $("prevPlayerPokersPane", Pane.class);
        }

        /**
         * 修复出牌时第三行会遮挡第二行的牌的问题
         * @param pokers
         */
        @Override
        public void renderPokers(List<Poker> pokers) {
            final int maxPerRowPokerCount = 8;

            for (int i = 0, size = pokers.size(); i < size; i++) {
                int row = i / maxPerRowPokerCount;
                ShowPokerPane pokerPane = new ShowPokerPane(pokers.get(i));
                pokerPane.setLayout((i % maxPerRowPokerCount) * ShowPokerPane.MARGIN_LEFT, ShowPokerPane.MARGIN_TOP * row);
                playerShowPokersPane.getChildren().add(pokerPane.getPane());
            }
        }

        @Override
        protected void refreshPlayerPokers(List<Poker> pokers) {
            CurrentRoomInfo4P currentRoomInfo4P = BeanUtil.getBean("currentRoomInfo4P");

            refreshPrevPlayerPokers(currentRoomInfo4P.getPrevPlayerSurplusPokerCount());
        }
    }

    private class NextPlayerPaneOperator extends AbstractPlayerPaneOperator {

        NextPlayerPaneOperator() {
            super("nextPlayerShowPane");

            this.tips = (Label) playerShowPane.lookup(".tips");
            this.playerShowPokersPane = (Pane) playerShowPane.lookup("#nextPlayerShowPokersPane");

            this.playerPokersPane = $("nextPlayerPokersPane", Pane.class);
        }

        @Override
        public void renderPokers(List<Poker> pokers) {
            final int maxPerRowPokerCount = 8;
            final int parentPaneWidth = 380;
            final int showPokerPaneWidth = 40;

            /**
             * 修改nextPlayer出牌时牌面覆盖，看不清的问题
             */
            int size = pokers.size();
            int r = size / maxPerRowPokerCount;
            int row = size % maxPerRowPokerCount == 0 ? r : r + 1;

            for(int i = 0; i < row; i++) {
                for(int ir = Math.min(size, (i + 1) * maxPerRowPokerCount) - 1; ir >= i * maxPerRowPokerCount; ir--) {
                    ShowPokerPane pokerPane = new ShowPokerPane(pokers.get(ir));
                    int layoutX = parentPaneWidth - (showPokerPaneWidth + ShowPokerPane.MARGIN_LEFT * (ir % maxPerRowPokerCount));
                    pokerPane.setLayout(layoutX, ShowPokerPane.MARGIN_TOP * i);
                    playerShowPokersPane.getChildren().add(pokerPane.getPane());
                }
            }
        }

        @Override
        protected void refreshPlayerPokers(List<Poker> pokers) {
            CurrentRoomInfo4P currentRoomInfo4P = BeanUtil.getBean("currentRoomInfo4P");

            refreshNextPlayerPokers(currentRoomInfo4P.getNextPlayerSurplusPokerCount());
        }
    }

    private class CrossPlayerPaneOperator extends AbstractPlayerPaneOperator {

        CrossPlayerPaneOperator() {
            super("midPlayerShowPane");

            this.tips = (Label) playerShowPane.lookup(".tips");
            this.playerShowPokersPane = (Pane) playerShowPane.lookup("#midPlayerShowPokersPane");

            this.playerPokersPane = $("midPlayerPokersPane", Pane.class);
        }

        @Override
        public void renderPokers(List<Poker> pokers) {
            final int maxPerRowPokerCount = 8;
            final int parentPaneWidth = 400;

            for (int i = 0, size = pokers.size(); i < size; i++) {
                int row = i / maxPerRowPokerCount;
                ShowPokerPane pokerPane = new ShowPokerPane(pokers.get(i));
                pokerPane.setLayout(parentPaneWidth + (i % maxPerRowPokerCount) * ShowPokerPane.MARGIN_LEFT, ShowPokerPane.MARGIN_TOP * row);
                playerShowPokersPane.getChildren().add(pokerPane.getPane());
            }
        }

        @Override
        protected void refreshPlayerPokers(List<Poker> pokers) {
            CurrentRoomInfo4P currentRoomInfo4P = BeanUtil.getBean("currentRoomInfo4P");

            refreshNextPlayerPokers(currentRoomInfo4P.getNextPlayerSurplusPokerCount());
        }
    }

    private class CurrentPlayerPaneOperator extends AbstractPlayerPaneOperator {

        CurrentPlayerPaneOperator() {
            super("playerShowPane");

            this.tips = (Label) playerShowPane.lookup(".primary-tips");
            this.playerShowPokersPane = (Pane) playerShowPane.lookup("#playerShowPokersPane");

            this.playerPokersPane = $("pokersPane", Pane.class);
        }

        @Override
        public void showMessage(String message) {
            hidePokerPlayButtons();

            super.showMessage(message);
        }

        @Override
        public synchronized void robLandlord() {
            showRobButtons();

            if (future == null || future.isDone()) {
                CountDownTask task = new CountDownTask(timer, 60,
                        node -> hideRobButtons(),
                        surplusTime -> Platform.runLater(() -> timer.setText(surplusTime.toString())));

                future = task.start();
            }
        }

        @Override
        public void showPokers(List<Poker> pokers) {
            hidePokerPlayButtons();

            super.showPokers(pokers);
        }

        @Override
        public void play() {
            super.play();

            showPokerPlayButtons();
        }

        @Override
        public void clear() {
            super.clear();

            hidePokerPlayButtons();
        }

        @Override
        public void renderPokers(List<Poker> pokers) {
            final int parentPaneWidth = 870;
            final int showPokerPaneWidth = 30;
            int size = pokers.size();
            int offset = (parentPaneWidth - (showPokerPaneWidth + ShowPokerPane.MARGIN_LEFT * (size - 1))) / 2;

            for (int i = 0; i < size; i++) {
                ShowPokerPane pokerPane = new ShowPokerPane(pokers.get(i));
                pokerPane.setLayout(offset + i * ShowPokerPane.MARGIN_LEFT, 0);
                playerShowPokersPane.getChildren().add(pokerPane.getPane());
            }
        }

        @Override
        protected void refreshPlayerPokers(List<Poker> pokers) {
            User user = BeanUtil.getBean("user");

            refreshPlayPokers(user.getPokers());
        }
    }
}
