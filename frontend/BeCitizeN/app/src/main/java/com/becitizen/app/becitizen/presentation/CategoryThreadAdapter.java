package com.becitizen.app.becitizen.presentation;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.entities.CategoryThread;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CategoryThreadAdapter extends ArrayAdapter<CategoryThread> implements View.OnClickListener{

    private ArrayList<CategoryThread> dataSet;
    Context context;

    // View lookup cache
    private static class ViewHolder {
        private TextView title;
        private TextView subtitle;
        private TextView votes;
    }

    public CategoryThreadAdapter(ArrayList<CategoryThread> data, Context context) {
        super(context, R.layout.row_category_thread, data);
        this.dataSet = data;
        this.context=context;
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        Object object = getItem(position);
        CategoryThread categoryThread = (CategoryThread)object;

        switch (v.getId())
        {
            case R.id.element:
                System.out.println(position);
                break;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.row_category_thread, parent, false);

        // 3. Get the two text view from the rowView
        TextView titleView = (TextView) rowView.findViewById(R.id.threadTitle);
        TextView subtitleView = (TextView) rowView.findViewById(R.id.threadSubtitle);
        TextView votesView = (TextView) rowView.findViewById(R.id.votes);

        // 4. Set the text for textView
        titleView.setText(dataSet.get(position).getTitle());

        //get x seconds/hours/days/years string
        String dateString;
        try{
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            cal.setTime(sdf.parse(dataSet.get(position).getCreatedAt()));

            Calendar calendar = Calendar.getInstance();
            long now = calendar.getTimeInMillis();
            long time = cal.getTimeInMillis();

            long diff = now - time;

            int seconds = (int) (diff / 1000);
            int minutes = (int) (diff / (1000*60));
            int hours   = (int) (diff / (1000*60*60));
            int days = (int) (diff / (1000*60*60*24));

            if (seconds == 1) dateString = String.valueOf(seconds) + " " + context.getResources().getString(R.string.second);
            else if (seconds < 60) dateString = String.valueOf(seconds) + " " + context.getResources().getString(R.string.seconds);
            else if (minutes == 1) dateString = String.valueOf(minutes) + " " + context.getResources().getString(R.string.minute);
            else if (minutes < 60) dateString = String.valueOf(minutes) + " " + context.getResources().getString(R.string.minutes);
            else if (hours == 1) dateString = String.valueOf(hours) + " " + context.getResources().getString(R.string.hour);
            else if (hours < 24) dateString = String.valueOf(hours) + " " + context.getResources().getString(R.string.hours);
            else if (days == 1) dateString = String.valueOf(days) + " " + context.getResources().getString(R.string.day);
            else if (days < 365) dateString = String.valueOf(days) + " " + context.getResources().getString(R.string.days);
            else if (days/365 == 1) dateString = String.valueOf(days/365) + " " + context.getResources().getString(R.string.year);
            else dateString = String.valueOf(days/365) + " " + context.getResources().getString(R.string.years);

        }catch(ParseException e){
            dateString = "";
            System.out.println(e.toString());
        }

        subtitleView.setText(dateString + " " + subtitleView.getText() + " " +
            dataSet.get(position).getAuthor());
        votesView.setText(String.valueOf(dataSet.get(position).getVotes()));

        return rowView;
    }
}