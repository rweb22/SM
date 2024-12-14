package com.tripleseven.android;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class HomeFragment extends Fragment {

    protected ScrollView scrollView;
    RecyclerView recyclerview;
    SharedPreferences preferences;
    String url;
    String is_gateway = "0";
    SwipeRefreshLayout swiperefresh;
    ImageView loading_gif;

    ViewDialog viewDialog2;
    private latonormal balance_home;
    private ImageView loadingGif;
    private latonormal hometext;
    private LinearLayout playStarline, playStarline2;
    private LinearLayout mainMarkets;
    private LinearLayout bidHistory;
    private RecyclerView recyclerview2;
    private LinearLayout delhiMarkets;
    SliderView sliderView;
    private SliderAdapter adapter;
    private SliderView imageSlider;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(


    LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);

        url = constant.prefix2 + "dashboard";
     //   balance_home.setText((Integer.parseInt(this.getActivity().getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("wallet","0")))+" ₹");

       /// SharedPreferences preferences = this.getActivity().getSharedPreferences(constant.prefs, Context.MODE_PRIVATE);

//        playStarline.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), jackpot_timings.class).putExtra("market", "Jackpot"));
//            }
//        });

//        public void startFullScreenChat() {
//            if (fullScreenChatWindow == null) {
//                fullScreenChatWindow = ChatWindowView.createAndAttachChatWindowInstance(getActivity());
//                fullScreenChatWindow.setUpWindow(configuration);
//                fullScreenChatWindow.setUpListener(this);
//                fullScreenChatWindow.initialize();
//            }
//            fullScreenChatWindow.showChatWindow();
//        }


        bidHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),played.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","delhi"));
            }
        });

