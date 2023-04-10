package com.handsome.landlords.client.javafx.ui.view.room.four;

import com.handsome.landlords.client.javafx.entity.CurrentRoomInfo;
import com.handsome.landlords.client.javafx.entity.CurrentRoomInfo4P;
import com.handsome.landlords.client.javafx.util.BeanUtil;
import com.handsome.landlords.entity.Poker;
import com.handsome.landlords.enums.PokerLevel;
import com.handsome.landlords.enums.PokerType;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

public class PokerPane4P {

    public static final int MARGIN_LEFT = 30;

    private Poker poker;
    // start at 0
    private int index;
    private int offsetX;

    private Pane pane;

    public PokerPane4P(int index, int offsetX, Poker poker) {
        this(index, offsetX, poker, false);
    }

    public PokerPane4P(int index, int offsetX, Poker poker, boolean isBomb) {
        this.poker = poker;
        this.index = index;
        this.offsetX = offsetX;

        if (PokerLevel.LEVEL_SMALL_KING.equals(poker.getLevel()) ||
                PokerLevel.LEVEL_BIG_KING.equals(poker.getLevel())) {
            createJokerPokerPane(isBomb);
        } else {
            createNormalPokerPane(isBomb);
        }

        pane.setOnMouseClicked(e -> {
            double y = pane.getLayoutY();
            boolean alreadyChecked = y == 5;
            CurrentRoomInfo4P currentRoomInfo4P = BeanUtil.getBean("currentRoomInfo4P");

            // 取消选中
            if (alreadyChecked) {
                pane.setLayoutY(y + 15);
                currentRoomInfo4P.removeUncheckedPoker(poker);
            }
            // 选中
            else {
                pane.setLayoutY(y - 15);
                currentRoomInfo4P.addCheckedPoker(poker);
            }
        });
    }

    private void createNormalPokerPane(boolean isBomb) {
        pane = new Pane();
        pane.getStyleClass().add("horizontal-poker");
        pane.setLayoutX(index * MARGIN_LEFT + offsetX);
        pane.setLayoutY(20);

        Text level = new Text();
        level.getStyleClass().add("level");
        level.setLayoutX(8);
        level.setLayoutY(34);
        level.setText(poker.getLevel().getName());

        Text typeSmall = new Text();
        typeSmall.getStyleClass().add("type-small");
        typeSmall.setLayoutX(8);
        typeSmall.setLayoutY(60);
        typeSmall.setText(poker.getType().getName());

        Text bomb = new Text();
        if(isBomb) {
            bomb.setStyle("-fx-font-size: 16");
            bomb.setLayoutX(8);
            bomb.setLayoutY(110);
            bomb.setText("炸");
        }

        Text typeBig = new Text();
        typeBig.getStyleClass().add("type-big");
        typeBig.setLayoutX(35);
        typeBig.setLayoutY(90);
        typeBig.setText(poker.getType().getName());

        if (PokerType.CLUB.equals(poker.getType()) || PokerType.SPADE.equals(poker.getType())) {
            level.setFill(Paint.valueOf("black"));
            typeSmall.setFill(Paint.valueOf("black"));
            typeBig.setFill(Paint.valueOf("black"));
        } else if (PokerType.DIAMOND.equals(poker.getType()) || PokerType.HEART.equals(poker.getType())) {
            level.setFill(Paint.valueOf("#9c2023"));
            typeSmall.setFill(Paint.valueOf("#9c2023"));
            typeBig.setFill(Paint.valueOf("#9c2023"));
        }

        ObservableList<Node> children = pane.getChildren();
        children.add(level);
        children.add(typeSmall);
        children.add(typeBig);
        if(isBomb) {
            children.add(bomb);
        }
    }

    private void createJokerPokerPane(boolean isBomb) {
        pane = new Pane();
        pane.getStyleClass().add("horizontal-poker");
        pane.setLayoutX(index * MARGIN_LEFT + offsetX);
        pane.setLayoutY(20);

        Text text1 = new Text();
        text1.getStyleClass().add("joker-level");
        text1.setLayoutX(13);
        text1.setLayoutY(26);
        text1.setText("J");

        Text text2 = new Text();
        text2.getStyleClass().add("joker-level");
        text2.setLayoutX(8);
        text2.setLayoutY(40);
        text2.setText("O");

        Text text3 = new Text();
        text3.getStyleClass().add("joker-level");
        text3.setLayoutX(10);
        text3.setLayoutY(54);
        text3.setText("K");

        Text text4 = new Text();
        text4.getStyleClass().add("joker-level");
        text4.setLayoutX(10);
        text4.setLayoutY(68);
        text4.setText("E");

        Text text5 = new Text();
        text5.getStyleClass().add("joker-level");
        text5.setLayoutX(10);
        text5.setLayoutY(82);
        text5.setText("R");

        Text bomb = new Text();
        if(isBomb) {
            bomb.setStyle("-fx-font-size: 16");
            bomb.setLayoutX(8);
            bomb.setLayoutY(110);
            bomb.setText("炸");
        }

        Text logo = new Text();
        logo.setLayoutX(40);
        logo.setLayoutY(100);
        logo.setStyle("-fx-font-size: 28");
        logo.setStyle("-fx-text-fill: silver");
        logo.setText("ratel");

        if (PokerLevel.LEVEL_SMALL_KING.equals(poker.getLevel())) {
            text1.setFill(Paint.valueOf("black"));
            text2.setFill(Paint.valueOf("black"));
            text3.setFill(Paint.valueOf("black"));
            text4.setFill(Paint.valueOf("black"));
            text5.setFill(Paint.valueOf("black"));
        } else if (PokerLevel.LEVEL_BIG_KING.equals(poker.getLevel())) {
            text1.setFill(Paint.valueOf("#9c2023"));
            text2.setFill(Paint.valueOf("#9c2023"));
            text3.setFill(Paint.valueOf("#9c2023"));
            text4.setFill(Paint.valueOf("#9c2023"));
            text5.setFill(Paint.valueOf("#9c2023"));
        }

        ObservableList<Node> children = pane.getChildren();
        children.add(text1);
        children.add(text2);
        children.add(text3);
        children.add(text4);
        children.add(text5);
        if(isBomb) {
            children.add(bomb);
        }
    }

    public Pane getPane() {
        return pane;
    }
}
