package com.handsome.landlords.server.robot;

import org.nico.noson.Noson;
import com.handsome.landlords.entity.ClientSide;
import com.handsome.landlords.entity.PokerSell;
import com.handsome.landlords.entity.Room;
import com.handsome.landlords.enums.SellType;
import com.handsome.landlords.enums.ServerEventCode;
import com.handsome.landlords.helper.PokerHelper;
import com.handsome.landlords.helper.TimeHelper;
import com.handsome.landlords.print.SimplePrinter;
import com.handsome.landlords.robot.RobotDecisionMakers;
import com.handsome.landlords.server.ServerContains;
import com.handsome.landlords.server.event.ServerEventListener;

public class RobotEventListener_CODE_GAME_POKER_PLAY implements RobotEventListener {

	@Override
	public void call(ClientSide robot, String data) {
		ServerContains.THREAD_EXCUTER.execute(() -> {
			Room room = ServerContains.getRoom(robot.getRoomId());

			PokerSell lastPokerSell = null;
			PokerSell pokerSell = null;
			if (room.getLastSellClient() != robot.getId()) {
				lastPokerSell = room.getLastPokerShell();
				pokerSell = RobotDecisionMakers.howToPlayPokers(room.getDifficultyCoefficient(), lastPokerSell, robot);
			} else {
				pokerSell = RobotDecisionMakers.howToPlayPokers(room.getDifficultyCoefficient(), null, robot);
			}

			if (pokerSell != null && lastPokerSell != null) {
				SimplePrinter.serverLog("Robot monitoring[room:" + room.getId() + "]");
				SimplePrinter.serverLog("last  sell  -> " + lastPokerSell.toString());
				SimplePrinter.serverLog("robot sell  -> " + pokerSell.toString());
				SimplePrinter.serverLog("robot poker -> " + PokerHelper.textOnlyNoType(robot.getPokers()));
			}

			TimeHelper.sleep(300);

			if (pokerSell == null || pokerSell.getSellType() == SellType.ILLEGAL) {
				ServerEventListener.get(ServerEventCode.CODE_GAME_POKER_PLAY_PASS).call(robot, data);
			} else {
				Character[] cs = new Character[pokerSell.getSellPokers().size()];
				for (int index = 0; index < cs.length; index++) {
					cs[index] = pokerSell.getSellPokers().get(index).getLevel().getAlias()[0];
				}
				ServerEventListener.get(ServerEventCode.CODE_GAME_POKER_PLAY).call(robot, Noson.reversal(cs));
			}
		});
	}
}
