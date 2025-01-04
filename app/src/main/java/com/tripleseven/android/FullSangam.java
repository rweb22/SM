package com.tripleseven.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.tripleseven.android.dto.BetItem;
import com.tripleseven.android.dto.GameOption;
import com.tripleseven.android.dto.GameType;
import com.tripleseven.android.dto.MarketDto;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FullSangam extends AppCompatActivity {

    protected RelativeLayout toolbar;
    protected latonormal firstTitle;
    protected EditText first;
    protected latonormal secondTitle;
    protected EditText second;
    protected EditText totalAmount;
    protected latobold submit;
    protected ScrollView scrollView;

    ArrayList<String> patti = new ArrayList<>();

    GameOption game;
    MarketDto market;

    SharedPreferences prefs;

    ViewDialog progressDialog;
    String url;

    ArrayList<BetItem> betItems = new ArrayList<>();

    protected latonormal marketText;

    public static class Item {
        public String number1;
        public String number2;

        public String amount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_fullsangam);
        initView();
        url = constant.prefix2 + getString(R.string.bet);
        prefs = getSharedPreferences(constant.prefs, MODE_PRIVATE);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        game = GameOption.valueOf(getIntent().getStringExtra("game"));
        market = (MarketDto) getIntent().getSerializableExtra("market");

        marketText.setText(market.getName());

        patti.addAll(getPatti());

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (first.getText().toString().isEmpty() || second.getText().toString().isEmpty() || !patti.contains(first.getText().toString()) || !patti.contains(second.getText().toString()))
                {
                    Toast.makeText(FullSangam.this, getString(R.string.invalid_number), Toast.LENGTH_SHORT).show();
                }
                else if (Integer.parseInt(totalAmount.getText().toString()) < (Integer.parseInt(prefs.getString("wallet","0"))+Integer.parseInt(prefs.getString("winning","0"))+Integer.parseInt(prefs.getString("bonus","0")))) {
                    apicall();
                }
                else
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(FullSangam.this);
                    builder1.setMessage(getString(R.string.insufficient_balance));
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Recharge",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(new Intent(getApplicationContext(), wallet.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
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

    }




    public ArrayList<String> getPatti() {

        ArrayList<String> number = new ArrayList<>();

        // 1
        number.add("Line of 1");
        number.add("100");
        number.add("119");
        number.add("155");
        number.add("227");
        number.add("335");
        number.add("344");
        number.add("399");
        number.add("588");
        number.add("669");
        number.add("128");
        number.add("137");
        number.add("146");
        number.add("236");
        number.add("245");
        number.add("290");
        number.add("380");
        number.add("470");
        number.add("489");
        number.add("560");
        number.add("678");
        number.add("579");
        number.add("777");

        //2
        number.add("Line of 2");
        number.add("200");
        number.add("110");
        number.add("228");
        number.add("255");
        number.add("336");
        number.add("499");
        number.add("660");
        number.add("688");
        number.add("778");
        number.add("129");
        number.add("138");
        number.add("147");
        number.add("156");
        number.add("237");
        number.add("246");
        number.add("345");
        number.add("390");
        number.add("480");
        number.add("570");
        number.add("679");
        number.add("589");

        number.add("444");

        // 3
        number.add("Line of 3");
        number.add("300");
        number.add("166");
        number.add("229");
        number.add("337");
        number.add("355");
        number.add("445");
        number.add("599");
        number.add("779");
        number.add("788");
        number.add("120");
        number.add("139");
        number.add("148");
        number.add("157");
        number.add("238");
        number.add("247");
        number.add("256");
        number.add("346");
        number.add("490");
        number.add("580");
        number.add("670");
        number.add("689");

        number.add("111");

        // 4
        number.add("Line of 4");
        number.add("400");
        number.add("112");
        number.add("220");
        number.add("266");
        number.add("338");
        number.add("446");
        number.add("455");
        number.add("699");
        number.add("770");
        number.add("130");
        number.add("149");
        number.add("158");
        number.add("167");
        number.add("239");
        number.add("248");
        number.add("257");
        number.add("347");
        number.add("356");
        number.add("590");
        number.add("680");
        number.add("789");

        number.add("888");

        // 5
        number.add("Line of 5");
        number.add("500");
        number.add("113");
        number.add("122");
        number.add("177");
        number.add("339");
        number.add("366");
        number.add("447");
        number.add("799");
        number.add("889");
        number.add("140");
        number.add("159");
        number.add("168");
        number.add("230");
        number.add("249");
        number.add("258");
        number.add("267");
        number.add("348");
        number.add("357");
        number.add("456");
        number.add("690");
        number.add("780");

        number.add("555");

        // 6
        number.add("Line of 6");
        number.add("600");
        number.add("114");
        number.add("277");
        number.add("330");
        number.add("448");
        number.add("466");
        number.add("556");
        number.add("880");
        number.add("899");
        number.add("123");
        number.add("150");
        number.add("169");
        number.add("178");
        number.add("240");
        number.add("259");
        number.add("268");
        number.add("349");
        number.add("358");
        number.add("457");
        number.add("367");
        number.add("790");

        number.add("222");

        // 7
        number.add("Line of 7");
        number.add("700");
        number.add("115");
        number.add("133");
        number.add("188");
        number.add("223");
        number.add("377");
        number.add("449");
        number.add("557");
        number.add("566");
        number.add("124");
        number.add("160");
        number.add("179");
        number.add("250");
        number.add("269");
        number.add("278");
        number.add("340");
        number.add("359");
        number.add("368");
        number.add("458");
        number.add("467");
        number.add("890");
        number.add("999");

        // 8
        number.add("Line of 8");
        number.add("800");
        number.add("116");
        number.add("224");
        number.add("233");
        number.add("288");
        number.add("440");
        number.add("477");
        number.add("558");
        number.add("990");
        number.add("125");
        number.add("134");
        number.add("170");
        number.add("189");
        number.add("260");
        number.add("279");
        number.add("350");
        number.add("369");
        number.add("378");
        number.add("459");
        number.add("567");
        number.add("468");

        number.add("666");

        // 9
        number.add("Line of 9");
        number.add("900");
        number.add("117");
        number.add("144");
        number.add("199");
        number.add("225");
        number.add("388");
        number.add("559");
        number.add("577");
        number.add("667");
        number.add("126");
        number.add("135");
        number.add("180");
        number.add("234");
        number.add("270");
        number.add("289");
        number.add("360");
        number.add("379");
        number.add("450");
        number.add("469");
        number.add("478");
        number.add("568");

        number.add("333");

        // 0
        number.add("Line of 0");
        number.add("550");
        number.add("668");
        number.add("244");
        number.add("299");
        number.add("226");
        number.add("488");
        number.add("677");
        number.add("118");
        number.add("334");
        number.add("127");
        number.add("136");
        number.add("145");
        number.add("190");
        number.add("235");
        number.add("280");
        number.add("370");
        number.add("479");
        number.add("460");
        number.add("569");
        number.add("389");
        number.add("578");

        number.add("000");


        return number;
    }


    private void apicall() {
        progressDialog = new ViewDialog(FullSangam.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            if (jsonObject1.getString("success").equalsIgnoreCase("1")) {

                                AlertDialog.Builder builder1 = new AlertDialog.Builder(FullSangam.this);
                                LayoutInflater factory = LayoutInflater.from(FullSangam.this);
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
                                        Intent in = new Intent(FullSangam.this, HomeScreen.class);
                                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        in.putExtra("market",constant.market);
                                        in.putExtra("is_open",constant.is_open);
                                        in.putExtra("is_close",constant.is_close);
                                        in.putExtra("market_type",constant.market_type);
                                        //startActivity(in);
                                        FullSangam.this.finish();
                                    }
                                });
                                close.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alert11.dismiss();

                                        Intent in = new Intent(FullSangam.this, HomeScreen.class);
                                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        in.putExtra("market",constant.market);
                                        in.putExtra("is_open",constant.is_open);
                                        in.putExtra("is_close",constant.is_close);
                                        in.putExtra("market_type",constant.market_type);
                                        //startActivity(in);
                                        FullSangam.this.finish();
                                    }
                                });
                                alert11.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                alert11.show();

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
                        Toast.makeText(FullSangam.this, getString(R.string.api_error_msg), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                BetItem item = new BetItem();
                item.number1 = first.getText().toString();
                item.number2 = second.getText().toString();
                item.amount = totalAmount.getText().toString();
                item.gameType = GameType.FULL_SANGAM.getId();

                betItems.add(item);

                Gson gson = new Gson();
                String betItemsString = gson.toJson(betItems);

                params.put("items", betItemsString);
                params.put("market_id", market.getMarketId());
                params.put("market_name", market.getName());
                params.put("total", totalAmount.getText().toString());
                params.put("game_option", game.getId());
                params.put("game_session", "N/A");
                params.put("mobile", prefs.getString("mobile", null));

                System.out.println(params.toString());
                params.put("session", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    private void openWhatsApp() {
        String url = constant.getWhatsapp(getApplicationContext());

        Uri uri = Uri.parse(url);
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(sendIntent);
    }

    private void initView() {
        firstTitle = (latonormal) findViewById(R.id.firstitle);
        first = findViewById(R.id.first);
        secondTitle = (latonormal) findViewById(R.id.secondtitle);
        second = findViewById(R.id.second);
        totalAmount = (EditText) findViewById(R.id.totalamount);
        submit = (latobold) findViewById(R.id.submit);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        marketText = (latonormal) findViewById(R.id.marketText);
    }
}
