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

public class CategoryInformationAdapter extends ArrayAdapter<CategoryThread> implements View.OnClickListener{

    private ArrayList<CategoryThread> dataSet;
    Context context;


    public CategoryInformationAdapter(ArrayList<CategoryThread> data, Context context) {
        super(context, R.layout.row_category_thread, data);
        this.dataSet = data;
        this.context=context;
    }

    @Override
    public void onClick(View v) {
        if (v.findViewById(R.id.list).getVisibility() == View.VISIBLE) {
            //TODO: check opened
            v.findViewById(R.id.infoContent).setVisibility(View.GONE);
        }
        else {
            //TODO: check closed
            v.findViewById(R.id.infoContent).setVisibility (View.VISIBLE);
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        final View rowView = inflater.inflate(R.layout.row_category_information, parent, false);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rowView.findViewById(R.id.infoContent).getVisibility() == View.VISIBLE)
                    rowView.findViewById(R.id.infoContent).setVisibility(View.GONE);
                else
                    rowView.findViewById(R.id.infoContent).setVisibility(View.VISIBLE);
            }
        });

        // 3. Get the two text view from the rowView
        TextView titleView = (TextView) rowView.findViewById(R.id.infoTitle);
        TextView contentView = (TextView) rowView.findViewById(R.id.infoContent);

        //TODO: check if open
        contentView.setVisibility(View.GONE);

        // 4. Set the text for textView
        if (dataSet.get(position).getTitle().length() < 22)
            titleView.setText(dataSet.get(position).getTitle());
        else
            titleView.setText(dataSet.get(position).getTitle().substring(0, 22) + "...");

        contentView.setText(dataSet.get(position).getAuthor());

        return rowView;
    }
}