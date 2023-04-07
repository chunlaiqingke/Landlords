package com.handsome.landlords.client.javafx.ui.view.room.four;

import com.handsome.landlords.entity.Poker;
import com.handsome.landlords.enums.PokerLevel;
import com.handsome.landlords.enums.PokerType;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

public class SurplusPokerPane4P {
    public static final double MARGIN_LEFT = 30;

    private int index;

    private Pane pane;

    private Poker poker;
    // start at 0
    private int offsetX;

    public SurplusPokerPane4P(int index) {
        this.index = index;

        pane = new Pane();
        pane.getStyleClass().add("surplus-poker");
        pane.setLayoutX(index * (MARGIN_LEFT));
    }

    public SurplusPokerPane4P(int index, int offsetX, Poker poker) {
        this.index = index;
        this.offsetX = offsetX;
        this.poker = poker;

        if (PokerLevel.LEVEL_SMALL_KING.equals(poker.getLevel()) ||
                PokerLevel.LEVEL_BIG_KING.equals(poker.getLevel())) {
            createJokerPokerPane();
        } else {
            createNormalPokerPane();
        }
    }

    private void createNormalPokerPane() {
        pane = new Pane();
        pane.getStyleClass().add("surplus-horizontal-poker");
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


        if (PokerType.CLUB.equals(poker.getType()) || PokerType.SPADE.equals(poker.getType())) {
            level.setFill(Paint.valueOf("black"));
            typeSmall.setFill(Paint.valueOf("black"));
        } else if (PokerType.DIAMOND.equals(poker.getType()) || PokerType.HEART.equals(poker.getType())) {
            level.setFill(Paint.valueOf("#9c2023"));
            typeSmall.setFill(Paint.valueOf("#9c2023"));
        }

        ObservableList<Node> children = pane.getChildren();
        children.add(level);
        children.add(typeSmall);
    }

    private void createJokerPokerPane() {
        pane = new Pane();
        pane.getStyleClass().add("surplus-horizontal-poker");
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
    }

    public Pane getPane() {
        return pane;
    }
}
