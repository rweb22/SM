package com.samratmatka.android;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.samratmatka.android.dto.ClubDto;

import java.util.List;

public class AdapterClubItem extends RecyclerView.Adapter<AdapterClubItem.ViewHolder> {
    Context context;
    List<ClubDto> clubs;

    public AdapterClubItem(Context context, List<ClubDto> clubs) {
        this.context = context;
        this.clubs = clubs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.club_item, parent, false);
        return new AdapterClubItem.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        System.out.println("inside recycler view item");
        ClubDto clubDto = clubs.get(position);

        String overlayTextContent = clubDto.getTotalPlayingUser() + constant.SPACE + context.getString(R.string.PLAYING);
        holder.overlayText.setText(overlayTextContent);

        String url = constant.admin_root + clubDto.getIconImageSlug();
        Glide.with(context).load(url).into(holder.itemIcon);

        holder.itemLayout.setOnClickListener(view ->
                context.startActivity(new Intent(context, ClubDashboard.class).putExtra("clubDto", clubDto))
        );
        System.out.println("end of recycler view item");
    }

    @Override
    public int getItemCount() {
        return clubs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView overlayText;
        ImageView itemIcon;
        CardView itemLayout;

        public ViewHolder(View view) {
            super(view);
            overlayText = view.findViewById(R.id.textOverlay);
            itemIcon = view.findViewById(R.id.marketIconImage);
            itemLayout = view.findViewById(R.id.itemCard);
        }
    }
}


