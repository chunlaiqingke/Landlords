package com.handsome.landlords.enums;

import java.io.Serializable;

public enum ServerEventCode implements Serializable {

	CODE_CLIENT_EXIT("玩家退出"),

	CODE_4P_CLIENT_EXIT("4人房玩家退出"),

	CODE_CLIENT_OFFLINE("玩家离线"),

	CODE_CLIENT_INFO_SET("设置客户端信息"),

	CODE_CLIENT_NICKNAME_SET("设置昵称"),

	CODE_CLIENT_HEAD_BEAT("不出"),

	CODE_ROOM_CREATE("创建PVP房间"),

	CODE_4P_ROOM_CREATE("创建4人房间"),
	CODE_ROOM_CREATE_PVE("创建PVE房间"),

	CODE_GET_ROOMS("获取房间列表"),

	CODE_ROOM_JOIN("加入房间"),
	CODE_4P_ROOM_JOIN("加入房间"),

	CODE_GAME_STARTING("游戏开始"),

	CODE_4P_GAME_STARTING("4人游戏开始"),

	CODE_GAME_READY("玩家准备"),

	CODE_GAME_LANDLORD_ELECT("抢地主"),

	CODE_4P_GAME_LANDLORD_ELECT("4人抢地主"),

	CODE_GAME_POKER_PLAY("出牌环节"),
	CODE_4P_GAME_POKER_PLAY("4人出牌环节"),

	CODE_GAME_POKER_PLAY_REDIRECT("出牌重定向"),

	CODE_4P_GAME_POKER_PLAY_REDIRECT("4人出牌重定向"),

	CODE_GAME_POKER_PLAY_PASS("不出"),

	CODE_4P_GAME_POKER_PLAY_PASS("4人房不出"),

	CODE_GAME_WATCH("观战"),

	CODE_GAME_WATCH_EXIT("退出观战"), ;


	private String msg;

	ServerEventCode(String msg) {
		this.msg = msg;
	}

	public final String getMsg() {
		return msg;
	}

	public final void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * 在需要兼容3p和4p的地方调用
	 * @param roomType 房间类型
	 * @param code 事件类型
	 * @return
	 */
	public static ServerEventCode mapping4P(RoomType roomType, ServerEventCode code){
		switch (code) {
			case CODE_ROOM_CREATE:
			case CODE_4P_ROOM_CREATE:
				return roomType == RoomType.PVP4P ? CODE_4P_ROOM_CREATE : CODE_ROOM_CREATE;
			case CODE_GAME_STARTING:
			case CODE_4P_GAME_STARTING:
				return roomType == RoomType.PVP4P ? CODE_GAME_STARTING : CODE_4P_GAME_STARTING;
			case CODE_ROOM_JOIN:
			case CODE_4P_ROOM_JOIN:
				return roomType == RoomType.PVP4P ? CODE_4P_ROOM_JOIN : CODE_ROOM_JOIN;
			case CODE_4P_GAME_LANDLORD_ELECT:
			case CODE_GAME_LANDLORD_ELECT:
				return roomType == RoomType.PVP4P ? CODE_4P_GAME_LANDLORD_ELECT : CODE_GAME_LANDLORD_ELECT;
		}
		return code;
	}

}
