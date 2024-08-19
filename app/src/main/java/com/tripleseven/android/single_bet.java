package com.tripleseven.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class single_bet extends AppCompatActivity {

    private CardView back;
    private Spinner type;
    private AutoCompleteTextView number;
    private EditText amount;
    private latobold add;
    private RecyclerView recyclerview;
    private EditText totalamount;
    private latobold submit;
    TextView title,balance,screenTitle;

    String open_av = "0";

    SharedPreferences prefs;
    ArrayList<String> list;
    ArrayList<String> numbers = new ArrayList<>();
    adapterbetting adapterbetting;
    String market,game;
    ViewDialog progressDialog;
    String url;
    int total = 0;
    ArrayList<String> fillnumber = new ArrayList<>();
    ArrayList<String> fillamount = new ArrayList<>();
    ArrayList<String> fillmarket = new ArrayList<>();
    String numb,amou,types;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singe_bet);
        initViews();
        open_av = getIntent().getStringExtra("open_av");
        url = constant.prefix2 + getString(R.string.bet);

        prefs = getSharedPreferences(constant.prefs,MODE_PRIVATE);
        game = getIntent().getStringExtra("game");
        market = getIntent().getStringExtra("market");
        numbers = getIntent().getStringArrayListExtra("list");

        title.setText(market.replace("_",""));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, R.layout.simple_list_item_2,numbers);
        number.setAdapter(adapter);

        if (!game.equals("jodi")){
            ArrayList<String> typeof = new ArrayList<>();

            if (open_av.equals("1")){ typeof.add("OPEN");}
            typeof.add("CLOSE");

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(single_bet.this, R.layout.simple_list_item_2, typeof);
            type.setAdapter(arrayAdapter);
        } else  {
            title.setVisibility(View.GONE);
            type.setVisibility(View.GONE);
        }


        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("PARSE INT" + s);
                if (s == null || s.toString().isEmpty()) {
                    // DO NOTHING FIELD IS EMPTY
                } else if (Integer.parseInt(s.toString()) > constant.max_single) {
                    amount.setText(constant.max_single+"");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String num = intent.getStringExtra("number");
                fillamount.remove(Integer.parseInt(num));
                fillnumber.remove(Integer.parseInt(num));
                fillmarket.remove(Integer.parseInt(num));

                AdapterSingleGames rc = new AdapterSingleGames(single_bet.this,fillnumber,fillamount,fillmarket);
                recyclerview.setLayoutManager(new GridLayoutManager(single_bet.this, 1));
                recyclerview.setAdapter(rc);
                rc.notifyDataSetChanged();


                total = 0;
                for (int a = 0; a < fillamount.size(); a++) {
                    total = total+Integer.parseInt(fillamount.get(a));
                }
                totalamount.setText(total+"");
            }
        };

        IntentFilter intentFilter = new IntentFilter("android.intent.action.MAIN");
        registerReceiver(mReceiver, intentFilter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number.getText().toString().isEmpty() || !numbers.contains(number.getText().toString())){
                    number.setError("Enter valid number");
                } else if (amount.getText().toString().isEmpty() || Integer.parseInt(amount.getText().toString()) < constant.min_single){
                    amount.setError("Enter amount between "+constant.min_single+" - "+constant.max_single);
                } else {
                    fillnumber.add(number.getText().toString());
                    fillamount.add(amount.getText().toString());
                    if (game.equals("jodi"))
                    {
                        fillmarket.add("");
                    }
                    else {
                        fillmarket.add(type.getSelectedItem().toString());
                    }

                    AdapterSingleGames rc = new AdapterSingleGames(single_bet.this,fillnumber,fillamount,fillmarket);
                    recyclerview.setLayoutManager(new GridLayoutManager(single_bet.this, 1));
                    recyclerview.setAdapter(rc);
                    rc.notifyDataSetChanged();


                    total = 0;
                    for (int a = 0; a < fillamount.size(); a++) {
                        total = total+Integer.parseInt(fillamount.get(a));
                    }
                    totalamount.setText(total+"");

                    number.setText("");
                    amount.setText("");

                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fillnumber.size() > 0){
                    if (total <= (Integer.parseInt(prefs.getString("wallet",null))+Integer.parseInt(prefs.getString("winning",null))+Integer.parseInt(prefs.getString("bonus",null)))) {
                        numb = "";
                        amou = "";
                        types = "";

                        numb = TextUtils.join(",", fillnumber);
                        amou = TextUtils.join(",", fillamount);
                        types = TextUtils.join(",", fillmarket);


                        apicall();
                    }
                    else
                    {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(single_bet.this);
                        builder1.setMessage("You don't have enough wallet balance to place this bet, Recharge your wallet to play");
                        builder1.setCancelable(true);
                        builder1.setNegativeButton(
                                "Close",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                } else {
                    Toast.makeText(single_bet.this, "Please place a bet first", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        balance.setText((Integer.parseInt(prefs.getString("wallet",null))+""));
        super.onResume();
    }

    private void apicall() {

        progressDialog = new ViewDialog(single_bet.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String response = null;

        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("edsa", "efsdc" + response);
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);

                            if (jsonObject1.getString("active").equals("0")) {
                                Toast.makeText(single_bet.this, "Your account temporarily disabled by admin", Toast.LENGTH_SHORT).show();

                                getSharedPreferences(constant.prefs, MODE_PRIVATE).edit().clear().apply();
                                Intent in = new Intent(getApplicationContext(), login.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                finish();
                            }

                            if (!jsonObject1.getString("session").equals(getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null))) {
                                Toast.makeText(single_bet.this, "Session expired ! Please login again", Toast.LENGTH_SHORT).show();

                                getSharedPreferences(constant.prefs, MODE_PRIVATE).edit().clear().apply();
                                Intent in = new Intent(getApplicationContext(), login.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                finish();
                            }

                            if (jsonObject1.getString("success").equalsIgnoreCase("1")) {

                                Intent in = new Intent(getApplicationContext(), thankyou.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                finish();

                            } else {
                                Toast.makeText(getApplicationContext(), jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(single_bet.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("number",numb);
                params.put("amount",amou);
                params.put("bazar",market);
                params.put("total",total+"");
                params.put("game",game);
                params.put("mobile", prefs.getString("mobile",null));
                params.put("types",types);

                params.put("session",getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    private void initViews() {
        back = findViewById(R.id.back);
        type = findViewById(R.id.type);
        number = findViewById(R.id.number);
        amount = findViewById(R.id.amount2);
        add = findViewById(R.id.add);
        recyclerview = findViewById(R.id.recyclerview);
        totalamount = findViewById(R.id.totalamount);
        submit = findViewById(R.id.submit);
        title = findViewById(R.id.title);
        balance = findViewById(R.id.balance);
        screenTitle = findViewById(R.id.title);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}