package com.samratmatka.android;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class AdapterBetHistoryItem extends RecyclerView.Adapter<AdapterBetHistoryItem.ViewHolder> {

    Context context;

    ArrayList<BetModel> models = new ArrayList<>();

    public AdapterBetHistoryItem(Context context, ArrayList<BetModel> models) {
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

        BetModel BetModel = models.get(position);

        holder.market.setText(BetModel.getMarketName());
        holder.date.setText(BetModel.getDate());
        holder.bidType.setText(BetModel.getBid_type());
        holder.bidNumber.setText(BetModel.getNumber());
        holder.amount.setText(BetModel.getAmount());
        holder.status.setText(BetModel.getMsg());
        holder.id.setText(BetModel.getSn());

        if (BetModel.getStatus().equals("0")){
            holder.status.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.md_deep_orange_500));
        } else if (BetModel.getStatus().equals("1")){
            holder.status.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.green));
        } else {
            holder.status.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.red));
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
