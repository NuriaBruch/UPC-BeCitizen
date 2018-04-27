package com.becitizen.app.becitizen.presentation;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.entities.CategoryThread;

import java.util.ArrayList;

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
        subtitleView.setText(dataSet.get(position).getCreatedAt().toString() + subtitleView.getText() +
            dataSet.get(position).getAuthor());
        votesView.setText(String.valueOf(dataSet.get(position).getVotes()));

        // 5. retrn rowView
        return rowView;
    }
}