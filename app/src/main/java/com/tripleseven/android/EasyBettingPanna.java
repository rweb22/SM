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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.tripleseven.android.dto.GameOption;
import com.tripleseven.android.dto.GameType;
import com.tripleseven.android.dto.MarketDto;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EasyBettingPanna extends Fragment {
    TextView lastSelected;
    private RecyclerView recyclerview;
    private EditText totalAmount;
    private latobold submit;
    ArrayList<TextView> tab = new ArrayList<>();
    ArrayList<String> numbers = new ArrayList<>();
    ArrayList<String> AllNumbers = new ArrayList<>();
    SharedPreferences prefs;
    ArrayList<String> amountList;
    ArrayList<String> digits = new ArrayList<>();
    AdapterBetItem2 adapterBetItem;
    GameOption game;
    MarketDto market;
    String url;
    int total = 0;
    ArrayList<String> fillNumber = new ArrayList<>();
    ArrayList<String> fillAmount = new ArrayList<>();

    String numb, amou;
    private LinearLayout tabs;

    String session;

    public static class Item {
        public String number1;
        public String gameType;
        public String amount;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_easy_betting_panna, container, false);
        initViews(view);

        url = constant.prefix2 + getString(R.string.bet);

        prefs = requireActivity().getSharedPreferences(constant.prefs, MODE_PRIVATE);
        game = GameOption.valueOf(requireActivity().getIntent().getStringExtra("game"));
        market = (MarketDto) requireActivity().getIntent().getSerializableExtra("market");
        digits = requireActivity().getIntent().getStringArrayListExtra("digits");
        session = requireActivity().getIntent().getStringExtra("session");

        AllNumbers = digits;

        for (int index = 0; index < 10; index++) {
            latobold textView = new latobold(getActivity());
            textView.setText(index + "");
            textView.setTextColor(getResources().getColor(R.color.font));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
            layoutParams.setMargins(0,10,0,10);
            textView.setLayoutParams(layoutParams);
            textView.setBackground(getResources().getDrawable(R.drawable.button_not));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setCurrentGroup(textView);
                }
            });
            tab.add(textView);
            tabs.addView(textView);
        }

        setCurrentGroup(tab.get(0));

        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                amountList = adapterBetItem.getAmountList();
                System.out.println("Number in Easy Betting Pana");
                for (int a = 0; a < amountList.size(); a++) {
                    if (!amountList.get(a).equals("0")){
                        if (fillNumber.contains(numbers.get(a))) {
                            fillAmount.set(fillNumber.indexOf(numbers.get(a)), amountList.get(a));
                            fillNumber.set(fillNumber.indexOf(numbers.get(a)), numbers.get(a));
                        } else {
                            fillAmount.add(amountList.get(a));
                            fillNumber.add(numbers.get(a));
                        }
                    }

                    total = total + Integer.parseInt(amountList.get(a));
                }

                total = 0;
                for (int a = 0; a < fillAmount.size(); a++) {
                    total = total + Integer.parseInt(fillAmount.get(a));
                }
                totalAmount.setText(total + "");
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
                Log.e("total", total + "");

                if (total < constant.min_total || total > constant.max_total) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setMessage("You can only bet between " + constant.min_total + " coins to " + constant.max_total + " INR");
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
                } else if (total <= (Integer.parseInt(prefs.getString("wallet", "0")) + Integer.parseInt(prefs.getString("winning", "0")) + Integer.parseInt(prefs.getString("bonus", "0")))) {
                    for (int a = 0; a < amountList.size(); a++) {
                        if (!amountList.get(a).equals("0") && Integer.parseInt(amountList.get(a)) < constant.min_single || Integer.parseInt(amountList.get(a)) > constant.max_single) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                            builder1.setMessage("You can only bet between " + constant.min_single + " coins to " + constant.max_single + " coins");
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
                        } else if (!amountList.get(a).equals("0")) {
//                            fillnumber.add(number.get(a) + "");
//                            fillamount.add(list.get(a));
                        }
                    }


                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    LayoutInflater factory = LayoutInflater.from(getActivity());
                    View v2 = factory.inflate(R.layout.bet_confirm, null);
                    builder1.setView(v2);
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();

                    TextView title = v2.findViewById(R.id.title);
                    RecyclerView recycler = v2.findViewById(R.id.recycler);
                    TextView total_bid = v2.findViewById(R.id.total_bid);
                    TextView total_amount = v2.findViewById(R.id.total_amount);
                    TextView cancel = v2.findViewById(R.id.cancel);
                    TextView submit = v2.findViewById(R.id.submit);

                    title.setText(getMarketName());

                    total_bid.setText(fillNumber.size() + "");
                    total_amount.setText(total + "");

                    AdapterSingleGamesConfirm adapterSingleGamesConfirm = new AdapterSingleGamesConfirm(getActivity(), fillNumber, fillAmount,
                            GameOption.toGameType(game).getDisplayName());
                    recycler.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                    recycler.setAdapter(adapterSingleGamesConfirm);

                    submit.setOnClickListener(v1 -> {
                        alert11.dismiss();
                        numb = TextUtils.join(",", fillNumber);
                        amou = TextUtils.join(",", fillAmount);
                        apicall();
                    });

                    cancel.setOnClickListener(v12 -> alert11.dismiss());
                    alert11.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    alert11.show();

                } else {
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
        String session = getActivity().getIntent().getStringExtra("session");
        if (Objects.isNull(session) || session.isEmpty()) {
            return market.getName();
        } else {
            return market.getName() + " " + session.substring(0, 1).toUpperCase() + session.substring(1).toLowerCase();
        }
    }


    private void apicall() {
        ViewDialog progressDialog = new ViewDialog((AppCompatActivity) getActivity());
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
                                Toast.makeText(getActivity(), getString(R.string.ACCOUNT_DISABLE_ALERT), Toast.LENGTH_SHORT).show();
                                requireActivity().getSharedPreferences(constant.prefs, MODE_PRIVATE).edit().clear().apply();
                                Intent in = new Intent(getActivity(), signup.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                requireActivity().finish();
                            }


                            if (jsonObject1.getString("success").equalsIgnoreCase("1")) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                                LayoutInflater factory = LayoutInflater.from(getActivity());
                                View v = factory.inflate(R.layout.msg_dialog, null);

                                TextView close = v.findViewById(R.id.close);
                                TextView msgView = v.findViewById(R.id.msg);
                                msgView.setText(getString(R.string.bet_placed));

                                builder1.setView(v);
                                builder1.setCancelable(false);
                                AlertDialog alert11 = builder1.create();
                                alert11.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        Intent in = new Intent(getActivity(), HomeScreen.class);
                                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        in.putExtra("market", constant.market);
                                        in.putExtra("is_open", constant.is_open);
                                        in.putExtra("is_close", constant.is_close);
                                        in.putExtra("market_type", constant.market_type);
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
                                        in.putExtra("market", constant.market);
                                        in.putExtra("is_open", constant.is_open);
                                        in.putExtra("is_close", constant.is_close);
                                        in.putExtra("market_type", constant.market_type);
                                        startActivity(in);
                                        requireActivity().finish();
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

                ArrayList<Item> betItems = new ArrayList<>();
                for(int i=0; i < fillNumber.size(); i++) {
                    Item item = new Item();
                    item.number1 = fillNumber.get(i);
                    item.amount = fillAmount.get(i);
                    item.gameType = GameOption.toGameType(game).getId();
                    betItems.add(item);
                }

                Gson gson = new Gson();
                String betItemsString = gson.toJson(betItems);

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

    public void setCurrentGroup(TextView textView) {
        if (lastSelected != null){
            lastSelected.setBackground(getResources().getDrawable(R.drawable.button_not));
            lastSelected.setTextColor(getResources().getColor(R.color.font));
        }

        lastSelected = textView;

        textView.setBackground(getResources().getDrawable(R.drawable.hor_button));
        textView.setTextColor(getResources().getColor(R.color.md_white_1000));
        numbers = new ArrayList<>(AllNumbers);
        numbers.retainAll(getGroup(textView.getText().toString()));

        adapterBetItem = new AdapterBetItem2(getActivity(), numbers, fillNumber, fillAmount);
        recyclerview.setAdapter(adapterBetItem);
        adapterBetItem.notifyDataSetChanged();
    }


    public ArrayList<String> getGroup(String group) {
        String[] groupData = null;
        switch (group) {
            case "0":
                groupData = "118, 226, 244, 299, 334, 488, 550, 668, 677, 127, 136, 145, 190, 235, 280, 370, 389, 460, 479, 569, 578".split(", ");
                break;
            case "1":
                groupData = "100, 119, 155, 227, 335, 344, 399, 588, 669, 128, 137, 146, 236, 245, 290, 380, 470, 489, 560, 579, 678".split(", ");
                break;
            case "2":
                groupData = "129, 138, 147, 156, 237, 246, 345, 390, 480, 570, 589, 679, 110, 200, 228, 255, 336, 499, 660, 688, 778".split(", ");
                break;
            case "3":
                groupData = "166, 229, 300, 337, 355, 445, 599, 779, 788, 120, 139, 148, 157, 238, 247, 256, 346, 490, 580, 670, 689".split(", ");
                break;
            case "4":
                groupData = "130, 149, 158, 167, 239, 248, 257, 347, 356, 590, 680, 789, 112, 220, 266, 338, 400, 446, 455, 699, 770".split(", ");
                break;
            case "5":
                groupData = "140, 159, 168, 230, 249, 258, 267, 348, 357, 456, 690, 780, 113, 122, 177, 339, 366, 447, 500, 799, 889".split(", ");
                break;
            case "6":
                groupData = "123, 150, 169, 178, 240, 259, 268, 349, 358, 367, 457, 790, 600, 114, 277, 330, 448, 466, 556, 880, 899".split(", ");
                break;
            case "7":
                groupData = "124, 160, 278, 179, 250, 269, 340, 359, 368, 458, 467, 890, 115, 133, 188, 223, 377, 449, 557, 566, 700".split(", ");
                break;
            case "8":
                groupData = "125, 134, 170, 189, 260, 279, 350, 369, 468, 378, 459, 567, 116, 224, 233, 288, 440, 477, 558, 800, 990".split(", ");
                break;
            case "9":
                groupData = "126, 135, 180, 234, 270, 289, 360, 379, 450, 469, 478, 568, 117, 144, 199, 225, 388, 559, 577, 667, 900".split(", ");
                break;
        }
        if (groupData != null) {
            return new ArrayList(Arrays.asList(groupData));
        }
        return null;
    }

    private void initViews(View view) {
        recyclerview = view.findViewById(R.id.recyclerview);
        totalAmount = view.findViewById(R.id.totalamount);
        submit = view.findViewById(R.id.submit);
        tabs = view.findViewById(R.id.tabs);
    }
}