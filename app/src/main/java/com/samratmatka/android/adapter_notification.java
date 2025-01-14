package com.samratmatka.android;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class adapter_notification extends RecyclerView.Adapter<adapter_notification.ViewHolder> {

    Context context;
    String market;
    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> msg = new ArrayList<>();
     ArrayList<String> date = new ArrayList<>();


    public adapter_notification(Context context,  ArrayList<String> title, ArrayList<String> msg, ArrayList<String> date) {
        this.context = context;

        this.title = title;
        this.msg = msg;
        this.date = date;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_layout, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        holder.name.setText(title.get(position));

        holder.time.setText(msg.get(position));
        holder.sinfo.setText(date.get(position));



        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return title.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,time, sinfo;
        RelativeLayout layout;

        public ViewHolder(View view) {
            super(view);
            layout = view.findViewById(R.id.layoutj);
            name = view.findViewById(R.id.ntitle);
            time = view.findViewById(R.id.nmsg);
            sinfo = view.findViewById(R.id.ndate);

        }
    }



}
