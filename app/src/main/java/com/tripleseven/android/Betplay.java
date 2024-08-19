package com.tripleseven.android;

import android.app.Application;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;

import java.util.HashMap;
import java.util.Map;

public class Betplay extends Application {

    public static HashMap<String, String> temp_follows = new HashMap<>();
    static public FeedModel feedModel;
    private static Betplay _instance;

    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    private static final String SESSION_COOKIE = "session";

    private RequestQueue _requestQueue;
    private SharedPreferences _preferences;

    static Boolean isLocked = true;

    static public FeedModel getFeedModel() {
        return feedModel;
    }

    static public void setFeedModel(FeedModel feedModel2) {
        feedModel = feedModel2;
    }


    public static Betplay get() {
        return _instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isLocked = true;
    }

    static public void addTempFollow(String mobile, String status){
        temp_follows.put(mobile, status);
    }

    public static void setIsLocked(Boolean isLocked) {
        Betplay.isLocked = isLocked;
    }

    public static Boolean getIsLocked() {
        return isLocked;
    }

    static public String checkFollow(String mobile){
        if(temp_follows.containsKey(mobile) && temp_follows.get(mobile).equals("1"))
        {
            return "1";
        } else if (temp_follows.containsKey(mobile) && temp_follows.get(mobile).equals("0")){
            return "0";
        } else {
            return "-1";
        }
    }

    /**
     * Checks the response headers for session cookie and saves it
     * if it finds it.
     * @param headers Response Headers.
     */
    public final void checkSessionCookie(Map<String, String> headers) {
        if (headers.containsKey(SET_COOKIE_KEY)
                && headers.get(SET_COOKIE_KEY).startsWith(SESSION_COOKIE)) {
            String cookie = headers.get(SET_COOKIE_KEY);
            if (cookie.length() > 0) {
                String[] splitCookie = cookie.split(";");
                String[] splitSessionId = splitCookie[0].split("=");
                cookie = splitSessionId[1];
                SharedPreferences.Editor prefEditor = _preferences.edit();
                prefEditor.putString(SESSION_COOKIE, cookie);
                prefEditor.commit();
            }
        }
    }

    /**
     * Adds session cookie to headers if exists.
     * @param headers
     */
    public final void addSessionCookie(Map<String, String> headers) {
        String sessionId = _preferences.getString(SESSION_COOKIE, "");
        if (sessionId.length() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(SESSION_COOKIE);
            builder.append("=");
            builder.append(sessionId);
            if (headers.containsKey(COOKIE_KEY)) {
                builder.append("; ");
                builder.append(headers.get(COOKIE_KEY));
            }
            headers.put(COOKIE_KEY, builder.toString());
        }
    }
}
