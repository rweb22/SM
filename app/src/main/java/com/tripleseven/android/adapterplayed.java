package com.tripleseven.android;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class adapterplayed extends RecyclerView.Adapter<adapterplayed.ViewHolder> {

    Context context;

    ArrayList<playedModel> models = new ArrayList<>();

    public adapterplayed(Context context, ArrayList<playedModel> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.played, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        playedModel playedModel = models.get(position);

        holder.market.setText(playedModel.getMarketName());
        holder.date.setText(playedModel.getDate());
        holder.bidType.setText(playedModel.getBid_type());
        holder.bidNumber.setText(playedModel.getNumber());
        holder.amount.setText(playedModel.getAmount());
        holder.status.setText(playedModel.getMsg());
        holder.id.setText(playedModel.getSn());

        if (playedModel.getStatus().equals("0")){
            holder.status.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.md_deep_orange_500));
           // holder.img.setBackground(Glide.with(context).load();
        } else if (playedModel.getStatus().equals("1")){
            holder.status.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.green));
        } else {
            holder.status.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.red));
            //holder.img.setBackground(context.getResources().getDrawable(R.drawable.lose));
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        latobold market;
        latobold date;
        latobold bidType;
        latobold bidNumber;
        latobold amount;
        latobold status;
        latobold id;
        ImageView img;

        public ViewHolder(View view) {
            super(view);

            this.market = view.findViewById(R.id.remark2);
            this.date = view.findViewById(R.id.date2);
            this.bidType = view.findViewById(R.id.bid_type);
            this.bidNumber = view.findViewById(R.id.bid_number);
            this.amount = view.findViewById(R.id.amount2);
            this.status = view.findViewById(R.id.status);
            this.id = view.findViewById(R.id.id);
            this.img = view.findViewById(R.id.img);
        }
    }


}
