package com.tripleseven.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WithdrawHistory extends AppCompatActivity {

    private CardView back;
    private TextView title;
    private View toolbar;
    private RecyclerView recycler;

    ViewDialog progressDialog;
    final String url = constant.prefix + "withdraw_requests.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_history);
        initViews();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        apiCall();
    }

    private void apiCall() {

        progressDialog = new ViewDialog(WithdrawHistory.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url,
                response -> {
                    progressDialog.hideDialog();
                    try {
                        JSONObject jsonObject1 = new JSONObject(response);

                        JSONArray jsonArray = jsonObject1.getJSONArray("data");

                        ArrayList<WithdrawModel> withdraws = new ArrayList<>();

                        for (int a = 0; a < jsonArray.length(); a++){

                            JSONObject jsonObject = jsonArray.getJSONObject(a);

                            withdraws.add(new WithdrawModel(jsonObject.getString("mode"),jsonObject.getString("info"),jsonObject.getString("info"),jsonObject.getString("status"),jsonObject.getString("amount"),jsonObject.getString("date")));

                        }

                        AdapterWithdraw rc = new AdapterWithdraw(WithdrawHistory.this, withdraws);
                        recycler.setLayoutManager(new GridLayoutManager(WithdrawHistory.this, 1));
                        recycler.setAdapter(rc);
                        rc.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.hideDialog();
                    }
                },
                error -> {

                    error.printStackTrace();
                    progressDialog.hideDialog();
                    Toast.makeText(WithdrawHistory.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("mobile", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile", null));

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    private void initViews() {
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        toolbar = findViewById(R.id.toolbar);
        recycler = findViewById(R.id.recycler);
    }
}