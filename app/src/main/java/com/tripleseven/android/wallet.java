package com.tripleseven.android;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class wallet extends AppCompatActivity {

    private static final String COOKIE_KEY = "Cookie";
    private static final String SESSION_COOKIE = "session";

    private latobold totalBalance, deposit_balance, winning_balance,cash_balance;
    private latobold addMoney;
    private EditText amount;
    private latobold depositMoney;
    LinearLayout paytm_gateway, razorpay, bank;
    TextView bank_details;
    ImageView image_preview;

    ViewDialog progressDialog;
    String url = constant.prefix2 + "get_wallet";

    String gw_url = constant.prefix2 + "initiate_gw_payment";
    String url2 = constant.prefix2 + "verify_deposit";
    String url3 = constant.prefix2 + "create_deposit";
    String _amount = "0";
    final int UPI_PAYMENT = 0;
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();
    String hash, hashKey;
    String package_name = "";
    private RelativeLayout addCash;
    private ImageView back;
    private ImageView backAdd;
    private ImageView walletColorIcon;
    private latonormal newBalance;
    private latonormal depost500, min_deposit;
    private latonormal depost1000;
    private LinearLayout paytmIcon;
    private LinearLayout gpayIcon;
    private LinearLayout phonepeIcon;
    private LinearLayout BhimIcon;
    String selectedApp;
    private RecyclerView recycler;
    private LinearLayout paytmGateway;
    private latobold bankDetails;
    private ImageView imagePreview;
    private latonormal depost200;
    private latonormal depost2000;
    private latobold withdrawButton;
    private latobold addFund;
    private LinearLayout methods;

    private String gIntent;
    private String phIntent;
    private String ptIntent;
    private String bIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        initViews();

        withdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        min_deposit.setText("Minimum Deposit "+getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("min_deposit", "10")+"/- per transactions\nMaximum deposit - 5 lakhs per day");

        //Check if Gateway enabled, redirect to UPI Gateway else show other methods
        addFund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String upiGateway = getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("UPI_GATEWAY", "0");
                if (Objects.equals(upiGateway, "0")) {
                    Log.d("Starting UPI G payment", "yes");
                    getUpiGatewayPayUrl();
                } else {
                    methods.setVisibility(View.VISIBLE);
                }
            }
        });

        back.setOnClickListener(v -> finish());

        backAdd.setOnClickListener(v -> addCash.setVisibility(View.GONE));

        if (getIntent().hasExtra("action")) {
            if (getIntent().getStringExtra("action").equals("deposit")) {
                addCash.setVisibility(View.VISIBLE);
            }
        }

        paytmIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (amount.getText().toString().isEmpty() || amount.getText().toString().equals("0")) {
                    amount.setError("Enter points");
                    return;
                } else if (Integer.parseInt(amount.getText().toString()) < Integer.parseInt(getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("min_deposit", "10"))) {
                    amount.setError("Enter points above " + getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("min_deposit", "10"));
                    return;
                }
               // startActivity(new Intent(wallet.this, webview.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("amount", amount.getText().toString()).putExtra("gateway", "razorpay"));

                payUsingUpi(ptIntent, "paytm");

            }
        });

        bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (amount.getText().toString().isEmpty() || amount.getText().toString().equals("0")) {
                    amount.setError("Enter points");
                    return;
                } else if (Integer.parseInt(amount.getText().toString()) < Integer.parseInt(getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("min_deposit", "10"))) {
                    amount.setError("Enter points above " + getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("min_deposit", "10"));
                    return;
                }

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(wallet.this);
            }
        });

        gpayIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount.getText().toString().isEmpty() || amount.getText().toString().equals("0")) {
                    amount.setError("Enter points");
                    return;
                } else if (Integer.parseInt(amount.getText().toString()) < Integer.parseInt(getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("min_deposit", "10"))) {
                    amount.setError("Enter points above " + getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("min_deposit", "10"));
                    return;
                }

                payUsingUpi(gIntent, "gpay");
            }
        });

        phonepeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount.getText().toString().isEmpty() || amount.getText().toString().equals("0")) {
                    amount.setError("Enter points");
                    return;
                } else if (Integer.parseInt(amount.getText().toString()) < Integer.parseInt(getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("min_deposit", "10"))) {
                    amount.setError("Enter points above " + getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("min_deposit", "10"));
                    return;
                }

                payUsingUpi(phIntent,"phonepe");
            }
        });

        BhimIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount.getText().toString().isEmpty() || amount.getText().toString().equals("0")) {
                    amount.setError("Enter points");
                    return;
                } else if (Integer.parseInt(amount.getText().toString()) < Integer.parseInt(getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("min_deposit", "10"))) {
                    amount.setError("Enter points above " + getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("min_deposit", "10"));
                    return;
                }
              //  startActivity(new Intent(wallet.this, webview.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("amount", amount.getText().toString()).putExtra("gateway", "razorpay"));

                  payUsingUpi(bIntent,"bhim");
            }
        });

        paytm_gateway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount.getText().toString().isEmpty() || amount.getText().toString().equals("0")) {
                    amount.setError("Enter points");
                    return;
                } else if (Integer.parseInt(amount.getText().toString()) < Integer.parseInt(getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("min_deposit", "10"))) {
                    amount.setError("Enter points above " + getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("min_deposit", "10"));
                    return;
                }

                startActivity(new Intent(wallet.this, webview.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("amount", amount.getText().toString()).putExtra("gateway", "paytm"));
            }
        });

        razorpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount.getText().toString().isEmpty() || amount.getText().toString().equals("0")) {
                    amount.setError("Enter points");
                    return;
                } else if (Integer.parseInt(amount.getText().toString()) < Integer.parseInt(getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("min_deposit", "10"))) {
                    amount.setError("Enter points above " + getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("min_deposit", "10"));
                    return;
                }

                startActivity(new Intent(wallet.this, webview.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("amount", amount.getText().toString()).putExtra("gateway", "razorpay"));
            }
        });

        addMoney.setOnClickListener(v -> addCash.setVisibility(View.VISIBLE));

        depositMoney.setOnClickListener(v -> {

            if (amount.getText().toString().isEmpty()) {
                amount.setError("Enter amount");
            } else {
                //  apicall3();
            }

        });
        depost200.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount.setText("200");
            }
        });

        depost500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount.setText("500");
            }
        });

        depost1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount.setText("1000");
            }
        });
        depost2000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount.setText("2000");
            }
        });

        apicall();
    }

    @Override
    public void onBackPressed() {
        if (addCash.getVisibility() == View.VISIBLE) {
            addCash.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onResume() {
        if (addCash.getVisibility() == View.VISIBLE) {
            Log.d("calling api bal", "yes");
            apicall();
            addCash.setVisibility(View.GONE);
        }
        super.onResume();
    }


    public static byte[] getFileDataFromDrawable(Context context, Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void apiCall() throws URISyntaxException {

        ProgressDialog waitingDialog = new ProgressDialog(wallet.this);
        waitingDialog.setCancelable(false);
        waitingDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        waitingDialog.setTitle("Updating Profile");
        waitingDialog.setMessage("Uploading media files");
        waitingDialog.show();

        File file;
        file = new File(getFilesDir() + "/jpeg/");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(getFileDataFromDrawable(getApplicationContext(), image_preview.getDrawable()));
            fos.close();
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
        }

        try {
            AndroidNetworking.upload(constant.prefix2 + "deposit_bank")
                    .addMultipartFile("img", file)
                    .addHeaders(COOKIE_KEY, CookieManager.getInstance().getCookie(SESSION_COOKIE))
                    .addMultipartParameter("mobile", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile", ""))
                    .addMultipartParameter("amount", amount.getText().toString())
                    .setPriority(Priority.HIGH)
                    .build()
                    .setUploadProgressListener((bytesUploaded, totalBytes) -> {

                        waitingDialog.setMax(Integer.parseInt(totalBytes / 1024 + ""));
                        waitingDialog.setProgress(Integer.parseInt(bytesUploaded / 1024 + ""));

                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            waitingDialog.dismiss();
                            try {
                                if (response.getString("success").equals("1")) {

                                    Toast.makeText(wallet.this, "We received your deposit request successfully", Toast.LENGTH_SHORT).show();

                                    Intent in = new Intent(getApplicationContext(), HomeScreen.class);
                                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(in);
                                    finish();
                                } else {
                                    Toast.makeText(wallet.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError error) {
                            waitingDialog.dismiss();
                            Toast.makeText(wallet.this, "We are not able to upload image, check internet connection or contact our team if issue persist", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPI_PAYMENT) {
            if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                if (data != null) {
                    String trxt = data.getStringExtra("response");
                    Log.d("UPI", "onActivityResult: " + trxt);
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add(trxt);
                    upiPaymentDataOperation(dataList);
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
            } else {
                Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                ArrayList<String> dataList = new ArrayList<>();
                dataList.add("nothing");
                upiPaymentDataOperation(dataList);
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUriContent();
                image_preview.setImageURI(resultUri);
                image_preview.setVisibility(View.VISIBLE);
                try {
                    apiCall();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(wallet.this)) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: " + str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(wallet.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                apicall2();
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(wallet.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                //  apicall2();
            } else {
                Toast.makeText(wallet.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(wallet.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }


    String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }


    private void apicall() {

        progressDialog = new ViewDialog(wallet.this);
        progressDialog.showDialog();

        hashKey = randomString(10);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String response = null;

        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url,
                response1 -> {
                    Log.e("edsa", "efsdc" + response1);

                    progressDialog.hideDialog();
                    try {
                        JSONObject jsonObject1 = new JSONObject(response1);

                        newBalance.setText("₹ "+jsonObject1.getString("total") );
                        deposit_balance.setText("₹ "+jsonObject1.getString("wallet"));
                        totalBalance.setText("₹ "+jsonObject1.getString("total"));
                        winning_balance.setText("₹ "+jsonObject1.getString("winning"));
                        cash_balance.setText("₹ "+jsonObject1.getString("bonus"));
                        if (jsonObject1.getString("paytm").equals("1")) {
                            paytm_gateway.setVisibility(View.VISIBLE);
                        }
//                        if (Integer.valueOf(jsonObject1.getString("winning")) < 500){
////                            withdrawButton.setVisibility(View.GONE);
////                        }
                        double winningamt = Integer.valueOf(jsonObject1.getString("winning"));

                        if (jsonObject1.getString("razorpay").equals("1")) {
                            razorpay.setVisibility(View.VISIBLE);
                        }

                        bank_details.setText(getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("bank_details", "N/A"));

                        Integer min_withdraw_amount = jsonObject1.getInt("min_withdraw");
                        String is_open = jsonObject1.getString("withdraw_open");
                        String w_msg = jsonObject1.getString("withdraw_open_msg");

                        if (jsonObject1.getString("is_bank").equals("1")) {
                            withdrawButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (winningamt < min_withdraw_amount){
                                        Toast.makeText(wallet.this, "Minimum " + min_withdraw_amount + " winning balance required to withdraw", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (is_open.equals("1")) {
                                            startActivity(new Intent(wallet.this, withdraw.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                        } else {
                                            Toast.makeText(wallet.this, w_msg, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                        } else {
                            withdrawButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (winningamt < min_withdraw_amount){
                                        Toast.makeText(wallet.this, "Minimum " + min_withdraw_amount + " winning balance required to withdraw", Toast.LENGTH_SHORT).show();
                                    }else {
                                        if (is_open.equals("1")) {
                                            startActivity(new Intent(wallet.this, withdraw.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                        } else {
                                            Toast.makeText(wallet.this, w_msg, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                        }


                        ArrayList<String> date = new ArrayList<>();
                        ArrayList<String> remark = new ArrayList<>();
                        ArrayList<String> amount = new ArrayList<>();
                        ArrayList<String> type = new ArrayList<>();

                        JSONArray jsonArray = jsonObject1.getJSONArray("data");
                        for (int a = 0; jsonArray.length() > a; a++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(a);


                            date.add(jsonObject.getString("date"));
                            amount.add(jsonObject.getString("amount"));
                            remark.add(jsonObject.getString("remark"));
                            type.add(jsonObject.getString("type"));

                        }

                        adapter_transaction_funds rc = new adapter_transaction_funds(wallet.this, date, remark, amount, type);
                        recycler.setLayoutManager(new GridLayoutManager(wallet.this, 1));
                        recycler.setAdapter(rc);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.hideDialog();
                    }
                },
                error -> {
                    error.printStackTrace();
                    progressDialog.hideDialog();
                    Toast.makeText(wallet.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile", null));
                params.put("session", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));
                params.put("amount", amount.getText().toString());
                params.put("hash_key", hashKey);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    private JSONObject getUpiGatewayPayUrl() {
        progressDialog = new ViewDialog(wallet.this);
        progressDialog.showDialog();

        hashKey = randomString(10);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final JSONObject[] response = {null};

        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, gw_url,
                response1 -> {
                    progressDialog.hideDialog();
                    try {
                        JSONObject jsonObject1 = new JSONObject(response1);
                        Log.d("response initiate pay", jsonObject1.toString());

                        if (jsonObject1.getString("success").equals("1")) {
                            JSONObject res = jsonObject1.getJSONObject("data");
                            Log.d("Start UPIG W payment", "yes");
                            String payUrl = res.getString("payment_url");
                            startActivity(
                                    new Intent(wallet.this, webview.class)
                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("url", payUrl)
                            );
                        } else {
                            Toast.makeText(wallet.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.hideDialog();
                        Toast.makeText(wallet.this, "Not able to get the payment link", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    progressDialog.hideDialog();
                    Toast.makeText(wallet.this, "Not able to get the payment link", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile", null));
                params.put("session", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));
                params.put("amount", amount.getText().toString());
                params.put("hash_key", hashKey);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);

        Log.d("restuened res:", Arrays.toString(response));
        return response[0];
    }

    private void apicall3(String upiApp, String type) {

        progressDialog = new ViewDialog(wallet.this);
        progressDialog.showDialog();

        hashKey = randomString(10);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String response = null;

        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url3,
                response1 -> {
                    Log.e("edsa", "efsdc" + response1);
                    progressDialog.hideDialog();
                    try {
                        JSONObject jsonObject1 = new JSONObject(response1);

                        if (jsonObject1.getString("success").equals("1")) {
                            hash = jsonObject1.getString("hash");
                            //payUsingUpi(amount.getText().toString(), getString(R.string.app_name), "Adding amount to wallet", upiApp);
                        } else {
                            Toast.makeText(wallet.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.hideDialog();
                    }
                },
                error -> {
                    error.printStackTrace();
                    progressDialog.hideDialog();
                    Toast.makeText(wallet.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile", null));
                params.put("session", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));
                params.put("amount", amount.getText().toString());
                params.put("hash_key", hashKey);
                params.put("type", type);


                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    private void apicall2() {

        progressDialog = new ViewDialog(wallet.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String response = null;

        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url2,
                response1 -> {
                    Log.e("edsa", "efsdc" + response1);
                    progressDialog.hideDialog();
                    try {
                        JSONObject jsonObject1 = new JSONObject(response1);

                        if (jsonObject1.getString("success").equals("0")) {


                            android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(wallet.this);
                            LayoutInflater factory = LayoutInflater.from(wallet.this);
                            View v = factory.inflate(R.layout.msg_dialog, null);

                            TextView close = v.findViewById(R.id.close);
                            TextView msgView = v.findViewById(R.id.msg);
                            msgView.setText("We received your payment successfully, We will update your wallet balance in sometime");

                            builder1.setView(v);
                            builder1.setCancelable(true);
                            android.app.AlertDialog alert11 = builder1.create();
                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alert11.dismiss();
                                    startActivity(new Intent(getApplicationContext(), HomeScreen.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                    finish();
                                }
                            });
                            alert11.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            alert11.show();


                        } else {
                            Toast.makeText(wallet.this, "Amount added to wallet", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HomeScreen.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.hideDialog();
                    }
                },
                error -> {
                    error.printStackTrace();
                    progressDialog.hideDialog();
                    Toast.makeText(wallet.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile", null));
                params.put("hash_key", hashKey);
                params.put("selectedApp", selectedApp);
                params.put("hash", hash);
                params.put("amount", amount.getText().toString());
                params.put("session", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    String getRandom() {
        Random random = new Random();
        int length = 8;
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            stringBuilder.append(random.nextInt(10)); // Generates a random digit (0-9)
        }
        return stringBuilder.toString();
    }

    void payUsingUpi(String url, String upiApp) {
        selectedApp = upiApp;

        Intent chooser = new Intent(Intent.ACTION_VIEW);
         chooser.setData(Uri.parse(url));


        switch (upiApp) {
            case "gpay":
                package_name = getString(R.string.gpay);
                break;
            case "paytm":
                package_name = getString(R.string.paytm);
                break;
            case "phonepe":
                package_name = getString(R.string.phonepe);
                break;
            case "bhim":
                package_name = "in.org.npci.upiapp";
                break;
        }

        chooser.setPackage(package_name);

        PackageManager pm = getPackageManager();

        if (!isPackageInstalled(package_name, pm)) {
            new AlertDialog.Builder(wallet.this)
                    .setTitle(upiApp + " Not Installed")
                    .setMessage("Your device don't have application installed, Do you want to download now ?")
                    .setPositiveButton("Download", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + package_name)));
                            } catch (ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + package_name)));
                            }
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton("Later", null)
                    .show();

            return;
        }

        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(wallet.this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }
    }



    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        Log.e("checkingPackage", ":" + packageName);
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    private void initViews() {
        deposit_balance = findViewById(R.id.deposit_balance);
        winning_balance = findViewById(R.id.winnig_balance);
        cash_balance = findViewById(R.id.cash_balance);
        min_deposit = findViewById(R.id.min_deposit);
        totalBalance = findViewById(R.id.total_balance);
        addMoney = findViewById(R.id.add_money);
        amount = findViewById(R.id.amount2);
        depositMoney = findViewById(R.id.deposit_money);
        addCash = findViewById(R.id.addCash);
        back = findViewById(R.id.back);
        backAdd = findViewById(R.id.back_add);
        walletColorIcon = findViewById(R.id.wallet_color_icon);
        newBalance = findViewById(R.id.new_balance);
        depost500 = findViewById(R.id.depost_500);
        depost1000 = findViewById(R.id.depost_1000);
        paytmIcon = findViewById(R.id.paytm_icon);
        gpayIcon = findViewById(R.id.gpay_icon);
        phonepeIcon = findViewById(R.id.phonepe_icon);
        BhimIcon = findViewById(R.id.bhim_icon);
        paytm_gateway = findViewById(R.id.paytm_gateway);
        razorpay = findViewById(R.id.razorpay);
        bank = findViewById(R.id.bank);
        bank_details = findViewById(R.id.bank_details);
        image_preview = findViewById(R.id.image_preview);
        recycler = findViewById(R.id.recycler);
        depost200 = findViewById(R.id.depost_200);
        depost2000 = findViewById(R.id.depost_2000);
        withdrawButton = findViewById(R.id.withdraw_button);
        addFund = findViewById(R.id.add_fund);
        bankDetails = findViewById(R.id.bank_details);
        methods = findViewById(R.id.methods);
    }
}