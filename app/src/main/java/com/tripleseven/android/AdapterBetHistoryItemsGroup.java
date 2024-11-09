package com.tripleseven.android;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class AdapterBetHistoryItemsGroup extends RecyclerView.Adapter<AdapterBetHistoryItemsGroup.ViewHolder> {
    Context context;
    ArrayList<String> dates = new ArrayList<>();
    ArrayList<ArrayList<BetModel>> models = new ArrayList<>();


    public AdapterBetHistoryItemsGroup(Context context, ArrayList<String> dates, ArrayList<ArrayList<BetModel>> models) {
        this.context = context;
        this.dates = dates;
        this.models = models;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.played_group, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.name.setText(dates.get(position));

        AdapterBetHistoryItem rc = new AdapterBetHistoryItem(holder.itemView.getContext(), models.get(position));
        holder.recycler.setLayoutManager(new GridLayoutManager(holder.itemView.getContext(), 1));
        holder.recycler.setAdapter(rc);
    }


    @Override
    public int getItemCount() {
        return dates.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        RecyclerView recycler;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.date2);
            recycler = view.findViewById(R.id.recycler);
        }
    }
}
