package com.tripleseven.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    protected ScrollView scrollView;
    protected TextView balance;
    protected CardView single;
    protected CardView jodi;
    protected CardView singlepatti;
    protected CardView doublepatti;
    protected CardView tripepatti;
    protected CardView halfsangam;
    protected CardView fullsangam;
    protected latonormal hometext;
    protected CardView crossing;
    protected CardView exit;
    protected CardView logout;
    protected CardView refresh;
    protected TextView supportno;
    protected CardView support;

    RecyclerView recyclerview;
    SharedPreferences preferences;
    String url;
    String is_gateway = "0";
    LinearLayout deposit_money, wallet_history, withdraw_money, game_charts;
    SwipeRefreshLayout swiperefresh;

    ImageView loading_gif;
    private ImageView loadingGif;
    private LinearLayout walletView;
    private RelativeLayout toolbar;
    private CircleImageView appIcon;
    private ImageView timeInfo;
    private latobold top;
    private latobold whatsappNumber;
    private ImageView whatsappIcon;
    private LinearLayout depositMoney;
    private LinearLayout withdrawMoney;
    private LinearLayout openChart;
    private LinearLayout walletHistory;
    private CardView back;
    private ImageView coin;
    private latobold homeTitle;
    private latonormal homeTag;
    private latobold depositButton;
    private latobold withdrawButton;

    SliderView sliderView;
    private SliderAdapter adapter;
    ActivityResultLauncher<Intent> lockScreenLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        initViews();
        url = constant.prefix + getString(R.string.home);
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String url = constant.getWhatsapp(getApplicationContext());
                    if (url != null && !url.isEmpty()) {
                        Uri uri = Uri.parse(url);
                        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(sendIntent);
                    } else {
                        Toast.makeText(MainActivity.this, "WhatsApp support not available", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("MainActivity", "Error opening WhatsApp support", e);
                    Toast.makeText(MainActivity.this, "Unable to open WhatsApp", Toast.LENGTH_SHORT).show();
                }
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Process.killProcess(Process.myPid());
                System.exit(1);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.edit().clear().apply();
                Intent in = new Intent(getApplicationContext(), signup.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                finish();
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Refreshing...", Toast.LENGTH_SHORT).show();
                apicall();
            }
        });

        apicall();

        if (preferences.getString("wallet", null) != null) {
            balance.setText(preferences.getString("wallet", null));
        } else {
            balance.setText("Loading");
        }

        if (preferences.getString("homeline", null) != null) {
            if (preferences.getString("homeline", "").equals("")) {
                hometext.setVisibility(View.GONE);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    hometext.setText(Html.fromHtml(preferences.getString("homeline", ""), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    hometext.setText(Html.fromHtml(preferences.getString("homeline", null)));
                }
            }
        } else {
            hometext.setText("Loading...");
        }


        single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, bazar.class).putExtra("game", "single").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        jodi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, bazar.class).putExtra("game", "jodi").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        crossing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, bazar.class).putExtra("game", "crossing").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        singlepatti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, bazar.class).putExtra("game", "singlepatti").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        doublepatti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, bazar.class).putExtra("game", "doublepatti").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        tripepatti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, bazar.class).putExtra("game", "tripepatti").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });


        halfsangam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, bazar.class).putExtra("game", "halfsangam").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        fullsangam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, bazar.class).putExtra("game", "fullsangam").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        crossing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, bazar.class).putExtra("game", "crossing").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        deposit_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_gateway.equals("1")) {
                    startActivity(new Intent(MainActivity.this, wallet.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    openWhatsApp();
                }
            }
        });

        game_charts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChartMenu.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });


        withdraw_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, withdraw.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });


        wallet_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BetHistory.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });


        Typeface face = Typeface.createFromAsset(getAssets(), "Oxygen-Bold.ttf");


        PrimaryDrawerItem home = new PrimaryDrawerItem().withName("Home").withIcon(R.drawable.house).withIdentifier(999).withTypeface(face);
        PrimaryDrawerItem account = new PrimaryDrawerItem().withName("My Profile").withIcon(R.drawable.user_icon).withIdentifier(1).withTypeface(face);
        PrimaryDrawerItem charts = new PrimaryDrawerItem().withName("Charts").withIdentifier(101).withIcon(R.drawable.chart_new).withTypeface(face);
        PrimaryDrawerItem rate = new PrimaryDrawerItem().withName("Game Rate").withIdentifier(2).withIcon(R.drawable.game_rate_new).withTypeface(face);
        PrimaryDrawerItem earn = new PrimaryDrawerItem().withName("Refer and Earn").withIcon(R.drawable.refer_icon).withIdentifier(21).withTypeface(face);
        PrimaryDrawerItem notice = new PrimaryDrawerItem().withName("Notice").withIcon(R.drawable.notice_new).withIdentifier(3).withTypeface(face);
        PrimaryDrawerItem deposit = new PrimaryDrawerItem().withName("Deposit").withIcon(R.drawable.deposit_new).withIdentifier(4).withTypeface(face);
        PrimaryDrawerItem withdraw = new PrimaryDrawerItem().withName("Withdrawal").withIcon(R.drawable.withdraw_new).withIdentifier(41).withTypeface(face);
        PrimaryDrawerItem ledger = new PrimaryDrawerItem().withName("Winning History").withIcon(R.drawable.game_ledger_new).withIdentifier(6).withTypeface(face);
        PrimaryDrawerItem transaction = new PrimaryDrawerItem().withName("Transaction History").withIcon(R.drawable.balance_enquiry_new).withIdentifier(8).withTypeface(face);
        PrimaryDrawerItem played = new PrimaryDrawerItem().withName("Played Match").withIcon(R.drawable.history_new).withIdentifier(9).withTypeface(face);
        PrimaryDrawerItem howto = new PrimaryDrawerItem().withName("How to Play").withIcon(R.drawable.question).withIdentifier(10).withTypeface(face);
        PrimaryDrawerItem share = new PrimaryDrawerItem().withName("Share").withIcon(R.drawable.share_icon).withIdentifier(11).withTypeface(face);
        PrimaryDrawerItem logout = new PrimaryDrawerItem().withName("Logout").withIcon(R.drawable.logout_icon).withIdentifier(7).withTypeface(face);


        final Drawer drawer = new DrawerBuilder()
                .withHeaderDivider(true)
                .withActivity(this)
                .withSliderBackgroundColor(getResources().getColor(R.color.md_white_1000))
                .withTranslucentStatusBar(true)
                .withHeader(R.layout.header)
                .withActionBarDrawerToggle(false)
                .addDrawerItems(
                        home, played, charts, ledger, earn, account, rate, notice, deposit, withdraw, howto, transaction, share, logout
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem.equals(1)) {
                            startActivity(new Intent(MainActivity.this, profile.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                        if (drawerItem.equals(101)) {
                            startActivity(new Intent(MainActivity.this, ChartMenu.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                        if (drawerItem.equals(2)) {
                            startActivity(new Intent(MainActivity.this, GameRate.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                        if (drawerItem.equals(21)) {
                            startActivity(new Intent(MainActivity.this, earn.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                        if (drawerItem.equals(3)) {
                            startActivity(new Intent(MainActivity.this, notice.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                        if (drawerItem.equals(4)) {
                            if (is_gateway.equals("1")) {
                                startActivity(new Intent(MainActivity.this, wallet.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            } else {
                                openWhatsApp();
                            }
                        }
                        if (drawerItem.equals(41)) {
                            openWhatsApp();
                        }
                        if (drawerItem.equals(10)) {
                            startActivity(new Intent(MainActivity.this, howot.class));
                        }
                        if (drawerItem.equals(11)) {

                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT,
                                    "Download " + getString(R.string.app_name) + " and earn coins at home, Download link - https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                            sendIntent.setType("text/plain");
                            startActivity(sendIntent);
                        }
                        if (drawerItem.equals(7)) {
                            preferences.edit().clear().apply();
                            Intent in = new Intent(getApplicationContext(), login.class);
                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(in);
                            finish();
                        }
                        if (drawerItem.equals(6)) {
                            startActivity(new Intent(MainActivity.this, PassBook.class));
                        }
                        if (drawerItem.equals(8)) {
                            startActivity(new Intent(MainActivity.this, transactions.class));
                        }
                        if (drawerItem.equals(9)) {
                            startActivity(new Intent(MainActivity.this, BetHistory.class));
                        }

                        return false;
                    }
                })
                .build();


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen()) {
                    drawer.closeDrawer();
                } else {
                    drawer.openDrawer();
                }
            }
        });

    }



    private void checkLock(){
        if (Betplay.getIsLocked()){
            if (!preferences.getString("is_pin_asked","").equals("true") || !preferences.getString("mpin","").equals("")){
                Intent intent = new Intent(this, MPIN.class);
                lockScreenLauncher.launch(intent);
            }
        }
    }


    private void apicall() {


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);
                        try {

                            JSONObject jsonObject1 = new JSONObject(response);

                            if (jsonObject1.getString("active").equals("0")) {
                                Toast.makeText(MainActivity.this, "Your account temporarily disabled by admin", Toast.LENGTH_SHORT).show();

                                preferences.edit().clear().apply();
                                Intent in = new Intent(getApplicationContext(), login.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                finish();
                            }

                            if (!jsonObject1.getString("session").equals(getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null))) {
                                Toast.makeText(MainActivity.this, "Session expired ! Please login again", Toast.LENGTH_SHORT).show();

                                preferences.edit().clear().apply();
                                Intent in = new Intent(getApplicationContext(), login.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                finish();
                            }

                            balance.setText(jsonObject1.getString("wallet"));

                            if (jsonObject1.getString("homeline").equals("")) {
                                hometext.setVisibility(View.GONE);
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    hometext.setText(Html.fromHtml(jsonObject1.getString("homeline"), Html.FROM_HTML_MODE_COMPACT));
                                } else {
                                    hometext.setText(Html.fromHtml(jsonObject1.getString("homeline")));
                                }
                            }


                            ArrayList<String> name = new ArrayList<>();
                            ArrayList<String> result = new ArrayList<>();


                            ArrayList<String> is_open = new ArrayList<>();
                            ArrayList<String> open_time = new ArrayList<>();
                            ArrayList<String> close_time = new ArrayList<>();
                            ArrayList<String> open_av = new ArrayList<>();

                            JSONArray jsonArray = jsonObject1.getJSONArray("result");
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(a);

                                open_time.add(jsonObject.getString("open_time"));
                                close_time.add(jsonObject.getString("close_time"));
                                name.add(jsonObject.getString("market"));
                                result.add(jsonObject.getString("result"));
                                is_open.add(jsonObject.getString("is_open"));
                                open_av.add(jsonObject.getString("is_close"));

                            }

//
//                            adapter_result rc = new adapter_result(MainActivity.this, name, result, is_open, open_time, close_time, open_av);
//                            recyclerview.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
//                            recyclerview.setAdapter(rc);
//                            rc.notifyDataSetChanged();

                            adapter = new SliderAdapter(MainActivity.this);

                            JSONArray jsonArrayx = jsonObject1.getJSONArray("images");
                            for (int a = 0; a < jsonArrayx.length(); a++){
                                JSONObject jsonObject = jsonArrayx.getJSONObject(a);

                                SliderItem sliderItem1 = new SliderItem();
                                sliderItem1.setImageUrl(constant.admin_root+jsonObject.getString("image"));
                                adapter.addItem(sliderItem1);

                            }


                            sliderView.setSliderAdapter(adapter);



                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("wallet", jsonObject1.getString("wallet")).apply();
                            editor.putString("homeline", jsonObject1.getString("homeline")).apply();
                            editor.putString("code", jsonObject1.getString("code")).apply();
                            editor.putString("is_gateway", jsonObject1.getString("gateway")).apply();
                            editor.putString("whatsapp", jsonObject1.getString("whatsapp")).apply();
                            is_gateway = jsonObject1.getString("gateway");

                            if (swiperefresh.isRefreshing()) {
                                swiperefresh.setRefreshing(false);
                            }


                            if (swiperefresh.getVisibility() == View.GONE) {
                                Glide.with(MainActivity.this).load(R.drawable.logo).into(loading_gif);
                                loading_gif.setVisibility(View.GONE);
                                swiperefresh.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();

                        Toast.makeText(MainActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("app", "kalyanpro");
                params.put("mobile", preferences.getString("mobile", null));
                params.put("session", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    @Override
    protected void onResume() {
        apicall();

        checkLock();
        super.onResume();
    }

    private void openWhatsApp() {
        String url = constant.getWhatsapp(getApplicationContext());

        Uri uri = Uri.parse(url);
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(sendIntent);
    }


    private void initViews() {

        preferences = getSharedPreferences(constant.prefs, MODE_PRIVATE);
        balance = findViewById(R.id.balance);
        hometext = findViewById(R.id.hometext);
        single = findViewById(R.id.single);
        jodi = findViewById(R.id.jodi);
        crossing = findViewById(R.id.crossing);
        singlepatti = findViewById(R.id.singlepatti);
        doublepatti = findViewById(R.id.doublepatti);
        tripepatti = findViewById(R.id.tripepatti);
        halfsangam = findViewById(R.id.halfsangam);
        fullsangam = findViewById(R.id.fullsangam);
        exit = findViewById(R.id.exit);
        logout = findViewById(R.id.logout);
        refresh = findViewById(R.id.refresh);
        supportno = findViewById(R.id.supportno);
        support = findViewById(R.id.support);
        scrollView = findViewById(R.id.scrollView);
        recyclerview = findViewById(R.id.recyclerview);

        game_charts = findViewById(R.id.open_chart);
        deposit_money = findViewById(R.id.deposit_money);
        withdraw_money = findViewById(R.id.withdraw_money);
        wallet_history = findViewById(R.id.wallet_history);
        swiperefresh = findViewById(R.id.swiperefresh);
        loading_gif = findViewById(R.id.loading_gif);
        back = findViewById(R.id.back);
        coin = findViewById(R.id.coin);
        homeTitle = findViewById(R.id.home_title);
        homeTag = findViewById(R.id.home_tag);
        depositButton = findViewById(R.id.deposit_button);
        withdrawButton = findViewById(R.id.withdraw_button);

        sliderView = findViewById(R.id.imageSlider);
        swiperefresh.setVisibility(View.GONE);
        Glide.with(MainActivity.this).load(R.drawable.loading_animation).into(loading_gif);
        loading_gif.setVisibility(View.VISIBLE);

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                apicall();
            }
        });

        hometext.setMovementMethod(LinkMovementMethod.getInstance());

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        walletView = findViewById(R.id.wallet_view);
        toolbar = findViewById(R.id.toolbar);
        appIcon = findViewById(R.id.app_icon);
        timeInfo = findViewById(R.id.time_info);
        top = findViewById(R.id.top);
        whatsappNumber = findViewById(R.id.whatsapp_number);
        whatsappIcon = findViewById(R.id.whatsapp_icon);

        whatsappNumber.setText("Contact us - "+preferences.getString("whatsapp", ""));
        top.setText(preferences.getString("home_marq", ""));
        homeTitle.setText(preferences.getString("home_title", ""));
        homeTag.setText(preferences.getString("home_tag", ""));

        whatsappIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsApp();
            }
        });

        findViewById(R.id.whatsapp_icon2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsApp();
            }
        });

        findViewById(R.id.whatsapp_icon1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsApp();
            }
        });

        whatsappNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsApp();
            }
        });


        depositButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_gateway.equals("1")) {
                    startActivity(new Intent(MainActivity.this, wallet.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    openWhatsApp();
                }
            }
        });


        withdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, withdraw.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });


        top.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        top.setMarqueeRepeatLimit(-1);
        top.setSingleLine(true);
        top.setSelected(true);


        lockScreenLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            checkLock();
                        }
                    }
                });
    }
}
