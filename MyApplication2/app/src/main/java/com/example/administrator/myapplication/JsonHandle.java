package com.example.administrator.myapplication;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/2/1.
 */
public class JsonHandle {
    public String stringToJson(String str) throws JSONException {
        JSONObject jsonObject = new JSONObject(str);
        JSONObject data = jsonObject.getJSONObject("data");
        String token = data.optString("token");
        return token;
    }
}
