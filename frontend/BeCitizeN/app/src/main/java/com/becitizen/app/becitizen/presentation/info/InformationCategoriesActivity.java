package com.becitizen.app.becitizen.presentation.info;

import android.accounts.NetworkErrorException;
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
import android.widget.Toast;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.presentation.controllers.ControllerForumPresentation;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class InformationCategoriesActivity extends Fragment {

    private View rootView;
    private ArrayList<String> categories = new ArrayList<>();
    ArrayAdapter<String> adapter;
    private Handler UIUpdater = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            for (int i = 0; i < categories.size(); ++i)
                adapter.add(categories.get(i));
            progressBar.setVisibility(View.GONE);
            if (msg.what == 1) {
                Toast toast = Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.networkError), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    };
    private ProgressBar progressBar;

    public InformationCategoriesActivity() {
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
                CategoryInformationActivity cia = new CategoryInformationActivity();
                cia.setCategory(categories.get(position));
                Fragment fragment = cia;
                fragmentTransaction(fragment, "CATEGORY_INFORMATION_ACTIVITY");
            }
        });

        Runnable loadCategories = new Runnable() {
            public void run() {
                try {
                    categories = ControllerForumPresentation.getUniqueInstance().getCategories();
                    UIUpdater.sendEmptyMessage(0);
                } catch (NetworkErrorException e) {
                    UIUpdater.sendEmptyMessage(1);
                }
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