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
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.util.Log;

public class played extends AppCompatActivity {

    protected RecyclerView recyclerview;
    ImageView img;
    ViewDialog progressDialog;
    String url, url2;
    ArrayList<String> market = new ArrayList<>();
    private RelativeLayout back;
    private latonormal balanceHome;
    private LinearLayout walletBlock;
    private RelativeLayout toolbar;
    private Spinner markets;

    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_played);
        initViews();
        initView();
      //  Glide.with(this).asGif().load(R.drawable.won).into(img);
        if (getIntent().hasExtra("type")){
            type = getIntent().getStringExtra("type");
        }
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
     //   constant.setMenu(played.this);

        market.add("Select Market");


        get_market();



    }


    private void get_market() {

        progressDialog = new ViewDialog(played.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);

                            if (jsonObject1.has("data")){
                                JSONArray jsonArray = jsonObject1.getJSONArray("data");
                                for (int a = 0; jsonArray.length() > a; a++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(a);

                                    market.add(jsonObject.getString("market"));
                                }
                            }

                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                    (played.this, R.layout.simple_list_item_3,
                                            market); //selected item will look like a spinner set from XML
                            spinnerArrayAdapter.setDropDownViewResource(R.layout
                                    .simple_list_item_4);
                            markets.setAdapter(spinnerArrayAdapter);


                            apicall("");

                            markets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (!markets.getSelectedItem().toString().equals("Select Market")) {
                                        apicall(market.get(position));
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
                        Toast.makeText(played.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile", null));

                if (!type.equals("")){
                    params.put("type",type);
                }

                Log.e("params",params.toString());

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    private void apicall(String market) {

        progressDialog = new ViewDialog(played.this);
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

                            ArrayList<String> date = new ArrayList<>();
                            ArrayList<ArrayList<playedModel>> models = new ArrayList<>();

                            if (jsonObject1.has("dates")){
                                JSONArray jsonArray = jsonObject1.getJSONArray("dates");
                                for (int a = 0; jsonArray.length() > a; a++) {


//                                    JSONObject jsonObject2 = jsonArray.getJSONObject(a);


                                    date.add(jsonArray.getString(a));

                                    ArrayList<playedModel> models1 = new ArrayList<>();

                                    JSONArray jsonArray1 = jsonObject1.getJSONArray(jsonArray.getString(a));
                                    for (int a2 = 0; jsonArray1.length() > a2; a2++) {
                                        JSONObject jsonObject = jsonArray1.getJSONObject(a2);

                                        playedModel playedModel = new playedModel();

                                        playedModel.setAmount(jsonObject.getString("amount"));
                                        playedModel.setNumber(jsonObject.getString("number"));
                                        playedModel.setDate(jsonObject.getString("date"));
                                        playedModel.setMarketName(jsonObject.getString("market"));

                                        String bgame = jsonObject.getString("game").replace("singlepatti","Single Pana");
                                        bgame = bgame.replace("doublepatti","Double Pana");
                                        bgame = bgame.replace("triplepatti","Triple Pana");
                                        playedModel.setBid_type(bgame);

                                        playedModel.setStatus(jsonObject.getString("status"));
                                        playedModel.setMsg(jsonObject.getString("msg"));
                                        playedModel.setSn(jsonObject.getString("sn"));

                                        models1.add(playedModel);
                                    }
                                    models.add(models1);
                                }
                            }

                            adapter_played_group rc = new adapter_played_group(played.this, date, models);
                            recyclerview.setLayoutManager(new GridLayoutManager(played.this, 1));
                            recyclerview.setAdapter(rc);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.hideDialog();

                            ArrayList<String> date = new ArrayList<>();
                            ArrayList<ArrayList<playedModel>> models = new ArrayList<>();

                            adapter_played_group rc = new adapter_played_group(played.this, date, models);
                            recyclerview.setLayoutManager(new GridLayoutManager(played.this, 1));
                            recyclerview.setAdapter(rc);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                        progressDialog.hideDialog();
                        Toast.makeText(played.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile", null));

                if (!market.equals("")) {
                    params.put("market", market);
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
