package com.tripleseven.android;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Statistics extends AppCompatActivity {

    private RelativeLayout back;
    private latonormal balanceHome;
    private LinearLayout walletBlock;
    private RelativeLayout toolbar;
    private latobold marketName;
    private latobold time;
    private RecyclerView openRecycler;
    private RecyclerView closeRecycler;
    private RecyclerView jodiRecycler;

    ViewDialog progressDialog;
    String url = constant.prefix+"get_stats.php";
    String market;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        market = getIntent().getStringExtra("market");
        initViews();

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        marketName.setText(market);

        time.setText(currentDateandTime);

        apicall();
    }


    private void apicall() {

        progressDialog = new ViewDialog(Statistics.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("res",response);
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);

                            ArrayList<String> number = new ArrayList<>();
                            ArrayList<String> percent = new ArrayList<>();

                            if (jsonObject1.has("open")){
                                JSONArray jsonArray = jsonObject1.getJSONArray("open");
                                for (int a = 0; jsonArray.length() > a; a++) {
                                    JSONObject jsonObject2 = jsonArray.getJSONObject(a);

                                    number.add(jsonObject2.getString("number"));
                                    percent.add(jsonObject2.getString("stats"));
                                }
                            }

                            adapter_stats rc = new adapter_stats(Statistics.this, number, percent);
                            openRecycler.setLayoutManager(new GridLayoutManager(Statistics.this, 1));
                            openRecycler.setAdapter(rc);


                            ArrayList<String> number2 = new ArrayList<>();
                            ArrayList<String> percent2 = new ArrayList<>();

                            if (jsonObject1.has("close")){
                                JSONArray jsonArray = jsonObject1.getJSONArray("close");
                                for (int a = 0; jsonArray.length() > a; a++) {
                                    JSONObject jsonObject2 = jsonArray.getJSONObject(a);

                                    number2.add(jsonObject2.getString("number"));
                                    percent2.add(jsonObject2.getString("stats"));
                                }
                            }

                            adapter_stats rc2 = new adapter_stats(Statistics.this, number2, percent2);
                            closeRecycler.setLayoutManager(new GridLayoutManager(Statistics.this, 1));
                            closeRecycler.setAdapter(rc2);


                            ArrayList<String> number3 = new ArrayList<>();
                            ArrayList<String> percent3 = new ArrayList<>();

                            if (jsonObject1.has("jodi")){
                                JSONArray jsonArray = jsonObject1.getJSONArray("jodi");
                                for (int a = 0; jsonArray.length() > a; a++) {
                                    JSONObject jsonObject2 = jsonArray.getJSONObject(a);

                                    number3.add(jsonObject2.getString("number"));
                                    percent3.add(jsonObject2.getString("stats"));
                                }
                            }

                            adapter_stats rc3 = new adapter_stats(Statistics.this, number3, percent3);
                            jodiRecycler.setLayoutManager(new GridLayoutManager(Statistics.this, 1));
                            jodiRecycler.setAdapter(rc3);

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
                        Toast.makeText(Statistics.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile", null));
                params.put("market",market);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    private void initViews() {
        back = findViewById(R.id.back);
        balanceHome = findViewById(R.id.balance_home);
        walletBlock = findViewById(R.id.wallet_block);
        toolbar = findViewById(R.id.toolbar);
        marketName = findViewById(R.id.market_name);
        time = findViewById(R.id.nmsg);
        openRecycler = findViewById(R.id.open_recycler);
        closeRecycler = findViewById(R.id.close_recycler);
        jodiRecycler = findViewById(R.id.jodi_recycler);
    }
}