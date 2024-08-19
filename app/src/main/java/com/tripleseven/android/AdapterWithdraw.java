package com.tripleseven.android;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterWithdraw extends RecyclerView.Adapter<AdapterWithdraw.ViewHolder> {

    final Context context;
    final ArrayList<WithdrawModel> user;


    public AdapterWithdraw(Context context, ArrayList<WithdrawModel> user) {
        this.context = context;
        this.user = user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.withdraw, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.account.setText(user.get(position).getAccount());
        holder.upi.setText(user.get(position).getUpi());
        holder.status.setText(user.get(position).getStatus());
        holder.amount.setText(user.get(position).getAmount());
        holder.date.setText(user.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return user.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView account;
        TextView upi;
        TextView status;
        TextView amount;
        TextView date;

        public ViewHolder(View view) {
            super(view);


            this.account = itemView.findViewById(R.id.account);
            this.upi = itemView.findViewById(R.id.upi);
            this.status = itemView.findViewById(R.id.status);
            this.amount = itemView.findViewById(R.id.amount2);
            this.date = itemView.findViewById(R.id.date2);
        }
    }


}
