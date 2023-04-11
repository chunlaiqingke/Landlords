package com.handsome.landlords.helper;

import com.google.common.collect.Sets;
import com.handsome.landlords.entity.Poker;
import com.handsome.landlords.entity.PokerSell4P;
import com.handsome.landlords.enums.PokerLevel;
import com.handsome.landlords.enums.PokerType;
import com.handsome.landlords.enums.SellType;
import com.handsome.landlords.enums.SellType4P;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 4人斗地主
 */
public class PokerHelper4P {

    private static final List<Poker> basePokers = new ArrayList<>(108);

    private static final Comparator<Poker> pokerComparator = Comparator.comparingInt(o -> o.getLevel().getLevel());

    static {
        PokerLevel[] pokerLevels = PokerLevel.values();
        PokerType[] pokerTypes = PokerType.values();

        for(int i = 0 ;i < 2; i++) {
            for (PokerLevel level : pokerLevels) {
                if (level == PokerLevel.LEVEL_BIG_KING) {
                    basePokers.add(new Poker(level, PokerType.BLANK));
                    continue;
                }
                if (level == PokerLevel.LEVEL_SMALL_KING) {
                    basePokers.add(new Poker(level, PokerType.BLANK));
                    continue;
                }
                for (PokerType type : pokerTypes) {
                    if (type == PokerType.BLANK) {
                        continue;
                    }
                    basePokers.add(new Poker(level, type));
                }
            }
        }
    }

    /**
     * 思路：每个类型逐个检查
     * @param pokers
     * @return
     */
    public static PokerSell4P checkPokerType(List<Poker> pokers) {
        PokerHelper.sortPoker(pokers);
        Map<Integer, Integer> pokerCount = new HashMap<>();
        for(Poker p : pokers){
            Integer count = pokerCount.getOrDefault(p.getLevel().getLevel(), 0);
            pokerCount.put(p.getLevel().getLevel(), count + 1);
        }
        for(SellType4P sell4P : SellType4P.values()) {
            switch (sell4P) {
                case ILLEGAL:
                    break;
                case BOMB_FOUR:
                    if(pokerCount.size() == 1 && pokerCount.values().stream().findFirst().get() == 4) {
                        Integer level = pokerCount.keySet().stream().findFirst().get();
                        return new PokerSell4P(sell4P, pokers, level);
                    }
                    break;
                case BOMB_FIVE:
                    if(pokerCount.size() == 1 && pokerCount.values().stream().findFirst().get() == 5) {
                        Integer level = pokerCount.keySet().stream().findFirst().get();
                        return new PokerSell4P(sell4P, pokers, level);
                    }
                    break;
                case BOMB_SIX:
                    if(pokerCount.size() == 1 && pokerCount.values().stream().findFirst().get() == 6) {
                        Integer level = pokerCount.keySet().stream().findFirst().get();
                        return new PokerSell4P(sell4P, pokers, level);
                    }
                    break;
                case BOMB_SEVEN:
                    if(pokerCount.size() == 1 && pokerCount.values().stream().findFirst().get() == 7) {
                        Integer level = pokerCount.keySet().stream().findFirst().get();
                        return new PokerSell4P(sell4P, pokers, level);
                    }
                    break;
                case BOMB_EIGHT:
                    if(pokerCount.size() == 1 && pokerCount.values().stream().findFirst().get() == 8) {
                        Integer level = pokerCount.keySet().stream().findFirst().get();
                        return new PokerSell4P(sell4P, pokers, level);
                    }
                    break;
                case KING_BOMB:
                    if(pokerCount.size() == 2
                            && pokerCount.values().stream().allMatch(c -> c == 2)
                            && pokerCount.keySet().stream().allMatch(p -> p == PokerLevel.LEVEL_SMALL_KING.getLevel() || p == PokerLevel.LEVEL_BIG_KING.getLevel())) {
                        Integer level = pokerCount.keySet().stream().findFirst().get();
                        return new PokerSell4P(sell4P, pokers, level);
                    }
                    break;
                case SINGLE:
                    if(pokerCount.size() == 1 && pokerCount.values().stream().findFirst().get() == 1) {
                        Integer level = pokerCount.keySet().stream().findFirst().get();
                        return new PokerSell4P(sell4P, pokers, level);
                    }
                    break;
                case DOUBLE: //对子
                    if(pokerCount.size() == 1 && pokerCount.values().stream().findFirst().get() == 2) {
                        Integer level = pokerCount.keySet().stream().findFirst().get();
                        return new PokerSell4P(sell4P, pokers, level);
                    }
                    break;
                case THREE: //三个
                    if(pokerCount.size() == 1 && pokerCount.values().stream().findFirst().get() == 3) {
                        Integer level = pokerCount.keySet().stream().findFirst().get();
                        return new PokerSell4P(sell4P, pokers, level);
                    }
                    break;
                case THREE_ZONES_DOUBLE: //三带二
                    if(pokerCount.size() == 2) {
                        List<Integer> pokerList = new ArrayList<>(pokerCount.keySet());
                        Integer firstPoker = pokerList.get(0);
                        Integer secondPoker = pokerList.get(1);
                        Integer firstCount = pokerCount.get(firstPoker);
                        Integer secondCount = pokerCount.get(secondPoker);
                        if(firstCount == 3 && secondCount == 2) {
                            return new PokerSell4P(sell4P, pokers, firstPoker);
                        } else if (firstCount == 2 && secondCount == 3) {
                            return new PokerSell4P(sell4P, pokers, secondPoker);
                        }
                    }
                    break;
                case THREE_STRAIGHT: // 飞机
                    if(pokerCount.size() >= 2
                            && pokerCount.values().stream().allMatch(c -> c == 3)
                            && pokerCount.keySet().stream().allMatch(p -> p >=3 && p <= 14)) {
                        List<Integer> pokerList = new ArrayList<>(pokerCount.keySet());

                        if(allStraight(pokerList)) {
                            return new PokerSell4P(sell4P, pokers, pokerList.get(0));
                        }
                    }
                    break;
                case DOUBLE_STRAIGHT: // 连对
                    if(pokerCount.size() >=3
                            && pokerCount.values().stream().allMatch(c -> c == 2)
                            && pokerCount.keySet().stream().allMatch(p -> p >=3 && p <= 14)) {
                        List<Integer> pokerList = new ArrayList<>(pokerCount.keySet());

                        if(allStraight(pokerList)) {
                            return new PokerSell4P(sell4P, pokers, pokerList.get(0));
                        }
                    }
                    break;
                case THREE_STRAIGHT_WITH_DOUBLE: //飞机带翅膀
                    if(pokerCount.size() >= 4 && pokerCount.size() % 2 == 0) {
                        Map<Integer, Integer> three = pokerCount.entrySet().stream()
                                .filter(e -> e.getValue() == 3)
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                        Map<Integer, Integer> double_ = pokerCount.entrySet().stream()
                                .filter(e -> e.getValue() == 2)
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                        List<Integer> pokerList = new ArrayList<>(three.keySet());
                        boolean allStraight = allStraight(pokerList);
                        //是飞机，且飞机和翅膀相等, 飞机带翅膀的大小是飞机决定
                        if(allStraight && three.size() == double_.size() && pokerCount.size() == (three.size() + double_.size())) {
                            return new PokerSell4P(sell4P, pokers, pokerList.get(0));
                        }
                    }
                    break;
                default:
                    return new PokerSell4P(SellType4P.ILLEGAL, null, -1);
            }
        }
        return new PokerSell4P(SellType4P.ILLEGAL, null, -1);
    }

