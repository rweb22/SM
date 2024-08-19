package com.tripleseven.android;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public class MarketList extends AppCompatActivity {

    ViewDialog progressDialog;
    String url = constant.prefix + "get_markets.php";;

    private RelativeLayout back;
    private latonormal balanceHome;
    private LinearLayout walletBlock;
    private RelativeLayout toolbar;
    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_list);
        initViews();

        TextView tx = findViewById(R.id.balance_home);
        tx.setText((Integer.parseInt(getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("wallet","0")))+" ₹");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        get_market();
    }


    private void get_market() {

        progressDialog = new ViewDialog(MarketList.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        
        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);

                            ArrayList<String> markets = new ArrayList<>();

                            if (jsonObject1.has("data")){
                                JSONArray jsonArray = jsonObject1.getJSONArray("data");
                                for (int a = 0; jsonArray.length() > a; a++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(a);

                                    markets.add(jsonObject.getString("market"));
                                }
                            }

                            adapter_market_list rc = new adapter_market_list(MarketList.this,markets);
                            recycler.setLayoutManager(new GridLayoutManager(MarketList.this, 1));
                            recycler.setAdapter(rc);


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
                        Toast.makeText(MarketList.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
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

    private void initViews() {
        back = findViewById(R.id.back);
        balanceHome = findViewById(R.id.balance_home);
        walletBlock = findViewById(R.id.wallet_block);
        toolbar = findViewById(R.id.toolbar);
        recycler = findViewById(R.id.recycler);
    }
}