package com.tripleseven.android;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class change_bank extends AppCompatActivity {

    private EditText name;
    private EditText holder;
    private EditText ac;
    private EditText ifsc;
    private latobold submit;
    ViewDialog progressDialog;
    String url = constant.prefix2 +"update_bank_details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_bank);
        initViews();

        findViewById(R.id.back).setOnClickListener(v -> onBackPressed());

        if (getIntent().hasExtra("ac")){
            ac.setText(getIntent().getStringExtra("ac"));
            ifsc.setText(getIntent().getStringExtra("ifsc"));
            holder.setText(getIntent().getStringExtra("holder"));
            name.setText(getIntent().getStringExtra("name"));
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ac.getText().toString().isEmpty()){
                    ac.setError("Enter valid details");
                } else if (ifsc.getText().toString().isEmpty()){
                    ifsc.setError("Enter valid details");
                } else if (holder.getText().toString().isEmpty()){
                    holder.setError("Enter valid details");
                } else if (name.getText().toString().isEmpty()){
                    name.setError("Enter valid details");
                } else {
                    apicall();
                }
            }
        });
    }


    private void apicall() {

        progressDialog = new ViewDialog(change_bank.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                            if (jsonObject1.getString("success").equalsIgnoreCase("1")) {

                                finish();

                            }
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
                        Toast.makeText(change_bank.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("holder", holder.getText().toString());
                params.put("ac", ac.getText().toString());
                params.put("ifsc", ifsc.getText().toString());
                params.put("bank", name.getText().toString());
                params.put("mobile", getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("mobile",null));

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    private void initViews() {
        name = findViewById(R.id.ntitle);
        holder = findViewById(R.id.holder);
        ac = findViewById(R.id.ac);
        ifsc = findViewById(R.id.ifsc);
        submit = findViewById(R.id.submit);
    }
}