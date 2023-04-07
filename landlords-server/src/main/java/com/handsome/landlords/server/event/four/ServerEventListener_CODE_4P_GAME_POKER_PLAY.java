package com.handsome.landlords.server.event.four;

import com.handsome.landlords.channel.ChannelUtils;
import com.handsome.landlords.entity.*;
import com.handsome.landlords.enums.*;
import com.handsome.landlords.features.Features;
import com.handsome.landlords.helper.MapHelper;
import com.handsome.landlords.helper.PokerHelper;
import com.handsome.landlords.helper.PokerHelper4P;
import com.handsome.landlords.print.SimplePrinter;
import com.handsome.landlords.server.ServerContains;
import com.handsome.landlords.server.event.ServerEventListener;
import com.handsome.landlords.server.robot.RobotEventListener;
import com.handsome.landlords.utils.LastCardsUtils;
import org.nico.noson.Noson;

import java.util.ArrayList;
import java.util.List;

public class ServerEventListener_CODE_4P_GAME_POKER_PLAY implements ServerEventListener {

	@Override
	public void call(ClientSide clientSide, String data) {
		Room room = ServerContains.getRoom(clientSide.getRoomId());
		if (room == null) {
			return;
		}
		if (room.getCurrentSellClient() != clientSide.getId()) {
			ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_GAME_POKER_PLAY_ORDER_ERROR, null);
			return;
		}
		Character[] options = Noson.convert(data, Character[].class);
		int[] indexes = PokerHelper.getIndexes(options, clientSide.getPokers());
		if (!PokerHelper.checkPokerIndex(indexes, clientSide.getPokers())) {
			ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_4P_GAME_POKER_PLAY_INVALID, null);
			return;
		}

		boolean sellFlag = true;
		List<Poker> currentPokers = PokerHelper.getPoker(indexes, clientSide.getPokers());
		PokerSell4P currentPokerSell4P = PokerHelper4P.checkPokerType(currentPokers);
		if (currentPokerSell4P.getSellType4P() == SellType4P.ILLEGAL) {
			ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_4P_GAME_POKER_PLAY_INVALID, null);
			return;
		}
		//此是压牌，而不是主动出牌权
		if (room.getLastSellClient() != clientSide.getId() && room.getLastPokerShell4P() != null) {
			PokerSell4P lastPokerSell = room.getLastPokerShell4P();

			//check牌型
			if (!PokerSell4P.matchSellType(lastPokerSell, currentPokerSell4P)) {
				String result = MapHelper.newInstance()
						.put("playType", currentPokerSell4P.getSellType4P())
						.put("playCount", currentPokerSell4P.getSellPokers().size())
						.put("preType", lastPokerSell.getSellType4P())
						.put("preCount", lastPokerSell.getSellPokers().size())
						.json();
				ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_4P_GAME_POKER_PLAY_MISMATCH, result);
				return;
			}
			//比较大小
			if (lastPokerSell.getScore() >= currentPokerSell4P.getScore()) {
				String result = MapHelper.newInstance()
						.put("playScore", currentPokerSell4P.getScore())
						.put("preScore", lastPokerSell.getScore())
						.json();
				ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_4P_GAME_POKER_PLAY_LESS, result);
				return;
			}
		}

		//出牌
		ClientSide next = clientSide.getNext();
		if (SellType4P.isBomb(currentPokerSell4P.getSellType4P())) {
			// 炸弹积分翻倍
			room.increaseRate();
		}

		room.setLastSellClient(clientSide.getId());
		room.setLastPokerShell4P(currentPokerSell4P);
		room.setCurrentSellClient(next.getId());

		List<List<Poker>> lastPokerList = new ArrayList<>();
		for(int i = 0; i < room.getClientSideList().size(); i++){
			if(room.getClientSideList().get(i).getId() != clientSide.getId()){
				lastPokerList.add(room.getClientSideList().get(i).getPokers());
			}
		}
		String lastPokers = LastCardsUtils.getLastCards(lastPokerList);
		lastPokerList = new ArrayList<>();
		clientSide.getPokers().removeAll(currentPokers);
		MapHelper mapHelper = MapHelper.newInstance()
				.put("clientId", clientSide.getId())
				.put("clientNickname", clientSide.getNickname())
				.put("clientType", clientSide.getType())
				.put("pokers", currentPokers)
				.put("lastSellClientId", clientSide.getId())
				.put("lastSellPokers", currentPokers)
				.put("lastPokers",lastPokers);

		if (!clientSide.getPokers().isEmpty()) {
			mapHelper.put("sellClientNickname", next.getNickname());
		}

		String result = mapHelper.json();

		for (ClientSide client : room.getClientSideList()) {
			if (client.getRole() == ClientRole.PLAYER) {
				ChannelUtils.pushToClient(client.getChannel(), ClientEventCode.CODE_4P_SHOW_POKERS, result);
			}
		}

