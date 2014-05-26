package com.openwords.util;

import org.json.JSONObject;

import com.google.gson.*;
//Need to import GSON
//Follow https://www.youtube.com/watch?v=fzdIcbnnwSA

public class PrettyPrint {
	public static String print(String uglyJSONString) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(uglyJSONString);
		String prettyJsonString = gson.toJson(je);
		return prettyJsonString;
	}
}
