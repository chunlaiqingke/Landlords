package com.handsome.landlords.helper;

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
}
