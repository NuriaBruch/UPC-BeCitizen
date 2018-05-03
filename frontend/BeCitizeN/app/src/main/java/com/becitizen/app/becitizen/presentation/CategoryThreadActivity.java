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
import android.widget.Toast;

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
    private String category = "";

    public CategoryThreadActivity() {
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_category_thread, container, false);

        listView = (ListView)rootView.findViewById(R.id.list);

        dataModels = ControllerThreadPresentation.getUniqueInstance().getThreadsCategory(category);

        if (dataModels.size() == 0) {
            Toast toast = Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT);
            toast.show();
        }

        adapter = new CategoryThreadAdapter(dataModels, getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CategoryThread dataModel= dataModels.get(position);
            }
        });

        return rootView;
    }

}