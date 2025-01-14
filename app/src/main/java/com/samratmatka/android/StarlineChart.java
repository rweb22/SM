package com.samratmatka.android;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
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

public class StarlineChart extends AppCompatActivity {

    private ImageView back;
    private CardView toolbar;
    private RecyclerView recyclerview;
    String marketName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starline_chart);
        initViews();
        marketName = getIntent().getStringExtra("market");

        back.setOnClickListener(v -> onBackPressed());

        apicall();
    }


    private void apicall() {

        ViewDialog progressDialog = new ViewDialog(StarlineChart.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, constant.prefix+"get_starline_chart.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hideDialog();
                        Log.e("edsa", "efsdc" + response);
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);

                            ArrayList<String> date = new ArrayList<>();
                            ArrayList<ArrayList<String>> open = new ArrayList<>();
                            ArrayList<ArrayList<String>> close = new ArrayList<>();

                            JSONArray jsonArray = jsonObject1.getJSONArray("data");
                            for (int a =0; a < jsonArray.length(); a++){
                                JSONObject jsonObject = jsonArray.getJSONObject(a);

                                date.add(jsonObject.getString("date"));

                                ArrayList<String> single = new ArrayList<>();
                                ArrayList<String> panna = new ArrayList<>();

                                JSONArray jsonArray1 = jsonObject.getJSONArray("results");
                                for (int i = 0; i < jsonArray1.length(); i++){
                                    JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                                    single.add(jsonObject2.getString("number"));
                                    panna.add(jsonObject2.getString("panna"));
                                }

                                open.add(single);
                                close.add(panna);

                            }

                            AdapterStarlineChart rc = new AdapterStarlineChart(StarlineChart.this, date, open, close);
                            recyclerview.setLayoutManager(new GridLayoutManager(StarlineChart.this, 1));
                            recyclerview.setAdapter(rc);

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
                        Toast.makeText(StarlineChart.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("market", marketName);


                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }


    private void initViews() {
        back = findViewById(R.id.back);
        toolbar = findViewById(R.id.toolbar);
        recyclerview = findViewById(R.id.recyclerview);
    }
}