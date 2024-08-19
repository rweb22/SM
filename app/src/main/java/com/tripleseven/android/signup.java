package com.tripleseven.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class signup extends AppCompatActivity {

    protected EditText mobile;
    protected EditText name;
    protected EditText email;
    protected EditText password;
    protected LinearLayout submit;
    protected EditText refcode;
    TextView create, apply_refer;
    ViewDialog progressDialog;
    String url;
    String url2 = constant.prefix + "check_referral";
    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";

    String hash = "";

    ActivityResultLauncher<Intent> mStartForResult;

    String mobileNumber = "", otp = "";
    FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    String otpId;
    PhoneAuthProvider.ForceResendingToken otpToken;
    Boolean verified = false;
    private EditText confirm;
    private EditText mpin;
    private LinearLayout enterBlock;
    private LinearLayout signupForm;
    private LinearLayout createBlock;
    private latobold submitText;
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
    String ref_code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_signup);
        initViews();
        initView();
       // Glide.with(signup.this).load(R.drawable.signup_anim).into(image);
        url = constant.prefix + getString(R.string.register);
        hash = getRandomString(30);

        apply_refer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                referPopup.setVisibility(View.VISIBLE);
            }
        });
        mobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    image.setVisibility(View.VISIBLE);
                } else {
                    image.setVisibility(View.VISIBLE);
                }
            }
        });

        referSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_refer();
            }
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
                            Intent in = new Intent(getApplicationContext(), create_password.class).putExtra("mobile",mobile.getText().toString()).putExtra("forgot","signup").putExtra("refcode", ref_code.toString());
                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(in);
                            finish();




                        }
                    }
                });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signup.this, login.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobile.getText().toString().isEmpty() || mobile.getText().toString().length() != 10) {
                    mobile.setError("Enter valid mobile number");
                }
                 else {
                   // Log.e("kkk",mobile.getText().toString());
                   mStartForResult.launch(new Intent(signup.this, MobileVerification.class).putExtra("mobile", mobile.getText().toString()).putExtra("forgot","signup"));
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

    private void apicall3() {

        progressDialog = new ViewDialog(signup.this);
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
//

//
//                                Intent in = new Intent(getApplicationContext(), HomeScreen.class);
//                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(in);
//                                finish();

                                Intent in = new Intent(getApplicationContext(), create_password.class).putExtra("mobile",mobile.getText().toString()).putExtra("forgot","no").putExtra("refcode",ref_code.toString()).putExtra("name",name.getText().toString()).putExtra("email",email.getText().toString());
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(in);
                                finish();

                                //   Toast.makeText(signup.this, "Account created successfully, Please login now", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(getApplicationContext(), "Mobile Number Already Registered", Toast.LENGTH_SHORT).show();
                               // Toast.makeText(getApplicationContext(), jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(signup.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", mobile.getText().toString());
                params.put("session", hash);





                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }


    private void check_refer() {

        progressDialog = new ViewDialog(signup.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        referPopup.setVisibility(View.GONE);
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            if (jsonObject1.getString("success").equalsIgnoreCase("1")) {
                                ref_code = referCode.getText().toString();
                                apply_refer.setText("Refer code applied");
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
                        Toast.makeText(signup.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("refcode", referCode.getText().toString());


                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }



    private void initView() {
        mobile = findViewById(R.id.create_pass);
        name = findViewById(R.id.pass);
        email = findViewById(R.id.email_text);
        password = findViewById(R.id.pass);
        submit = findViewById(R.id.submit);
        refcode = findViewById(R.id.refcode);
        create = findViewById(R.id.create);

    }

    private void initViews() {
        mobile = findViewById(R.id.create_pass);
        name = findViewById(R.id.pass);
        email = findViewById(R.id.email_text);
        password = findViewById(R.id.pass);
        confirm = findViewById(R.id.confirm);
        refcode = findViewById(R.id.refcode);
        submit = findViewById(R.id.submit);
        create = findViewById(R.id.create);
        mpin = findViewById(R.id.mpin);
        enterBlock = findViewById(R.id.enter_block);
        signupForm = findViewById(R.id.signup_form);
        createBlock = findViewById(R.id.create_block);
        submitText = findViewById(R.id.submit_text);
        image = findViewById(R.id.image);
        apply_refer = findViewById(R.id.apply_refer);
        referCode = findViewById(R.id.refer_code);
        referPopup = findViewById(R.id.refer_popup);
        referSubmit = findViewById(R.id.refer_submit);
    }
}