//
//        if (preferences.getString("wallet", null) != null) {
//            balance.setText(preferences.getString("wallet", null));
//        } else {
//            balance.setText("Loading");
//        }


        return view;

    }


    private void apicall() {

        viewDialog2 = new ViewDialog((AppCompatActivity) getActivity());
        viewDialog2.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        
        final StringRequest postRequest = new MyStringRequest(
                getActivity().getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);
                        try {

                            JSONObject jsonObject1 = new JSONObject(response);

                            if (jsonObject1.getString("active").equals("0")) {
                                Toast.makeText(getActivity(), "Your account temporarily disabled by admin", Toast.LENGTH_SHORT).show();

                                preferences.edit().clear().apply();
                                Intent in = new Intent(getActivity(), signup.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                getActivity().finish();
                            }

                            if (jsonObject1.has("logout") && jsonObject1.getString("logout").equals("1")) {
                                preferences.edit().clear().apply();
                                Intent in = new Intent(getActivity(), login.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                getActivity().finish();
                            }


                            ArrayList<String> name2 = new ArrayList<>();
                            ArrayList<String> result2 = new ArrayList<>();
                            ArrayList<String> result3 = new ArrayList<>();


                            ArrayList<String> is_open2 = new ArrayList<>();
                            ArrayList<String> open_time2 = new ArrayList<>();
                            ArrayList<String> close_time2 = new ArrayList<>();
                            ArrayList<String> open_av2 = new ArrayList<>();
                            ArrayList<String> market_type2 = new ArrayList<>();
                            ArrayList<String> result_time = new ArrayList<>();
                            ArrayList<String> mOpen = new ArrayList<>();
                            ArrayList<String> mClose = new ArrayList<>();

                            JSONArray jsonArray2 = jsonObject1.getJSONArray("markets");
                            for (int a = 0; a < jsonArray2.length(); a++) {
                                JSONObject jsonObject = jsonArray2.getJSONObject(a);

                                open_time2.add(jsonObject.getString("open_time"));
                                close_time2.add(jsonObject.getString("close_time"));

                                name2.add(jsonObject.getString("market"));
                                result2.add(jsonObject.getString("result"));
                                result3.add(jsonObject.getString("result3"));
                                is_open2.add(jsonObject.getString("is_open"));
                                open_av2.add(jsonObject.getString("is_close"));
                                market_type2.add(jsonObject.getString("market_type"));
                                result_time.add(jsonObject.getString("result_time"));
                                mOpen.add(jsonObject.getString("mOpen"));
                                mClose.add(jsonObject.getString("mClose"));

                            }


                            adapter_result_delhi rc2 = new adapter_result_delhi(getActivity(), name2, result2,result3, is_open2, open_time2, close_time2, open_av2, market_type2,result_time,mOpen,mClose);
                            recyclerview2.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                            recyclerview2.setAdapter(rc2);

                            adapter = new SliderAdapter(getActivity());

                            if (jsonObject1.has("images")) {
                                JSONArray jsonArrayx = jsonObject1.getJSONArray("images");
                                for (int a = 0; a < jsonArrayx.length(); a++) {
                                    JSONObject jsonObject = jsonArrayx.getJSONObject(a);

                                    SliderItem sliderItem1 = new SliderItem();
                                    sliderItem1.setImageUrl(constant.admin_root + jsonObject.getString("image"));
                                    adapter.addItem(sliderItem1);

                                }


                                sliderView.setSliderAdapter(adapter);
                            } else{
                                sliderView.setVisibility(View.GONE);
                            }


                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("wallet", jsonObject1.getString("wallet")).apply();
                            //balance_home.setText(Integer.parseInt(jsonObject1.getString("wallet")));
                            editor.putString("winning", jsonObject1.getString("winning")).apply();
                            editor.putString("bonus", jsonObject1.getString("bonus")).apply();
                            editor.putString("homeline", jsonObject1.getString("homeline")).apply();
                            editor.putString("code", jsonObject1.getString("code")).apply();
                            editor.putString("is_gateway", jsonObject1.getString("gateway")).apply();
                            editor.putString("whatsapp", jsonObject1.getString("whatsapp")).apply();
                            editor.putString("bank_details", jsonObject1.getString("bank_details")).apply();
                            editor.putString("upi", jsonObject1.getString("upi")).apply();
                            editor.putString("merchant", jsonObject1.getString("merchant")).apply();
                            is_gateway = jsonObject1.getString("gateway");

                          //  balance_home.setText(preferences.getString("wallet","0"));




                            Log.e("sss", "sd");
                            Log.d("current-version", String.valueOf(BuildConfig.VERSION_CODE));

                            if (swiperefresh.isRefreshing()) {
                                swiperefresh.setRefreshing(false);
                            }


                            if (swiperefresh.getVisibility() == View.GONE) {
                                Glide.with(getActivity()).load(R.drawable.logo).into(loading_gif);
                               // loading_gif.setVisibility(View.GONE);
                                swiperefresh.setVisibility(View.VISIBLE);
                            }

                            viewDialog2.hideDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            viewDialog2.hideDialog();
                            Toast.makeText(getActivity(), "Something went wrong !", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();

                        viewDialog2.hideDialog();
                        Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

             //   Log.e("session",getActivity().getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));

                params.put("app", "kalyanpro");
                params.put("mobile", preferences.getString("mobile", null));
                params.put("session", getActivity().getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }


    @Override
    public void onResume() {
        apicall();
        super.onResume();

    }

    public static Set<String> get2DCombinations(String input) {
        return backtracking("", input, input.length() / 2) ;
    }

    public static Set<String> backtracking(String actual, String remaining, int length) {
        if (actual.length() == length) {
            return new HashSet<>(Arrays.asList(actual));
        }

        Set<String> result = new HashSet<>();
        for(int i = 0; i < remaining.length(); i++) {
            result.addAll(backtracking(actual + remaining.charAt(i), remaining.substring(0, i) + remaining.substring(i + 1), length));
        }
        return result;
    }



    private void initViews(View view) {


        preferences = getActivity().getSharedPreferences(constant.prefs, MODE_PRIVATE);
        scrollView = view.findViewById(R.id.scrollView);
        recyclerview = view.findViewById(R.id.recyclerview);

        swiperefresh = view.findViewById(R.id.swiperefresh);
        loading_gif = view.findViewById(R.id.loading_gif);


//        view.findViewById(R.id.play_starline2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               // System.out.println(get2DCombinations("329"));
//                startActivity(new Intent(getActivity(), card_timings.class).putExtra("market", "Starline"));
//                // startActivity(new Intent(getActivity(), starline_markets.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//            }
//        });




        swiperefresh.setVisibility(View.GONE);
      //  Glide.with(getActivity()).load(R.drawable.loading_animation).into(loading_gif);
      //  loading_gif.setVisibility(View.VISIBLE);

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                apicall();
            }
        });
        sliderView = view.findViewById(R.id.imageSlider);

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();


        balance_home = view.findViewById(R.id.balance_home);
        hometext = view.findViewById(R.id.hometext);
        //playStarline2 = view.findViewById(R.id.play_starline2);
        //playStarline = view.findViewById(R.id.play_starline);
        mainMarkets = view.findViewById(R.id.main_markets);
        bidHistory = view.findViewById(R.id.bid_history);
        recyclerview2 = view.findViewById(R.id.recyclerview2);
        delhiMarkets = view.findViewById(R.id.delhi_markets);
    }
}
