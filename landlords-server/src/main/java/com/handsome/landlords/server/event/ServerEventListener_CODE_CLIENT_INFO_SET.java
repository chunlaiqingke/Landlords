package com.handsome.landlords.server.event;

import com.handsome.landlords.entity.ClientSide;
import com.handsome.landlords.utils.JsonUtils;

import java.util.Map;

public class ServerEventListener_CODE_CLIENT_INFO_SET implements ServerEventListener {

	private static final String DEFAULT_VERSION = "v1.2.8";

	@Override
	public void call(ClientSide client, String info) {
		Map<?,?> infos = JsonUtils.fromJson(info, Map.class);
		// Get client version
		client.setVersion(DEFAULT_VERSION);
		if (infos.containsKey("version")){
			client.setVersion(String.valueOf(infos.get("version")));
		}
	}

}
