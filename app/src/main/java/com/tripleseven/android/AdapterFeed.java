package com.tripleseven.android;


import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterFeed extends RecyclerView.Adapter<AdapterFeed.ViewHolder> {

    final Context context;
    final ArrayList<FeedModel> user;

    ViewDialog progressDialog;
    final String url = constant.prefix + "like.php";

    public AdapterFeed(Context context, ArrayList<FeedModel> user) {
        this.context = context;
        this.user = user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed, parent, false);



        return new ViewHolder(v);
    }


    private void delete(String id, ImageView imageView, TextView textView) {

        imageView.clearColorFilter();
        Glide.with(context).load(R.drawable.loading).into(imageView);


        RequestQueue requestQueue = Volley.newRequestQueue(context);

        final StringRequest postRequest = new MyStringRequest(
                context.getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url,
                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        Betplay.addTempFollow(id,jsonObject.getString("success"));

                        if (jsonObject.getString("success").equals("1")){
                            Glide.with(context).load(R.drawable.liked).into(imageView);
                            imageView.setColorFilter(context.getResources().getColor(R.color.colorAccent));
                            textView.setTextColor(context.getResources().getColor(R.color.colorAccent));
                        } else {
                            Glide.with(context).load(R.drawable.like).into(imageView);
                            imageView.setColorFilter(context.getResources().getColor(R.color.font));
                            textView.setTextColor(context.getResources().getColor(R.color.font));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                },
                error -> {

                    error.printStackTrace();

                    Toast.makeText(context, "Check your internet connection", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("mobile", context.getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile", null));

                params.put("usermobile", id);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.title.setText(user.get(position).getTitle());


        if (!user.get(position).getMedia_type().equals("0")){
            Glide.with(context).load(constant.project_root+"admin/"+user.get(position).getMedia_url()).thumbnail(0.2f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.image);
            if (user.get(position).getMedia_type().equals("2")){
                holder.play.setVisibility(View.VISIBLE);
            } else {
                holder.play.setVisibility(View.GONE);
            }
            holder.image.setVisibility(View.VISIBLE);
        }

        if ((user.get(position).getIslike().equals("1") && Betplay.checkFollow(user.get(position).getId()).equals("-1")) || Betplay.checkFollow(user.get(position).getId()).equals("1")){
            Glide.with(context).load(R.drawable.liked).into(holder.likeIcon);
            holder.likeIcon.setColorFilter(context.getResources().getColor(R.color.colorAccent));
            holder.likeText.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }

        holder.share.setOnClickListener(v -> {
            final String appPackageName = context.getPackageName();
            String msg = user.get(position).getTitle()+", "+"Download "+context.getResources().getString(R.string.app_name)+" to view full story - https://play.google.com/store/apps/details?id=" + appPackageName;
            Intent intent2 = new Intent(); intent2.setAction(Intent.ACTION_SEND);
            intent2.setType("text/plain");
            intent2.putExtra(Intent.EXTRA_TEXT, msg );
            context.startActivity(Intent.createChooser(intent2, "Share via"));
        });

        if (user.get(position).getDescription().equals(""))
        {
            holder.content.setVisibility(View.GONE);
        }
        else
        {
            holder.content.setText(user.get(position).getDescription());
        }

        holder.time.setText(user.get(position).getTime());

        holder.newsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Betplay.setFeedModel(user.get(position));
                context.startActivity(new Intent(context,FeedDetail.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(user.get(position).getId(), holder.likeIcon, holder.likeText);
            }
        });

        holder.likes.setText(user.get(position).getLikes()+" Likes");
    }

    @Override
    public int getItemCount() {
        return user.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {


        CircleImageView icon;
        latobold name;
        RelativeLayout userProfile;
        latobold title;
        latonormal content;
        latonormal time;
        ImageView image;
        ImageView background;
        ImageView play;
        RelativeLayout attachView;
        LinearLayout newsLayout;
        latonormal likes;
        ImageView likeIcon;
        latonormal likeText;
        LinearLayout like;
        LinearLayout share;
        CardView newsCard;

        public ViewHolder(View view) {
            super(view);


            this.icon = itemView.findViewById(R.id.icon);
            this.name = itemView.findViewById(R.id.ntitle);
            this.userProfile = itemView.findViewById(R.id.userProfile);
            this.title = itemView.findViewById(R.id.title);
            this.content = itemView.findViewById(R.id.content);
            this.time = itemView.findViewById(R.id.nmsg);
            this.image = itemView.findViewById(R.id.image);
            this.background = itemView.findViewById(R.id.background);
            this.play = itemView.findViewById(R.id.play);
            this.attachView = itemView.findViewById(R.id.attachView);
            this.newsLayout = itemView.findViewById(R.id.news_layout);
            this.likes = itemView.findViewById(R.id.likes);
            this.likeIcon = itemView.findViewById(R.id.like_icon);
            this.likeText = itemView.findViewById(R.id.like_text);
            this.like = itemView.findViewById(R.id.like);
            this.share = itemView.findViewById(R.id.share);
            this.newsCard = itemView.findViewById(R.id.news_card);
        }
    }


}
