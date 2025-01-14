package com.samratmatka.android;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ForgotPassword extends AppCompatActivity {

    Boolean isFirst = true;
    Boolean verified = false;
    ViewDialog viewDialog;
    String mobileNumber = "", otp = "";
    FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    ActivityResultLauncher<Intent> mStartForResult;

    String otpId;
    PhoneAuthProvider.ForceResendingToken otpToken;
    ViewDialog progressDialog;
    String url = constant.prefix + "forgot.php";

    private CardView back;
    private EditText mobile;
    private latobold mobileVerified;
    private EditText otpView;
    private latobold resendButton;
    private EditText password;
    private latobold submit;
    private LinearLayout bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mAuth = FirebaseAuth.getInstance();

        Random rand = new Random();
        otp = String.format("%06d", rand.nextInt(1000000));


        initViews();

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mobile.getText().toString().isEmpty() || mobile.getText().toString().length() != 10) {
                    mobile.setError("Enter valid mobile number");
                }
                else {
                    mStartForResult.launch(new Intent(ForgotPassword.this, MobileVerification.class).putExtra("mobile", mobile.getText().toString()).putExtra("forgot","yes"));
                }}
        });

        mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent == null) return;
                        if (intent.hasExtra("verification") && intent.getStringExtra("verification").equals("success")) {
                            verified = true;
//                            mobile.setVisibility(View.GONE);
//                            submitText.setText("Continue");
//                            signupForm.setVisibility(View.VISIBLE);

                            startActivity(new Intent(ForgotPassword.this, create_password.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("forgot","yes").putExtra("mobile",mobile.getText().toString()));
                        }
                    }
                });

        otpView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 6) {
                    if (otp == null) return;
                    if (otpView.getText().toString().isEmpty() || otpView.getText().toString().length() != 6) {
                        Toast.makeText(ForgotPassword.this, "Enter OTP", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (otp.equals(otpView.getText().toString())){
                        mobileVerified.setVisibility(View.VISIBLE);
                                        otpView.setVisibility(View.GONE);
                                       resendButton.setVisibility(View.GONE);
                                       verified = true;
                    } else {
                        Toast.makeText(ForgotPassword.this, "Incorrect OTP entered", Toast.LENGTH_SHORT).show();
                    }

//                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpId, otpView.getText().toString());
//                    mAuth.signInWithCredential(credential)
//                            .addOnCompleteListener(ForgotPassword.this, new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    if (task.isSuccessful()) {
//                                        mobileVerified.setVisibility(View.VISIBLE);
//                                        otpView.setVisibility(View.GONE);
//                                        resendButton.setVisibility(View.GONE);
//                                        verified = true;
//                                    } else {
//
//                                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                            Toast.makeText(ForgotPassword.this, "Incorrect OTP entered", Toast.LENGTH_LONG).show();
//
//                                        } else {
//                                            Toast.makeText(ForgotPassword.this, "Unable to verify please retry later", Toast.LENGTH_LONG).show();
//                                        }
//                                    }
//                                }
                          //  });

                }
            }
        });

        resendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mobileNumber = mobile.getText().toString();

                if (mobile.getText().toString().isEmpty() || mobile.getText().toString().length() != 10) {
                    mobile.setError("Enter valid mobile number");
                } else {
                    if (isFirst) {
                        mobile.setEnabled(false);
                        sendWebOTP();
                    } else {
                        if (resendButton.getText().toString().equals("Resend OTP")) {
                            sendWebOTP();
                        } else {
                            Toast.makeText(ForgotPassword.this, "Wait before resend", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    public void sendWebOTP(){
        viewDialog = new ViewDialog(ForgotPassword.this);
        viewDialog.showDialog();


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, constant.prefix2 +"send_otp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response",response);
                        viewDialog.hideDialog();
                        Toast.makeText(ForgotPassword.this, "OTP Sent", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(ForgotPassword.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
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


    public void sendOTP() {

//        progressDialog = new ViewDialog(ForgotPassword.this);
//        progressDialog.showDialog();

        Toast.makeText(ForgotPassword.this, "Sending OTP", Toast.LENGTH_SHORT).show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + mobileNumber)       // Phone number to verify
                        .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity((Activity) ForgotPassword.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


    }

    private void apicall() {

        progressDialog = new ViewDialog(ForgotPassword.this);
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

                                Toast.makeText(ForgotPassword.this, "Password reset successfully, Please login now", Toast.LENGTH_SHORT).show();

                                Intent in = new Intent(getApplicationContext(), login.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(in);
                                finish();

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
                        Toast.makeText(ForgotPassword.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", mobile.getText().toString());
                params.put("pass", password.getText().toString());


                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }



    private void initViews() {
        back = findViewById(R.id.back);
        mobile = findViewById(R.id.create_pass);
        mobileVerified = findViewById(R.id.mobile_verified);
        otpView = findViewById(R.id.otp_view);
        resendButton = findViewById(R.id.resend_button);
        password = findViewById(R.id.create_pass2);
        submit = findViewById(R.id.submit);
        bottomBar = findViewById(R.id.bottom_bar);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                mobileVerified.setVisibility(View.VISIBLE);
                otpView.setVisibility(View.GONE);
                resendButton.setVisibility(View.GONE);
                verified = true;
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                otpId = verificationId;
                Log.e("cs", verificationId);
                otp = verificationId;
                otpToken = token;

                Toast.makeText(ForgotPassword.this, "OTP Sent", Toast.LENGTH_SHORT).show();

                // progressDialog.hideDialog();

                new CountDownTimer(60000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        resendButton.setText("wait " + millisUntilFinished / 1000 + " sec");
                        //here you can have your logic to set text to edittext
                    }

                    public void onFinish() {
                        resendButton.setText("Resend OTP");
                    }

                }.start();

            }
        };

    }
}