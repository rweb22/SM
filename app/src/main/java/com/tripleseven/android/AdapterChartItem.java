package com.tripleseven.android;


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

class AdapterChartItem extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    Context context;
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> result = new ArrayList<>();
    ArrayList<String> type = new ArrayList<>();
    ArrayList<Integer> itemTypes = new ArrayList<>();
    ArrayList<String> displayNames = new ArrayList<>();


    public AdapterChartItem(Context context, ArrayList<String> name, ArrayList<String> result, ArrayList<String> type) {
        this.context = context;
        this.name = name;
        this.result = result;
        this.type = type;

        // Build display list with headers
        buildDisplayList();
    }

    private void buildDisplayList() {
        itemTypes.clear();
        displayNames.clear();

        boolean delhiHeaderAdded = false;
        boolean kalyanHeaderAdded = false;

        for (int i = 0; i < name.size(); i++) {
            String marketType = type.get(i);

            // Add Delhi header before first Delhi market
            if (marketType.equalsIgnoreCase("delhi") && !delhiHeaderAdded) {
                itemTypes.add(VIEW_TYPE_HEADER);
                displayNames.add("DELHI MARKETS");
                delhiHeaderAdded = true;
            }

            // Add Kalyan header before first Kalyan market
            if (!marketType.equalsIgnoreCase("delhi") && !kalyanHeaderAdded) {
                itemTypes.add(VIEW_TYPE_HEADER);
                displayNames.add("KALYAN MARKETS");
                kalyanHeaderAdded = true;
            }

            // Add the actual market item
            itemTypes.add(VIEW_TYPE_ITEM);
            displayNames.add(name.get(i));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return itemTypes.get(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chart_header_layout, parent, false);
            return new HeaderViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chart_layout, parent, false);
            return new ItemViewHolder(v);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).headerText.setText(displayNames.get(position));
        } else if (holder instanceof ItemViewHolder) {
            // Calculate the actual data position (excluding headers)
            int dataPosition = getDataPosition(position);

            ((ItemViewHolder) holder).name.setText(name.get(dataPosition));
            ((ItemViewHolder) holder).name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, Charts.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("href",constant.prefix + "get_charts?market_id=" + result.get(dataPosition)));
                }
            });
        }
    }

    private int getDataPosition(int displayPosition) {
        int dataPos = 0;
        for (int i = 0; i < displayPosition; i++) {
            if (itemTypes.get(i) == VIEW_TYPE_ITEM) {
                dataPos++;
            }
        }
        return dataPos;
    }


    @Override
    public int getItemCount() {
        return itemTypes.size();
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        RelativeLayout layout;

        public ItemViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.ntitle);
            layout = view.findViewById(R.id.layoutj);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerText;

        public HeaderViewHolder(View view) {
            super(view);
            headerText = view.findViewById(R.id.header_text);
        }
    }
}
