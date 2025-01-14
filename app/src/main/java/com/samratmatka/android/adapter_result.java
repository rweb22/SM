package com.samratmatka.android;


import android.content.Context;
import android.content.Intent;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.iwgang.countdownview.CountdownView;

class adapter_result extends RecyclerView.Adapter<adapter_result.ViewHolder> {

    Context context;
 //   ArrayList<String> name = new ArrayList<>();
 //   ArrayList<String> result = new ArrayList<>();

 //   ArrayList<String> is_open = new ArrayList<>();
 //   ArrayList<String> open_time = new ArrayList<>();
 ///   ArrayList<String> close_time = new ArrayList<>();
 //   private ArrayList<String> open_av = new ArrayList<>();
 //   ArrayList<String> market_type = new ArrayList<>();
    ArrayList<ResultModel> results = new ArrayList<>();

    public adapter_result(Context context, ArrayList<String> name, ArrayList<String> result, ArrayList<String> is_open, ArrayList<String> open_time, ArrayList<String> close_time, ArrayList<String> open_av,ArrayList<String> market_type,ArrayList<ResultModel> results) {
        this.context = context;
      //  this.name = name;
      //  this.result = result;
      //  this.is_open = is_open;
      //  this.open_time = open_time;
      //  this.close_time = close_time;
      //  this.open_av = open_av;
      //  this.market_type = market_type;
        this.results=results;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_layout, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        ResultModel result=results.get(position);

        holder.name.setText(result.get_name());
        holder.result.setText(result.get_result());

        holder.open_time.setText(result.get_openTime());
        holder.close_time.setText(result.get_closeTime());

    //    holder.name.setText(name.get(position));
    //    holder.result.setText(result.get(position));

    //    holder.open_time.setText(open_time.get(position));
    //    holder.close_time.setText(close_time.get(position));

        holder.stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Statistics.class).putExtra("market", result.get_name()).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        holder.chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Charts.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("href",constant.prefix+"charted/index.php?market="+result.get_name()));
                Log.e("curl",constant.prefix+"chart2/getChart.php?market="+result.get_name());
            }
        });


        if (result.get_openAv().equals("1") || result.get_openAv().equals("1")) { // here we are getting open itrem
            holder.info.setText("Betting is Running Now");
            holder.info.setTextColor(context.getResources().getColor(R.color.green));
          //  holder.info.setBackground(context.getResources().getDrawable(R.drawable.green_box));
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    context.startActivity(new Intent(context, MarketGames.class)
                        //    .putExtra("market", name.get(position))
                        //    .putExtra("is_open", is_open.get(position))
                        //    .putExtra("is_close", open_av.get(position))
                        //    .putExtra("market_type",market_type.get(position))

                            .putExtra("market", result.get_name())
                            .putExtra("is_open", result.get_isOpen())
                            .putExtra("is_close",result.get_openAv())
                            .putExtra("market_type",result.get_marketType())
                    );
                }
            });

            if (result.get_isOpen().equals("1")){
                holder.play_button.setBackgroundResource(R.drawable.play_icon2);
        } else {
                holder.play_button.setBackgroundResource(R.drawable.play_icon2);
            }



            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = df.format(c);
            String givenDateString = formattedDate + " " +result.get_closeTime();

            System.out.println(result.get_name());
            System.out.println(givenDateString);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
            try {
                Date mDate = sdf.parse(givenDateString);
                long timeInMilliseconds = mDate.getTime();
                //System.out.println(timeInMilliseconds);
                holder.timer.start(timeInMilliseconds - System.currentTimeMillis());
                //System.out.println("started");
                holder.timer.setOnCountdownIntervalListener(1, new CountdownView.OnCountdownIntervalListener() {
                    @Override
                    public void onInterval(CountdownView cv, long remainTime) {

                        //   System.out.println(name.get(position)+" - "+cv.getRemainTime());
                    }
                });
                // System.out.println(timeInMilliseconds-System.currentTimeMillis());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else { // and here closed
            holder.play_button.setBackgroundResource(R.drawable.play_icon2);
            holder.info.setText("Betting closed for today");
            holder.info.setTextColor(context.getResources().getColor(R.color.red));
          //  holder.info.setBackground(context.getResources().getDrawable(R.drawable.red_box));

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(context);
                    LayoutInflater factory = LayoutInflater.from(context);
                    View v2 = factory.inflate(R.layout.msg_dialog_error, null);

                    TextView close = v2.findViewById(R.id.close);
                    TextView msgView = v2.findViewById(R.id.msg);
                    msgView.setText("Betting is already closed for this market");

                    builder1.setView(v2);
                    builder1.setCancelable(false);
                    android.app.AlertDialog alert11 = builder1.create();

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert11.dismiss();
                        }
                    });
                    alert11.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    alert11.show();

                }
            });

            //    holder.play_button.setVisibility(View.GONE);
        }

        holder.setIsRecyclable(true);

    }


    @Override
    public int getItemCount() {
        return results.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, result, info, close_time, open_time;
        LinearLayout play_game;
        CardView layout;
        CountdownView timer;
        ImageView blueTick;
        latonormal openTime;
        latonormal closeTime;
        CardView play_button;
        latobold textView2;
        LinearLayout stats;
        LinearLayout chart;
        LinearLayout playGame;
        heronormal top;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.ntitle);
            result = view.findViewById(R.id.result);
            info = view.findViewById(R.id.info);
            play_game = view.findViewById(R.id.play_game);
            layout = view.findViewById(R.id.layoutj);
            close_time = view.findViewById(R.id.close_time);
            open_time = view.findViewById(R.id.closeTime);
            timer = view.findViewById(R.id.timer);
            play_button = view.findViewById(R.id.play_button);
            this.blueTick = itemView.findViewById(R.id.blue_tick);
            this.textView2 = itemView.findViewById(R.id.textView2);
            this.stats = itemView.findViewById(R.id.stats);
            this.chart = itemView.findViewById(R.id.chart);
            this.top = itemView.findViewById(R.id.top);
        }
    }


}
