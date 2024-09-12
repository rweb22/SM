package com.tripleseven.android;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.google.gson.Gson;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.tripleseven.android.dto.DashboardApiResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class DashboardFragment extends Fragment {
    private SharedPreferences preferences;
    private final String dashboardApiUrl = constant.prefix2 + "clubs";
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView loadingGifImageView;
    private RecyclerView marketRecyclerView;
    private SliderView sliderView;
    private SliderAdapter sliderAdapter;
    private TextView marqueText;
    private ImageView whatsApp, telegram, instagram, youtube;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard_fragement, container, false);
        initViews(view);
        return view;
    }


    private void doDashboardApiCall() {
        ViewDialog loadingViewDialog = new ViewDialog((AppCompatActivity) getActivity());
        loadingViewDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(requireActivity());

        final StringRequest postRequest = new MyStringRequest(
                requireActivity().getSharedPreferences(constant.prefs, MODE_PRIVATE),
                Request.Method.POST,
                dashboardApiUrl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            if (!jsonObject1.getString(constant.SUCCESS).equals(constant.ONE)) {
                                Toast.makeText(getActivity(), jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                            } else {
                                if (jsonObject1.getString(constant.SUCCESS).equals(constant.ZERO)) {
                                    Toast.makeText(getActivity(), R.string.ACCOUNT_DISABLE_ALERT, Toast.LENGTH_SHORT).show();
                                    preferences.edit().clear().apply();

                                    Intent in = new Intent(getActivity(), signup.class);
                                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(in);

                                    requireActivity().finish();
                                }

                                if (jsonObject1.getString("logout").equals(constant.ONE)) {
                                    preferences.edit().clear().apply();

                                    Intent in = new Intent(getActivity(), login.class);
                                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(in);

                                    requireActivity().finish();
                                }

                                Gson gson = new Gson();
                                DashboardApiResponse apiResponse = gson.fromJson(response,
                                        DashboardApiResponse.class);

                                AdapterClubItem marketItemAdapter = new AdapterClubItem(getActivity(), apiResponse.getClubs());
                                marketRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                                marketRecyclerView.setAdapter(marketItemAdapter);


                                sliderAdapter = new SliderAdapter(getActivity());

                                if (Objects.nonNull(apiResponse.getSliderImageSlugs())) {
                                    List<String> imageUrls = apiResponse.getSliderImageSlugs();

                                    for (int a = 0; a < imageUrls.size(); a++) {
                                        SliderItem sliderItem1 = new SliderItem();
                                        sliderItem1.setImageUrl(constant.admin_root + imageUrls.get(a));
                                        sliderAdapter.addItem(sliderItem1);
                                    }

                                    sliderView.setSliderAdapter(sliderAdapter);
                                } else {
                                    sliderView.setVisibility(View.GONE);
                                }

                                marqueText.setText(apiResponse.getMarqueeText());
                                telegram.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Uri uri = Uri.parse(apiResponse.getTelegramLink());
                                        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(sendIntent);
                                    }
                                });

                                whatsApp.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Uri uri = Uri.parse(apiResponse.getWhatsAppLink());
                                        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(sendIntent);
                                    }
                                });

                                instagram.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Uri uri = Uri.parse(apiResponse.getInstagramLink());
                                        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(sendIntent);
                                    }
                                });

                                youtube.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Uri uri = Uri.parse(apiResponse.getYoutubeLink());
                                        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(sendIntent);
                                    }
                                });

                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("wallet", apiResponse.getTotalBalance()).apply();
                                editor.putString("code", apiResponse.getReferralCode()).apply();
                                editor.putString("whatsapp", apiResponse.getWhatsAppLink()).apply();

                                if (swipeRefreshLayout.isRefreshing()) {
                                    swipeRefreshLayout.setRefreshing(false);
                                }

                                if (swipeRefreshLayout.getVisibility() == View.GONE) {
                                    Glide.with(requireActivity()).load(R.drawable.logo).into(loadingGifImageView);
                                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                                }
                                loadingViewDialog.hideDialog();
                            }
                        } catch (JSONException e) {
                                e.printStackTrace();
                                loadingViewDialog.hideDialog();
                                Toast.makeText(getActivity(), getString(R.string.api_error_msg), Toast.LENGTH_SHORT).show();
                            }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        loadingViewDialog.hideDialog();
                        Toast.makeText(getActivity(), getString(R.string.api_error_msg), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", preferences.getString("mobile", null));
                params.put("session", preferences.getString("session", null));

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }


    @Override
    public void onResume() {
        doDashboardApiCall();
        super.onResume();

    }

    private void initViews(View view) {
        preferences = requireActivity().getSharedPreferences(constant.prefs, MODE_PRIVATE);

        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doDashboardApiCall();
            }
        });

        loadingGifImageView = view.findViewById(R.id.loading_gif);

        sliderView = view.findViewById(R.id.imageSlider);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        marketRecyclerView = view.findViewById(R.id.recyclerview2);

        marqueText = view.findViewById(R.id.marqueeText);
        marqueText.setSelected(true);

        telegram = view.findViewById(R.id.telegramLink);
        whatsApp = view.findViewById(R.id.whatsappLink);
        instagram = view.findViewById(R.id.instagramLink);
        youtube = view.findViewById(R.id.ytLink);
    }
}