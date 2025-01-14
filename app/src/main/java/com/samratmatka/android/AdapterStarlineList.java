package com.samratmatka.android;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class AdapterStarlineList extends RecyclerView.Adapter<AdapterStarlineList.ViewHolder> {

    Context context;
    ArrayList<String> open = new ArrayList<>();
    ArrayList<String> panna = new ArrayList<>();

    public AdapterStarlineList(Context context, ArrayList<String> open, ArrayList<String> panna) {
        this.context = context;
        this.open = open;
        this.panna = panna;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.starline_single_chart_item, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.openPanna.setText(panna.get(position));
        holder.open.setText(open.get(position));

    }

    @Override
    public int getItemCount() {
        return open.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {


        TextView openPanna;
        TextView open;

        public ViewHolder(View view) {
            super(view);

            this.openPanna = itemView.findViewById(R.id.panna);
            this.open = itemView.findViewById(R.id.open);
        }
    }


}
