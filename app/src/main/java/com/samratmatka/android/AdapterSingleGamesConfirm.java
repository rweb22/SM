package com.samratmatka.android;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class AdapterSingleGamesConfirm extends RecyclerView.Adapter<AdapterSingleGamesConfirm.ViewHolder> {

    Context context;
    ArrayList<String> digit = new ArrayList<>();
    ArrayList<String> amount = new ArrayList<>();
    String game;

    public AdapterSingleGamesConfirm(Context context, ArrayList<String> digit, ArrayList<String> amount, String game) {
        this.context = context;
        this.digit = digit;
        this.amount = amount;
        this.game = game;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_bets_confirm, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.date.setText(digit.get(position));
        holder.remark.setText(game);
        holder.amount.setText(amount.get(position));


    }

    @Override
    public int getItemCount() {
        return digit.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView date,amount,remark;

        public ViewHolder(View view) {
            super(view);

            date = view.findViewById(R.id.date2);
            amount = view.findViewById(R.id.amount2);
            remark = view.findViewById(R.id.remark);
        }
    }



}
