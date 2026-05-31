package com.tripleseven.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RazorpayPaymentActivity extends AppCompatActivity implements PaymentResultListener {

    private static final String TAG = "RazorpayPayment";
    private String amount;
    private String transactionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get amount from intent
        amount = getIntent().getStringExtra("amount");

        if (amount == null || amount.isEmpty()) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize Razorpay payment
        initiateRazorpayPayment();
    }

    private void initiateRazorpayPayment() {
        // Call backend to create Razorpay order
        String url = constant.prefix + "initiate_gw_payment";

        RequestQueue queue = Volley.newRequestQueue(this);
        MyStringRequest stringRequest = new MyStringRequest(
                getSharedPreferences(constant.prefs, MODE_PRIVATE),
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            if (jsonResponse.has("order_id")) {
                                String orderId = jsonResponse.getString("order_id");
                                String keyId = jsonResponse.getString("key_id");
                                int amountPaise = jsonResponse.getInt("amount");
                                String currency = jsonResponse.getString("currency");
                                transactionId = jsonResponse.getString("transaction_id");

                                // Start Razorpay checkout
                                startRazorpayCheckout(orderId, keyId, amountPaise, currency);
                            } else {
                                Toast.makeText(RazorpayPaymentActivity.this,
                                    "Failed to create order", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON parsing error: " + e.getMessage());
                            Toast.makeText(RazorpayPaymentActivity.this,
                                "Error processing response", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "API call error: " + error.toString());
                        Toast.makeText(RazorpayPaymentActivity.this,
                            "Network error. Please try again", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("amount", amount);
                params.put("mobile", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile", null));
                params.put("session", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private void startRazorpayCheckout(String orderId, String keyId, int amount, String currency) {
        Checkout checkout = new Checkout();
        checkout.setKeyID(keyId);

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Samrat 777");
            options.put("description", "Add Money to Wallet");
            options.put("order_id", orderId);
            options.put("currency", currency);
            options.put("amount", amount);

            // Prefill user details if available
            JSONObject prefill = new JSONObject();
            String mobile = getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile", null);
            if (mobile != null && !mobile.isEmpty()) {
                prefill.put("contact", mobile);
            }
            options.put("prefill", prefill);

            // Theme color
            JSONObject theme = new JSONObject();
            theme.put("color", "#667eea");
            options.put("theme", theme);

            checkout.open(this, options);

        } catch (JSONException e) {
            Log.e(TAG, "Error in starting Razorpay checkout: " + e.getMessage());
            Toast.makeText(this, "Error starting payment", Toast.LENGTH_SHORT).show();
            finish();
        }
    }



    @Override
    public void onPaymentSuccess(String razorpayPaymentId) {
        Log.d(TAG, "Payment successful: " + razorpayPaymentId);

        // Payment successful - navigate to success/home screen
        Toast.makeText(this, "Payment Successful!", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, HomeScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPaymentError(int code, String description) {
        Log.e(TAG, "Payment failed: " + code + " - " + description);

        Toast.makeText(this, "Payment failed: " + description, Toast.LENGTH_LONG).show();
        finish();
    }
}