package com.tripleseven.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class DelhiGames extends AppCompatActivity {

    private ImageView back, time_info;
    private LinearLayout play_game;
    private RecyclerView recycler;
    String url = constant.prefix + "delhi_markets.php";
    ViewDialog viewDialog2;
    private latobold single;
    private  View inactive;
    private latobold single2;
    private latobold jodi;
    private latonormal rate;
    private latonormal bidHistory;
    private latonormal resultHistory;
    private latonormal chart;
    private ImageView gif;
    private CardView layout;
    private TextView name,result, info,top, play_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delhi_games2);
      //  gif = findViewById(R.id.gifNew);
       // Glide.with(this).load(R.drawable.giff).into(gif);
        initViews();
        apicall();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        resultHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DelhiGames.this, ledger.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

//        bidHistory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(DelhiGames.this, BidHistory.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//            }
//        });

        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DelhiGames.this, charts.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("href","https://satta-king-online.in/result-chart.php"));
            }
        });

    }


    private void apicall() {

        viewDialog2 = new ViewDialog(DelhiGames.this);
        viewDialog2.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(DelhiGames.this);

        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);
                        try {

                            JSONObject jsonObject1 = new JSONObject(response);


                            ArrayList<String> name2 = new ArrayList<>();
                            ArrayList<String> result2 = new ArrayList<>();
                            ArrayList<String> result3 = new ArrayList<>();


                            ArrayList<String> is_open2 = new ArrayList<>();
                            ArrayList<String> open_time2 = new ArrayList<>();
                            ArrayList<String> close_time2 = new ArrayList<>();
                            ArrayList<String> open_av2 = new ArrayList<>();
                            ArrayList<String> market_type2 = new ArrayList<>();
                            ArrayList<String> result_time = new ArrayList<>();
                            ArrayList<String> mOpen = new ArrayList<>();
                            ArrayList<String> mClose = new ArrayList<>();

                            JSONArray jsonArray2 = jsonObject1.getJSONArray("result2");
                            for (int a = 0; a < jsonArray2.length(); a++) {
                                JSONObject jsonObject = jsonArray2.getJSONObject(a);

                                open_time2.add(jsonObject.getString("open_time"));
                                close_time2.add(jsonObject.getString("close_time"));

                                name2.add(jsonObject.getString("market"));
                                result2.add(jsonObject.getString("result"));
                                result3.add(jsonObject.getString("result3"));
                                is_open2.add(jsonObject.getString("is_open"));
                                open_av2.add(jsonObject.getString("is_close"));
                                market_type2.add(jsonObject.getString("market_type"));
                                result_time.add(jsonObject.getString("result_time"));
                                mOpen.add(jsonObject.getString("mOpen"));
                                mClose.add(jsonObject.getString("mClose"));

                            }


                            adapter_result_delhi rc2 = new adapter_result_delhi(DelhiGames.this, name2, result2,result3, is_open2, open_time2, close_time2, open_av2, market_type2,result_time,mOpen,mClose);
                            recycler.setLayoutManager(new GridLayoutManager(DelhiGames.this, 1));
                            recycler.setAdapter(rc2);

                            single.setText(jsonObject1.getString("single"));
                            single2.setText(jsonObject1.getString("single"));
                            jodi.setText(jsonObject1.getString("jodi"));

                            viewDialog2.hideDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            viewDialog2.hideDialog();
                            Toast.makeText(DelhiGames.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();

                        viewDialog2.hideDialog();
                        Toast.makeText(DelhiGames.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile", null));
                params.put("session", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }


    private void initViews() {
        back = findViewById(R.id.back);
        recycler = findViewById(R.id.recycler);
        single = findViewById(R.id.delhi_single);
        single2 = findViewById(R.id.single2);
        jodi = findViewById(R.id.delhi_jodi);
        rate = findViewById(R.id.rate);
        bidHistory = findViewById(R.id.bid_history);
        resultHistory = findViewById(R.id.result_history);
        chart = findViewById(R.id.chart);
       // gif = findViewById(R.id.gifNew);
        name = findViewById(R.id.ntitle);
        result = findViewById(R.id.result);
        info = findViewById(R.id.info);
        time_info = findViewById(R.id.time_info);
        play_game = findViewById(R.id.play_game);
        layout = findViewById(R.id.layoutj);
        top = findViewById(R.id.top);
        inactive = findViewById(R.id.inactive);
        play_text = findViewById(R.id.play_text);
    }
}