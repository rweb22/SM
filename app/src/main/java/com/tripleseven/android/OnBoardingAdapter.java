package com.tripleseven.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class OnBoardingAdapter extends
        SliderViewAdapter<OnBoardingAdapter.SliderAdapterVH> {

    private Context context;
    private List<OnBoardingItem> mSliderItems = new ArrayList<>();

    public OnBoardingAdapter(Context context) {
        this.context = context;
    }

    public void renewItems(List<OnBoardingItem> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(OnBoardingItem sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.on_boarding_layout, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        OnBoardingItem sliderItem = mSliderItems.get(position);

        Glide.with(viewHolder.itemView)
                .load(sliderItem.getImage())
                .fitCenter()
                .into(viewHolder.imageViewBackground);

        viewHolder.title.setText(sliderItem.getTitle());
        viewHolder.desc.setText(sliderItem.getDescription());

    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    class SliderAdapterVH extends ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        TextView title, desc;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            this.itemView = itemView;
        }
    }

}
