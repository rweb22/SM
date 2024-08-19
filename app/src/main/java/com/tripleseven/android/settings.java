package com.tripleseven.android;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;

public class settings extends AppCompatActivity {

    private CardView back;
    private CardView toolbar;
    private SwitchCompat pin;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!preferences.getString("mpin", "").equals("")) {
            pin.setChecked(!preferences.getString("mpin", "").equals(""));
        } else {
            pin.setChecked(false);
        }


        pin.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                startActivity(new Intent(settings.this,LockScreen.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            } else {
                preferences.edit().remove("mpin").apply();
            }
        });
    }

    private void initViews() {
        back = findViewById(R.id.back);
        toolbar = findViewById(R.id.toolbar);
        pin = findViewById(R.id.pin);

        preferences = getSharedPreferences(constant.prefs, MODE_PRIVATE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}