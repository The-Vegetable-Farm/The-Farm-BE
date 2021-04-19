package com.veggie.thefarm.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by Bohyun on 2021.04.16
 */
public class JSONUtils {

    public static JSONObject getJsonObject(String payload) {
        try {
            return (JSONObject) new JSONParser().parse(payload);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getJsonValue(JSONObject jsonObject, String key, String defValue) {
        try {
            return jsonObject.get(key).toString();
        } catch (Exception e) {
            return defValue;
        }
    }

}
