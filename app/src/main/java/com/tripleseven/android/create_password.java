package com.tripleseven.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class create_password extends AppCompatActivity {


    protected EditText confirmPassword;
    protected EditText password;
    protected latobold submit;

    ViewDialog progressDialog;
    String url,url3;
    String url2 = constant.prefix+"referral_apply.php";
    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";

    String hash = "";

    ActivityResultLauncher<Intent> mStartForResult;

    String mobileNumber = "", otp = "";
    FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mobile;
    String otpId;
    PhoneAuthProvider.ForceResendingToken otpToken;
    Boolean verified = false;
    private EditText confirm;
    private EditText mpin;
    private LinearLayout enterBlock;
    private LinearLayout signupForm;
    private LinearLayout createBlock;
    private latobold login;
    private EditText mMpin;
    private LinearLayout mEnterBlock;
    private EditText mMobile;
    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mConfirm;
    private EditText mRefcode;
    private LinearLayout mSignupForm;
    private LinearLayout mCreateBlock;
    private latobold mSubmitText;
    private LinearLayout mSubmit;
    private latobold mCreate;
    private ImageView image;
    private EditText referCode;
    private RelativeLayout referPopup;
    private latobold applyRefer;
    private latobold referSubmit;
    String reff_code, name,email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_create_password);

        String forgot;
        forgot = getIntent().getStringExtra("forgot");
        initViews();
        initView();
        url3 = constant.prefix2 + getString(R.string.register);
        // Glide.with(signup.this).load(R.drawable.signup_anim).into(image);
        url = constant.prefix2 + "forgot_password";
        hash = getRandomString(30);

        mobile = getIntent().getStringExtra("mobile");

        if (getIntent().hasExtra("refcode")){
            reff_code = getIntent().getStringExtra("refcode");
        }

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(create_password.this, login.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().isEmpty() || confirmPassword.getText().toString().isEmpty()) {
                    password.setError(getString(R.string.enter_valid_pass));
                    confirmPassword.setError(getString(R.string.enter_valid_pass));
                }else if(password.getText().length() < 6){
                    password.setError(getString(R.string.min_pass_len));
                   // confirmPassword.setError("Password must be minimunm 6 digit");
                }
                else if(!password.getText().toString().equals(confirmPassword.getText().toString())){
                    confirmPassword.setError(getString(R.string.pass_not_match));
                }
                else {
                    if (forgot.equals("signup")){
                        SharedPreferences.Editor editor = getSharedPreferences(constant.prefs, MODE_PRIVATE).edit();
                        editor.putString("session", hash).apply();
                        apicall2();
                    } else {
                    apicall(forgot);}
                }

            }
        });
    }


    private static String getRandomString(final int sizeOfRandomString) {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    @Override
    public void onBackPressed() {
        if (referPopup.getVisibility() != View.GONE){
            referPopup.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }

    private void apicall2() {

        progressDialog = new ViewDialog(create_password.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url3,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            if (jsonObject1.getString("success").equalsIgnoreCase("1")) {
                                SharedPreferences.Editor editor = getSharedPreferences(constant.prefs, MODE_PRIVATE).edit();
                                editor.putString("mobile", mobile.toString()).apply();
                                editor.putString("login", "true").apply();
                                editor.putString("name", name).apply();
                                editor.putString("email", email).apply();
                                editor.putString("session", jsonObject1.getString("session"));
                                editor.putString("first_time","done").commit();

                                Intent in = new Intent(getApplicationContext(), HomeScreen.class);
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
                        Toast.makeText(create_password.this, getString(R.string.api_error_msg), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", mobile.toString());
                params.put("name", "");
                params.put("email", "");
                params.put("pass", password.getText().toString());
                params.put("refcode", reff_code);
                params.put("session", hash);


                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    private void apicall(String forgott) {

        progressDialog = new ViewDialog(create_password.this);
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

                               // Toast.makeText(create_password.this, "Password reset successfully, Please login now", Toast.LENGTH_SHORT).show();
                                if(forgott.equals("no")) {
                                    Intent in = new Intent(getApplicationContext(), HomeScreen.class);
                                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(in);
                                    finish();
                                } else {
                                    Toast.makeText(create_password.this, getString(R.string.pass_reset_done), Toast.LENGTH_SHORT).show();
                                    Intent in = new Intent(getApplicationContext(), login.class);
                                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(in);
                                    finish();
                                }


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
                        Toast.makeText(create_password.this, getString(R.string.api_error_msg), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", mobile.toString());
                params.put("pass", password.getText().toString());


                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    private void initView() {

        password = findViewById(R.id.create_pass);
        confirmPassword = findViewById(R.id.create_pass2);
        submit = findViewById(R.id.submit);
        login = findViewById(R.id.login);

    }

    private void initViews() {

        password = findViewById(R.id.create_pass);
        confirmPassword = findViewById(R.id.create_pass2);
        login = findViewById(R.id.login);

        submit = findViewById(R.id.submit);

        mpin = findViewById(R.id.mpin);
        enterBlock = findViewById(R.id.enter_block);
        signupForm = findViewById(R.id.signup_form);
        createBlock = findViewById(R.id.create_block);

        image = findViewById(R.id.image);

        referCode = findViewById(R.id.refer_code);
        referPopup = findViewById(R.id.refer_popup);
        referSubmit = findViewById(R.id.refer_submit);
    }
}

