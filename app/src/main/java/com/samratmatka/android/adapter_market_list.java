package com.samratmatka.android;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class adapter_market_list extends RecyclerView.Adapter<adapter_market_list.ViewHolder> {

    Context context;
    ArrayList<String> name = new ArrayList<>();


    public adapter_market_list(Context context, ArrayList<String> name) {
        this.context = context;
        this.name = name;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.market_list_layout, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.name.setText(name.get(position));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,Statistics.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("market",name.get(position)));
            }
        });

    }


    @Override
    public int getItemCount() {
        return name.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        RelativeLayout layout;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.ntitle);
            layout = view.findViewById(R.id.layoutj);


        }
    }



}
