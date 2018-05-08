package com.becitizen.app.becitizen.presentation;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.becitizen.app.becitizen.R;

import java.util.List;

/**
 * Created by Nuria on 30/04/2018.
 */

class ImageListArrayAdapter extends ArrayAdapter<ImageSelection.ProfileImage> {

    private final List<ImageSelection.ProfileImage> list;
    private final Activity context;

    static class ViewHolder {
        protected ImageView image;
    }

    public ImageListArrayAdapter(Activity context, List<ImageSelection.ProfileImage> list) {
        super(context, R.layout.activity_image_selection, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.activity_image_selection, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) view.findViewById(R.id.image);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.image.setImageDrawable(list.get(position).getPng());
        return view;
    }

}
