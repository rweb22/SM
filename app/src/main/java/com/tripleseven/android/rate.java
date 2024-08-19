package com.tripleseven.android;

import android.os.Bundle;
import android.view.View;
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

public class rate extends AppCompatActivity {

    protected latonormal text;
    protected latonormal single;
    protected latonormal doublegame;
    protected latonormal singlepatti;
    protected latonormal doublepatti;
    protected latonormal tripepatti;
    protected latonormal halfsangam;
    protected latonormal fullsangam;

    protected latonormal st_single;

    protected latonormal st_singlepatti;
    protected latonormal st_doublepatti;
    protected latonormal st_tripepatti;

    ViewDialog progressDialog;
    String url ;
    String url2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_rate);
        initView();
        url = constant.prefix + getString(R.string.rate);
        url2 = constant.prefix + "star_rate.php";
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        doublegame.setText("Jodi : ₹1000 KA ₹95000");
        singlepatti.setText("Harf : ₹1000 KA ₹9500");
        //apicall();
        //apicall2();
    }


    private void apicall() {

        progressDialog = new ViewDialog(rate.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);

                            single.setText("Single - "+jsonObject1.getString("single"));
                            doublepatti.setText("Double Panna - "+jsonObject1.getString("doublepatti"));
                            tripepatti.setText("Triple Panna - "+jsonObject1.getString("triplepatti"));
                            halfsangam.setText("Half Sangam - "+jsonObject1.getString("halfsangam"));
                            fullsangam.setText("Full Sangam - "+jsonObject1.getString("fullsangam"));


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
                        Toast.makeText(rate.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }


                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }


    private void apicall2() {

//        progressDialog = new ViewDialog(rate.this);
//        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);

                            st_single.setText("Single - "+jsonObject1.getString("single"));

                            st_singlepatti.setText("Single Panna - "+jsonObject1.getString("singlepatti"));
                            st_doublepatti.setText("Double Panna - "+jsonObject1.getString("doublepatti"));
                            st_tripepatti.setText("Triple Panna - "+jsonObject1.getString("triplepatti"));
                            progressDialog.hideDialog();



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
                        Toast.makeText(rate.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }


                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }


    private void initView() {
        text = (latonormal) findViewById(R.id.text);
        single = (latonormal) findViewById(R.id.single);
        doublegame = (latonormal) findViewById(R.id.doublegame);
        singlepatti = (latonormal) findViewById(R.id.singlepatti);
        doublepatti = (latonormal) findViewById(R.id.doublepatti);
        tripepatti = (latonormal) findViewById(R.id.tripepatti);
        halfsangam = (latonormal) findViewById(R.id.halfsangam);
        fullsangam = (latonormal) findViewById(R.id.fullsangam);


        st_single = (latonormal) findViewById(R.id.st_single);

        st_singlepatti = (latonormal) findViewById(R.id.st_singlepatti);
        st_doublepatti = (latonormal) findViewById(R.id.st_doublepatti);
        st_tripepatti = (latonormal) findViewById(R.id.st_tripepatti);
     }
}
