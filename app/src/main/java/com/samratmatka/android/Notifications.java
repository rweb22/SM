package com.samratmatka.android;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

public class Notifications extends AppCompatActivity {

    RecyclerView recyclerview;
    ViewDialog progressDialog;
    String url2 ;
    String market = "";
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        initViews();
       // market = getIntent().getStringExtra("market");
        title.setText("Notification History");
        url2 = constant.prefix + "get_notification.php";
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        apicall2();
    }

    private void apicall2() {

        progressDialog = new ViewDialog(Notifications.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response",response);
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);

                            JSONArray jsonArray = jsonObject1.getJSONArray("data");

                            ArrayList<String> title = new ArrayList<>();
                            ArrayList<String> msg = new ArrayList<>();
                            ArrayList<String> date = new ArrayList<>();


                            for (int a = 0; a < jsonArray.length(); a++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(a);

                                title.add(jsonObject.getString("title"));
                                msg.add(jsonObject.getString("msg"));
                                date.add(jsonObject.getString("created_at"));

                            }

                            adapter_notification rc = new adapter_notification(Notifications.this,title,msg,date);
                            recyclerview.setLayoutManager(new GridLayoutManager(Notifications.this, 1));
                            recyclerview.setAdapter(rc);
                            rc.notifyDataSetChanged();


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
                        Toast.makeText(Notifications.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
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
        title = findViewById(R.id.title);
    }
}
