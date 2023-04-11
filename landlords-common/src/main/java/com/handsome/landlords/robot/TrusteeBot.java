package com.handsome.landlords.robot;

import com.handsome.landlords.entity.Poker;
import com.handsome.landlords.entity.PokerSell4P;
import com.handsome.landlords.enums.SellType4P;
import com.handsome.landlords.helper.PokerHelper4P;

import java.util.*;
import java.util.stream.Collectors;

import static com.handsome.landlords.enums.SellType4P.ILLEGAL;

/**
 * 托管机器人
 */
public class TrusteeBot {
    private int currentPokerType;

    private List<PokerSell4P> allBiggerPokerType;

    /**
     * 提示出牌,返回出牌索引
     */
    public PokerSell4P hint(List<Poker> pokers, List<Poker> lastSellPokers){
        //如果是空，则是自己牌权, 出最小的单张
        if(lastSellPokers == null || lastSellPokers.isEmpty()) {
            PokerSell4P pokerSell4P = PokerHelper4P.checkPokerType(Collections.singletonList(pokers.get(pokers.size() - 1)));
            pokerSell4P.setPositions(Collections.singletonList(pokers.size() - 1));
            return pokerSell4P;
        }
        PokerSell4P pokerSell4P = PokerHelper4P.checkPokerType(lastSellPokers);
        //如果上家出的牌是炸弹，我们只能用炸弹去打
        if(allBiggerPokerType == null) {
            allBiggerPokerType = findAllBiggerPokerType(pokers, pokerSell4P);
        }
        if(allBiggerPokerType.size() == 0) {
            return new PokerSell4P(ILLEGAL, null, -1);
        } else {
            if(currentPokerType < allBiggerPokerType.size()) {
                PokerSell4P pokerSell4P1 = allBiggerPokerType.get(currentPokerType);
                currentPokerType ++;
                return pokerSell4P1;
            } else {
                return allBiggerPokerType.get(allBiggerPokerType.size() - 1);
            }
        }
    }

    /**
     * 寻找所有大于这个的牌
     * @param pokers
     * @param pokerSell4P
     * @return
     */
    public List<PokerSell4P> findAllBiggerPokerType(List<Poker> pokers, PokerSell4P pokerSell4P){

        List<PokerSell4P> res = new ArrayList<>();
        List<PokerSell4P> bombs = findBombPokerType(pokers, pokerSell4P);
        if(!SellType4P.isBomb(pokerSell4P.getSellType4P())) {
            List<PokerSell4P> generals = findGeneralPokerType(pokers, pokerSell4P);
            res.addAll(generals);
            res.addAll(bombs);
            return res;
        } else {
            return bombs;
        }
    }

    /**
     * 找出炸弹
     * @param pokers 当前手里的牌
     * @param pokerSell4P 需要出的牌型
     * @return
     */
    private List<PokerSell4P> findBombPokerType(List<Poker> pokers, PokerSell4P pokerSell4P){
        List<PokerSell4P> bombs = PokerHelper4P.findAllBombPokers(pokers);
        //如果不是炸弹，那所有的炸弹都要返回
        if(!SellType4P.isBomb(pokerSell4P.getSellType4P())) {
            return bombs;
        } else {//如果是炸弹，就要保留大的炸弹
            return bombs.stream().filter(b -> b.getScore() > pokerSell4P.getScore()).collect(Collectors.toList());
        }
    }

    /**
     * 除了炸弹，只能是相同牌型才能压制
     * @param pokers 当前手里的牌
     * @param pokerSell4P 需要出的牌型
     * @return 支持的牌
     */
    private List<PokerSell4P> findGeneralPokerType(List<Poker> pokers, PokerSell4P pokerSell4P){
        return PokerHelper4P.findPokersByPokerType(pokers, pokerSell4P);
    }

    public void clear(){
        currentPokerType = 0;
        allBiggerPokerType = null;
    }
}
