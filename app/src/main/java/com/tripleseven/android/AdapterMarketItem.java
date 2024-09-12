package com.tripleseven.android;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tripleseven.android.dto.MarketDto;

import java.util.Calendar;
import java.util.List;

public class AdapterMarketItem extends RecyclerView.Adapter<AdapterMarketItem.ViewHolder> {
    Context context;
    private final List<MarketDto> markets;
    private final String clubType;

    public AdapterMarketItem(Context context, List<MarketDto> markets, String clubType) {
        this.context = context;
        this.markets = markets;
        this.clubType = clubType;
    }

    @NonNull
    @Override
    public AdapterMarketItem.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if ("TWO_DIGIT".equals(clubType)) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.market_item_2d, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.market_item_3d, parent, false);
        }
        return new ViewHolder(view, clubType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.marketName.setText(markets.get(position).getName());
        holder.closeTime.setText(markets.get(position).getCloseBidEndTime());
        holder.resultTime.setText(markets.get(position).getCloseResultTime());
        holder.leftNumber.setText(markets.get(position).getLeftNumber());
        holder.rightNumber.setText(markets.get(position).getRightNumber());

        if (holder.clubType.equalsIgnoreCase("THREE_DIGIT")) {
            holder.closeTime.setText(markets.get(position).getOpenBidEndTime());
            holder.resultTime.setText(markets.get(position).getCloseResultTime());
            holder.midNumber.setText(markets.get(position).getMidNumber());
        }

        holder.info.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.info.setMarqueeRepeatLimit(-1);
        holder.info.setSingleLine(true);
        holder.info.setSelected(true);

        holder.chartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(
                        new Intent(context, charts.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra("href",constant.prefix + "get_charts?marketId=" + markets.get(position).getMarketId())
                );
            }
        });

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if (markets.get(position).getCloseSessionOn()) {
            holder.info.setText(markets.get(position).getInfoText());
            holder.info.setTextColor(context.getResources().getColor(R.color.md_green_800));
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, MarketGames.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("market", markets.get(position))
                            .putExtra("club_type", clubType)
                    );
                }
            });
        } else {
            holder.info.setText(markets.get(position).getInfoText());
            holder.info.setTextColor(context.getResources().getColor(R.color.md_red_600));

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    new androidx.appcompat.app.AlertDialog.Builder(context)
//                            .setTitle(R.string.market_close)
//                            .setMessage(markets.get(position).getInfoText())
//                            .setNegativeButton(android.R.string.no, null)
//                            .show();
                    Toast.makeText(context, context.getString(R.string.bet_closed), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return markets.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView marketName, leftNumber, midNumber, rightNumber, info, closeTime, resultTime;
        LinearLayout playGameLayout, chartLayout;
        CardView layout;
        String clubType;

        public ViewHolder(View view, String clubType) {
            super(view);

            this.clubType = clubType;
            marketName = view.findViewById(R.id.marketName);
            chartLayout = view.findViewById(R.id.chart);
            leftNumber = view.findViewById(R.id.leftNumber);
            rightNumber = view.findViewById(R.id.rightNumber);
            closeTime = view.findViewById(R.id.closeTime);
            resultTime = view.findViewById(R.id.resultTime);
            info = view.findViewById(R.id.info);
            playGameLayout = view.findViewById(R.id.play_game);

            if ("TWO_DIGIT".equalsIgnoreCase(clubType)) {
                layout = view.findViewById(R.id.market_item_2d);
            } else {
                layout = view.findViewById(R.id.market_item_3d);
                midNumber = view.findViewById(R.id.midNumber);
            }
        }
    }
}
