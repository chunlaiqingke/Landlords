package com.handsome.landlords.enums;

public enum RoomType {

	PVP("玩家对战"),
	PVP4P("4人对战"),
	PVE("人机对战"),

	;
	private String msg;

	RoomType(String msg) {
		this.msg = msg;
	}

	public final String getMsg() {
		return msg;
	}

	public final void setMsg(String msg) {
		this.msg = msg;
	}

}
