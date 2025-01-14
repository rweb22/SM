package com.samratmatka.android;


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

class AdapterChartItem extends RecyclerView.Adapter<AdapterChartItem.ViewHolder> {

    Context context;
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> result = new ArrayList<>();
    ArrayList<String> type = new ArrayList<>();


    public AdapterChartItem(Context context, ArrayList<String> name, ArrayList<String> result, ArrayList<String> type) {
        this.context = context;
        this.name = name;
        this.result = result;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chart_layout, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.name.setText(name.get(position));
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Charts.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("href",constant.prefix + "get_charts?market_id=" + result.get(position)));
            }
        });

    }


    @Override
    public int getItemCount() {
        return result.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        RelativeLayout layout;



        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.ntitle);
            layout = view.findViewById(R.id.layoutj);
        }
    }
}
