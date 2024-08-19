package com.tripleseven.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MPIN extends AppCompatActivity {


    ActivityResultLauncher<Intent> mStartForResult;

    private EditText mpin;
    private LinearLayout enterBlock;
    private EditText createMpin;
    private EditText confirmMpin;
    private LinearLayout createBlock;
    private LinearLayout submit;
    SharedPreferences preferences;
    private latobold forgotMpin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpin);
        initViews();
        preferences = getSharedPreferences(constant.prefs, MODE_PRIVATE);

        mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent == null) return;
                        if (intent.hasExtra("verification") && intent.getStringExtra("verification").equals("success")) {

//                            mobile.setVisibility(View.GONE);
//                            submitText.setText("Continue");
//                            signupForm.setVisibility(View.VISIBLE);

                            SharedPreferences.Editor editor = getSharedPreferences(constant.prefs, MODE_PRIVATE).edit();
                            editor.putString("mpin", "").apply();

                            enterBlock.setVisibility(View.GONE);
                            createBlock.setVisibility(View.VISIBLE);

                        }
                    }
                });

        forgotMpin.setOnClickListener( v -> {
            mStartForResult.launch(new Intent(MPIN.this, MobileVerification.class).putExtra("mobile", preferences.getString("mobile","")).putExtra("forgot","mpin"));
        });




        if (!preferences.getString("mpin", "").equals("")) {
            enterBlock.setVisibility(View.VISIBLE);
            createBlock.setVisibility(View.GONE);

            new Handler().postDelayed(new Runnable() {

                public void run() {
                    mpin.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0f, 0f, 0));
                    mpin.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0f, 0f, 0));
                }
            }, 200);

            mpin.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().length() == 4){
                        submit.performClick();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            submit.setOnClickListener(v -> {
                if (preferences.getString("mpin", "").equals(mpin.getText().toString())) {
                    Betplay.setIsLocked(false);
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", "lock_passed");
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    Toast.makeText(MPIN.this, "Wrong pin entered", Toast.LENGTH_SHORT).show();
                    mpin.setText("");
                }
            });

        } else {
            enterBlock.setVisibility(View.GONE);
            createBlock.setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {

                public void run() {
                    createMpin.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0f, 0f, 0));
                    createMpin.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0f, 0f, 0));
                }
            }, 200);

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (createMpin.getText().toString().isEmpty()){
                        createMpin.setError("Enter MPIN");
                    } else if (confirmMpin.getText().toString().isEmpty()){
                        confirmMpin.setError("Enter confirm MPIN");
                    }else if (createMpin.getText().length() != 4){
                        createMpin.setError("Enter Minimum 4 Digit");
                    } else if (!confirmMpin.getText().toString().equals(confirmMpin.getText().toString())){
                        confirmMpin.setError("MPIN does not match");
                    } else {
                        Betplay.setIsLocked(false);
                        preferences.edit().putString("mpin", createMpin.getText().toString()).apply();
                        preferences.edit().putString("is_pin_asked", "true").apply();
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result", "lock_passed");
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                }
            });
        }


    }

    private void initViews() {
        forgotMpin = findViewById(R.id.fmpin);
        mpin = findViewById(R.id.mpin);
        enterBlock = findViewById(R.id.enter_block);
        createMpin = findViewById(R.id.create_mpin);
        confirmMpin = findViewById(R.id.confirm_mpin);
        createBlock = findViewById(R.id.create_block);
        submit = findViewById(R.id.submit);
    }
}