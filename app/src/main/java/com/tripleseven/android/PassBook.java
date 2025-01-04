package com.tripleseven.android;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class PassBook extends AppCompatActivity {

    protected RecyclerView recyclerview;
    ViewDialog progressDialog;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_ledger);
        initView();
        url = constant.prefix2 + "get_transactions";
        findViewById(R.id.back).setOnClickListener(v -> finish());
        apiCall();
    }

    private void apiCall() {
        progressDialog = new ViewDialog(PassBook.this);
        progressDialog.showDialog();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url,
                response -> {
                    progressDialog.hideDialog();
                    try {
                        JSONObject jsonObject1 = new JSONObject(response);

                        ArrayList<String> type = new ArrayList<>();
                        ArrayList<String> date = new ArrayList<>();
                        ArrayList<String> remark = new ArrayList<>();
                        ArrayList<String> amount = new ArrayList<>();
                        ArrayList<String> status = new ArrayList<>();

                        JSONArray jsonArray = jsonObject1.getJSONArray("data");
                        for (int a= 0; jsonArray.length()>a;a++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(a);

                            date.add(jsonObject.getString("date"));
                            amount.add(jsonObject.getString("amount"));
                            remark.add(jsonObject.getString("remark"));
                            type.add(jsonObject.getString("type"));
                            status.add(jsonObject.getString("status"));

                            AdapterTransactionItem rc = new AdapterTransactionItem(PassBook.this,date,remark,amount,type, status);
                            recyclerview.setLayoutManager(new GridLayoutManager(PassBook.this, 1));
                            recyclerview.setAdapter(rc);
                            rc.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        progressDialog.hideDialog();
                        Toast.makeText(PassBook.this, getString(R.string.api_error_msg), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    progressDialog.hideDialog();
                    Toast.makeText(PassBook.this, getString(R.string.api_error_msg), Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("mobile",null));
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }


    private void initView() {
        recyclerview = findViewById(R.id.recyclerview);
    }
}
