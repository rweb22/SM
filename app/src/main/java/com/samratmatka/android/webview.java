package com.samratmatka.android;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;

import android.os.Looper;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import im.delight.android.webview.AdvancedWebView;

public class webview extends AppCompatActivity implements AdvancedWebView.Listener {

    AdvancedWebView mWebView;
    Context context;
    String url;
    ViewDialog progressDialog;

    private static final int TIMEOUT_DELAY = 3 * 60 * 1000; // Timeout delay in milliseconds
    private Handler handler;
    private Runnable timeoutRunnable;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        context = this;
        url = getIntent().getStringExtra("url");

        mWebView = (AdvancedWebView) findViewById(R.id.webview);
//        mWebView.setListener(this, this);
//        mWebView.setMixedContentAllowed(false);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.addJavascriptInterface(new WebviewInterface(), "Interface");

        mWebView.loadUrl(url);
    }


    public class WebviewInterface {
        @JavascriptInterface
        public void paymentResponse(String client_txn_id, String txn_id) {
            verifyUpiGwPayment(client_txn_id, txn_id);
            finish();
        }

        @JavascriptInterface
        public void errorResponse() {
            // this function is called when Transaction in Already Done or Any other Issue.
            Toast.makeText(context, "Transaction Error.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void verifyUpiGwPayment(String clientTxnId, String txnId) {
        String url3 = constant.prefix2 + "check_upi_gw_txn";

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url3,
                response1 -> {
                    try {
                        JSONObject jsonObject1 = new JSONObject(response1);
                        if (jsonObject1.getString("success").equals("1")) {
                            String status = jsonObject1.getString("msg");
                            //String balance = jsonObject1.getString("total_balance");
                            Toast.makeText(webview.this, status, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(webview.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(webview.this, "Unable to fetch the transaction status. Please contact support", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    //progressDialog.hideDialog();
                    Toast.makeText(webview.this, "Unable to fetch the transaction status. Please contact support", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile", null));
                params.put("session", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));
                params.put("client_txn_id", clientTxnId);
                params.put("txn_id", txnId);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
    }

    @Override
    public void onPageFinished(String url) {
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }
}