package com.samratmatka.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

import lib.kingja.switchbutton.SwitchMultiButton;

public class withdraw extends AppCompatActivity {


    ViewDialog progressDialog;
    String url, withdraw_request;
    ArrayList<String> gateways = new ArrayList<>();
    String gateway = "";
    SwitchMultiButton mSwitchMultiButton;
    EditText amount, info;
    Spinner mode;
    ViewDialog viewDialog;

    ArrayList<String> payment_mode = new ArrayList<>();
    ArrayList<String> payment_info = new ArrayList<>();
    private CardView toolbar;
    private latobold submit, winning;
    private latonormal holderName;
    private latonormal bank;
    private latonormal ac;
    private latonormal ifsc;
    private latobold change;
    private LinearLayout whatsapp;

    String withdrawInfo = "";
    private LinearLayout verified;
    private LinearLayout error;
    private ImageView back;
    private latonormal balanceHome;
    private LinearLayout walletBlock;
    private ImageView walletColorIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        initViews();
        url = constant.prefix2 + getString(R.string.withdraw_modes);
        withdraw_request = constant.prefix2 + getString(R.string.withdraw_request);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        info = findViewById(R.id.info);
        amount = findViewById(R.id.amount2);
        mode = findViewById(R.id.mode);
        winning.setText("₹ "+getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("winning","0"));

