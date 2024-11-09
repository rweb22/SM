package com.tripleseven.android;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.util.Log;

public class BetHistory extends AppCompatActivity {

    protected RecyclerView recyclerview;
    ImageView img;
    ViewDialog progressDialog;
    String url, url2;
    ArrayList<String> marketNames = new ArrayList<>();
    ArrayList<String> marketIds = new ArrayList<>();
    private RelativeLayout back;
    private latonormal balanceHome;
    private LinearLayout walletBlock;
    private RelativeLayout toolbar;
    private Spinner markets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_played);
        initViews();
        initView();
        url = constant.prefix2 + getString(R.string.games);
        url2 = constant.prefix2 + "get_markets";
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView tx = findViewById(R.id.balance_home);
        tx.setText((Integer.parseInt(getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("wallet","0")))+" ₹");

        marketNames.add("Select Market");
        get_market();
    }


    private void get_market() {

        progressDialog = new ViewDialog(BetHistory.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);

                            if (jsonObject1.has("data")) {
                                JSONArray jsonArray = jsonObject1.getJSONArray("data");
                                for (int a = 0; jsonArray.length() > a; a++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(a);
                                    marketNames.add(jsonObject.getString("market"));
                                    marketIds.add(jsonObject.getString("market_id"));
                                }
                            }

                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                    (BetHistory.this, R.layout.simple_list_item_3,
                                            marketNames); //selected item will look like a spinner set from XML
                            spinnerArrayAdapter.setDropDownViewResource(R.layout
                                    .simple_list_item_4);
                            markets.setAdapter(spinnerArrayAdapter);


                            apiCall("");

                            markets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (!markets.getSelectedItem().toString().equals("Select Market")) {
                                        apiCall(marketNames.get(position));
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

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
                        Toast.makeText(BetHistory.this, getString(R.string.api_error_msg), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile", null));
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    private void apiCall(String market) {
        progressDialog = new ViewDialog(BetHistory.this);
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

                            ArrayList<String> dates = new ArrayList<>();
                            ArrayList<ArrayList<BetModel>> models = new ArrayList<>();

                            if (jsonObject1.has("dates")){
                                JSONArray jsonArray = jsonObject1.getJSONArray("dates");
                                for (int a = 0; jsonArray.length() > a; a++) {
                                    dates.add(jsonArray.getString(a));
                                    ArrayList<BetModel> models1 = new ArrayList<>();

                                    JSONArray jsonArray1 = jsonObject1.getJSONArray(jsonArray.getString(a));
                                    for (int a2 = 0; jsonArray1.length() > a2; a2++) {
                                        JSONObject jsonObject = jsonArray1.getJSONObject(a2);

                                        BetModel BetModel = new BetModel();

                                        BetModel.setAmount(jsonObject.getString("amount"));
                                        BetModel.setNumber(jsonObject.getString("number"));
                                        BetModel.setDate(jsonObject.getString("date"));
                                        BetModel.setMarketName(jsonObject.getString("market"));
                                        BetModel.setBid_type(jsonObject.getString("game_name"));
                                        BetModel.setStatus(jsonObject.getString("status"));
                                        BetModel.setMsg(jsonObject.getString("msg"));
                                        BetModel.setSn(jsonObject.getString("sn"));

                                        models1.add(BetModel);
                                    }
                                    models.add(models1);
                                }
                            }

                            AdapterBetHistoryItemsGroup rc = new AdapterBetHistoryItemsGroup(BetHistory.this, dates, models);
                            recyclerview.setLayoutManager(new GridLayoutManager(BetHistory.this, 1));
                            recyclerview.setAdapter(rc);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.hideDialog();

                            ArrayList<String> dates = new ArrayList<>();
                            ArrayList<ArrayList<BetModel>> models = new ArrayList<>();

                            AdapterBetHistoryItemsGroup rc = new AdapterBetHistoryItemsGroup(BetHistory.this, dates, models);
                            recyclerview.setLayoutManager(new GridLayoutManager(BetHistory.this, 1));
                            recyclerview.setAdapter(rc);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressDialog.hideDialog();
                        Toast.makeText(BetHistory.this, getString(R.string.api_error_msg), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile", null));

                if (!market.isEmpty()) {
                    params.put("market_name", market);
                }

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }


    private void initView() {
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
    }

    private void initViews() {
        back = findViewById(R.id.back);
        balanceHome = findViewById(R.id.balance_home);
        walletBlock = findViewById(R.id.wallet_block);
        toolbar = findViewById(R.id.toolbar);
        markets = findViewById(R.id.markets);
        recyclerview = findViewById(R.id.recyclerview);
        img = findViewById(R.id.img);
    }
}
