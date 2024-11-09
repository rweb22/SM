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
import android.text.TextWatcher;
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
import com.google.gson.Gson;
import com.tripleseven.android.dto.GameOption;
import com.tripleseven.android.dto.GameType;
import com.tripleseven.android.dto.MarketDto;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SpecialMode extends Fragment {
    SharedPreferences prefs;
    ArrayList<String> numbers = new ArrayList<>();
    GameOption game;
    MarketDto market;
    ViewDialog progressDialog;
    String url;
    int total = 0;
    ArrayList<String> fillNumber = new ArrayList<>();
    ArrayList<String> fillAmount = new ArrayList<>();
    ArrayList<GameType> fillGameTypes = new ArrayList<>();

    ArrayList<Item> betItems = new ArrayList<>();

    public static class Item {
        public String number1;
        public String gameType;
        public String amount;
    }

    private AutoCompleteTextView number;
    private EditText amount;
    private latobold add;
    private RecyclerView recyclerview;
    private EditText totalamount;
    private latobold submit;
    String session;

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


        market = (MarketDto) requireActivity().getIntent().getSerializableExtra("market");
        url = constant.prefix2 + getString(R.string.bet);

        prefs = requireActivity().getSharedPreferences(constant.prefs,MODE_PRIVATE);
        game = GameOption.valueOf(requireActivity().getIntent().getStringExtra("game"));
        numbers = requireActivity().getIntent().getStringArrayListExtra("digits");
        session = requireActivity().getIntent().getStringExtra("session");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (requireActivity(), R.layout.simple_list_item_2, numbers);
        number.setAdapter(adapter);

        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.toString().isEmpty()) {
                    // DO NOTHING FIELD IS EMPTY
                } else if (Integer.parseInt(s.toString()) > constant.max_single) {
                    amount.setText(constant.max_single + "");
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
                String type = intent.getStringExtra("type");
                System.out.println("Number in special mode : " + num);
                System.out.println("type in special mode : " + type);

                if (Objects.isNull(num) || fillNumber.isEmpty() || "panna".equalsIgnoreCase(type)) {
                    return;
                }

                System.out.println(fillNumber.toString() + " : " + fillAmount.toString());

                fillAmount.remove(Integer.parseInt(num));
                fillNumber.remove(Integer.parseInt(num));
                fillGameTypes.remove(Integer.parseInt(num));

                AdapterSingleGames rc = new AdapterSingleGames(getActivity(), fillNumber, fillAmount, fillGameTypes);
                recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                recyclerview.setAdapter(rc);
                rc.notifyDataSetChanged();


                total = 0;
                for (int a = 0; a < fillAmount.size(); a++) {
                    total = total+Integer.parseInt(fillAmount.get(a));
                }
                totalamount.setText(total+"");
            }
        };

        IntentFilter intentFilter = new IntentFilter("android.intent.action.MAIN");
        getActivity().registerReceiver(mReceiver, intentFilter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number.getText().toString().isEmpty() || !numbers.contains(number.getText().toString())) {
                    number.setError(getString(R.string.invalid_number));
                } else if (amount.getText().toString().isEmpty() || Integer.parseInt(amount.getText().toString()) < constant.min_single) {
                    amount.setError(getString(R.string.invalid_number) + " " + constant.min_single+" - "+constant.max_single);
                } else {
                    fillNumber.add(number.getText().toString());
                    fillAmount.add(amount.getText().toString());
                    fillGameTypes.add(GameOption.toGameType(game));

                    AdapterSingleGames rc = new AdapterSingleGames(getActivity(), fillNumber, fillAmount, fillGameTypes);
                    recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                    recyclerview.setAdapter(rc);
                    rc.notifyDataSetChanged();


                    total = 0;
                    for (int a = 0; a < fillAmount.size(); a++) {
                        total = total+Integer.parseInt(fillAmount.get(a));
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
                if (fillNumber.size() > 0){
                    if (total <= (Integer.parseInt(prefs.getString("wallet", String.valueOf(0)))+Integer.parseInt(prefs.getString("winning",String.valueOf(0)))+Integer.parseInt(prefs.getString("bonus",String.valueOf(0))))) {
                        apicall();
                    }
                    else
                    {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                        builder1.setMessage(getString(R.string.insufficient_balance));
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
                    Toast.makeText(getActivity(), getString(R.string.place_bet_first), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private GameType getGameType() {
        return null;
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
                                android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(getActivity());
                                LayoutInflater factory = LayoutInflater.from(getActivity());
                                View v = factory.inflate(R.layout.msg_dialog, null);

                                TextView close = v.findViewById(R.id.close);
                                TextView msgView = v.findViewById(R.id.msg);
                                msgView.setText(R.string.bet_placed);

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
                                        in.putExtra("market",constant.market);
                                        in.putExtra("is_open",constant.is_open);
                                        in.putExtra("is_close",constant.is_close);
                                        in.putExtra("market_type",constant.market_type);
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

                for(int i=0; i < fillNumber.size(); i++) {
                    Item item = new Item();
                    item.number1 = fillNumber.get(i);
                    item.gameType = fillGameTypes.get(i).getId();
                    item.amount = fillAmount.get(i);

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

                System.out.println(params.toString());

                params.put("session", requireActivity().getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    private String getMarketName() {
        if (Objects.isNull(session) || session.isEmpty()) {
            return market.getName();
        } else {
            return market.getName() + " " + session.substring(0, 1).toUpperCase() + session.substring(1).toLowerCase();
        }
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