package com.tripleseven.android;



import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;

public class LockScreen extends AppCompatActivity implements PinLockListener {

    private PinLockView pinLockView;
    SharedPreferences preferences;
    private ImageView profileImage;
    private TextView profileName;
    private IndicatorDots indicatorDots;
    private latobold skipButton;
    private LinearLayout forgotButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        preferences = getSharedPreferences(constant.prefs, MODE_PRIVATE);
        initViews();

    }

    private void initViews() {
        pinLockView = findViewById(R.id.pin_lock_view);
        profileImage = findViewById(R.id.profile_image);
        profileName = findViewById(R.id.profile_name);
        indicatorDots = findViewById(R.id.indicator_dots);
        skipButton = findViewById(R.id.skip_button);
        forgotButton = findViewById(R.id.forgot_button);

        if (preferences.getString("mpin", "").equals("")) {
            profileName.setText("Create 6 digit MPIN");
        } else {
            profileName.setText("Enter 6 digit MPIN");
            forgotButton.setVisibility(View.VISIBLE);
            forgotButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    preferences.edit().clear().apply();
                    Intent in = new Intent(getApplicationContext(), splash.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                    finish();
                }
            });
        }

        if (!preferences.getString("is_pin_asked", "").equals("true")) {
            skipButton.setVisibility(View.VISIBLE);

            skipButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    preferences.edit().putString("is_pin_asked", "true").apply();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", "lock_passed");
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            });
        }


        pinLockView.attachIndicatorDots(indicatorDots);
        pinLockView.setPinLockListener(this);
        // pinLockView.setCustomKeySet(new int[]{2, 3, 1, 5, 9, 6, 7, 0, 8, 4});
        // pinLockView.enableLayoutShuffling();

        pinLockView.setPinLength(6);
        pinLockView.setTextColor(ContextCompat.getColor(this, R.color.white));

        indicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL_WITH_ANIMATION);

    }

    @Override
    public void onComplete(String pin) {
        if (preferences.getString("mpin", "").equals("")) {
            Betplay.setIsLocked(false);
            preferences.edit().putString("mpin", pin).apply();
            preferences.edit().putString("is_pin_asked", "true").apply();
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", "lock_passed");
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } else if (preferences.getString("mpin", "").equals(pin)) {
            Betplay.setIsLocked(false);
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", "lock_passed");
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } else {
            Toast.makeText(this, "Wrong pin entered", Toast.LENGTH_SHORT).show();
            pinLockView.resetPinLockView();
        }
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onPinChange(int pinLength, String intermediatePin) {

    }
}