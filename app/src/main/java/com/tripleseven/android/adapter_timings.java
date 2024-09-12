package com.tripleseven.android;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class adapter_timings extends RecyclerView.Adapter<adapter_timings.ViewHolder> {

    Context context;
    String market;
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> is_open = new ArrayList<>();
    ArrayList<Integer> image = new ArrayList<>();
    ArrayList<String> number = new ArrayList<>();
    private ArrayList<String> time = new ArrayList<>();
    ArrayList<String> game = new ArrayList<>();


    public adapter_timings(Context context, String market, ArrayList<String> name, ArrayList<String> is_open, ArrayList<String> time,ArrayList<String> game) {
        this.context = context;
        this.market = market;
        this.name = name;
        this.is_open = is_open;
        this.time = time;
        this.game = game;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.market_timing_layout, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        holder.name.setText(time.get(position));
        if(game.get(position).equals("Hukum") && name.get(position).equals("J")){
            holder.time.setBackgroundResource(R.drawable.hukum_j);
        }else if(game.get(position).equals("Hukum") && name.get(position).equals("Q")){
            holder.time.setBackgroundResource(R.drawable.hukum_q);
        }else if(game.get(position).equals("Hukum") && name.get(position).equals("K")){
            holder.time.setBackgroundResource(R.drawable.hukum_k);
        }else if(game.get(position).equals("Pan") && name.get(position).equals("J")){
            holder.time.setBackgroundResource(R.drawable.pan_j);
        }else if(game.get(position).equals("Pan") && name.get(position).equals("Q")){
            holder.time.setBackgroundResource(R.drawable.pan_q);
        }else if(game.get(position).equals("Pan") && name.get(position).equals("K")){
            holder.time.setBackgroundResource(R.drawable.pan_k);
        }else if(game.get(position).equals("Chidi") && name.get(position).equals("J")){
            holder.time.setBackgroundResource(R.drawable.chidi_j);
        }else if(game.get(position).equals("Chidi") && name.get(position).equals("Q")){
            holder.time.setBackgroundResource(R.drawable.chidi_q);
        }else if(game.get(position).equals("Chidi") && name.get(position).equals("K")){
            holder.time.setBackgroundResource(R.drawable.chidi_k);
        }else if(game.get(position).equals("Eint") && name.get(position).equals("J")){
            holder.time.setBackgroundResource(R.drawable.eint_j);
        }else if(game.get(position).equals("Eint") && name.get(position).equals("Q")){
            holder.time.setBackgroundResource(R.drawable.eint_q);
        }else if(game.get(position).equals("Eint") && name.get(position).equals("K")){
            holder.time.setBackgroundResource(R.drawable.eint_k);
        }else{
            holder.time.setText("-");
        }




        if (is_open.get(position).equals("1")) {
            holder.sinfo.setTextColor(context.getResources().getColor(R.color.green));
            holder.sinfo.setText("Betting is Running Now");
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    askOpenClose(is_open.get(position),time.get(position),starline_games.class);
                 //   context.startActivity(new Intent(context,starline_games.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("is_jodi",is_jodi.get(position)).putExtra("market",market).putExtra("timing",time.get(position)));
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

    public void askOpenClose(String openn ,String timee, Class<? extends Activity> ActivityToOpen) {

        if (ActivityToOpen == null) {
            ActivityToOpen = BettingScreen.class;
        }

        if (openn.equals("1")) {

            android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(context);
            LayoutInflater factory = LayoutInflater.from(context);
            View v2 = factory.inflate(R.layout.popup_layout, null);

           CardView pan = v2.findViewById(R.id.pan);
            CardView hukum = v2.findViewById(R.id.hukum);
            CardView chidi = v2.findViewById(R.id.chidi);
            CardView eint = v2.findViewById(R.id.eint);


            Class<? extends Activity> finalActivityToOpen = ActivityToOpen;


            hukum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.app.AlertDialog alert11 = builder1.create();
                    alert11.dismiss();
                    single();
                    hukum();
                    context.startActivity(new Intent(context, cardBetting.class)
                            .putExtra("market", market).putExtra("digits", image)
                            .putExtra("game", "Hukum")
                            .putExtra("number", number)
                            .putExtra("time",timee)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                }
            });
            pan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.app.AlertDialog alert11 = builder1.create();
                    alert11.dismiss();
                   single();
                    pan();
                    context.startActivity(new Intent(context, cardBetting.class)
                            .putExtra("market", market).putExtra("digits", image)
                            .putExtra("game", "Pan")
                            .putExtra("number", number)
                            .putExtra("time",timee)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                }
            });

            chidi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.app.AlertDialog alert11 = builder1.create();
                    alert11.dismiss();
                    single();
                    chidi();
                    context.startActivity(new Intent(context, cardBetting.class)
                            .putExtra("market", market).putExtra("digits", image)
                            .putExtra("game", "Chidi")
                            .putExtra("number", number)
                            .putExtra("time",timee)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                }
            });

            eint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.app.AlertDialog alert11 = builder1.create();
                    alert11.dismiss();
                    single();
                    eint();
                    context.startActivity(new Intent(context, cardBetting.class)
                            .putExtra("market", market).putExtra("digits", image)
                            .putExtra("game", "Eint")
                            .putExtra("number", number)
                            .putExtra("time",timee)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                }
            });

            builder1.setView(v2);
            builder1.setCancelable(true);
            android.app.AlertDialog alert11 = builder1.create();


            alert11.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            alert11.show();


        } else {
            android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(context);
            LayoutInflater factory = LayoutInflater.from(context);
            View v2 = factory.inflate(R.layout.msg_dialog_error, null);

            TextView close = v2.findViewById(R.id.close);
            TextView msgView = v2.findViewById(R.id.msg);
            msgView.setText("This game is already closed for this market");

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

    }
    public void single() {
        number.clear();
        number.add("J");
        number.add("Q");
        number.add("K");

    }

    public void hukum() {
        image.clear();
        image.add(R.drawable.hukum_j);
        image.add(R.drawable.hukum_q);
        image.add(R.drawable.hukum_k);

    }

    public void chidi() {
        image.clear();
        image.add(R.drawable.chidi_j);
        image.add(R.drawable.chidi_q);
        image.add(R.drawable.chidi_k);

    }

    public void pan() {
        image.clear();
        image.add(R.drawable.pan_j);
        image.add(R.drawable.pan_q);
        image.add(R.drawable.pan_k);

    }
    public void eint() {
        image.clear();
        image.add(R.drawable.eint_j);
        image.add(R.drawable.eint_q);
        image.add(R.drawable.eint_k);

    }


    @Override
    public int getItemCount() {
        return time.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,time, sinfo;
        RelativeLayout layout;
        CardView card;


        public ViewHolder(View view) {
            super(view);

            card = view.findViewById(R.id.img_card);
            layout = view.findViewById(R.id.layoutj);
            name = view.findViewById(R.id.ntitle);
            time = view.findViewById(R.id.nmsg);
            sinfo = view.findViewById(R.id.ndate);

        }
    }



}