    /**
     * 是不是顺子，连对，飞机
     * @param pokerList
     * @return
     */
    private static boolean allStraight(List<Integer> pokerList){
        pokerList.sort(Comparator.comparingInt(a -> a));
        //顺子只能3到A
        if(!pokerList.stream().allMatch(p -> p >= 3 && p <= 14)) {
            return false;
        }
        boolean allStraight = true;
        for(int i = 1; i < pokerList.size(); i++) {
            if(Math.abs(pokerList.get(i) - pokerList.get(i - 1)) !=1) {
                allStraight = false;
                break;
            }
        }
        return allStraight;
    }

    public static int parseScore(SellType4P sellType4P, int level) {
        if (sellType4P == SellType4P.BOMB_FOUR) {
            return level * 4 + 999;
        }else if (sellType4P == SellType4P.BOMB_FIVE) {
            return level * 4 + 1999;
        }else if (sellType4P == SellType4P.BOMB_SIX) {
            return level * 4 + 2999;
        }else if (sellType4P == SellType4P.BOMB_SEVEN) {
            return level * 4 + 3999;
        }else if (sellType4P == SellType4P.BOMB_EIGHT) {
            return level * 4 + 4999;
        } else if (sellType4P == SellType4P.KING_BOMB) {
            return Integer.MAX_VALUE;
        } else if (sellType4P == SellType4P.SINGLE || sellType4P == SellType4P.DOUBLE || sellType4P == SellType4P.THREE) {
            return level;
        } else if (sellType4P == SellType4P.DOUBLE_STRAIGHT || sellType4P == SellType4P.THREE_STRAIGHT) {
            return level;
        } else if (sellType4P == SellType4P.THREE_ZONES_DOUBLE || sellType4P == SellType4P.THREE_STRAIGHT_WITH_DOUBLE) {
            return level;
        }
        return -1;
    }

