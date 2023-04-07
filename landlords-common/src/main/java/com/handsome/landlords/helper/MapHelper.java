package com.handsome.landlords.helper;

import org.nico.noson.Noson;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapHelper {

	private final Map<String, Object> data;

	private MapHelper() {
		this.data = new LinkedHashMap<>();
	}

	public static MapHelper newInstance() {
		return new MapHelper();
	}

	public static Map<String, Object> parser(String json) {
		return Noson.convert(json, Map.class);
	}

	public MapHelper put(String name, Object Object) {
		this.data.put(name, Object);
		return this;
	}

	public String json() {
		return Noson.reversal(data);
	}

	public Map<String, Object> map() {
		return data;
	}

}
