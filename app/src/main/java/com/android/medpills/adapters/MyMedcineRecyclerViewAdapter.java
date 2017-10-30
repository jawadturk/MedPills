package com.android.medpills.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.medpills.Model.PillAlarm;
import com.android.medpills.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MyMedcineRecyclerViewAdapter extends RecyclerView.Adapter<MyMedcineRecyclerViewAdapter.PillsViewHolder> {
    private List<PillAlarm> items = new ArrayList<PillAlarm>();

    public MyMedcineRecyclerViewAdapter() {

    }

    public void setItems(List<PillAlarm> items) {
        this.items = items;
    }

    public List<PillAlarm> getItems() {
        return this.items;
    }

    @Override
    public PillsViewHolder onCreateViewHolder(ViewGroup parent,
                                              int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_medicine_layout_item, parent, false);
        return new PillsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PillsViewHolder holder, int position) {
        PillAlarm item = items.get(position);
        holder.textView_pillName.setText(item.getPillName());
        holder.textView_pillTime.setText(item.getStringTime());
        if(!TextUtils.isEmpty(item.getPillPhotoPath()))
        {
            Glide.with(holder.imageView_pillImage.getContext()).load(item.getPillPhotoPath()).into(holder.imageView_pillImage);

        }else {
            Glide.with(holder.imageView_pillImage.getContext()).load(R.drawable.pills).into(holder.imageView_pillImage);

        }
        //TODO Fill in your logic for binding the view.
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    public static class PillsViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView_pillImage;
        private TextView textView_pillName;
        private TextView textView_pillTime;
        public PillsViewHolder(View itemView) {
            super(itemView);
            imageView_pillImage = (ImageView) itemView.findViewById(R.id.imageView_pillImage);
            textView_pillName = (TextView) itemView.findViewById(R.id.textView_pillName);
            textView_pillTime = (TextView) itemView.findViewById(R.id.textView_pillTime);
        }
    }
}