    public static List<List<Poker>> distributePoker() {
        Collections.shuffle(basePokers);
        List<List<Poker>> pokersList = new ArrayList<List<Poker>>();
        List<Poker> pokers1 = new ArrayList<>(25);
        pokers1.addAll(basePokers.subList(0, 25));
        List<Poker> pokers2 = new ArrayList<>(25);
        pokers2.addAll(basePokers.subList(25, 50));
        List<Poker> pokers3 = new ArrayList<>(25);
        pokers3.addAll(basePokers.subList(50, 75));
        List<Poker> pokers4 = new ArrayList<>(25);
        pokers4.addAll(basePokers.subList(75, 100));
        List<Poker> pokers5 = new ArrayList<>(8);
        pokers5.addAll(basePokers.subList(100, 108));
        pokersList.add(pokers1);
        pokersList.add(pokers2);
        pokersList.add(pokers3);
        pokersList.add(pokers4);
        pokersList.add(pokers5);
        for (List<Poker> pokers : pokersList) {
            sortPoker(pokers);
        }
        return pokersList;
    }

    public static void sortPoker(List<Poker> pokers) {
        pokers.sort(pokerComparator);
    }

    /**
     * 返回bomb牌的索引
     * @param pokers
     * @return
     */
    public static List<Integer> whereBomb(List<Poker> pokers){
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

    /**
     * 找到指定类型的牌
     * @param pokers
     * @param pokerSell4P
     * @return 返回满足的类型
     */
    public static List<PokerSell4P> findPokersByPokerType(List<Poker> pokers, PokerSell4P pokerSell4P) {

        Map<Integer, List<Integer>> pokerIndex = new HashMap<>();
        for(int i = 0; i < pokers.size(); i++){
            Poker p = pokers.get(i);
            List<Integer> indexList = pokerIndex.getOrDefault(p.getLevel().getLevel(), new ArrayList<>());
            indexList.add(i);
            pokerIndex.put(p.getLevel().getLevel(), indexList);
        }

        //获取第一个不同牌型的位置
        List<Integer> diffIndex = new ArrayList<>();
        diffIndex.add(0);
        for(Integer k : pokerIndex.keySet()) {
            List<Integer> indexList = pokerIndex.get(k);
            diffIndex.add(indexList.get(0));
        }

        List<PokerSell4P> res = new ArrayList<>();
        switch (pokerSell4P.getSellType4P()) {
            case SINGLE :
                for(int i = 0; i < pokers.size(); i++) {
                    Poker poker = pokers.get(i);
                    PokerSell4P futureType = PokerHelper4P.checkPokerType(new ArrayList<>(Collections.singletonList(poker)));
                    if(pokerSell4P.getSellType4P() == futureType.getSellType4P() && futureType.getScore() > pokerSell4P.getScore()) {
                        futureType.setPositions(new ArrayList<>(List.of(i)));
                        res.add(futureType);
                    }
                }
                break;
            case DOUBLE:
                for(int i = 0; i < diffIndex.size(); i++) {
                    Poker poker = pokers.get(diffIndex.get(i));
                    Integer count = pokerIndex.get(poker.getLevel().getLevel()).size();
                    if(count < 2) {
                        continue;
                    }
                    Poker poker1 = pokers.get(i);
                    Poker poker2 = pokers.get(i + 1);
                    PokerSell4P futureType = PokerHelper4P.checkPokerType(new ArrayList<>(Arrays.asList(poker1, poker2)));
                    if(pokerSell4P.getSellType4P() == futureType.getSellType4P() && futureType.getScore() > pokerSell4P.getScore()) {
                        futureType.setPositions(new ArrayList<>(Arrays.asList(i, i+1)));
                        res.add(futureType);
                    }
                }
                break;
            case THREE:
                for(int i = 0; i < diffIndex.size(); i++) {
                    Poker poker = pokers.get(diffIndex.get(i));
                    Integer count = pokerIndex.get(poker.getLevel().getLevel()).size();
                    if(count < 3) {
                        continue;
                    }
                    Poker poker1 = pokers.get(i);
                    Poker poker2 = pokers.get(i + 1);
                    Poker poker3 = pokers.get(i + 2);
                    PokerSell4P futureType = PokerHelper4P.checkPokerType(new ArrayList<>(Arrays.asList(poker1, poker2, poker3)));
                    if(pokerSell4P.getSellType4P() == futureType.getSellType4P() && futureType.getScore() > pokerSell4P.getScore()) {
                        futureType.setPositions(new ArrayList<>(Arrays.asList(i, i+1, i+2)));
                        res.add(futureType);
                    }
                }
                break;
            case THREE_ZONES_DOUBLE:
                boolean hasThree = pokerIndex.values().stream().anyMatch(p -> p.size() >= 3);
                boolean hasTwo = pokerIndex.values().stream().anyMatch(p -> p.size() >= 2);
                if(hasThree && hasTwo) {
                    List<Integer> three = pokerIndex.entrySet().stream().filter(e -> e.getValue().size() >= 3).map(Map.Entry::getKey).toList();
                    List<Integer> two = pokerIndex.entrySet().stream().filter(e -> e.getValue().size() >= 2).map(Map.Entry::getKey).toList();
                    for(int th : three) {
                        for(int t : two) {
                            List<Integer> threeIndexList = pokerIndex.get(th);
                            List<Poker> futurePokers = threeIndexList.stream().map(pokers::get).limit(3).collect(Collectors.toList());
                            List<Integer> twoIndexList = pokerIndex.get(t);
                            futurePokers.addAll(twoIndexList.stream().map(pokers::get).limit(2).toList());
                            PokerSell4P futureSell = PokerHelper4P.checkPokerType(futurePokers);
                            if(pokerSell4P.getSellType4P() == futureSell.getSellType4P() && futureSell.getScore() > pokerSell4P.getScore()) {
                                futureSell.addPositions(threeIndexList.stream().limit(3).collect(Collectors.toList()));
                                futureSell.addPositions(twoIndexList.stream().limit(2).collect(Collectors.toList()));
                                res.add(futureSell);
                            }
                        }
                    }
                }
                break;
            case DOUBLE_STRAIGHT:
                //几连对
                int straightCount = pokerSell4P.getStraightCount();
                for(int i = 0; i < diffIndex.size(); i++) {
                    Integer index = diffIndex.get(i);
                    Poker poker = pokers.get(index);
                    int level = poker.getLevel().getLevel();
                    if(level < 3 || level > 14 - straightCount + 1) { //2和大小王不够成连对
                        continue;
                    }
                    List<Poker> futurePokers = new ArrayList<>();
                    List<Integer> positions = new ArrayList<>();
                    for(int startLevel = level; startLevel < level + straightCount; startLevel ++){
                        List<Integer> indexList = pokerIndex.get(startLevel);
                        if(indexList.size() < 2) {
                            break;
                        }
                        Poker poker1 = pokers.get(indexList.get(0));
                        Poker poker2 = pokers.get(indexList.get(1));
                        futurePokers.add(poker1);
                        futurePokers.add(poker2);
                        positions.addAll(Arrays.asList(indexList.get(0), indexList.get(1)));
                    }
                    if(futurePokers.size() < straightCount * 2) {
                        continue;
                    }
                    PokerSell4P futureSell = PokerHelper4P.checkPokerType(futurePokers);
                    if(pokerSell4P.getSellType4P() == futureSell.getSellType4P() && futureSell.getScore() > pokerSell4P.getScore()) {
                        futureSell.setPositions(positions);
                        res.add(futureSell);
                    }
                }
                break;
            case THREE_STRAIGHT:
                //几连飞机
                int sc = pokerSell4P.getStraightCount();
                for(int i = 0; i < diffIndex.size(); i++) {
                    Integer index = diffIndex.get(i);
                    Poker poker = pokers.get(index);
                    int level = poker.getLevel().getLevel();
                    if(level < 3 || level > 14 - sc + 1) { //2和大小王不够成连对
                        continue;
                    }
                    List<Poker> futurePokers = new ArrayList<>();
                    List<Integer> positions = new ArrayList<>();
                    for(int startLevel = level; startLevel < level + sc; startLevel ++){
                        List<Integer> indexList = pokerIndex.get(startLevel);
                        if(indexList.size() < 3) {
                            break;
                        }
                        Poker poker1 = pokers.get(indexList.get(0));
                        Poker poker2 = pokers.get(indexList.get(1));
                        Poker poker3 = pokers.get(indexList.get(2));
                        futurePokers.add(poker1);
                        futurePokers.add(poker2);
                        futurePokers.add(poker3);
                        positions.addAll(Arrays.asList(indexList.get(0), indexList.get(1), indexList.get(2)));
                    }
                    if(futurePokers.size() < sc * 3) {
                        continue;
                    }
                    PokerSell4P futureSell = PokerHelper4P.checkPokerType(futurePokers);
                    if(pokerSell4P.getSellType4P() == futureSell.getSellType4P() && futureSell.getScore() > pokerSell4P.getScore()) {
                        futureSell.setPositions(positions);
                        res.add(futureSell);
                    }
                }
                break;
            case THREE_STRAIGHT_WITH_DOUBLE:
                boolean hasTriple = pokerIndex.values().stream().filter(e -> e.size() >= 3).count() >= pokerSell4P.getStraightCount();
                boolean hasDouble = pokerIndex.values().stream().filter(e -> e.size() >= 2).count() >= pokerSell4P.getStraightCount();
                if(hasTriple && hasDouble) {
                    List<Integer> three = pokerIndex.entrySet().stream().filter(e -> e.getValue().size() >= 3).map(Map.Entry::getKey).toList();
                    List<Integer> two = pokerIndex.entrySet().stream().filter(e -> e.getValue().size() >= 3).map(Map.Entry::getKey).toList();
                    int sCount = pokerSell4P.getStraightCount();
                    //获取翅膀的所有组合
                    List<Set<Integer>> combinations = new ArrayList<>(Sets.combinations(new HashSet<>(two), 2));

                    //飞机需要连续
                    for(int th = 0; th < three.size() - sCount + 1; th ++) {
                        List<Poker> futurePokers = new ArrayList<>();
                        List<Integer> positions = new ArrayList<>();
                        for (int startLevel = th; startLevel < th + sCount; startLevel++) {
                            List<Integer> indexList = pokerIndex.get(startLevel);
                            Poker poker1 = pokers.get(indexList.get(0));
                            Poker poker2 = pokers.get(indexList.get(1));
                            Poker poker3 = pokers.get(indexList.get(2));
                            futurePokers.add(poker1);
                            futurePokers.add(poker2);
                            futurePokers.add(poker3);
                            positions.addAll(Arrays.asList(indexList.get(0), indexList.get(1), indexList.get(2)));
                        }
                        for(Set<Integer> combination : combinations) {
                            List<Poker> dd = combination.stream().map(pokerIndex::get).flatMap(Collection::stream).map(pokers::get).collect(Collectors.toList());
                            futurePokers.addAll(dd);
                            positions.addAll(combination);
                        }
                        PokerSell4P futureSell = PokerHelper4P.checkPokerType(futurePokers);
                        if(pokerSell4P.getSellType4P() == futureSell.getSellType4P() && futureSell.getScore() > pokerSell4P.getScore()) {
                            futureSell.addPositions(positions);
                            res.add(futureSell);
                        }
                    }
                }
                break;
            case BOMB_FOUR:
            case BOMB_FIVE:
            case BOMB_SIX:
            case BOMB_SEVEN:
            case BOMB_EIGHT:
                for(List<Integer> indexList : pokerIndex.values()){
                    if(indexList.size() >= 4) {
                        List<Poker> futureSell = new ArrayList<>();
                        for(int i : indexList) {
                            futureSell.add(pokers.get(i));
                        }
                        PokerSell4P type = PokerHelper4P.checkPokerType(futureSell);
                        if(type.getScore() > pokerSell4P.getScore()) {
                            type.setPositions(indexList);
                            res.add(type);
                        }
                    }
                }
//            case KING_BOMB: 王炸就是要不起
            default:
                break;
        }
        return res;
    }

    public static List<PokerSell4P> findAllBombPokers(List<Poker> pokers) {
        List<PokerSell4P> res = new ArrayList<>();

        Map<Integer, List<Integer>> pokerIndex = new HashMap<>();
        for(int i = 0; i < pokers.size(); i++){
            Poker p = pokers.get(i);
            List<Integer> indexList = pokerIndex.getOrDefault(p.getLevel().getLevel(), new ArrayList<>());
            indexList.add(i);
            pokerIndex.put(p.getLevel().getLevel(), indexList);
        }

        for(List<Integer> indexList : pokerIndex.values()){
            if(indexList.size() >= 4) {
                List<Poker> futureSell = new ArrayList<>();
                for(int i : indexList) {
                    futureSell.add(pokers.get(i));
                }
                PokerSell4P type = PokerHelper4P.checkPokerType(futureSell);
                type.setPositions(indexList);
                res.add(type);
            }
        }
        return res;
    }
}
