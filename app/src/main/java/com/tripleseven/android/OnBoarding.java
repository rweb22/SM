package com.tripleseven.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import com.smarteist.autoimageslider.SliderView;

public class OnBoarding extends AppCompatActivity {

    private SliderView imageSlider;
    private LinearLayout submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        initViews();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageSlider.getCurrentPagePosition() == 2){
                    getSharedPreferences(constant.prefs,MODE_PRIVATE).edit().putString("first_time","done").apply();
                    startActivity(new Intent(OnBoarding.this, signup.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    imageSlider.slideToNextPosition();
                }
            }
        });


        OnBoardingAdapter adapter = new OnBoardingAdapter(OnBoarding.this);

        OnBoardingItem sliderItem1 = new OnBoardingItem();
        sliderItem1.setImage(R.drawable.withdraw_anim);
        sliderItem1.setTitle("WITHDRAWAL\nWITHIN 10 MIN");
        sliderItem1.setDescription("TO YOUR BANK ACCOUNT");
        adapter.addItem(sliderItem1);

        OnBoardingItem sliderItem2 = new OnBoardingItem();
        sliderItem2.setImage(R.drawable.secure_anim);
        sliderItem2.setTitle("100% SECURE");
        sliderItem2.setDescription("WITH BEST SECURE WALL");
        adapter.addItem(sliderItem2);

        OnBoardingItem sliderItem3 = new OnBoardingItem();
        sliderItem3.setImage(R.drawable.reward_anim);
        sliderItem3.setTitle("GOOD REWARDS\nAND BONUS");
        sliderItem3.setDescription("");
        adapter.addItem(sliderItem3);

        imageSlider.setSliderAdapter(adapter);
        imageSlider.startAutoCycle();
        if (imageSlider.isAutoCycle()){
            Log.e("autoCycle","true");
        }

        imageSlider.setCurrentPageListener(position -> {
            if (position == 2){
                imageSlider.stopAutoCycle();
            }
        });
    }

    private void initViews() {
        imageSlider = findViewById(R.id.imageSlider);
        submit = findViewById(R.id.submit);
    }
}