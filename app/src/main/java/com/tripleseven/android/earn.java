package com.tripleseven.android;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
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

public class earn extends AppCompatActivity {

    protected latobold code;
    protected latobold comm_text;
    protected latobold share;
    protected NestedScrollView scrollView;

    ViewDialog progressDialog;
    String url = constant.prefix + "refers";
    private RelativeLayout back;
    private latonormal balanceHome;
    private LinearLayout walletBlock;
    private RelativeLayout toolbar;
    private latobold totalRefer;
    private latobold totalEarning;
    private latobold easyText;
    private View easyLine;
    private LinearLayout easyMode;
    private latobold sepcialText;
    private View specialLine;
    private LinearLayout specialMode;
    private RecyclerView userRecycler;
    private RecyclerView transactions;
    private ImageView facebook;
    private ImageView telegram;
    private ImageView shareall;
    private ImageView whatsapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_earn);
        initViews();
        initView();
        code.setText(getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("code", null));

        TextView tx = findViewById(R.id.balance_home);
        tx.setText((Integer.parseInt(getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("wallet","0")))+" ₹");

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String appName = "com.facebook.katana";
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                myIntent.setPackage(appName);
                myIntent.putExtra(Intent.EXTRA_TEXT, "Download SAMRAT SATTA App and Play Matka Games in Genuine Safer App" +
                        "\n"+
                        "\nEnjoy the Features like Instant withdrawls Quick support system, Use it in your native language and get a signup bonus" +
                        "\n" +
                        "\nClick 'register via referral code' on signup page and insert referral code : " + getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("code", null) + "\n\nDownload link - " + constant.link);//
                startActivity(Intent.createChooser(myIntent, "Share with"));


//                Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.setType("text/plain");
//                Intent chooserIntent = Intent.createChooser(shareIntent, "Share");
//
//                // for 21+, we can use EXTRA_REPLACEMENT_EXTRAS to support the specific case of Facebook
//                // (only supports a link)
//                // >=21: facebook=link, other=text+link
//                // <=20: all=link
//
//                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Use this referral code " + getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("code", null) + " at signup, Download " + getString(R.string.app_name) + " and earn money at home, Download link - " + constant.link);
//                    Bundle facebookBundle = new Bundle();
//                    Bundle replacement = new Bundle();
//                    replacement.putBundle("com.facebook.katana", facebookBundle);
//                    chooserIntent.putExtra(Intent.EXTRA_REPLACEMENT_EXTRAS, replacement);
//
//
//                chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(chooserIntent);
            }
        });

        telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appName = "org.telegram.messenger";
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                myIntent.setPackage(appName);
                myIntent.putExtra(Intent.EXTRA_TEXT, "Download SAMRAT SATTA App and Play Matka Games in Genuine Safer App" +
                        "" +
                        "Enjoy the Features like Instant withdrawals Quick support system, Use it in your native language and get a signup bonus" +
                        "" +
                        "Click 'register via referral code' on signup page and insert referral code : " + getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("code", null) + " Download link - " + constant.link);//
                startActivity(Intent.createChooser(myIntent, "Share with"));
            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Download SAMRAT SATTA App and Play Matka Games in Genuine Safer App" +
                        "" +
                        "Enjoy the Features like Instant withdrawals Quick support system, Use it in your native language and get a signup bonus" +
                        "" +
                        "Click 'register via referral code' on signup page and insert referral code : " + getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("code", null) + " Download link - " + constant.link);//
                startActivity(Intent.createChooser(whatsappIntent, "Share with"));
                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(earn.this, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        shareall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Download SAMRAT SATTA App and Play Matka Games in Genuine Safer App" +
                                "" +
                                "Enjoy the Features like Instant withdrawals Quick support system, Use it in your native language and get a signup bonus" +
                                "" +
                                "Click 'register via referral code' on signup page and insert referral code : " + getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("code", null) + " Download link - " + constant.link);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Referral Code", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("code", null));
                clipboard.setPrimaryClip(clip);
