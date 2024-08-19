package com.tripleseven.android;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

public class WalletFragment extends Fragment {

    private latobold totalBalance;
    private latobold addMoney;
    private latobold depositBalance;
    private ImageView depositInfo;
    private latobold winnigBalance;
    private ImageView winningInfo;
    private latobold cashBalance;
    private ImageView cashInfo;
    private CardView transactionHistory;
    private EditText amount;
    private latobold depositMoney;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);

        return view;
    }
}