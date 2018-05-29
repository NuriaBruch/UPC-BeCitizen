package com.becitizen.app.becitizen.presentation.info;

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
import android.widget.TextView;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.entities.CategoryThread;
import com.becitizen.app.becitizen.domain.entities.Information;
import com.becitizen.app.becitizen.domain.entities.Marker;
import com.becitizen.app.becitizen.presentation.controllers.ControllerInformationPresentation;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CategoryInformationAdapter extends RecyclerView.Adapter<CategoryInformationAdapter.ViewHolder> {

    private List<Information> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        JustifiedTextView content;
        ImageView expander;
        Button openInBrowser;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.infoTitle);
            content = itemView.findViewById(R.id.infoContent);
            expander = itemView.findViewById(R.id.expander);
            openInBrowser = itemView.findViewById(R.id.openInBrowser);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());

            Information info = null;

            if (content.getVisibility() == View.GONE) {

                try {
                    info = ControllerInformationPresentation.getUniqueInstance().getInformation(mData.get(getAdapterPosition()).getId());
                } catch (NetworkErrorException e) {
                    Toast toast = Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.networkError), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            String url = "";
            ArrayList<Marker> markers = new ArrayList<>();

            if (info != null) {
                content.setText(info.getContent());
                url = info.getUrl();
                markers = info.getMarkers();
                openInBrowser.setOnClickListener(new OpenInBrowserOnClick(url, markers));
            }

            if (content.getVisibility() == View.VISIBLE) {
                expander.setRotation(0);
                content.setVisibility(View.GONE);
                openInBrowser.setVisibility(View.GONE);
            }
            else {
                expander.setRotation(180);
                content.setVisibility(View.VISIBLE);
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    openInBrowser.setVisibility(View.VISIBLE);
                } else if (markers.size() > 0) {
                    openInBrowser.setText(mContext.getResources().getString(R.string.openMap));
                    openInBrowser.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public class OpenInBrowserOnClick implements View.OnClickListener
    {

        String url;
        ArrayList<Marker> markers;
        public OpenInBrowserOnClick(String url, ArrayList<Marker> markers) {
            this.url = url;
            this.markers = markers;
        }

        @Override
        public void onClick(View v)
        {
            if (url.startsWith("http://") || url.startsWith("https://")) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mContext.startActivity(browserIntent);
            } else {
                Intent mapsIntent = new Intent(mContext, MapsActivity.class);
                mapsIntent.putExtra("markers", markers);
                mContext.startActivity(mapsIntent);
            }
        }

    };

    // data is passed into the constructor
    CategoryInformationAdapter(Context context, List<Information> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
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
        holder.openInBrowser.setVisibility(View.GONE);

        if (mData.get(position).getTitle().length() < 22)
            holder.title.setText(mData.get(position).getTitle());
        else
            holder.title.setText(mData.get(position).getTitle().substring(0, 22) + "...");

        //holder.content.setText(mData.get(position).getContent());
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