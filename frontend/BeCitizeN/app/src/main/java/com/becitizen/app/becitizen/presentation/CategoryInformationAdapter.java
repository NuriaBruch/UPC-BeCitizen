package com.becitizen.app.becitizen.presentation;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.entities.CategoryThread;
import com.becitizen.app.becitizen.domain.entities.Information;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CategoryInformationAdapter extends RecyclerView.Adapter<CategoryInformationAdapter.ViewHolder> {

    private List<Information> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, content;
        ImageView expander;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.infoTitle);
            content = itemView.findViewById(R.id.infoContent);
            expander = itemView.findViewById(R.id.expander);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            if (content.getVisibility() == View.VISIBLE) {
                expander.setRotation(0);
                content.setVisibility(View.GONE);
            }
            else {
                expander.setRotation(180);
                content.setVisibility(View.VISIBLE);
            }
        }
    }

    // data is passed into the constructor
    CategoryInformationAdapter(Context context, List<Information> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View rowView = mInflater.inflate(R.layout.row_category_information, parent, false);
        return new ViewHolder(rowView);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.content.setVisibility(View.GONE);

        if (mData.get(position).getTitle().length() < 22)
            holder.title.setText(mData.get(position).getTitle());
        else
            holder.title.setText(mData.get(position).getTitle().substring(0, 22) + "...");

        holder.content.setText(mData.get(position).getContent());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // convenience method for getting data at click position
    Information getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    private ArrayList<CategoryThread> dataSet;
    Context context;

}