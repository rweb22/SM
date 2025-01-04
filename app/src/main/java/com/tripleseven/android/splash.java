package com.tripleseven.android;

import static android.content.pm.PackageManager.GET_META_DATA;

import static com.tripleseven.android.MyStringRequest.SESSION_COOKIE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.webkit.CookieManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class splash extends AppCompatActivity {
    String url;
    ImageView gif;
    final int UPDATE_REQUEST = 99;

    private void launchApp() {
       // FirebaseMessaging.getInstance().subscribeToTopic("all")
        //        .addOnCompleteListener(task -> {
                    if (getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("first_time", null) == null) {
                        Intent in = new Intent(getApplicationContext(), OnBoarding.class);
                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(in);
                        finish();
                    } else {
                        String session = getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null);
                        if (Objects.isNull(session)) {
                            Intent in = new Intent(getApplicationContext(), login.class);
                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(in);
                            finish();
                        } else {
                            Intent in = new Intent(getApplicationContext(), HomeScreen.class);
                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(in);
                            finish();
                        }
                    }
                }
                //);
   // }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("lang","").equals("")){
            startActivity(new Intent(getApplicationContext(),language.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
            );
            finish();
            return;
        }

        String languageToLoad = getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("lang","en"); // your language

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        try {
            ActivityInfo info = getPackageManager().getActivityInfo(getComponentName(), GET_META_DATA);
            if (info.labelRes != 0) {
                setTitle(info.labelRes);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_splash);
      //  gif = findViewById(R.id.logo);
    //    Glide.with(splash.this).load(R.drawable.splash_gif).into(gif);



        url = constant.prefix2 + getString(R.string.getconfig);

        apicall();
    }



    private void apicall() {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            Log.d("result of config", jsonObject1.toString());

                            String currentVersion = jsonObject1.getString("latest_version");

                            if (Integer.parseInt(currentVersion) > BuildConfig.VERSION_CODE) {
                                Log.d("Update available", "yes");
                                long lastShowAt = getSharedPreferences(constant.prefs, MODE_PRIVATE).getLong("last_update_warning", 0);
                                long twoDaysInMillis = 24 * 60 * 60 * 1000;
                                if (System.currentTimeMillis() > lastShowAt + twoDaysInMillis ) {
                                    Intent in = new Intent(getApplicationContext(), Update.class)
                                            .putExtra("link", jsonObject1.getString("update_link"))
                                            .putExtra("log", jsonObject1.getString("update_log"))
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                                    startActivity(in);
                                    finish();
                                } else {
                                    launchApp();
                                }
                            } else {
                                launchApp();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();

                        Toast.makeText(splash.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                try {
                    params.put("version", getPackageManager()
                            .getPackageInfo(getPackageName(), 0).versionCode+"");
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }


                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

}
