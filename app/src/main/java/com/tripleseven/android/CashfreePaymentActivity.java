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
import com.cashfree.pg.CFPaymentService;
import com.cashfree.pg.api.CFPaymentGatewayService;
import com.cashfree.pg.core.api.CFSession;
import com.cashfree.pg.core.api.CFTheme;
import com.cashfree.pg.core.api.callback.CFCheckoutResponseCallback;
import com.cashfree.pg.core.api.exception.CFException;
import com.cashfree.pg.core.api.utils.CFErrorResponse;
import com.cashfree.pg.ui.api.CFPaymentComponent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CashfreePaymentActivity extends AppCompatActivity implements CFCheckoutResponseCallback {

    private static final String TAG = "CashfreePayment";
    private String amount;
    private String transactionId;
    private CFPaymentComponent cfPaymentComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "CashfreePaymentActivity onCreate");

        // Initialize Cashfree Payment component
        try {
            cfPaymentComponent = new CFPaymentComponent(this, this);
        } catch (CFException e) {
            Log.e(TAG, "Failed to initialize Cashfree component: " + e.getMessage());
            Toast.makeText(this, "Payment initialization failed", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // ✅ FIX: Restore state after process death (rotation, low memory, etc.)
        if (savedInstanceState != null) {
            transactionId = savedInstanceState.getString("transaction_id");
            amount = savedInstanceState.getString("amount");
            Log.d(TAG, "Restored from savedInstanceState - transactionId: " + transactionId);
        }

        // Check if order details are already provided
        boolean skipBackendCall = getIntent().getBooleanExtra("skip_backend_call", false);

        if (skipBackendCall) {
            Log.d(TAG, "Order already created - using provided details");
            String orderId = getIntent().getStringExtra("order_id");
            String paymentSessionId = getIntent().getStringExtra("payment_session_id");
            if (transactionId == null) {  // Only get from intent if not restored
                transactionId = getIntent().getStringExtra("transaction_id");
            }

            if (orderId != null && paymentSessionId != null) {
                startCashfreeCheckout(orderId, paymentSessionId);
            } else {
                Log.e(TAG, "Invalid order details provided");
                Toast.makeText(this, "Invalid payment details", Toast.LENGTH_SHORT).show();
                finish();
            }
            return;
        }

        // Normal flow: Get amount from intent and create order
        if (amount == null) {  // Only get from intent if not restored
            amount = getIntent().getStringExtra("amount");
        }

        Log.d(TAG, "Amount from intent: " + amount);

        if (amount == null || amount.isEmpty()) {
            Log.e(TAG, "Amount is null or empty");
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize Cashfree payment
        Log.d(TAG, "Calling initiateCashfreePayment");
        initiateCashfreePayment();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // ✅ FIX: Save critical state for process death recovery
        if (transactionId != null) {
            outState.putString("transaction_id", transactionId);
        }
        if (amount != null) {
            outState.putString("amount", amount);
        }
        Log.d(TAG, "Saved state - transactionId: " + transactionId);
    }

    @Override
    public void onBackPressed() {
        // ✅ FIX: Handle back button as user cancellation
        Log.d(TAG, "User pressed back button - treating as payment cancellation");

        Intent resultIntent = new Intent();
        resultIntent.putExtra("payment_status", "cancelled");
        if (transactionId != null) {
            resultIntent.putExtra("transaction_id", transactionId);
        }
        setResult(Activity.RESULT_CANCELED, resultIntent);

        super.onBackPressed();
    }

    private void initiateCashfreePayment() {
        // Call backend to create Cashfree order
        String url = constant.prefix + "initiate_gw_payment";

        Log.d(TAG, "Initiating payment - URL: " + url);
        Log.d(TAG, "Amount: " + amount);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String success = jsonResponse.optString("success", "0");

                            if (success.equals("1")) {
                                JSONObject data = jsonResponse.getJSONObject("data");
                                String orderId = data.getString("order_id");
                                String paymentSessionId = data.getString("payment_session_id");
                                transactionId = data.getString("transaction_id");

                                Log.d(TAG, "Order created - Order ID: " + orderId);
                                Log.d(TAG, "Payment Session ID: " + paymentSessionId);
                                Log.d(TAG, "Transaction ID: " + transactionId);

                                startCashfreeCheckout(orderId, paymentSessionId);
                            } else {
                                String msg = jsonResponse.optString("msg", "Failed to create order");
                                Log.e(TAG, "Order creation failed: " + msg);
                                Toast.makeText(CashfreePaymentActivity.this, msg, Toast.LENGTH_LONG).show();
                                finish();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON parsing error: " + e.getMessage());
                            e.printStackTrace();
                            Toast.makeText(CashfreePaymentActivity.this, "Payment initialization failed", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Volley error: " + error.toString());
                Toast.makeText(CashfreePaymentActivity.this, "Network error. Please try again.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("amount", amount);

                params.put("mobile", wallet.getMobile());
                params.put("session", wallet.getSession());
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private void startCashfreeCheckout(String orderId, String paymentSessionId) {
        Log.d(TAG, "Starting Cashfree checkout");
        Log.d(TAG, "Order ID: " + orderId);
        Log.d(TAG, "Payment Session ID: " + paymentSessionId);

        try {
            // Create Cashfree session
            CFSession.Environment environment = CFSession.Environment.PRODUCTION;

            CFSession cfSession = new CFSession.CFSessionBuilder()
                    .setEnvironment(environment)
                    .setPaymentSessionID(paymentSessionId)
                    .setOrderId(orderId)
                    .build();

            // Create theme (optional - customize colors)
            CFTheme cfTheme = new CFTheme.CFThemeBuilder()
                    .setNavigationBarBackgroundColor("#FF6200EE")
                    .setNavigationBarTextColor("#FFFFFFFF")
                    .setButtonBackgroundColor("#FF6200EE")
                    .setButtonTextColor("#FFFFFFFF")
                    .setPrimaryTextColor("#000000")
                    .setSecondaryTextColor("#666666")
                    .build();

            // Start payment
            cfPaymentComponent.doPayment(cfSession, cfTheme);

        } catch (CFException e) {
            Log.e(TAG, "Cashfree session creation failed: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "Payment failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onPaymentVerify(String orderID) {
        // Payment verification callback - payment was successful
        Log.d(TAG, "Payment verify callback - Order ID: " + orderID);

        // Return to wallet activity with success
        Intent resultIntent = new Intent();
        resultIntent.putExtra("payment_status", "success");
        resultIntent.putExtra("order_id", orderID);
        resultIntent.putExtra("transaction_id", transactionId);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onPaymentFailure(CFErrorResponse cfErrorResponse, String orderID) {
        // Payment failure callback
        Log.e(TAG, "Payment failed - Order ID: " + orderID);
        Log.e(TAG, "Error: " + cfErrorResponse.getMessage());

        Toast.makeText(this, "Payment failed: " + cfErrorResponse.getMessage(), Toast.LENGTH_LONG).show();

        // Return to wallet activity with failure
        Intent resultIntent = new Intent();
        resultIntent.putExtra("payment_status", "failed");
        resultIntent.putExtra("order_id", orderID);
        resultIntent.putExtra("error", cfErrorResponse.getMessage());
        setResult(Activity.RESULT_CANCELED, resultIntent);
        finish();
    }
}
