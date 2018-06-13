package com.becitizen.app.becitizen.presentation.faq;

import android.accounts.NetworkErrorException;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.res.ResourcesCompat;
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
import com.becitizen.app.becitizen.presentation.controllers.ControllerFaqPresentation;
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
        TextView title, titleLong;
        JustifiedTextView content;
        ImageView expander;
        Button report;
        RatingBar ratingBar;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.faqTitle);
            titleLong = itemView.findViewById(R.id.faqTitleLong);
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
                titleLong.setVisibility(View.GONE);
            }
            else {
                expander.setRotation(180);
                content.setVisibility(View.VISIBLE);
                report.setVisibility(View.VISIBLE);
                ratingBar.setVisibility(View.VISIBLE);
                titleLong.setVisibility(View.VISIBLE);
            }
        }
    }

    public class ReportOnClick implements View.OnClickListener
    {
        final int id;
        public ReportOnClick(int id) {
            this.id = id;
        }

        @Override
        public void onClick(View v)
        {
            AlertDialog.Builder adb = new AlertDialog.Builder(mContext);


            adb.setTitle(mContext.getResources().getString(R.string.wantToReport));


            adb.setIcon(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_info_outline, null));


            adb.setPositiveButton(mContext.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    try {
                        if (!ControllerFaqPresentation.getUniqueInstance().reportFaq(id)) throw new NetworkErrorException();
                    } catch (NetworkErrorException e) {
                        Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.networkError),Toast.LENGTH_SHORT).show();
                    }
                } });


            adb.setNegativeButton(mContext.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                } });
            adb.show();
        }
    };

    public class RatingOnRatingBarChangeListener implements RatingBar.OnRatingBarChangeListener
    {
        final int id;
        public RatingOnRatingBarChangeListener(int id) {
            this.id = id;
        }

        @Override
        public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
            if (b) {
                final int value = Math.round(v);
                AlertDialog.Builder adb = new AlertDialog.Builder(mContext);


                adb.setTitle(mContext.getResources().getString(R.string.wantToRate));


                adb.setIcon(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_info_outline, null));


                adb.setPositiveButton(mContext.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            if (!ControllerFaqPresentation.getUniqueInstance().rateFaq(id, value))
                                throw new NetworkErrorException();
                        } catch (NetworkErrorException e) {
                            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.networkError), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                adb.setNegativeButton(mContext.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                adb.show();
            }
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
        holder.titleLong.setVisibility(View.GONE);

        if (mData.get(position).getQuestion().length() < 22)
            holder.title.setText(mData.get(position).getQuestion());
        else
            holder.title.setText(mData.get(position).getQuestion().substring(0, 22) + "...");

        holder.titleLong.setText(mData.get(position).getQuestion());

        holder.content.setText(mData.get(position).getAnswer());
        holder.ratingBar.setRating(mData.get(position).getRating());
        holder.ratingBar.setOnRatingBarChangeListener(new RatingOnRatingBarChangeListener(mData.get(position).getId()));
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