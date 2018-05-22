package com.becitizen.app.becitizen.presentation;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.ControllerUserDomain;
import com.becitizen.app.becitizen.domain.entities.CategoryThread;
import com.becitizen.app.becitizen.domain.entities.Information;
import com.becitizen.app.becitizen.exceptions.SharedPreferencesException;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CategoryInformationActivity extends Fragment implements CategoryInformationAdapter.ItemClickListener {

    private View rootView;
    List<Information> dataModels;
    ArrayList<Information> dataChunk;
    private static CategoryInformationAdapter adapter;
    private String category = "";
    private Thread threadLoadInformations;

    private Handler UIUpdater = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dataModels.addAll(dataChunk);
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
            if (dataModels.size() == 0) {
                Toast toast = Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    };
    private ProgressBar progressBar;

    private Runnable loadInformations = new Runnable() {
        public void run() {
            dataChunk = ControllerInformationPresentation.getUniqueInstance().getInformationsCategory(category);
            UIUpdater.sendEmptyMessage(0);
        }
    };

    public CategoryInformationActivity() {
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_category_information, container, false);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);

        dataModels = new ArrayList<Information>();

        progressBar.setVisibility(View.VISIBLE);
        if (threadLoadInformations != null && threadLoadInformations.isAlive())
            threadLoadInformations.interrupt();
        threadLoadInformations = new Thread(loadInformations);
        threadLoadInformations.start();

        RecyclerView recyclerView = rootView.findViewById(R.id.rvInfomation);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CategoryInformationAdapter(getContext(), dataModels);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        /* CODE FOR GETTING MORE DATA ONCE USER REACHED END OF LIST
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    progressBar.setVisibility(View.VISIBLE);
                    if (threadLoadInformations != null && threadLoadInformations.isAlive())
                        threadLoadInformations.interrupt();
                    threadLoadInformations = new Thread(loadInformations);
                    threadLoadInformations.start();
                }
            }
        });*/

        return rootView;
    }

    /*@Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        if (threadLoadThreads != null && threadLoadThreads.isAlive())
            threadLoadThreads.interrupt();
        threadLoadThreads = new Thread(loadThreads);
        threadLoadThreads.start();
    }*/

    @Override
    public void onPause() {
        super.onPause();
        if (threadLoadInformations != null && threadLoadInformations.isAlive())
            threadLoadInformations.interrupt();
    }

    private void fragmentTransaction(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}