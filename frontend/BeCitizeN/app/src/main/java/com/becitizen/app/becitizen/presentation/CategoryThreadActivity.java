package com.becitizen.app.becitizen.presentation;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.entities.CategoryThread;

import java.time.LocalTime;
import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CategoryThreadActivity extends Fragment {

    private View rootView;
    private LinearLayout parentLinearLayout;
    ArrayList<CategoryThread> dataModels;
    ListView listView;
    private static CategoryThreadAdapter adapter;

    public CategoryThreadActivity() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_category_thread, container, false);
        parentLinearLayout = (LinearLayout) rootView.findViewById(R.id.parent_linear_layout);
        //for (int i = 0; i < 20; ++i) addThreadRow("Test", "Guillem", "22 mins ago", 3);

        listView = (ListView)rootView.findViewById(R.id.list);

        dataModels= new ArrayList<>();

        dataModels.add(new CategoryThread("Apple Pie", "Android 1.0", "10-02-2017",1));
        dataModels.add(new CategoryThread("Apple Pie", "Android 1.0", "10-02-2017",1));

        adapter = new CategoryThreadAdapter(dataModels,getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CategoryThread dataModel= dataModels.get(position);
            }
        });

        return rootView;
    }

    private void addThreadRow(String title, String author, String time, int votes) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.row_category_thread, null);

        //set title
        TextView titleView = (TextView) rowView.findViewById(R.id.threadTitle);
        titleView.setText(title);

        //set subtitle
        TextView subtitleView = (TextView) rowView.findViewById(R.id.threadSubtitle);
        subtitleView.setText(time + " by " + author);

        //set votes
        TextView votesView = (TextView) rowView.findViewById(R.id.votes);
        votesView.setText(String.valueOf(votes));

        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
    }

    public void onDelete(View v) {
        parentLinearLayout.removeView((View) v.getParent());
    }

}