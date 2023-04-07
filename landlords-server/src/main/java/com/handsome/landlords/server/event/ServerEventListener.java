package com.handsome.landlords.server.event;


import com.handsome.landlords.entity.ClientSide;
import com.handsome.landlords.enums.ServerEventCode;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public interface ServerEventListener {

	void call(ClientSide client, String data);

	Map<ServerEventCode, ServerEventListener> LISTENER_MAP = new HashMap<>();

	String LISTENER_PREFIX = "com.handsome.landlords.server.event.ServerEventListener_";

	String LISTENER_PREFIX_4P = "com.handsome.landlords.server.event.four.ServerEventListener_";

	@SuppressWarnings("unchecked")
	static ServerEventListener get(ServerEventCode code) {
		ServerEventListener listener = null;
		try {
			if (ServerEventListener.LISTENER_MAP.containsKey(code)) {
				listener = ServerEventListener.LISTENER_MAP.get(code);
			} else {
				String eventListener = LISTENER_PREFIX + code.name();
				Class<ServerEventListener> listenerClass = null;
				try {
					listenerClass = (Class<ServerEventListener>) Class.forName(eventListener);
				} catch (ClassNotFoundException e) {
					eventListener = LISTENER_PREFIX_4P + code.name();
					listenerClass = (Class<ServerEventListener>) Class.forName(eventListener);
				}
				try {
					listener = listenerClass.getDeclaredConstructor().newInstance();
				} catch (InvocationTargetException | NoSuchMethodException e) {
					e.printStackTrace();
				}
				ServerEventListener.LISTENER_MAP.put(code, listener);
			}
			return listener;
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

}
