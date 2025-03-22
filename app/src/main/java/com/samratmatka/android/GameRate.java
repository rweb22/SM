package com.samratmatka.android;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GameRate extends AppCompatActivity {
    protected latonormal delhiJodi;
    protected latonormal delhiHarf;

    protected latonormal single;

    protected latonormal jodi;
    protected latonormal singlePatti;
    protected latonormal doublePatti;
    protected latonormal tripePatti;
    protected latonormal halfSangam;
    protected latonormal fullSangam;
    ViewDialog progressDialog;
    String rateApiUrl ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_rate);
        initView();
        rateApiUrl = constant.prefix + getString(R.string.rate);
        findViewById(R.id.back).setOnClickListener(v -> finish());
        apiCall();
    }


    private void apiCall() {
        progressDialog = new ViewDialog(GameRate.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, rateApiUrl,
                response -> {
                    progressDialog.hideDialog();
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        if (responseObject.has("rates")) {
                            JSONObject rates = responseObject.getJSONObject("rates");
                            System.out.println(response);
                            if (rates.has("TWO_DIGIT")) {
                                JSONObject twoDigit = rates.getJSONObject("TWO_DIGIT");
                                delhiJodi.setText(String.format("Jodi - %s", twoDigit.getString("JODI")));
                                delhiHarf.setText(String.format("Harf - %s", twoDigit.getString("HARF")));
                            }

                            if (rates.has("THREE_DIGIT")) {
                                JSONObject threeDigit = rates.getJSONObject("THREE_DIGIT");
                                System.out.println(threeDigit);
                                single.setText(String.format("Single - %s", threeDigit.getString("SINGLE")));
                                jodi.setText(String.format("Jodi - %s", threeDigit.getString("JODI")));
                                singlePatti.setText(String.format("Single Panna - %s", threeDigit.getString("SP")));
                                doublePatti.setText(String.format("Double Panna - %s", threeDigit.getString("DP")));
                                tripePatti.setText(String.format("Triple Panna - %s", threeDigit.getString("TP")));
                                halfSangam.setText(String.format("Half Sangam - %s", threeDigit.getString("HALF_SANGAM")));
                                fullSangam.setText(String.format("Full Sangam - %s", threeDigit.getString("FULL_SANGAM")));
                            }
                        }
                    } catch (JSONException e) {
                        progressDialog.hideDialog();
                    }
                },
                error -> {
                    progressDialog.hideDialog();
                    Toast.makeText(GameRate.this, getString(R.string.api_error_msg), Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return new HashMap<>();
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }


    private void initView() {
        delhiJodi = findViewById(R.id.delhi_jodi_rate);
        delhiHarf = findViewById(R.id.delhi_harf_rate);
        single = findViewById(R.id.single_rate);
        jodi = findViewById(R.id.jodi_rate);
        singlePatti = findViewById(R.id.sp_rate);
        doublePatti = findViewById(R.id.dp_rate);
        tripePatti = findViewById(R.id.tp_rate);
        halfSangam = findViewById(R.id.halfsangam_rate);
        fullSangam = findViewById(R.id.fullsangam_rate);
     }
}
