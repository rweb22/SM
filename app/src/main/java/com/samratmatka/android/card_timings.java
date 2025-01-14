package com.samratmatka.android;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class card_timings extends AppCompatActivity {

    RecyclerView recyclerview;
    ViewDialog progressDialog;
    String url2 ;
    String market = "";
    TextView title;
    latobold bet_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starline_timings);
        initViews();
        market = getIntent().getStringExtra("market");
        title.setText("PLAY CARD GAME");
        url2 = constant.prefix + "card_timings.php";
        bet_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(card_timings.this, cardBidHistory.class));
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        apicall2();
    }

    private void apicall2() {

        progressDialog = new ViewDialog(card_timings.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response",response);
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);

                            JSONArray jsonArray = jsonObject1.getJSONArray("data");

                            ArrayList<String> name = new ArrayList<>();
                            ArrayList<String> is_open = new ArrayList<>();
                            ArrayList<String> time = new ArrayList<>();
                            ArrayList<String> game = new ArrayList<>();


                            for (int a = 0; a < jsonArray.length(); a++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(a);

                                name.add(jsonObject.getString("name"));
                                is_open.add(jsonObject.getString("is_open"));
                                time.add(jsonObject.getString("time"));
                                game.add(jsonObject.getString("game"));

                            }

                            adapter_timings rc = new adapter_timings(card_timings.this,market,name,is_open,time,game);
                            recyclerview.setLayoutManager(new GridLayoutManager(card_timings.this, 1));
                            recyclerview.setAdapter(rc);
                            rc.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.hideDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                        progressDialog.hideDialog();
                        Toast.makeText(card_timings.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("session",getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));
                params.put("market",market);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }



    private void initViews() {
        bet_history = findViewById(R.id.bid_history_button);
        recyclerview = findViewById(R.id.recyclerview);
        title = findViewById(R.id.title);
    }
}
