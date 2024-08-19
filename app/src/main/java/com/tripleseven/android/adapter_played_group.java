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

class adapter_played_group extends RecyclerView.Adapter<adapter_played_group.ViewHolder> {

    Context context;
    ArrayList<String> date = new ArrayList<>();
    ArrayList<ArrayList<playedModel>> models = new ArrayList<>();


    public adapter_played_group(Context context, ArrayList<String> date, ArrayList<ArrayList<playedModel>> models) {
        this.context = context;
        this.date = date;
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

        holder.name.setText(date.get(position));

        adapterplayed rc = new adapterplayed(holder.itemView.getContext(), models.get(position));
        holder.recycler.setLayoutManager(new GridLayoutManager(holder.itemView.getContext(), 1));
        holder.recycler.setAdapter(rc);



    }


    @Override
    public int getItemCount() {
        return date.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        RecyclerView recycler;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.date2);
            recycler = view.findViewById(R.id.recycler);


        }
    }



}
