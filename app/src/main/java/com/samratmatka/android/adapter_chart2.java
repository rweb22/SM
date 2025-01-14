package com.samratmatka.android;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class adapter_chart2 extends RecyclerView.Adapter<adapter_chart2.ViewHolder> {

    Context context;
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> result = new ArrayList<>();
    ArrayList<String> type = new ArrayList<>();


    public adapter_chart2(Context context, ArrayList<String> name, ArrayList<String> result,ArrayList<String> type) {
        this.context = context;
        this.name = name;
        this.result = result;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chart_layout, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.name.setText(name.get(position));
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("type",type.get(position));

                //   context.startActivity(new Intent(context,charts.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("href",result.get(position)));
                context.startActivity(new Intent(context, Charts.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("href",constant.prefix+"charted/index2.php?market="+name.get(position)));
            }
        });

    }


    @Override
    public int getItemCount() {
        return result.size();
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
