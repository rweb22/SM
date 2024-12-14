package com.tripleseven.android;

import static android.content.Context.MODE_PRIVATE;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class SpecialMode extends Fragment {


    String open_av = "0";
    String open_type = "";

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

    private AutoCompleteTextView number;
    private EditText amount;
    private latobold add;
    private RecyclerView recyclerview;
    private EditText totalamount;
    private latobold submit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_special_mode, container, false);
        initViews(view);


        open_av = getActivity().getIntent().getStringExtra("open_av");
        url = constant.prefix2 + getString(R.string.bet);

        prefs = getActivity().getSharedPreferences(constant.prefs,MODE_PRIVATE);
        game = getActivity().getIntent().getStringExtra("game");
        market = getActivity().getIntent().getStringExtra("market");
        numbers = getActivity().getIntent().getStringArrayListExtra("list");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity(), R.layout.simple_list_item_2,numbers);
        number.setAdapter(adapter);

        if (!game.equals("jodi")){
            open_type = getActivity().getIntent().getStringExtra("open_type");
        } else  {
            open_type = "";
        }


        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty() || s == null) {
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
                String flag = intent.getStringExtra("game");

                if (!"ASG".equalsIgnoreCase(flag)) return;

                String num = intent.getStringExtra("number");
                fillamount.remove(Integer.parseInt(num));
                fillnumber.remove(Integer.parseInt(num));
                fillmarket.remove(Integer.parseInt(num));

                AdapterSingleGames rc = new AdapterSingleGames(getActivity(), fillnumber, fillamount,fillmarket);
                recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1));
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
        getActivity().registerReceiver(mReceiver, intentFilter);

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
                        fillmarket.add(open_type);
                    }

                    AdapterSingleGames rc = new AdapterSingleGames(getActivity(),fillnumber,fillamount,fillmarket);
                    recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1));
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
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
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
                    Toast.makeText(getActivity(), "Please place a bet first", Toast.LENGTH_SHORT).show();
                }
            }
        });



        return view;
    }


    private void apicall() {

        progressDialog = new ViewDialog((AppCompatActivity) getActivity());
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String response = null;

        final StringRequest postRequest = new MyStringRequest(
                getActivity().getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("edsa", "efsdc" + response);
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);

                            if (jsonObject1.getString("active").equals("0")) {
                                Toast.makeText(getActivity(), "Your account temporarily disabled by admin", Toast.LENGTH_SHORT).show();

                                getActivity().getSharedPreferences(constant.prefs, MODE_PRIVATE).edit().clear().apply();
                                Intent in = new Intent(getActivity(), signup.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                getActivity().finish();
                            }

//                            if (!jsonObject1.getString("session").equals(getActivity().getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null))) {
//                                Toast.makeText(getActivity(), "Session expired ! Please login again", Toast.LENGTH_SHORT).show();
//
//                                getActivity().getSharedPreferences(constant.prefs, MODE_PRIVATE).edit().clear().apply();
//                                Intent in = new Intent(getActivity(), signup.class);
//                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(in);
//                                getActivity().finish();
//                            }

                            if (jsonObject1.getString("success").equalsIgnoreCase("1")) {

                                android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(getActivity());
                                LayoutInflater factory = LayoutInflater.from(getActivity());
                                View v = factory.inflate(R.layout.msg_dialog, null);

                                TextView close = v.findViewById(R.id.close);
                                TextView msgView = v.findViewById(R.id.msg);
                                msgView.setText("Your bet placed successfully");

                                builder1.setView(v);
                                builder1.setCancelable(false);
                                android.app.AlertDialog alert11 = builder1.create();
                                alert11.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {

                                        Intent in = new Intent(getActivity(), HomeScreen.class);
                                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        in.putExtra("market",constant.market);
                                        in.putExtra("is_open",constant.is_open);
                                        in.putExtra("is_close",constant.is_close);
                                        in.putExtra("market_type",constant.market_type);
                                        startActivity(in);
                                        getActivity().finish();
                                    }
                                });
                                close.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alert11.dismiss();

                                        Intent in = new Intent(getActivity(), HomeScreen.class);
                                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        in.putExtra("market",constant.market);
                                        in.putExtra("is_open",constant.is_open);
                                        in.putExtra("is_close",constant.is_close);
                                        in.putExtra("market_type",constant.market_type);
                                        startActivity(in);
                                        getActivity().finish();
                                    }
                                });
                                alert11.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                alert11.show();

                            } else {
                                Toast.makeText(getActivity(), jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_SHORT).show();
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

                params.put("session",getActivity().getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    private void initViews(View view) {
        number = view.findViewById(R.id.number);
        amount = view.findViewById(R.id.amount2);
        add = view.findViewById(R.id.add);
        recyclerview = view.findViewById(R.id.recyclerview);
        totalamount = view.findViewById(R.id.totalamount);
        submit = view.findViewById(R.id.submit);
    }
}