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

class adapterbetting2 extends RecyclerView.Adapter<adapterbetting2.ViewHolder> {

    Context context;

    ArrayList<String> selectedNum = new ArrayList<>();
    ArrayList<String> amount = new ArrayList<>();
    ArrayList<String> number = new ArrayList<>();
    private ArrayList<String> list = new ArrayList<>();

    public adapterbetting2(Context context, ArrayList<String> number) {
        this.context = context;
        this.number = number;
    }

    public adapterbetting2(Context context, ArrayList<String> number, ArrayList<String> selectedNum, ArrayList<String> amount) {
        this.context = context;
        this.number = number;
        this.amount = amount;
        this.selectedNum = selectedNum;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.betlayout, parent, false);
        return new ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        list.add("0");
        holder.amount.setText("");
        if (selectedNum != null) {
            for (int a = 0; a < selectedNum.size(); a++) {
                if (number.get(position).equals(selectedNum.get(a))) {
                    holder.amount.append(amount.get(a));
                }
            }
        }

        holder.number.setText(number.get(position));

        holder.amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                if (s.toString().length() != 3){
//                    return;
//                }

                if (s.toString().isEmpty() || s == null) {
                    list.set(position,"0");
                } else if (Integer.parseInt(s.toString()) > 10000) {
                    holder.amount.setText("10000");
                    list.set(position,10000+"");
                } else {
                    list.set(position, s.toString());
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



    public ArrayList<String> getNumber()
    {
        return list;
    }

    @Override
    public int getItemCount() {
        return number.size();
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
