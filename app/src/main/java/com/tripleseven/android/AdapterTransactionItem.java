package com.tripleseven.android;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

class AdapterTransactionItem extends RecyclerView.Adapter<AdapterTransactionItem.ViewHolder> {
    Context context;
    ArrayList<String> date = new ArrayList<>();
    ArrayList<String> remark = new ArrayList<>();
    ArrayList<String> amount = new ArrayList<>();
    ArrayList<String> type = new ArrayList<>();
    ArrayList<String> status = new ArrayList<>();

    public AdapterTransactionItem(Context context,
                                  ArrayList<String> date,
                                  ArrayList<String> remark,
                                  ArrayList<String> amount,
                                  ArrayList<String> type,
                                  ArrayList<String> status) {
        this.context = context;
        this.date = date;
        this.remark = remark;
        this.amount = amount;
        this.type = type;
        this.status = status;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.date.setText(date.get(position));
        holder.remark.setText(remark.get(position));
        holder.amount.setText(String.format("₹ %s", amount.get(position)));

        String tStatus = status.get(position);
        switch (type.get(position)) {
            case "0":
                holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.wallet_out_img));
                holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.white));
                holder.date.setTextColor(context.getResources().getColor(R.color.red));
                holder.remark.setTextColor(context.getResources().getColor(R.color.red));
                holder.amount.setTextColor(context.getResources().getColor(R.color.red));

                if (Objects.equals(tStatus, "2")) {
                    holder.image.setColorFilter(holder.itemView.getContext().getResources().getColor(R.color.yellow));
                    holder.date.setTextColor(context.getResources().getColor(R.color.yellow));
                    holder.remark.setTextColor(context.getResources().getColor(R.color.yellow));
                    holder.amount.setTextColor(context.getResources().getColor(R.color.yellow));
                } else if (Objects.equals(tStatus, "4")) {
                    holder.image.setColorFilter(holder.itemView.getContext().getResources().getColor(R.color.dark_red));
                    holder.date.setTextColor(context.getResources().getColor(R.color.dark_red));
                    holder.remark.setTextColor(context.getResources().getColor(R.color.dark_red));
                    holder.amount.setTextColor(context.getResources().getColor(R.color.dark_red));
                } else {
                    holder.image.setColorFilter(holder.itemView.getContext().getResources().getColor(R.color.red));
                    holder.date.setTextColor(context.getResources().getColor(R.color.red));
                    holder.remark.setTextColor(context.getResources().getColor(R.color.red));
                    holder.amount.setTextColor(context.getResources().getColor(R.color.red));
                }
                break;

            case "1":
                holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.wallet_in));
                holder.image.setColorFilter(holder.itemView.getContext().getResources().getColor(R.color.green));
                holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.white));
                holder.date.setTextColor(context.getResources().getColor(R.color.green));
                holder.remark.setTextColor(context.getResources().getColor(R.color.green));
                holder.amount.setTextColor(context.getResources().getColor(R.color.green));

                if (Objects.equals(tStatus, "2")) {
                    holder.image.setColorFilter(holder.itemView.getContext().getResources().getColor(R.color.yellow));
                    holder.date.setTextColor(context.getResources().getColor(R.color.yellow));
                    holder.remark.setTextColor(context.getResources().getColor(R.color.yellow));
                    holder.amount.setTextColor(context.getResources().getColor(R.color.yellow));
                } else if (Objects.equals(tStatus, "4")) {
                    holder.image.setColorFilter(holder.itemView.getContext().getResources().getColor(R.color.dark_red));
                    holder.date.setTextColor(context.getResources().getColor(R.color.dark_red));
                    holder.remark.setTextColor(context.getResources().getColor(R.color.dark_red));
                    holder.amount.setTextColor(context.getResources().getColor(R.color.dark_red));
                } else {
                    holder.image.setColorFilter(holder.itemView.getContext().getResources().getColor(R.color.green));
                    holder.date.setTextColor(context.getResources().getColor(R.color.green));
                    holder.remark.setTextColor(context.getResources().getColor(R.color.green));
                    holder.amount.setTextColor(context.getResources().getColor(R.color.green));
                }

                break;

            case "2":
                holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.wallet_out));
                holder.image.setColorFilter(holder.itemView.getContext().getResources().getColor(R.color.blue));
                holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.blue));
                holder.date.setTextColor(context.getResources().getColor(R.color.blue));
                holder.remark.setTextColor(context.getResources().getColor(R.color.blue));
                holder.amount.setTextColor(context.getResources().getColor(R.color.blue));
                break;

            case "3" :
            case "4":
                holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.wallet_in));
                holder.image.setColorFilter(holder.itemView.getContext().getResources().getColor(R.color.gold));
                holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.gold));
                holder.date.setTextColor(context.getResources().getColor(R.color.gold));
                holder.remark.setTextColor(context.getResources().getColor(R.color.gold));
                holder.amount.setTextColor(context.getResources().getColor(R.color.gold));
                break;

        }



    }

    @Override
    public int getItemCount() {
        return date.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView date,amount,remark;
        ImageView image;
        CardView card;

        public ViewHolder(View view) {
            super(view);

            date = view.findViewById(R.id.date2);
            amount = view.findViewById(R.id.amount2);
            remark = view.findViewById(R.id.remark2);
            image = view.findViewById(R.id.image);
            card = view.findViewById(R.id.card);
        }
    }



}
