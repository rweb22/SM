package com.tripleseven.android;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Telephony;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobileVerification extends AppCompatActivity {

    private static final int  REQ_USER_CONSENT = 200;
    SmsBroadcastReciever smsBroadcastReciever;

    private EditText otp1;
    private EditText otp2;
    private EditText otp3;
    private EditText otp4;
    private EditText otp5;
    private EditText otp6;
    private TextView verify;
    private TextView resendButton;
    private latonormal mobile_n;

    String mobileNumber = "", otp = "";
    FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String forgot;
    String otpId;
    String url;
    PhoneAuthProvider.ForceResendingToken otpToken;

    ViewDialog viewDialog;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verification);
        context = this;
        initViews();
        url = constant.prefix + "user_check";
        mAuth = FirebaseAuth.getInstance();
        mobileNumber = getIntent().getStringExtra("mobile");
        mobile_n.setText("+91 "+getIntent().getStringExtra("mobile"));
        forgot = getIntent().getStringExtra("forgot");
        Random rand = new Random();
        otp = String.format("%06d", rand.nextInt(1000000));

        otp1.addTextChangedListener(new GenericTextWatcher(otp1));
        otp2.addTextChangedListener(new GenericTextWatcher(otp2));
        otp3.addTextChangedListener(new GenericTextWatcher(otp3));
        otp4.addTextChangedListener(new GenericTextWatcher(otp4));
        otp5.addTextChangedListener(new GenericTextWatcher(otp5));
        otp6.addTextChangedListener(new GenericTextWatcher(otp6));

        sendWebOTP();
        startSmartUserConsent();

        verify.setOnClickListener(view -> {
            if (otp == null) return;
            if (getOtp().isEmpty() || getOtp().length() != 6){
                Toast.makeText(this, "Enter OTP", Toast.LENGTH_SHORT).show();
                return;
            }
            if (otp.equals(getOtp())){
                if(forgot.equals("no")){
                Intent intent=new Intent();
                intent.putExtra("verification","success");
                setResult(RESULT_OK,intent);
                finish();
                }else if(forgot.equals("mpin")){
                    Intent intent=new Intent();
                    intent.putExtra("verification","success");
                    setResult(RESULT_OK,intent);
                    finish();
                }else if(forgot.equals("signup")){
                    Intent intent=new Intent();
                    intent.putExtra("verification","success");
                    setResult(RESULT_OK,intent);
                    finish();
                }
                else{
                    apicall();
                }
            } else {
                Toast.makeText(MobileVerification.this, "Incorrect OTP entered", Toast.LENGTH_SHORT).show();
            }
//            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpId, getOtp());
//            mAuth.signInWithCredential(credential)
//                    .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                Intent intent=new Intent();
//                                intent.putExtra("verification","success");
//                                setResult(RESULT_OK,intent);
//                                finish();
//                            } else {
//
//                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                    Toast.makeText(context, "Incorrect OTP entered", Toast.LENGTH_LONG).show();
//
//                                } else {
//                                    Toast.makeText(context, "Unable to verify please retry later", Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        }
//                    });

        });

        resendButton.setOnClickListener(view -> {
            if (resendButton.getText().toString().equals(getString(R.string.resend_otp))) {
                sendWebOTP();
//                if (otpToken != null && otpId != null) {
//
//                    resendButton.setText("Sending");
//                    resendButton.setOnClickListener(v -> Toast.makeText(context, "Sending OTP", Toast.LENGTH_SHORT).show());
//
//                    PhoneAuthOptions options =
//                            PhoneAuthOptions.newBuilder(mAuth)
//                                    .setPhoneNumber("+"+mobileNumber)       // Phone number to verify
//                                    .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
//                                    .setActivity((Activity) context)                 // Activity (for callback binding)
//                                    .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
//                                    .setForceResendingToken(otpToken)
//                                    .build();
//                    PhoneAuthProvider.verifyPhoneNumber(options);
//                }
            } else {
                Toast.makeText(this, "Wait before resend", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startSmartUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        client.startSmsUserConsent(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_USER_CONSENT){
            if ((resultCode == RESULT_OK) && (data != null)){

                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                getOtpFromMessage(message);
            }
        }
    }

    private void getOtpFromMessage(String message) {

        Pattern otpPattern = Pattern.compile("(|^)\\d{6}");
        Matcher matcher = otpPattern.matcher(message);
        if (matcher.find()){
            String str = matcher.group(0);

            // Creating array of string length
            // using length() method
            char[] ch = new char[str.length()];

            // Copying character by character into array
            // using for each loop
            for (int i = 0; i < str.length(); i++) {
                ch[i] = str.charAt(i);
            }

            // Printing the elements of array
            // using for each loop
            for (char c : ch) {
                System.out.println(c);

            }
          otp1.setText(String.valueOf(ch[0]));
            otp2.setText(String.valueOf(ch[1]));
            otp3.setText(String.valueOf(ch[2]));
            otp4.setText(String.valueOf(ch[3]));
            otp5.setText(String.valueOf(ch[4]));
            otp6.setText(String.valueOf(ch[5]));

        }
    }

    private  void registerBroadcastReciever(){
        smsBroadcastReciever = new SmsBroadcastReciever();
        smsBroadcastReciever.smsBroadcastRecieverListner = new SmsBroadcastReciever.SmsBroadcastRecieverListner() {
            @Override
            public void onSuccess(Intent intent) {

                startActivityForResult(intent,REQ_USER_CONSENT);

            }

            @Override
            public void onFailure() {

            }
        };

        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReciever,intentFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReciever();

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReciever);
    }


    private void apicall() {
        viewDialog = new ViewDialog(MobileVerification.this);
        viewDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        viewDialog.hideDialog();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            if (jsonObject1.getString("success").equalsIgnoreCase("1")) {
                                Intent in = new Intent(getApplicationContext(), create_password.class).putExtra("mobile", mobileNumber.toString()).putExtra("forgot","yes");
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            viewDialog.hideDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                        viewDialog.hideDialog();
                        Toast.makeText(MobileVerification.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", mobileNumber.toString());





                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    public void sendWebOTP(){
        viewDialog = new ViewDialog(MobileVerification.this);
        viewDialog.showDialog();
       // Log.e("hh",mobileNumber);


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, constant.prefix2 + "send_otp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response",response);
                        viewDialog.hideDialog();
                        Toast.makeText(context, "OTP Sent", Toast.LENGTH_SHORT).show();

                        new CountDownTimer(60000, 1000) {

                            public void onTick(long millisUntilFinished) {
                                resendButton.setText("wait " + millisUntilFinished / 1000+" sec");
                                //here you can have your logic to set text to edittext
                            }

                            public void onFinish() {
                                resendButton.setText(getString(R.string.resend_otp));
                            }

                        }.start();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        viewDialog.hideDialog();
                        error.printStackTrace();
                        Toast.makeText(MobileVerification.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", mobileNumber);
                params.put("otp", otp);
                params.put("code","38ho3f3ws");


                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    public void sendOTP(){

        viewDialog = new ViewDialog(MobileVerification.this);
        viewDialog.showDialog();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91"+mobileNumber)       // Phone number to verify
                        .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity((Activity) context)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


    }

    public String getOtp(){
        return otp1.getText().toString()+otp2.getText().toString()+otp3.getText().toString()+otp4.getText().toString()+otp5.getText().toString()+otp6.getText().toString();
    }

    private void initViews() {
        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        otp5 = findViewById(R.id.otp5);
        otp6 = findViewById(R.id.otp6);
        verify = findViewById(R.id.verify);
        resendButton = findViewById(R.id.resend_button);
        mobile_n = findViewById(R.id.mobile_n);


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Intent intent=new Intent();
                intent.putExtra("verification","success");
                setResult(RESULT_OK,intent);
                finish();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                otpId = verificationId;
                Log.e("cs",verificationId);
                otp = verificationId;
                otpToken = token;

                Toast.makeText(context, "OTP Sent", Toast.LENGTH_SHORT).show();

                viewDialog.hideDialog();

                new CountDownTimer(60000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        resendButton.setText("wait " + millisUntilFinished / 1000+" sec");
                        //here you can have your logic to set text to edittext
                    }

                    public void onFinish() {
                        resendButton.setText(getString(R.string.resend_otp));
                    }

                }.start();

            }
        };
    }

    public class GenericTextWatcher implements TextWatcher {
        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch (view.getId()) {

                case R.id.otp1:
                    if (text.length() == 1)
                        otp2.requestFocus();
                    break;
                case R.id.otp2:
                    if (text.length() == 1)
                        otp3.requestFocus();
                    else if (text.length() == 0)
                        otp1.requestFocus();
                    break;
                case R.id.otp3:
                    if (text.length() == 1)
                        otp4.requestFocus();
                    else if (text.length() == 0)
                        otp2.requestFocus();
                    break;
                case R.id.otp4:
                    if (text.length() == 1)
                        otp5.requestFocus();
                    else if (text.length() == 0)
                        otp3.requestFocus();
                    break;
                case R.id.otp5:
                    if (text.length() == 1)
                        otp6.requestFocus();
                    else if (text.length() == 0)
                        otp3.requestFocus();
                    break;
                case R.id.otp6:
                    if (text.length() == 0)
                        otp5.requestFocus();
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            String text = arg0.toString();
            switch (view.getId()) {

                case R.id.otp1:
                    if (text.length() == 1)
                        otp2.requestFocus();
                    break;
                case R.id.otp2:
                    if (text.length() == 1)
                        otp3.requestFocus();
                    else if (text.length() == 0)
                        otp1.requestFocus();
                    break;
                case R.id.otp3:
                    if (text.length() == 1)
                        otp4.requestFocus();
                    else if (text.length() == 0)
                        otp2.requestFocus();
                    break;
                case R.id.otp4:
                    if (text.length() == 1)
                        otp5.requestFocus();
                    else if (text.length() == 0)
                        otp3.requestFocus();
                    break;
                case R.id.otp5:
                    if (text.length() == 1)
                        otp6.requestFocus();
                    else if (text.length() == 0)
                        otp4.requestFocus();
                    break;
                case R.id.otp6:
                    if (text.length() == 0)
                        otp5.requestFocus();
                    break;
            }
        }
    }
}