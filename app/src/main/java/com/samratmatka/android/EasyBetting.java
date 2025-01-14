package com.samratmatka.android;

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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samratmatka.android.dto.BetItem;
import com.samratmatka.android.dto.GameOption;
import com.samratmatka.android.dto.GameType;
import com.samratmatka.android.dto.MarketDto;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EasyBetting extends Fragment {

    private RecyclerView recyclerview;
    private EditText totalamount;
    private latobold submit;

    SharedPreferences prefs;
    ArrayList<String> amountList;
    ArrayList<String> digits = new ArrayList<>();
    AdapterBetItem adapterBetItem;
    GameOption game;
    MarketDto market;
    ViewDialog progressDialog;
    String url;
    int total = 0;
    ArrayList<String> fillNumber = new ArrayList<>();
    ArrayList<String> fillAmount = new ArrayList<>();
    String session;

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

        prefs = requireActivity().getSharedPreferences(constant.prefs,MODE_PRIVATE);
        game = GameOption.valueOf(requireActivity().getIntent().getStringExtra("game"));
        market = (MarketDto) requireActivity().getIntent().getSerializableExtra("market");
        session = requireActivity().getIntent().getStringExtra("session");
        digits = requireActivity().getIntent().getStringArrayListExtra("digits");

        adapterBetItem = new AdapterBetItem(getActivity(), digits);
        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                amountList = adapterBetItem.getAmountList();
                total = 0;
                for (int a = 0; a < amountList.size(); a++) {
                    total = total + Integer.parseInt(amountList.get(a));
                }
                totalamount.setText(total + "");
            }
        };

        IntentFilter intentFilter = new IntentFilter("android.intent.action.MAIN");
        getActivity().registerReceiver(mReceiver, intentFilter);

        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerview.setAdapter(adapterBetItem);
        adapterBetItem.notifyDataSetChanged();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (total < constant.min_total || total > constant.max_total)
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setMessage(getString(R.string.amount_between) + " " + constant.min_total+" coins to "+constant.max_total+" INR");
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
                    fillAmount.clear();
                    fillNumber.clear();

                    for (int a = 0; a < amountList.size(); a++) {
                        if (!amountList.get(a).equals("0") && Integer.parseInt(amountList.get(a)) < constant.min_single || Integer.parseInt(amountList.get(a)) > constant.max_single){
                            fillAmount.clear();
                            fillNumber.clear();

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                            builder1.setMessage(getString(R.string.amount_between)+" "+constant.min_single+" coins to "+constant.max_single+" coins");
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
                        else if (!amountList.get(a).equals("0")) {
                            fillNumber.add(digits.get(a) + "");
                            fillAmount.add(amountList.get(a));
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

                    title.setText(getMarketName());

                    total_bid.setText(fillNumber.size()+"");
                    total_amount.setText(total+"");

                    AdapterSingleGamesConfirm adapterSingleGamesConfirm = new AdapterSingleGamesConfirm(getActivity(), fillNumber, fillAmount,
                            GameOption.toGameType(game).getDisplayName());
                    recycler.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                    recycler.setAdapter(adapterSingleGamesConfirm);

                    submit.setOnClickListener(v1 -> {
                        alert11.dismiss();
                        apicall();
                    });

                    cancel.setOnClickListener(v12 -> alert11.dismiss());
                    alert11.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    alert11.show();

                }
                else
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setMessage(getString(R.string.insufficient_balance));
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Recharge",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(new Intent(getActivity(), wallet.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
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

    private String getMarketName() {
        String session = requireActivity().getIntent().getStringExtra("session");
        if (Objects.isNull(session) || session.isEmpty()) {
            return market.getName();
        } else {
            return market.getName() + " " + session.substring(0, 1).toUpperCase() + session.substring(1).toLowerCase();
        }
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
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            if (jsonObject1.getString("active").equals("0")) {
                                Toast.makeText(getActivity(), getString(R.string.ACCOUNT_DISABLE_ALERT), Toast.LENGTH_SHORT).show();

                                getActivity().getSharedPreferences(constant.prefs, MODE_PRIVATE).edit().clear().apply();
                                Intent in = new Intent(getActivity(), signup.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                getActivity().finish();
                            }


                            if (jsonObject1.getString("success").equalsIgnoreCase("1")) {
                                fillNumber.clear();
                                fillAmount.clear();
                                android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(getActivity());
                                LayoutInflater factory = LayoutInflater.from(getActivity());
                                View v = factory.inflate(R.layout.msg_dialog, null);

                                TextView close = v.findViewById(R.id.close);
                                TextView msgView = v.findViewById(R.id.msg);
                                msgView.setText(getString(R.string.bet_placed));

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
                                        //startActivity(in);
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
                                        //startActivity(in);
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
                            Toast.makeText(getActivity(), getString(R.string.api_error_msg), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressDialog.hideDialog();
                        Toast.makeText(getActivity(), getString(R.string.api_error_msg), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                ArrayList<BetItem> betItems = new ArrayList<>();

                for (int i = 0; i < fillNumber.size(); i++) {
                    BetItem item = new BetItem();
                    item.setNumber1(fillNumber.get(i));
                    item.setAmount(fillAmount.get(i));
                    item.setGameType(Objects.requireNonNull(GameOption.toGameType(game)).getId());

                    betItems.add(item);
                }


                Gson gson = new Gson();
                String betItemsString = gson.toJson(betItems);
                System.out.println(betItemsString);

                params.put("items", betItemsString);
                params.put("market_id", market.getMarketId());
                params.put("market_name", market.getName());
                params.put("total",total+"");
                params.put("game_option", game.getId());
                params.put("game_session", session);
                params.put("mobile", prefs.getString("mobile",null));
                params.put("session", requireActivity().getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));

                System.out.println(params.toString());
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    private void initViews(View view) {
        recyclerview = view.findViewById(R.id.recyclerview);
        totalamount = view.findViewById(R.id.totalamount);
        submit = view.findViewById(R.id.submit);
    }
}