package com.becitizen.app.becitizen.presentation;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.ControllerUserDomain;
import com.becitizen.app.becitizen.domain.MySharedPreferences;
import com.becitizen.app.becitizen.domain.entities.CategoryThread;
import com.becitizen.app.becitizen.exceptions.ServerException;
import com.becitizen.app.becitizen.exceptions.SharedPreferencesException;

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

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (ControllerUserDomain.getUniqueInstance().isLogged()) {
                        try {
                            ControllerThreadPresentation.getUniqueInstance().newThread(null);
                        } catch (ServerException e) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    } else throw new SharedPreferencesException("User not logged in");
                } catch (SharedPreferencesException e) {
                    Toast toast = Toast.makeText(getApplicationContext(), "You have to be logged in", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        adapter = new CategoryThreadAdapter(dataModels, getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CategoryThread thread = dataModels.get(position);
                ThreadActivity threadActivity = new ThreadActivity();
                threadActivity.setThreadId(thread.getId());
                Fragment fragment = threadActivity;
                fragmentTransaction(fragment, "THREAD_ACTIVITY");
            }
        });

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        dataModels = ControllerThreadPresentation.getUniqueInstance().getThreadsCategory(category);
    }

    private void fragmentTransaction(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

}