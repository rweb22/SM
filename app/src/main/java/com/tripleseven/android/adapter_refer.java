package com.tripleseven.android;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class adapter_refer extends RecyclerView.Adapter<adapter_refer.ViewHolder> {

    Context context;

    ArrayList<String> mobile = new ArrayList<>();
    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> date = new ArrayList<>();

    public adapter_refer(Context context, ArrayList<String> mobile, ArrayList<String> name, ArrayList<String> date) {
        this.context = context;
        this.name = name;
        this.date = date;
        this.mobile = mobile;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.refer_user, parent,
        false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.name.setText(name.get(position));
        holder.mobile.setText(mobile.get(position));
        holder.amount.setText(date.get(position));

    }

    @Override
    public int getItemCount() {
        return mobile.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        latobold name;
        latobold mobile;
        latobold amount;

        public ViewHolder(View view) {
            super(view);


            this.name = itemView.findViewById(R.id.ntitle);
            this.mobile = itemView.findViewById(R.id.create_pass);
            this.amount = itemView.findViewById(R.id.amount2);
        }
    }


}
