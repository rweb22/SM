package com.tripleseven.android;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class adapter_stats extends RecyclerView.Adapter<adapter_stats.ViewHolder> {

    Context context;
    ArrayList<String> number = new ArrayList<>();
    ArrayList<String> percent = new ArrayList<>();


    public adapter_stats(Context context, ArrayList<String> number, ArrayList<String> percent) {
        this.context = context;
        this.number = number;
        this.percent = percent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stats_layout, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.number.setText(number.get(position));
        holder.percent.setText(percent.get(position)+"%");

    }


    @Override
    public int getItemCount() {
        return percent.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView number,percent;
        RelativeLayout layout;

        public ViewHolder(View view) {
            super(view);
            number = view.findViewById(R.id.number);
            percent = view.findViewById(R.id.percent);


        }
    }



}
