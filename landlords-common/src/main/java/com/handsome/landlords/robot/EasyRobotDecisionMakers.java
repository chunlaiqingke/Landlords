package com.handsome.landlords.robot;

import com.handsome.landlords.entity.ClientSide;
import com.handsome.landlords.entity.Poker;
import com.handsome.landlords.entity.PokerSell;
import com.handsome.landlords.enums.SellType;
import com.handsome.landlords.helper.PokerHelper;

import java.util.List;
import java.util.Random;

/**
 * @author nico
 * @date 2018-11-15 12:13:49
 */
public class EasyRobotDecisionMakers extends AbstractRobotDecisionMakers {

	private static Random random = new Random();

	@Override
	public PokerSell howToPlayPokers(PokerSell lastPokerSell, ClientSide robot) {
		if (lastPokerSell != null && lastPokerSell.getSellType() == SellType.KING_BOMB) {
			return null;
		}

		List<PokerSell> sells = PokerHelper.parsePokerSells(robot.getPokers());
		if (lastPokerSell == null) {
			return sells.get(random.nextInt(sells.size()));
		}

		for (PokerSell sell : sells) {
			if (sell.getSellType() == lastPokerSell.getSellType()) {
				if (sell.getScore() > lastPokerSell.getScore() && sell.getSellPokers().size() == lastPokerSell.getSellPokers().size()) {
					return sell;
				}
			}
		}
		if (lastPokerSell.getSellType() != SellType.BOMB) {
			for (PokerSell sell : sells) {
				if (sell.getSellType() == SellType.BOMB) {
					return sell;
				}
			}
		}
		for (PokerSell sell : sells) {
			if (sell.getSellType() == SellType.KING_BOMB) {
				return sell;
			}
		}
		return null;
	}

	@Override
	public boolean howToChooseLandlord(List<Poker> leftPokers, List<Poker> rightPokers, List<Poker> myPokers) {
		List<PokerSell> leftSells = PokerHelper.parsePokerSells(leftPokers);
		List<PokerSell> mySells = PokerHelper.parsePokerSells(myPokers);
		List<PokerSell> rightSells = PokerHelper.parsePokerSells(rightPokers);
		return mySells.size() > leftSells.size() && mySells.size() > rightSells.size();
	}
}
