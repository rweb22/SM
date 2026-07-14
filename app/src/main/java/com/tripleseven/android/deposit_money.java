package com.tripleseven.android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class deposit_money extends AppCompatActivity {

    ViewDialog progressDialog;
    EditText amount;

    // New UPI Intent endpoints
    String urlInitiateUpi = constant.prefix + "initiate_upi_intent";
    String urlSubmitUtr = constant.prefix + "submit_upi_reference";

    final int UPI_PAYMENT = 0;
    String transactionId = "";  // Store transaction ID from initiate_upi_intent
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_money);

        amount = findViewById(R.id.amount2);

        // Check if amount was passed from wallet activity
        if (getIntent().hasExtra("amount")) {
            String prefilledAmount = getIntent().getStringExtra("amount");
            amount.setText(prefilledAmount);
            Log.d("deposit_money", "Pre-filled amount: " + prefilledAmount);
        }

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.whatsapp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = constant.getWhatsapp(getApplicationContext());
                Uri uri = Uri.parse(url);
                Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(sendIntent);
            }
        });

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount.getText().toString().isEmpty() || amount.getText().toString().equals("0")) {
                    amount.setError("Enter valid amount");
                } else if (Integer.parseInt(amount.getText().toString()) < Integer.parseInt(getSharedPreferences(constant.prefs, Context.MODE_PRIVATE).getString("min_deposit", constant.min_deposit + ""))) {
                    amount.setError("amount must be more than " + getSharedPreferences(constant.prefs, Context.MODE_PRIVATE).getString("min_deposit", constant.min_deposit + ""));
                } else {
                    // New UPI Intent flow - no gateway selection needed
                    Log.d("deposit_money", "Initiating UPI intent payment");
                    initiateUpiPayment();
                }
            }
        });
    }


    /**
     * Launch UPI payment using the upi:// URL from backend
     */
    void launchUpiIntent(String upiUrl) {
        try {
            Uri uri = Uri.parse(upiUrl);
            Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
            upiPayIntent.setData(uri);

            // Let user choose their preferred UPI app
            Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

            startActivityForResult(chooser, UPI_PAYMENT);
        } catch (Exception e) {
            Log.e("deposit_money", "Error launching UPI intent: " + e.getMessage());
            Toast.makeText(deposit_money.this, "Error launching payment app", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPI_PAYMENT) {
            Log.d("UPI", "onActivityResult - requestCode: " + requestCode + ", resultCode: " + resultCode);

            if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                if (data != null) {
                    String response = data.getStringExtra("response");
                    Log.d("UPI", "UPI Response: " + response);
                    handleUpiResponse(response);
                } else {
                    Log.d("UPI", "Return data is null");
                    showPaymentCancelled();
                }
            } else {
                Log.d("UPI", "Payment cancelled by user");
                showPaymentCancelled();
            }
        }
    }

    /**
     * Handle UPI response and extract UTR to submit to backend
     */
    private void handleUpiResponse(String response) {
        if (!isConnectionAvailable(deposit_money.this)) {
            Toast.makeText(deposit_money.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
            return;
        }

        if (response == null || response.isEmpty()) {
            showPaymentCancelled();
            return;
        }

        Log.d("UPI", "Parsing UPI response: " + response);

        String status = "";
        String utr = "";  // ApprovalRefNo is the UTR

        // Parse response: "Status=SUCCESS&txnId=xxx&ApprovalRefNo=214578963210&txnRef=TXN12345"
        String[] parts = response.split("&");
        for (String part : parts) {
            String[] keyValue = part.split("=");
            if (keyValue.length >= 2) {
                String key = keyValue[0].toLowerCase();
                String value = keyValue[1];

                if (key.equals("status")) {
                    status = value.toLowerCase();
                } else if (key.equals("approvalrefno")) {
                    utr = value;  // This is the UTR!
                }
            }
        }

        Log.d("UPI", "Status: " + status + ", UTR: " + utr);

        if (status.equals("success") && !utr.isEmpty()) {
            // Automatically submit UTR to backend
            submitUtrToBackend(utr);
        } else if (status.equals("failure") || status.equals("failed")) {
            Toast.makeText(deposit_money.this, "Payment failed. Please try again", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(deposit_money.this, "Payment status unclear. Please contact support if amount was deducted", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPaymentCancelled() {
        Toast.makeText(deposit_money.this, "Payment cancelled by user", Toast.LENGTH_SHORT).show();
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



    /**
     * Step 1: Call /initiate_upi_intent to create transaction and get UPI URL
     */
    private void initiateUpiPayment() {
        progressDialog = new ViewDialog(deposit_money.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, urlInitiateUpi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("UPI", "initiate_upi_intent response: " + response);
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("success").equals("1")) {
                                transactionId = jsonObject.getString("transaction_id");
                                String upiUrl = jsonObject.getString("upi_url");

                                Log.d("UPI", "Transaction ID: " + transactionId);
                                Log.d("UPI", "UPI URL: " + upiUrl);

                                // Launch UPI app with the payment URL
                                launchUpiIntent(upiUrl);
                            } else {
                                Toast.makeText(deposit_money.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.hideDialog();
                            Toast.makeText(deposit_money.this, "Error processing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressDialog.hideDialog();
                        Toast.makeText(deposit_money.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile", null));
                params.put("session", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));
                params.put("amount", amount.getText().toString());
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    /**
     * Step 2: Submit UTR to backend after successful UPI payment
     */
    private void submitUtrToBackend(String utr) {
        progressDialog = new ViewDialog(deposit_money.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, urlSubmitUtr,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("UPI", "submit_upi_reference response: " + response);
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("success").equals("1")) {
                                // Show success message and inform user about verification
                                new AlertDialog.Builder(deposit_money.this)
                                        .setTitle("Payment Reference Submitted")
                                        .setMessage("Your payment reference has been submitted successfully. Your wallet balance will be updated after admin verification (usually within a few minutes).")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivity(new Intent(getApplicationContext(), HomeScreen.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                                finish();
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                            } else {
                                Toast.makeText(deposit_money.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.hideDialog();
                            Toast.makeText(deposit_money.this, "Error processing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressDialog.hideDialog();
                        Toast.makeText(deposit_money.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile", null));
                params.put("session", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));
                params.put("transaction_id", transactionId);
                params.put("utr", utr);
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }
}