package com.tripleseven.android;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Feed extends Fragment {

    RecyclerView recycler;


    ViewDialog progressDialog;
    String url = constant.prefix+"feed.php";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        recycler = view.findViewById(R.id.recycler);


        apicall();

        return view;
    }


    private void apicall() {

        progressDialog = new ViewDialog((AppCompatActivity) getActivity());
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        final StringRequest postRequest = new MyStringRequest(
                getActivity().getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hideDialog();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);

                            JSONArray jsonArray = jsonObject1.getJSONArray("data");

                            ArrayList<FeedModel> feedModels = new ArrayList<>();

                            for (int a = 0; a < jsonArray.length(); a++){

                                JSONObject jsonObject = jsonArray.getJSONObject(a);

                                String content;
                                if (jsonObject.getString("content").length() > 181) {
                                    content = jsonObject.getString("content").substring(0, 181) + " .... more";
                                } else {
                                    content = jsonObject.getString("content");
                                }

                                FeedModel feedModel = new FeedModel(jsonObject.getString("title"),content,jsonObject.getString("islike"),jsonObject.getString("type"),jsonObject.getString("file"),jsonObject.getString("sn"),jsonObject.getString("likes"), jsonObject.getString("time"), jsonObject.getString("full_content"));

                                feedModels.add(feedModel);
                            }

                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            AdapterFeed rc = new AdapterFeed(getActivity(), feedModels);
                            recycler.setLayoutManager(layoutManager);
                            recycler.setAdapter(rc);

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
                        Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("mobile", getActivity().getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile",""));


                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }
}