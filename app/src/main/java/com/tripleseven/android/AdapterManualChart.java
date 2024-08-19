package com.tripleseven.android;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class AdapterManualChart extends RecyclerView.Adapter<AdapterManualChart.ViewHolder> {

    Context context;
    ArrayList<String> date = new ArrayList<>();
    ArrayList<String> open = new ArrayList<>();
    ArrayList<String> close = new ArrayList<>();
    ArrayList<String> jodi = new ArrayList<>();

    public AdapterManualChart(Context context, ArrayList<String> date, ArrayList<String> open, ArrayList<String> close,ArrayList<String> jodi) {
        this.context = context;
        this.date = date;
        this.open = open;
        this.close = close;
        this.jodi = jodi;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.manual_chart_item, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.date.setText(date.get(position));
        holder.openPanna.setText(open.get(position));
        holder.open.setText(String.valueOf(jodi.get(position).charAt(0)));
        holder.closePanna.setText(close.get(position));
        holder.close.setText(String.valueOf(jodi.get(position).charAt(1)));
        holder.jodi.setText(jodi.get(position));

    }

    @Override
    public int getItemCount() {
        return date.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {


        latonormal date;
        latonormal openPanna;
        latonormal open;
        latonormal closePanna;
        latonormal close;
        latonormal jodi;

        public ViewHolder(View view) {
            super(view);


            this.date = itemView.findViewById(R.id.date2);
            this.openPanna = itemView.findViewById(R.id.open_panna);
            this.open = itemView.findViewById(R.id.open);
            this.closePanna = itemView.findViewById(R.id.close_panna);
            this.close = itemView.findViewById(R.id.close);
            this.jodi = itemView.findViewById(R.id.jodi);
        }
    }


}
