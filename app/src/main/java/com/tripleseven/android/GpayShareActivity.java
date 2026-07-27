package com.tripleseven.android;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GpayShareActivity extends AppCompatActivity {

    private static final String TAG = "GPAY_SHARE";
    private static final int GPAY_REQUEST_CODE = 1001;
    private static final String GPAY_PACKAGE = "com.google.android.apps.nbu.paisa.user";

    private String amount;
    private String transactionId;
    private ViewDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        amount = getIntent().getStringExtra("amount");
        if (amount == null || amount.isEmpty()) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d(TAG, "GpayShareActivity.onCreate() - amount: " + amount);

        // Step 1: Create transaction on backend and get GPay deep link
        createTransaction();
    }

    /**
     * Step 1: Call backend /initiate_gpay_share to create transaction and get GPay URL
     */
    private void createTransaction() {
        progressDialog = new ViewDialog(this);
        progressDialog.showDialog();

        String url = constant.prefix2 + "initiate_gpay_share";

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new MyStringRequest(
                getSharedPreferences(constant.prefs, MODE_PRIVATE),
                Request.Method.POST, url,
                response1 -> {
                    progressDialog.hideDialog();
                    try {
                        JSONObject jsonObject1 = new JSONObject(response1);
                        Log.d(TAG, "initiate_gpay_share response: " + jsonObject1.toString());

                        if (jsonObject1.getString("success").equals("1")) {
                            JSONObject res = jsonObject1.getJSONObject("data");
                            transactionId = res.getString("transaction_id");
                            String gpayUrl = res.getString("gpay_url");

                            Log.d(TAG, "Transaction ID: " + transactionId);
                            Log.d(TAG, "GPay URL: " + gpayUrl);

                            // Step 2: Launch GPay Share Intent
                            launchGpayShareIntent(gpayUrl);
                        } else {
                            Toast.makeText(GpayShareActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(GpayShareActivity.this, "Unable to get payment link. Please try again.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                },
                error -> {
                    error.printStackTrace();
                    progressDialog.hideDialog();
                    Toast.makeText(GpayShareActivity.this, "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
                    finish();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mobile", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile", ""));
                params.put("session", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", ""));
                params.put("amount", amount);
                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        requestQueue.add(postRequest);
    }

    /**
     * Step 2: Launch GPay via deep link (gpay.app.goo.gl)
     */
    private void launchGpayShareIntent(String gpayUrl) {
        PackageManager pm = getPackageManager();
        try {
            // Check if GPay is installed
            pm.getPackageInfo(GPAY_PACKAGE, 0);

            Log.d(TAG, "Launching GPay with URL: " + gpayUrl);

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(gpayUrl));
            intent.setPackage(GPAY_PACKAGE);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Start for result to get payment response
            startActivityForResult(intent, GPAY_REQUEST_CODE);

        } catch (PackageManager.NameNotFoundException e) {
            // GPay not installed - fallback to UPI Intent flow
            Log.d(TAG, "GPay not installed, falling back to UPI Intent");
            Toast.makeText(this, "GPay not installed. Opening UPI apps...", Toast.LENGTH_LONG).show();
            fallbackToUpiIntent();
        }
    }

    /**
     * Fallback to standard UPI Intent flow if GPay not installed
     */
    private void fallbackToUpiIntent() {
        Intent intent = new Intent(GpayShareActivity.this, deposit_money.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra("amount", amount);
        startActivity(intent);
        finish();
    }

    /**
     * Step 3: Handle GPay response in onActivityResult
     * GPay returns response in Intent data with "response" extra containing:
     * "Status=SUCCESS&txnId=XXX&ApprovalRefNo=UTR123456&txnRef=TXN123"
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GPAY_REQUEST_CODE) {
            Log.d(TAG, "onActivityResult - requestCode: " + requestCode + ", resultCode: " + resultCode);

            String response = data != null ? data.getStringExtra("response") : null;
            Log.d(TAG, "GPay Response: " + response);

            if (response != null && !response.isEmpty()) {
                // Parse response: Status=SUCCESS&txnId=XXX&ApprovalRefNo=UTR123&txnRef=TXN123
                String status = "";
                String utr = "";

                for (String part : response.split("&")) {
                    String[] kv = part.split("=", 2);
                    if (kv.length == 2) {
                        String key = kv[0].toLowerCase();
                        String value = kv[1];
                        if (key.equals("status")) {
                            status = value.toLowerCase();
                        } else if (key.equals("approvalrefno")) {
                            utr = value;
                        }
                    }
                }

                Log.d(TAG, "Parsed - Status: " + status + ", UTR: " + utr);

                if ("success".equals(status) && !utr.isEmpty()) {
                    // Step 4: Submit UTR to backend for verification
                    submitUtrToBackend(utr);
                } else if ("failure".equals(status) || "failed".equals(status)) {
                    Toast.makeText(this, "Payment failed. Please try again.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // Payment cancelled or unclear status - still verify
                    Toast.makeText(this, "Payment status unclear. Verifying...", Toast.LENGTH_SHORT).show();
                    verifyTransaction();
                }
            } else {
                // No response data - user may have cancelled or GPay didn't return data
                Log.d(TAG, "No response data from GPay");
                verifyTransaction();
            }
        }
    }

    /**
     * Step 4: Submit UTR to backend (similar to UPI Intent flow)
     */
    private void submitUtrToBackend(String utr) {
        progressDialog = new ViewDialog(this);
        progressDialog.showDialog();

        String url = constant.prefix + "submit_upi_reference";

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new MyStringRequest(
                getSharedPreferences(constant.prefs, MODE_PRIVATE),
                Request.Method.POST, url,
                response1 -> {
                    progressDialog.hideDialog();
                    try {
                        JSONObject jsonObject1 = new JSONObject(response1);
                        Log.d(TAG, "submit_upi_reference response: " + jsonObject1.toString());

                        if (jsonObject1.getString("success").equals("1")) {
                            new androidx.appcompat.app.AlertDialog.Builder(this)
                                    .setTitle("Payment Reference Submitted")
                                    .setMessage("Your payment reference has been submitted successfully. Your wallet balance will be updated after admin verification (usually within a few minutes).")
                                    .setPositiveButton("OK", (dialog, which) -> {
                                        startActivity(new Intent(getApplicationContext(), HomeScreen.class)
                                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                        finish();
                                    })
                                    .setCancelable(false)
                                    .show();
                        } else {
                            Toast.makeText(this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error processing response", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                },
                error -> {
                    error.printStackTrace();
                    progressDialog.hideDialog();
                    Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    finish();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mobile", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile", null));
                params.put("session", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));
                params.put("transaction_id", transactionId);
                params.put("utr", utr);
                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    /**
     * Alternative verification if UTR not available - call verify endpoint
     */
    private void verifyTransaction() {
        progressDialog = new ViewDialog(this);
        progressDialog.showDialog();

        String url = constant.prefix2 + "verify_gpay_share";

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new MyStringRequest(
                getSharedPreferences(constant.prefs, MODE_PRIVATE),
                Request.Method.POST, url,
                response1 -> {
                    progressDialog.hideDialog();
                    try {
                        JSONObject jsonObject1 = new JSONObject(response1);
                        Toast.makeText(this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                        if (jsonObject1.getString("success").equals("1")) {
                            new androidx.appcompat.app.AlertDialog.Builder(this)
                                    .setTitle("Payment Verified")
                                    .setMessage(jsonObject1.getString("msg"))
                                    .setPositiveButton("OK", (dialog, which) -> {
                                        startActivity(new Intent(getApplicationContext(), HomeScreen.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                        finish();
                                    })
                                    .setCancelable(false)
                                    .show();
                        } else {
                            // Check if the message indicates pending approval
                            String msg = jsonObject1.getString("msg");
                            if (msg.contains("admin approval") || msg.contains("submitted for verification")) {
                                new androidx.appcompat.app.AlertDialog.Builder(this)
                                        .setTitle("Payment Submitted")
                                        .setMessage("Your payment has been submitted for verification. Balance will be updated after admin approval.")
                                        .setPositiveButton("OK", (dialog, which) -> {
                                            startActivity(new Intent(getApplicationContext(), HomeScreen.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                            finish();
                                        })
                                        .setCancelable(false)
                                        .show();
                            } else {
                                // Actual error message
                                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        finish();
                    }
                },
                error -> {
                    progressDialog.hideDialog();
                    Toast.makeText(this, "Verification failed", Toast.LENGTH_SHORT).show();
                    finish();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mobile", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile", ""));
                params.put("session", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", ""));
                params.put("transaction_id", transactionId);
                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }
}