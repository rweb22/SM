package com.tripleseven.android;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class ContactFragment extends Fragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);


        view.findViewById(R.id.whatsapp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String url = constant.getWhatsapp(getActivity());
                    if (url != null && !url.isEmpty()) {
                        Uri uri = Uri.parse(url);
                        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(sendIntent);
                    } else {
                        Toast.makeText(getActivity(), "WhatsApp contact not available", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("ContactFragment", "Error opening WhatsApp", e);
                    Toast.makeText(getActivity(), "Unable to open WhatsApp", Toast.LENGTH_SHORT).show();
                }
            }
        });


        view.findViewById(R.id.telegram).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String telegramLink = getActivity().getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("telegram","");
                    if (telegramLink != null && !telegramLink.isEmpty()) {
                        Intent telegramIntent = new Intent(Intent.ACTION_VIEW);
                        telegramIntent.setData(Uri.parse(telegramLink));
                        startActivity(telegramIntent);
                    } else {
                        Toast.makeText(getActivity(), "Telegram contact not available", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("ContactFragment", "Error opening Telegram", e);
                    Toast.makeText(getActivity(), "Unable to open Telegram", Toast.LENGTH_SHORT).show();
                }
            }
        });

        view.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HomeScreen.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                getActivity().finish();
            }
        });

        return view;
    }

}