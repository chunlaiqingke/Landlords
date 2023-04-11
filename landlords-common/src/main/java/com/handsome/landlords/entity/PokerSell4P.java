package com.handsome.landlords.entity;

import com.handsome.landlords.enums.SellType4P;
import com.handsome.landlords.helper.PokerHelper4P;

import java.util.List;

public class PokerSell4P {
    private int score;

    private SellType4P sellType4P;

    private List<Poker> sellPokers;

    /**
     * 在当前手牌中的索引
     */
    private List<Integer> positions;

    /**
     * 牌大小，比如三代二，三张牌的level决定了三代二的大小
     */
    private int coreLevel;

    /**
     * 连对是几对，顺子是几顺
     */
    private int straightCount;

    public PokerSell4P(SellType4P sellType4P, List<Poker> sellPokers, int coreLevel) {
        this.sellType4P = sellType4P;
        this.score = PokerHelper4P.parseScore(sellType4P, coreLevel);
        this.sellPokers = sellPokers;
        this.coreLevel = coreLevel;
        this.straightCount = -1;
    }

    public PokerSell4P(SellType4P sellType4P, List<Poker> sellPokers, int coreLevel, int straightCount) {
        this.sellType4P = sellType4P;
        this.score = PokerHelper4P.parseScore(sellType4P, coreLevel);
        this.sellPokers = sellPokers;
        this.coreLevel = coreLevel;
        this.straightCount = straightCount;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public SellType4P getSellType4P() {
        return sellType4P;
    }

    public void setSellType4P(SellType4P sellType4P) {
        this.sellType4P = sellType4P;
    }

    public List<Poker> getSellPokers() {
        return sellPokers;
    }

    public void setSellPokers(List<Poker> sellPokers) {
        this.sellPokers = sellPokers;
    }

    public int getCoreLevel() {
        return coreLevel;
    }

    public void setCoreLevel(int coreLevel) {
        this.coreLevel = coreLevel;
    }

    public int getStraightCount() {
        return straightCount;
    }

    public void setStraightCount(int straightCount) {
        this.straightCount = straightCount;
    }

    public List<Integer> getPositions() {
        return positions;
    }

    public void setPositions(List<Integer> positions) {
        this.positions = positions;
    }

    public void addPositions(List<Integer> positions) {
        if(this.positions == null) {
            this.positions = positions;
        } else {
            this.positions.addAll(positions);
        }
    }

    public static boolean matchSellType(PokerSell4P last, PokerSell4P current){
        if(last == null) {
            return true;
        }
        //牌型不同的情况下能返回true只有炸弹
        if(last.getSellType4P() != null && last.getSellType4P() == current.getSellType4P()) {
            //如果不是炸弹，牌数不一样的话，也不匹配，比如三飞机和2飞机
            return SellType4P.isBomb(current.getSellType4P()) || last.getSellPokers().size() == current.getSellPokers().size();
        }
        return SellType4P.isBomb(current.getSellType4P());
    }
}
