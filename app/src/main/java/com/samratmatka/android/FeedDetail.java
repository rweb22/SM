package com.samratmatka.android;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.potyvideo.library.AndExoPlayerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FeedDetail extends AppCompatActivity {

    private ImageView back;
    private latobold title;
    private ImageView image;
    private AndExoPlayerView andExoPlayerView;
    private WebView content;
    private ImageView likeIcon;
    private latonormal likeText;
    private LinearLayout like;
    private LinearLayout share;
    private LinearLayout likeView;

    FeedModel feedModel;
    Context context;

    final String url = constant.prefix + "like.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);
        initViews();
        feedModel = Betplay.getFeedModel();
        context = this;

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        title.setText(feedModel.getTitle());

        InputStream is = null;
        try {
            is = getResources().getAssets().open("content_template.html");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            content.loadDataWithBaseURL("file:///android_asset/",
                    new String(buffer).replace("[CONTENT]", feedModel.getFullContent()),
                    "text/html", "UTF-8", null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (feedModel.getMedia_type().equals("1")){
            Glide.with(FeedDetail.this).load(constant.project_root+"admin/"+feedModel.getMedia_url()).into(image);
            image.setVisibility(View.VISIBLE);
        } else if (feedModel.getMedia_type().equals("2")){
            andExoPlayerView.setSource(constant.project_root+"admin/"+feedModel.getMedia_url());
            andExoPlayerView.setPlayWhenReady(true);
            andExoPlayerView.setVisibility(View.VISIBLE);
        }

        if ((feedModel.getIslike().equals("1") && Betplay.checkFollow(feedModel.getId()).equals("-1")) || Betplay.checkFollow(feedModel.getId()).equals("1")){
            likeText.setText("Liked ( " + feedModel.getLikes() + " )");
            likeText.setTextColor(getResources().getColor(R.color.colorAccent));
            likeIcon.setColorFilter(getResources().getColor(R.color.colorAccent));
            Glide.with(FeedDetail.this).load(R.drawable.liked).into(likeIcon);
        } else {
            likeText.setText("Like ( " + feedModel.getLikes() + " )");
        }

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }



    private void delete(String id, ImageView imageView, TextView textView) {

        imageView.clearColorFilter();
        Glide.with(context).load(R.drawable.loading).into(imageView);


        RequestQueue requestQueue = Volley.newRequestQueue(context);

        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url,
                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        Betplay.addTempFollow(id,jsonObject.getString("success"));

                        if (jsonObject.getString("success").equals("1")){
                            Glide.with(context).load(R.drawable.liked).into(imageView);
                            imageView.setColorFilter(context.getResources().getColor(R.color.colorAccent));
                            textView.setTextColor(context.getResources().getColor(R.color.colorAccent));
                        } else {
                            Glide.with(context).load(R.drawable.like).into(imageView);
                            imageView.setColorFilter(context.getResources().getColor(R.color.font));
                            textView.setTextColor(context.getResources().getColor(R.color.font));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                },
                error -> {

                    error.printStackTrace();

                    Toast.makeText(context, "Check your internet connection", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("mobile", context.getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile", null));

                params.put("usermobile", id);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }



    private void initViews() {
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        image = findViewById(R.id.image);
        andExoPlayerView = findViewById(R.id.andExoPlayerView);
        content = findViewById(R.id.content);
        likeIcon = findViewById(R.id.like_icon);
        likeText = findViewById(R.id.like_text);
        like = findViewById(R.id.like);
        share = findViewById(R.id.share);
        likeView = findViewById(R.id.likeView);
    }
}