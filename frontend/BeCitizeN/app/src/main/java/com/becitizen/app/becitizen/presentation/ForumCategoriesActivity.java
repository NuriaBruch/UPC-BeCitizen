package com.becitizen.app.becitizen.presentation;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.becitizen.app.becitizen.R;

import java.util.ArrayList;

public class ForumCategoriesActivity extends Fragment {

    private View rootView;
    private ArrayList<String> categories;
    ArrayAdapter<String> adapter;
    private Handler UIUpdater = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            for (int i = 0; i < categories.size(); ++i)
                adapter.add(categories.get(i));
            progressBar.setVisibility(View.GONE);
        }
    };
    private ProgressBar progressBar;

    public ForumCategoriesActivity() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_forum_categories, container, false);

        ListView list = (ListView)rootView.findViewById(R.id.listView);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);

        adapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.row_forum_category);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CategoryThreadActivity cta = new CategoryThreadActivity();
                cta.setCategory(categories.get(position));
                Fragment fragment = cta;
                fragmentTransaction(fragment, "CATEGORY_THREAD_ACTIVITY");
            }
        });

        Runnable loadCategories = new Runnable() {
            public void run() {
                categories = ControllerThreadPresentation.getUniqueInstance().getCategories();
                UIUpdater.sendEmptyMessage(0);
            }
        };

        progressBar.setVisibility(View.VISIBLE);
        Thread threadLoadCategories = new Thread(loadCategories);
        threadLoadCategories.start();

        return rootView;
    }



    private void fragmentTransaction(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

}