        balanceHome.setText("₹"+getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("wallet","0"));

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(withdraw.this, change_bank.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        Log.e("wall", getSharedPreferences(constant.prefs, Context.MODE_PRIVATE).getString("wallet", "0"));

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (mode.getSelectedItemPosition() == 0) {
//                    Toast.makeText(withdraw.this, "Select Payment method", Toast.LENGTH_SHORT).show();
//                } else if (!mode.getSelectedItem().toString().equals("Bank Account") && info.getText().toString().isEmpty()) {
//                    info.setError("Enter valid withdraw information");
//
//                }
                //  else

                 if (amount.getText().toString().isEmpty() || amount.getText().toString().equals("0")) {
                    amount.setError("Enter valid amount");
                } else if (Integer.parseInt(amount.getText().toString()) < Integer.parseInt(getSharedPreferences(constant.prefs, Context.MODE_PRIVATE).getString("min_withdraw", "500"))) {
                    amount.setError("amount must be more than " + getSharedPreferences(constant.prefs, Context.MODE_PRIVATE).getString("min_withdraw", constant.min_deposit + ""));
                } else if (Integer.parseInt(amount.getText().toString()) > Integer.parseInt(getSharedPreferences(constant.prefs, Context.MODE_PRIVATE).getString("winning", "0"))) {
                    amount.setError("You don't have enough amount balance in winning wallet");
                } else {
                    if (mode.getSelectedItem().toString().equals("Bank Account")){
                        if (ac.getText().toString().equals("Not Set")) {
                            Toast.makeText(withdraw.this, "Update bank info first", Toast.LENGTH_SHORT).show();
                        } else {
                        withdrawInfo = "Bank Name - "+bank.getText().toString();
                        withdrawInfo += "<br>Account holder Name - "+holderName.getText().toString();
                        withdrawInfo += "<br>Account number - "+ac.getText().toString();
                        withdrawInfo += "<br>IFSC Code - "+ifsc.getText().toString();
                        apicall();
                       }
                    } else {
                        withdrawInfo = info.getText().toString();
                        apicall();
                    }
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        get_apicall();
    }

    private void get_apicall() {

        progressDialog = new ViewDialog(withdraw.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("edsa", "efsdc" + response);
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);

                            payment_mode.add("Select Payment Mode");

                            JSONArray jsonArray = jsonObject1.getJSONArray("data");
                            for (int a = 0; jsonArray.length() > a; a++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(a);

                                payment_info.add(jsonObject.getString("hint"));
                                payment_mode.add(jsonObject.getString("name"));
                            }

                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                    (withdraw.this, R.layout.simple_list_item_1,
                                            payment_mode);

                            mode.setAdapter(spinnerArrayAdapter);

                            mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (position != 0) {
                                        info.setHint(payment_info.get(position - 1));
                                        if (mode.getSelectedItem().toString().equals("Bank Account")){
                                            info.setVisibility(View.GONE);
                                        } else {
                                            info.setVisibility(View.VISIBLE);
                                        }
                                    } else {
                                        info.setHint("Select Payment Method");
                                    }


                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                            if (jsonObject1.getString("is_bank").equals("1")) {
                                verified.setVisibility(View.VISIBLE);
                                error.setVisibility(View.GONE);
                                holderName.setText(jsonObject1.getJSONObject("bank").getString("holder"));
                                ac.setText(jsonObject1.getJSONObject("bank").getString("ac"));
                                ifsc.setText(jsonObject1.getJSONObject("bank").getString("ifsc"));
                                bank.setText(jsonObject1.getJSONObject("bank").getString("bank"));

                                change.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(withdraw.this, change_bank.class)
                                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                .putExtra("ac", ac.getText().toString())
                                                .putExtra("holder", holderName.getText().toString())
                                                .putExtra("name", bank.getText().toString())
                                                .putExtra("ifsc", ifsc.getText().toString())
                                        );
                                    }
                                });
                            } else {
                                verified.setVisibility(View.GONE);
                                error.setVisibility(View.VISIBLE);
                                change.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(withdraw.this, change_bank.class)
                                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        );
                                    }
                                });
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
                        Toast.makeText(withdraw.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile", null));
                params.put("session", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    private void apicall() {

        progressDialog = new ViewDialog(withdraw.this);
        progressDialog.showDialog();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, withdraw_request,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            if (!jsonObject1.getString("success").equals("1")) {
                                Toast.makeText(withdraw.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                            }

                            if (jsonObject1.getString("active").equals("0")) {
                                Toast.makeText(withdraw.this, "Your account temporarily disabled by admin", Toast.LENGTH_SHORT).show();

                                getSharedPreferences(constant.prefs, MODE_PRIVATE).edit().clear().apply();
                                Intent in = new Intent(getApplicationContext(), login.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                finish();
                            }


                            if (jsonObject1.getString("success").equalsIgnoreCase("1")) {
                                SharedPreferences.Editor editor = getSharedPreferences(constant.prefs, MODE_PRIVATE).edit();
                                System.out.println(jsonObject1);
                                editor.putString("winning", jsonObject1.getString("winning")).apply();


                                android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(withdraw.this);
                                LayoutInflater factory = LayoutInflater.from(withdraw.this);
                                View v2 = factory.inflate(R.layout.msg_dialog, null);

                                TextView close = v2.findViewById(R.id.close);
                                TextView msgView = v2.findViewById(R.id.msg);
                                msgView.setText(jsonObject1.getString("msg"));

                                builder1.setView(v2);
                                builder1.setCancelable(false);
                                android.app.AlertDialog alert11 = builder1.create();

                                close.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alert11.dismiss();
                                      //  getSharedPreferences(constant.prefs, MODE_PRIVATE).edit().clear().apply();
                                        Intent in = new Intent(getApplicationContext(), HomeScreen.class);
                                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(in);
                                        finish();
                                    }
                                });
                                alert11.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                alert11.show();

                            } else {
                                android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(withdraw.this);
                                LayoutInflater factory = LayoutInflater.from(withdraw.this);
                                View v2 = factory.inflate(R.layout.msg_dialog_error, null);

                                TextView close = v2.findViewById(R.id.close);
                                TextView msgView = v2.findViewById(R.id.msg);
                                msgView.setText(jsonObject1.getString("msg"));

                                builder1.setView(v2);
                                builder1.setCancelable(false);
                                android.app.AlertDialog alert11 = builder1.create();

                                close.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alert11.dismiss();
                                    }
                                });
                                alert11.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                alert11.show();

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
                        Toast.makeText(withdraw.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile", null));
                params.put("session", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));
                params.put("amount", amount.getText().toString());
                params.put("mode", mode.getSelectedItem().toString());
                params.put("info", withdrawInfo);


                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        amount = findViewById(R.id.amount2);
        mode = findViewById(R.id.mode);
        info = findViewById(R.id.info);
        submit = findViewById(R.id.submit);
        holderName = findViewById(R.id.holder_name);
        winning = findViewById(R.id.w_amount);
        bank = findViewById(R.id.bank);
        ac = findViewById(R.id.ac);
        ifsc = findViewById(R.id.ifsc);
        change = findViewById(R.id.change);
        whatsapp = findViewById(R.id.whatsapp);
        verified = findViewById(R.id.verified);
        error = findViewById(R.id.error);
        back = findViewById(R.id.back);
        balanceHome = findViewById(R.id.balance_home);
        walletBlock = findViewById(R.id.wallet_block);
        walletColorIcon = findViewById(R.id.wallet_color_icon);
    }
}