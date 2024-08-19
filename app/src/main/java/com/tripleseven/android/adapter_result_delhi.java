package com.tripleseven.android;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

class adapter_result_delhi extends RecyclerView.Adapter<adapter_result_delhi.ViewHolder> {



    Context context;
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> result = new ArrayList<>();
    ArrayList<String> result3 = new ArrayList<>();
    ArrayList<String> result_time = new ArrayList<>();
    ArrayList<String> mOpen = new ArrayList<>();
    ArrayList<String> mClose = new ArrayList<>();

    ArrayList<String> is_open = new ArrayList<>();
    ArrayList<String> open_time = new ArrayList<>();
    ArrayList<String> close_time = new ArrayList<>();
    private ArrayList<String> open_av = new ArrayList<>();
    private ArrayList<String> market_type = new ArrayList<>();

    public adapter_result_delhi(Context context, ArrayList<String> name, ArrayList<String> result,ArrayList<String> result3, ArrayList<String> is_open, ArrayList<String> open_time, ArrayList<String> close_time, ArrayList<String> open_av, ArrayList<String> market_type, ArrayList<String> result_time,ArrayList<String> mOpen,ArrayList<String> mClose) {
        this.context = context;
        this.result_time =result_time;
        this.mOpen = mOpen;
        this.mClose = mClose;
        this.name = name;
        this.result = result;
        this.result3 = result3;
        this.is_open = is_open;
        this.open_time = open_time;
        this.close_time = close_time;
        this.open_av = open_av;
        this.market_type = market_type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.delhi_result_layout, parent, false);
        return new ViewHolder(v);

    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {




        holder.name.setText(name.get(position));
        holder.result.setText(result.get(position));
        holder.result2.setText(result3.get(position));

        holder.info.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.info.setMarqueeRepeatLimit(-1);
        holder.info.setSingleLine(true);
        holder.info.setSelected(true);

        holder.chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, charts.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("href",constant.prefix+"get_charts?market="+name.get(position)));
            }
        });

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        System.out.println(hour);


        holder.top.setText(result_time.get(position));
        holder.closeTime.setText(close_time.get(position));

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                LayoutInflater factory = LayoutInflater.from(context);
                View market_time = factory.inflate(R.layout.market_time, null);
                TextView close = market_time.findViewById(R.id.close);
                TextView open_time_view = market_time.findViewById(R.id.closeTime);
                TextView close_time_view = market_time.findViewById(R.id.close_time);
                builder1.setView(market_time);
                builder1.setCancelable(true);
                AlertDialog alert11 = builder1.create();
                alert11.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                open_time_view.setText(open_time.get(position));
                close_time_view.setText(close_time.get(position));

                alert11.show();

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert11.dismiss();
                    }
                });

            }
        });

        if (is_open.get(position).equals("1")) {

            holder.info.setText("Betting is Running Now");
            holder.info.setTextColor(context.getResources().getColor(R.color.md_green_800));
          //  holder.play_text.setText("PLAY GAME");
          //  holder.inactive.setVisibility(View.GONE);
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context,games.class)
                            .putExtra("market",name.get(position))
                            .putExtra("is_open",is_open.get(position))
                            .putExtra("is_close",open_av.get(position))
                            .putExtra("market_type",market_type.get(position))
                    );
                }
            });
        } else {
            holder.info.setText("Betting closed for today");
           // holder.play_text.setText("GAME OVER");
          //  holder.inactive.setVisibility(View.VISIBLE);
            holder.info.setTextColor(context.getResources().getColor(R.color.md_red_600));

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new androidx.appcompat.app.AlertDialog.Builder(context)
                            .setTitle("Market Close")
                            .setMessage("Betting is already closed for this market")
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                }
            });
        }


        if (name.get(position).equals("DESAWER")){

            holder.closeTime.setText("2:00 am");

            if (hour >= 2 && hour <7){

                holder.info.setText("Betting closed for today");
            //    holder.play_text.setText("GAME OVER");
            //    holder.inactive.setVisibility(View.VISIBLE);
                holder.info.setTextColor(context.getResources().getColor(R.color.md_red_600));


                holder.play_game.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new androidx.appcompat.app.AlertDialog.Builder(context)
                                .setTitle("Market Close")
                                .setMessage("Betting is already closed for this market")
                                .setNegativeButton(android.R.string.no, null)
                                .show();
                    }
                });

                holder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new androidx.appcompat.app.AlertDialog.Builder(context)
                                .setTitle("Market Close")
                                .setMessage("Betting is already closed for this market")
                                .setNegativeButton(android.R.string.no, null)
                                .show();
                    }
                });
            }else {

                holder.info.setText("Betting is Running Now");
                holder.info.setTextColor(context.getResources().getColor(R.color.md_green_800));
              //  holder.play_text.setText("PLAY GAME");
              //  holder.inactive.setVisibility(View.GONE);
                holder.play_game.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context,games.class)
                                .putExtra("market",name.get(position))
                                .putExtra("is_open",is_open.get(position))
                                .putExtra("is_close",open_av.get(position))
                                .putExtra("market_type",market_type.get(position))
                        );
                    }
                });

                holder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context,games.class)
                                .putExtra("market",name.get(position))
                                .putExtra("is_open",is_open.get(position))
                                .putExtra("is_close",open_av.get(position))
                                .putExtra("market_type",market_type.get(position))
                        );
                    }
                });
              //  holder.play_game.setEnabled(false);

            }

        }

    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView name,result,result2, info,top,closeTime, play_text;
        ImageView gif;
        LinearLayout play_game, chart;
        CardView layout;
        View inactive;

        public ViewHolder(View view) {
            super(view);
            ImageView gif;


           chart = view.findViewById(R.id.chart);
            result2 = view.findViewById(R.id.result2);
            closeTime = view.findViewById(R.id.closeTime);
            name = view.findViewById(R.id.ntitle);
            result = view.findViewById(R.id.result);
            info = view.findViewById(R.id.info);
           // time_info = view.findViewById(R.id.time_info);
            play_game = view.findViewById(R.id.play_game);
            layout = view.findViewById(R.id.layoutj);
            top = view.findViewById(R.id.close_time);
            inactive = view.findViewById(R.id.inactive);
            play_text = view.findViewById(R.id.play_text);
        }
    }



}