//		notifyWatcherPlayPoker(room, result); //先不管观战者

		if (!clientSide.getPokers().isEmpty()) {
			if (next.getRole() == ClientRole.ROBOT) {
				RobotEventListener.get(ClientEventCode.CODE_GAME_POKER_PLAY).call(next, data);
			} else {
				ServerEventListener.get(ServerEventCode.CODE_4P_GAME_POKER_PLAY_REDIRECT).call(next, result);
			}
			return;
		}

		gameOver(clientSide, room);
//        ServerEventListener.get(ServerEventCode.CODE_GAME_STARTING).call(clientSide, data);
	}

	private void setRoomClientScore(Room room, ClientType winnerType) {
		int landLordScore = room.getScore() * 2;
		int peasantScore = room.getScore();
		// 输的一方分数为负
		if (winnerType == ClientType.LANDLORD) {
			peasantScore = -peasantScore;
		} else {
			landLordScore = -landLordScore;
		}
		for (ClientSide client : room.getClientSideList()) {
			if (client.getType() == ClientType.LANDLORD) {
				client.addScore(landLordScore);
			} else {
				client.addScore(peasantScore);
			}
		}
	}

	private void gameOver(ClientSide winner, Room room) {
		ClientType winnerType = winner.getType();
		if (isSpring(winner, room)) {
			room.increaseRate();
		}

		setRoomClientScore(room, winnerType);

		ArrayList<Object> clientScores = new ArrayList<>();
		for (ClientSide client : room.getClientSideList()) {
			MapHelper score = MapHelper.newInstance()
					.put("clientId", client.getId())
					.put("nickName", client.getNickname())
					.put("score", client.getScore())
					.put("scoreInc", client.getScoreInc())
					.put("pokers", client.getPokers());
			clientScores.add(score.map());
		}

		SimplePrinter.serverLog(clientScores.toString());
		String result = MapHelper.newInstance()
				.put("winnerNickname", winner.getNickname())
				.put("winnerType", winner.getType())
				.put("scores", clientScores)
				.json();

		boolean supportReady = true;
		for (ClientSide client : room.getClientSideList()) {
			if (client.getRole() == ClientRole.ROBOT || ! Features.supported(client.getVersion(), Features.READY)) {
				supportReady = false;
				break;
			}
		}
		if (supportReady){
			room.setStatus(RoomStatus.WAIT);
			room.initScoreRate();
			for (ClientSide client : room.getClientSideList()) {
				client.setStatus(ClientStatus.NO_READY);
				ChannelUtils.pushToClient(client.getChannel(), ClientEventCode.CODE_GAME_OVER, result);
			}
		}else{
			ServerEventListener.get(ServerEventCode.CODE_CLIENT_EXIT).call(winner, "");
		}
		notifyWatcherGameOver(room, result);
	}

	private boolean isSpring(ClientSide winner, Room room) {
		boolean isSpring = true;
		for (ClientSide client: room.getClientSideList()) {
			if (client.getId() == winner.getId()) {
				continue;
			}
			if (client.getType() == ClientType.PEASANT && client.getPokers().size() < 17) {
				isSpring = false;
			}
			if (client.getType() == ClientType.LANDLORD && client.getPokers().size() < 20) {
				isSpring = false;
			}
		}
		return isSpring;
	}

	/**
	 * 通知观战者出牌信息
	 *
	 * @param room	房间
	 * @param result	出牌信息
	 */
	private void notifyWatcherPlayPoker(Room room, String result) {
		for (ClientSide watcher : room.getWatcherList()) {
			ChannelUtils.pushToClient(watcher.getChannel(), ClientEventCode.CODE_SHOW_POKERS, result);
		}
	}

	/**
	 * 通知观战者游戏结束
	 *
	 * @param room	房间
	 * @param result	出牌信息
	 */
	private void notifyWatcherGameOver(Room room, String  result) {
		for (ClientSide watcher : room.getWatcherList()) {
			ChannelUtils.pushToClient(watcher.getChannel(), ClientEventCode.CODE_GAME_OVER, result);
		}
	}
}
