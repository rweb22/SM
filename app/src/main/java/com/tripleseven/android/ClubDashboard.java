package com.tripleseven.android;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
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
import com.google.gson.Gson;
import com.tripleseven.android.dto.ClubDashboardApiResponse;
import com.tripleseven.android.dto.ClubDto;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ClubDashboard extends AppCompatActivity {
    static public latobold marketName;
    private RecyclerView marketItemRecView;
    private RelativeLayout backButton;
    private ClubDto clubDto;
    private static final String marketListApiUrl = constant.prefix2 + "club/dashboard";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_dashboard);
        this.clubDto = (ClubDto) getIntent().getSerializableExtra("clubDto");
        assert clubDto != null;
        initViews(clubDto);
        getMarketList();
    }

    private void getMarketList()  {
        ViewDialog loadingViewDialog = new ViewDialog(ClubDashboard.this);
        loadingViewDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final StringRequest postRequest = new MyStringRequest(
                getSharedPreferences(constant.prefs, MODE_PRIVATE),
                Request.Method.POST,
                marketListApiUrl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            if (!jsonObject1.getString(constant.SUCCESS).equals(constant.ONE)) {
                                Toast.makeText(getApplicationContext(), jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                            } else {
                                if (jsonObject1.getString(constant.SUCCESS).equals(constant.ZERO)) {
                                    Toast.makeText(getApplicationContext(), R.string.ACCOUNT_DISABLE_ALERT, Toast.LENGTH_SHORT).show();
                                    getSharedPreferences(constant.prefs, MODE_PRIVATE).edit().clear().apply();

                                    Intent in = new Intent(getApplicationContext(), signup.class);
                                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(in);

                                    finish();
                                }

                                if (jsonObject1.getString("logout").equals(constant.ONE)) {
                                    getSharedPreferences(constant.prefs, MODE_PRIVATE).edit().clear().apply();

                                    Intent in = new Intent(getApplicationContext(), login.class);
                                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(in);
                                    finish();
                                }

                                Gson gson = new Gson();
                                ClubDashboardApiResponse apiResponse = gson.fromJson(response,
                                        ClubDashboardApiResponse.class);

                                AdapterMarketItem marketItemAdapter = new AdapterMarketItem(getApplicationContext(),
                                        apiResponse.getMarkets(), apiResponse.getClubType());
                                marketItemRecView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                                marketItemRecView.setAdapter(marketItemAdapter);
                                loadingViewDialog.hideDialog();
                            }
                        } catch (JSONException e) {
                            loadingViewDialog.hideDialog();
                            Toast.makeText(getApplicationContext(), R.string.api_error_msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingViewDialog.hideDialog();
                        Toast.makeText(getApplicationContext(), R.string.api_error_msg, Toast.LENGTH_SHORT).show();
                        loadingViewDialog.hideDialog();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("clubId", clubDto.getId());
                params.put("mobile", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile", null));
                params.put("session", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));

                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }


    @Override
    protected void onResume() {
        TextView tx = findViewById(R.id.balance_home);
        tx.setText((Integer.parseInt(getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("wallet","0")))+" ₹");
        getMarketList();
        super.onResume();
    }

    private void initViews(ClubDto club) {
        TextView tx = findViewById(R.id.balance_home);
        tx.setText((Integer.parseInt(getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("wallet","0")))+" ₹");
        marketName = findViewById(R.id.club_name);
        marketName.setText(club.getName());

        LinearLayout walletBlock = findViewById(R.id.wallet_block);
        walletBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ClubDashboard.this, wallet.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        marketItemRecView = findViewById(R.id.market_item_recycler);
        backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
