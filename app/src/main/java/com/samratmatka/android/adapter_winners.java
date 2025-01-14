package com.samratmatka.android;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class adapter_winners extends RecyclerView.Adapter<adapter_winners.ViewHolder> {

    Context context;

    ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> rank = new ArrayList<>();
    private ArrayList<String> amount = new ArrayList<>();
    private ArrayList<String> since = new ArrayList<>();

    public adapter_winners(Context context, ArrayList<String> name, ArrayList<String> rank, ArrayList<String> amount, ArrayList<String> since) {
        this.context = context;
        this.name = name;
        this.rank = rank;
        this.amount = amount;
        this.since = since;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.winner, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.name.setText(name.get(position));
        holder.rank.setText("#"+rank.get(position));
        holder.amount.setText(amount.get(position)+" ₹");
        holder.since.setText("Playing since "+since.get(position));

    }

    @Override
    public int getItemCount() {
        return since.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, rank, amount, since;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.ntitle);
            amount = view.findViewById(R.id.amount2);
            rank = view.findViewById(R.id.rank);
            since = view.findViewById(R.id.since);
        }
    }



}
