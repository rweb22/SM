package com.tripleseven.android;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class adapterCardBetting extends RecyclerView.Adapter<adapterCardBetting.ViewHolder> {

    Context context;

    ArrayList<String> number = new ArrayList<>();
    ArrayList<Integer> img = new ArrayList<>();
    private ArrayList<String> list = new ArrayList<>();

    public adapterCardBetting(Context context,ArrayList<String> number, ArrayList<Integer> img) {
        this.context = context;
        this.number = number;
        this.img = img;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_bet_layout, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        list.add("0");

       // holder.number.setText(number.get(position));
//        holder.number.setBackgroundResource(img.get(position));
       holder.bet_img.setImageResource(img.get(position));

        holder.amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

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
        ImageView bet_img;

        public ViewHolder(View view) {
            super(view);
            bet_img = view.findViewById(R.id.bet_img);
            number = view.findViewById(R.id.number);
            amount = view.findViewById(R.id.amount2);


        }
    }



}
