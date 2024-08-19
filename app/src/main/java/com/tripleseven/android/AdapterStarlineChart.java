package com.tripleseven.android;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class AdapterStarlineChart extends RecyclerView.Adapter<AdapterStarlineChart.ViewHolder> {

    Context context;
    ArrayList<String> date = new ArrayList<>();
    ArrayList<ArrayList<String>> open = new ArrayList<>();
    ArrayList<ArrayList<String>> close = new ArrayList<>();

    public AdapterStarlineChart(Context context, ArrayList<String> date, ArrayList<ArrayList<String>> open, ArrayList<ArrayList<String>> close) {
        this.context = context;
        this.date = date;
        this.open = open;
        this.close = close;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.starline_chart_item, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.date.setText(date.get(position));

        AdapterStarlineList rc = new AdapterStarlineList(context,open.get(position), close.get(position));
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(rc);
    }

    @Override
    public int getItemCount() {
        return date.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {


        TextView date;
        RecyclerView recyclerView;

        public ViewHolder(View view) {
            super(view);


            this.date = itemView.findViewById(R.id.date2);
            this.recyclerView = itemView.findViewById(R.id.recycler);
        }
    }


}
