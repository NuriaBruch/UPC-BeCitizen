package com.becitizen.app.becitizen.presentation.faq;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.entities.CategoryThread;
import com.becitizen.app.becitizen.domain.entities.FaqEntry;
import com.becitizen.app.becitizen.domain.entities.Information;
import com.becitizen.app.becitizen.domain.entities.Marker;
import com.becitizen.app.becitizen.presentation.controllers.ControllerInformationPresentation;
import com.becitizen.app.becitizen.presentation.info.MapsActivity;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CategoryFaqAdapter extends RecyclerView.Adapter<CategoryFaqAdapter.ViewHolder> {

    private List<FaqEntry> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        JustifiedTextView content;
        ImageView expander;
        Button report;
        RatingBar ratingBar;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.faqTitle);
            content = itemView.findViewById(R.id.faqContent);
            expander = itemView.findViewById(R.id.expander);
            report = itemView.findViewById(R.id.report);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());

            if (content.getVisibility() == View.VISIBLE) {
                expander.setRotation(0);
                content.setVisibility(View.GONE);
                report.setVisibility(View.GONE);
                ratingBar.setVisibility(View.GONE);
            }
            else {
                expander.setRotation(180);
                content.setVisibility(View.VISIBLE);
                report.setVisibility(View.VISIBLE);
                ratingBar.setVisibility(View.VISIBLE);
            }
        }
    }

    public class ReportOnClick implements View.OnClickListener
    {
        int id;
        public ReportOnClick(int id) {
            this.id = id;
        }

        @Override
        public void onClick(View v)
        {

        }
    };

    // data is passed into the constructor
    CategoryFaqAdapter(Context context, List<FaqEntry> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View rowView = mInflater.inflate(R.layout.row_category_faq, parent, false);
        return new ViewHolder(rowView);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.content.setVisibility(View.GONE);
        holder.report.setVisibility(View.GONE);
        holder.ratingBar.setVisibility(View.GONE);

        if (mData.get(position).getQuestion().length() < 22)
            holder.title.setText(mData.get(position).getQuestion());
        else
            holder.title.setText(mData.get(position).getQuestion().substring(0, 22) + "...");

        holder.content.setText(mData.get(position).getAnswer());
        holder.report.setOnClickListener(new ReportOnClick(mData.get(position).getId()));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // convenience method for getting data at click position
    FaqEntry getItem(int id) {
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