package com.samratmatka.android;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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

public class ChartMenu extends AppCompatActivity {

    RecyclerView recyclerview;
    EditText search;
    String url;

    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> result = new ArrayList<>();
    ArrayList<String> type = new ArrayList<>();
    ViewDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_menu);
        initViews();
        url = constant.prefix + "get_markets";
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        apiCall();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!search.getText().toString().isEmpty()){
                    ArrayList<String> name2 = new ArrayList<>();
                    ArrayList<String> result2 = new ArrayList<>();

                    for (int a =0; a < name.size(); a++)
                    {
                        if (name.get(a).toLowerCase().contains(s.toString().toLowerCase())){
                            name2.add(name.get(a));
                            result2.add(result.get(a));
                        }
                    }

                    AdapterChartItem rc = new AdapterChartItem(ChartMenu.this, name2, result2, new ArrayList<>());
                    recyclerview.setLayoutManager(new GridLayoutManager(ChartMenu.this, 1));
                    recyclerview.setAdapter(rc);
                    rc.notifyDataSetChanged();
                }

                else  {
                    AdapterChartItem rc = new AdapterChartItem(ChartMenu.this, name, result, new ArrayList<>());
                    recyclerview.setLayoutManager(new GridLayoutManager(ChartMenu.this, 1));
                    recyclerview.setAdapter(rc);
                    rc.notifyDataSetChanged();
                }
            }
        });
    }


    private void apiCall() {
        progressDialog = new ViewDialog(ChartMenu.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            JSONArray jsonArray = jsonObject1.getJSONArray("data");

                            for (int a = 0; a < jsonArray.length(); a++){
                                JSONObject jsonObject = jsonArray.getJSONObject(a);

                                name.add(jsonObject.getString("market"));
                                result.add(jsonObject.getString("market_id"));
                                type.add(jsonObject.getString("type"));
                            }

                            AdapterChartItem rc = new AdapterChartItem(ChartMenu.this, name, result, type);
                            recyclerview.setLayoutManager(new GridLayoutManager(ChartMenu.this, 1));
                            recyclerview.setAdapter(rc);
                            rc.notifyDataSetChanged();

                        } catch (JSONException e) {
                            progressDialog.hideDialog();
                            Toast.makeText(ChartMenu.this, getString(R.string.api_error_msg), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hideDialog();
                        Toast.makeText(ChartMenu.this, getString(R.string.api_error_msg), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("session",getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    private void initViews() {
        recyclerview = findViewById(R.id.recyclerview);
        search = findViewById(R.id.search);
    }
}