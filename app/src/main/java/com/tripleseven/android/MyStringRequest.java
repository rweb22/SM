package com.tripleseven.android;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.webkit.CookieManager;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MyStringRequest extends com.android.volley.toolbox.StringRequest {
    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    public static final String SESSION_COOKIE = "session";

    private SharedPreferences preferences;


    public MyStringRequest(SharedPreferences preferences, int method, String url, Response.Listener<String> listener,
                           @Nullable Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.preferences = preferences;
    }

    public MyStringRequest(String url, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }


    /* (non-Javadoc)
     * @see com.android.volley.toolbox.StringRequest#parseNetworkResponse(com.android.volley.NetworkResponse)
     */
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        // since we don't know which of the two underlying network vehicles
        // will Volley use, we have to handle and store session cookies manually
       checkSessionCookie(response.headers);

       return super.parseNetworkResponse(response);
    }

    public final void checkSessionCookie(Map<String, String> headers) {
        if (headers.containsKey(SET_COOKIE_KEY)
                && headers.get(SET_COOKIE_KEY).startsWith(SESSION_COOKIE)) {
            String cookie = headers.get(SET_COOKIE_KEY);
            if (Objects.nonNull(cookie) && !cookie.isEmpty()) {

                CookieManager.getInstance().setCookie(SESSION_COOKIE, cookie);
            }
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();

        if (headers == null
                || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }

        String session = preferences.getString("session", null);
        headers.put("Authorization", session);
        return headers;
    }


}
