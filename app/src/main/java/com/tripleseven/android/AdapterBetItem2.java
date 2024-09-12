package com.tripleseven.android;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class AdapterBetItem2 extends RecyclerView.Adapter<AdapterBetItem2.ViewHolder> {

    Context context;

    ArrayList<String> fillNumber = new ArrayList<>();
    ArrayList<String> fillAmount = new ArrayList<>();
    ArrayList<String> digits = new ArrayList<>();
    private final ArrayList<String> amountList = new ArrayList<>();

    public AdapterBetItem2(Context context, ArrayList<String> digits) {
        this.context = context;
        this.digits = digits;
    }

    public AdapterBetItem2(Context context, ArrayList<String> digits, ArrayList<String> fillNumber, ArrayList<String> fillAmount) {
        this.context = context;
        this.digits = digits;
        this.fillAmount = fillAmount;
        this.fillNumber = fillNumber;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.betlayout, parent, false);
        return new ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        amountList.add("0");
        holder.amount.setText("");

        if (fillNumber != null) {
            for (int a = 0; a < fillNumber.size(); a++) {
                if (digits.get(position).equals(fillNumber.get(a))) {
                    holder.amount.append(fillAmount.get(a));
                }
            }
        }

        holder.number.setText(digits.get(position));

        holder.amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.toString().isEmpty()) {
                    amountList.set(position, "0");
                } else if (Integer.parseInt(s.toString()) > 10000) {
                    holder.amount.setText("10000");
                    amountList.set(position, 10000+"");
                } else {
                    amountList.set(position, s.toString());
                }

                BroadcastReceiver mReceiver = new BroadcastReceiver() { @Override public void onReceive(Context context, Intent intent) { }};

                IntentFilter intentFilter = new IntentFilter("android.intent.action.MAIN");
                context.registerReceiver(mReceiver, intentFilter);

                Intent i = new Intent("android.intent.action.MAIN");
                i.putExtra("type","panna");
                i.putExtra("amount",holder.amount.getText().toString());
                i.putExtra("number",holder.number.getText().toString());
                context.sendBroadcast(i);

                context.unregisterReceiver(mReceiver);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.setIsRecyclable(false);
    }



    public ArrayList<String> getAmountList()
    {
        return amountList;
    }

    @Override
    public int getItemCount() {
        return digits.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView number;
        EditText amount;

        public ViewHolder(View view) {
            super(view);
            number = view.findViewById(R.id.number);
            amount = view.findViewById(R.id.amount2);
        }
    }
}
