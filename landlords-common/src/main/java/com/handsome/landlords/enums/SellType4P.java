package com.handsome.landlords.enums;

/**
 * 4人斗地主牌型
 */
public enum SellType4P {
    ILLEGAL("非合法"),

    BOMB_FOUR("4炸"),
    BOMB_FIVE("5炸"),
    BOMB_SIX("6炸"),
    BOMB_SEVEN("7炸"),
    BOMB_EIGHT("8炸"),

    KING_BOMB("四大天王"),
    SINGLE("单个牌"),
    DOUBLE("对子牌"),
    /**
     * 三张相同的牌
     */
    THREE("三张牌"),
    THREE_ZONES_DOUBLE("三带对"),
    DOUBLE_STRAIGHT("连对"),
    /**
     * 飞机, 飞机分2飞机，3飞机，4飞机，4飞机....
     */
    THREE_STRAIGHT("飞机"),
    THREE_STRAIGHT_WITH_DOUBLE("飞机带对牌"),
    ;

    private String msg;

    SellType4P(String msg){
        this.msg = msg;
    }

    public final String getMsg() {
        return msg;
    }

    public final void setMsg(String msg) {
        this.msg = msg;
    }

    public static boolean isBomb(SellType4P sellType){
        return sellType == BOMB_FOUR || sellType == BOMB_FIVE || sellType == BOMB_SIX || sellType == BOMB_SEVEN || sellType == BOMB_EIGHT || sellType == KING_BOMB;
    }
}
