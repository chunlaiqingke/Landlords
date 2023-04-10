package com.handsome.landlords.helper;

import com.handsome.landlords.entity.Poker;
import com.handsome.landlords.enums.PokerLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 炸弹牌型提示
 */
public class BombHelper {

    /**
     * 返回bomb牌的索引
     * @param pokers
     * @return
     */
    public static List<Integer> where(List<Poker> pokers){
        Map<Integer, List<Integer>> count = new HashMap<>();
        List<Integer> indexRes = new ArrayList<>();
        for(int i = 0; i < pokers.size(); i++) {
            Poker poker = pokers.get(i);
            if(poker.getLevel() == PokerLevel.LEVEL_SMALL_KING || poker.getLevel() == PokerLevel.LEVEL_BIG_KING) {
                int kingKey = PokerLevel.LEVEL_SMALL_KING.getLevel() + PokerLevel.LEVEL_BIG_KING.getLevel();
                List<Integer> kingCount = count.getOrDefault(kingKey, new ArrayList<>());
                kingCount.add(i);
                count.put(kingKey, kingCount);
            } else {
                List<Integer> indexList = count.getOrDefault(poker.getLevel().getLevel(), new ArrayList<>());
                indexList.add(i);
                count.put(poker.getLevel().getLevel(), indexList);
            }
        }
        for(Integer k : count.keySet()) {
            List<Integer> indexList = count.get(k);
            if(indexList.size() >= 4){
                indexRes.addAll(indexList);
            }
        }
        return indexRes;
    }
}