//
//                Intent sendIntent = new Intent();
//                sendIntent.setAction(Intent.ACTION_SEND);
//                sendIntent.putExtra(Intent.EXTRA_TEXT,
//                        "Use this referral code " + getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("code", null) + " at signup, Download " + getString(R.string.app_name) + " and earn money at home, Download link - " + constant.link);
//                sendIntent.setType("text/plain");
//                startActivity(sendIntent);
            }
        });

        easyMode.setOnClickListener(v -> {
            scrollView.setVisibility(View.VISIBLE);
            transactions.setVisibility(View.GONE);
            easyText.setTextColor(getResources().getColor(R.color.red));
            easyLine.setBackgroundColor(getResources().getColor(R.color.red));
            specialLine.setBackgroundColor(getResources().getColor(R.color.white));
            sepcialText.setTextColor(getResources().getColor(R.color.font));
        });


        specialMode.setOnClickListener(v -> {
            scrollView.setVisibility(View.GONE);
            transactions.setVisibility(View.VISIBLE);
            easyText.setTextColor(getResources().getColor(R.color.font));
            easyLine.setBackgroundColor(getResources().getColor(R.color.white));
            specialLine.setBackgroundColor(getResources().getColor(R.color.red));
            sepcialText.setTextColor(getResources().getColor(R.color.red));
        });

        apicall();
    }


    private void apicall() {

        progressDialog = new ViewDialog(earn.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("res", response);
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);


                            ArrayList<String> mobile = new ArrayList<>();
                            ArrayList<String> name = new ArrayList<>();
                            ArrayList<String> date = new ArrayList<>();

                            if (jsonObject1.has("refer")) {
                                JSONArray jsonArray = jsonObject1.getJSONArray("refer");
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(a);

                                    mobile.add(jsonObject.getString("user"));
                                    name.add(jsonObject.getString("name"));
                                    date.add(jsonObject.getString("date"));
                                }
                            }

                            adapter_refer adapter_refer = new adapter_refer(earn.this, mobile, name, date);
                            userRecycler.setLayoutManager(new GridLayoutManager(earn.this, 1));
                            userRecycler.setAdapter(adapter_refer);

                            totalRefer.setText(mobile.size() + "");


                            ArrayList<String> date2 = new ArrayList<>();
                            ArrayList<String> remark = new ArrayList<>();
                            ArrayList<String> amount = new ArrayList<>();
                            ArrayList<String> type = new ArrayList<>();

                            if (jsonObject1.has("transaction")) {
                                JSONArray jsonArray = jsonObject1.getJSONArray("transaction");
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(a);

                                    remark.add(jsonObject.getString("remark"));
                                    amount.add(jsonObject.getString("amount"));
                                    type.add(jsonObject.getString("1"));
                                    date2.add(jsonObject.getString("date"));
                                }
                            }

                            adapter_transaction adapter_transaction = new adapter_transaction(earn.this, date2, remark, amount, type);
                            transactions.setLayoutManager(new GridLayoutManager(earn.this, 1));
                            transactions.setAdapter(adapter_transaction);

                            totalEarning.setText(jsonObject1.getString("total_amount") + "");

                            String text = "Life Time " + jsonObject1.getString("comm") +"% \nCommission on Every Deposit Made";
                            comm_text.setText(text);

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
                        Toast.makeText(earn.this, "Not able to fetch details. Contact support", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile", ""));

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }


    private void initView() {
        code = (latobold) findViewById(R.id.code);
        share = (latobold) findViewById(R.id.share);
        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
    }

    private void initViews() {
        back = findViewById(R.id.back);
        balanceHome = findViewById(R.id.balance_home);
        walletBlock = findViewById(R.id.wallet_block);
        toolbar = findViewById(R.id.toolbar);
        totalRefer = findViewById(R.id.total_refer);
        totalEarning = findViewById(R.id.total_earning);
        easyText = findViewById(R.id.easy_text);
        easyLine = findViewById(R.id.easy_line);
        easyMode = findViewById(R.id.easy_mode);
        sepcialText = findViewById(R.id.sepcial_text);
        specialLine = findViewById(R.id.special_line);
        specialMode = findViewById(R.id.special_mode);
        code = findViewById(R.id.code);
        comm_text = findViewById(R.id.comm_text);
        share = findViewById(R.id.share);
        userRecycler = findViewById(R.id.user_recycler);
        scrollView = findViewById(R.id.scrollView);
        transactions = findViewById(R.id.transactions);
        facebook = findViewById(R.id.facebook);
        telegram = findViewById(R.id.telegram);
        shareall = findViewById(R.id.shareall);
        whatsapp = findViewById(R.id.whatsapp);
    }
}
