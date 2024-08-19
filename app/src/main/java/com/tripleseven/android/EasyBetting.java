package com.tripleseven.android;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class EasyBetting extends Fragment {

    private RecyclerView recyclerview;
    private EditText totalamount;
    private latobold submit;

    SharedPreferences prefs;
    ArrayList<String> list;
    ArrayList<String> number = new ArrayList<>();
    adapterbetting adapterbetting;
    String market,game,timin,type = "";
    ViewDialog progressDialog;
    String url;
    int total = 0;
    ArrayList<String> fillnumber = new ArrayList<>();
    ArrayList<String> fillamount = new ArrayList<>();
    String numb,amou;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_easy_betting, container, false);
        initViews(view);

        url = constant.prefix2 + getString(R.string.bet);
        if (getActivity().getIntent().hasExtra("timin")){
            timin = getActivity().getIntent().getStringExtra("timin");
        }

        prefs = getActivity().getSharedPreferences(constant.prefs,MODE_PRIVATE);

        game = getActivity().getIntent().getStringExtra("game");
        market = getActivity().getIntent().getStringExtra("market");
        number = getActivity().getIntent().getStringArrayListExtra("list");

        adapterbetting = new adapterbetting(getActivity(), number);

        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                list = adapterbetting.getNumber();
                total = 0;
                for (int a = 0; a < list.size(); a++) {
                    total = total+Integer.parseInt(list.get(a));
                }
                totalamount.setText(total+"");
            }
        };

        IntentFilter intentFilter = new IntentFilter("android.intent.action.MAIN");
        getActivity().registerReceiver(mReceiver, intentFilter);

        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerview.setAdapter(adapterbetting);
        adapterbetting.notifyDataSetChanged();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("list",list.toString());

                Log.e("wallet",prefs.getString("wallet","0"));
                Log.e("winning",prefs.getString("winning","0"));
                Log.e("bonus",prefs.getString("bonus","0"));
                Log.e("total",total+"");

                if (total < constant.min_total || total > constant.max_total)
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setMessage("You can only bet between "+constant.min_total+" coins to "+constant.max_total+" INR");
                    builder1.setCancelable(true);
                    builder1.setNegativeButton(
                            "Okay",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }

                else if (total <= (Integer.parseInt(prefs.getString("wallet","0"))+Integer.parseInt(prefs.getString("winning","0"))+Integer.parseInt(prefs.getString("bonus","0")))) {
                    fillamount.clear();
                    fillnumber.clear();

                    for (int a = 0; a < list.size(); a++) {
                        if (!list.get(a).equals("0") && Integer.parseInt(list.get(a)) < constant.min_single || Integer.parseInt(list.get(a)) > constant.max_single){
                            fillamount.clear();
                            fillnumber.clear();

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                            builder1.setMessage("You can only bet between "+constant.min_single+" coins to "+constant.max_single+" coins");
                            builder1.setCancelable(true);
                            builder1.setNegativeButton(
                                    "Okay",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();

                            return;
                        }
                        else if (!list.get(a).equals("0")) {
                            fillnumber.add(number.get(a) + "");
                            fillamount.add(list.get(a));
                        }
                    }


                    android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(getActivity());
                    LayoutInflater factory = LayoutInflater.from(getActivity());
                    View v2 = factory.inflate(R.layout.bet_confirm, null);
                    builder1.setView(v2);
                    builder1.setCancelable(true);
                    android.app.AlertDialog alert11 = builder1.create();

                    TextView title = v2.findViewById(R.id.title);
                    RecyclerView recycler = v2.findViewById(R.id.recycler);
                    TextView total_bid = v2.findViewById(R.id.total_bid);
                    TextView total_amount = v2.findViewById(R.id.total_amount);
                    TextView cancel = v2.findViewById(R.id.cancel);
                    TextView submit = v2.findViewById(R.id.submit);

                    title.setText(market);

                    total_bid.setText(fillnumber.size()+"");
                    total_amount.setText(total+"");

                    String type = "";
                    if (market.contains("OPEN")){
                        type = "Open";
                    } else if (market.contains("CLOSE")){
                        type = "Close";
                    } else {
                        type = "N/A";
                    }

                    AdapterSingleGamesConfirm adapterSingleGamesConfirm = new AdapterSingleGamesConfirm(getActivity(), fillnumber, fillamount, type);
                    recycler.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                    recycler.setAdapter(adapterSingleGamesConfirm);

                    submit.setOnClickListener(v1 -> {
                        alert11.dismiss();
                        numb = TextUtils.join(",", fillnumber);
                        amou = TextUtils.join(",", fillamount);
                        apicall();
                    });

                    cancel.setOnClickListener(v12 -> alert11.dismiss());
                    alert11.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    alert11.show();

                }
                else
                {
                    Log.e("mybal",(Integer.parseInt(prefs.getString("wallet",null))+Integer.parseInt(prefs.getString("winning",null))+Integer.parseInt(prefs.getString("bonus",null)))+"");
                    Log.e("req",total+"");
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setMessage("You don't have enough wallet balance to place this bet, Recharge your wallet to play");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Recharge",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (getActivity().getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("is_gateway","0").equals("1")){
                                        startActivity(new Intent(getActivity(), wallet.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                    } else {
                                        openWhatsApp();
                                    }
                                    dialog.dismiss();
                                }
                            });

                    builder1.setNegativeButton(
                            "Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
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
                getActivity().getSharedPreferences(constant.prefs, MODE_PRIVATE),
                Request.Method.POST, url,
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
                                        getActivity().getSharedPreferences(constant.prefs, MODE_PRIVATE).edit().clear().apply();
                                        Intent in = new Intent(getActivity(), HomeScreen.class);
                                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

                params.put("session",getActivity().getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    private void openWhatsApp() {

        String url = constant.getWhatsapp(getActivity());

        Uri uri = Uri.parse(url);
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(sendIntent);
    }

    private void initViews(View view) {
        recyclerview = view.findViewById(R.id.recyclerview);
        totalamount = view.findViewById(R.id.totalamount);
        submit = view.findViewById(R.id.submit);
    }
}