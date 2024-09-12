package com.tripleseven.android;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class adapter_jackpot_timings extends RecyclerView.Adapter<adapter_jackpot_timings.ViewHolder> {

    Context context;
    String market;
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> is_open = new ArrayList<>();
    private ArrayList<String> time = new ArrayList<>();
    private ArrayList<String> is_jodi = new ArrayList<>();
    ArrayList<String> number = new ArrayList<>();

    public adapter_jackpot_timings(Context context, String market, ArrayList<String> name, ArrayList<String> is_open, ArrayList<String> time,ArrayList<String> is_jodi) {
        this.context = context;
        this.market = market;
        this.name = name;
        this.is_open = is_open;
        this.time = time;
        this.is_jodi = is_jodi;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.jackpot_timing_layout, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Animation animation = AnimationUtils.loadAnimation(context,R.anim.bouncee);




        holder.name.startAnimation(animation);


        holder.name.setText(time.get(position));

        holder.time.setText(name.get(position));

        if (is_open.get(position).equals("1")) {
            holder.sinfo.setTextColor(context.getResources().getColor(R.color.green));
            holder.sinfo.setText("Betting is Running Now");
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.single();
                    context.startActivity(new Intent(context, BettingScreen.class)
                            .putExtra("market", market).putExtra("digits", number)
                            .putExtra("game", "single")
                            .putExtra("timin",time.get(position))
                            .putExtra("type","jackpot")
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });

        } else {
            holder.sinfo.setTextColor(context.getResources().getColor(R.color.red));
            holder.sinfo.setText("Betting is Closed Now");
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Market is already closed", Toast.LENGTH_SHORT).show();
                }
            });

            // holder.layout.setBackgroundColor(context.getResources().getColor(R.color.md_red_600));
        }

        holder.setIsRecyclable(false);
    }


    @Override
    public int getItemCount() {
        return time.size();
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

        public void single() {
            number.clear();
            number.add("0");
            number.add("1");
            number.add("2");
            number.add("3");
            number.add("4");
            number.add("5");

        }
    }



}
