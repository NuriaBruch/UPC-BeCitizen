package com.becitizen.app.becitizen.presentation;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
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
import com.becitizen.app.becitizen.exceptions.SharedPreferencesException;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CategoryInformationActivity extends Fragment  {

    private View rootView;
    ArrayList<CategoryThread> dataModels;
    ArrayList<CategoryThread> dataChunk;
    private int block = -1;
    ListView listView;
    private int preLast;
    private static CategoryInformationAdapter adapter;
    private String category = "";
    private Thread threadLoadThreads;

    private Handler UIUpdater = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            adapter.addAll(dataChunk);
            progressBar.setVisibility(View.GONE);
            if (adapter.getCount() == 0) {
                Toast toast = Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    };
    private ProgressBar progressBar;

    private Runnable loadThreads = new Runnable() {
        public void run() {
            ++block;
            dataChunk = ControllerInformationPresentation.getUniqueInstance().getInformationsCategory(category, block);
            for (int i = 0; i < 10; ++i)
                dataChunk.add(new CategoryThread("title", "asdasdsadas", "", 0, 0));
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

        rootView = inflater.inflate(R.layout.activity_category_thread, container, false);

        listView = (ListView)rootView.findViewById(R.id.list);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);

        dataModels = new ArrayList<CategoryThread>();

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        adapter = new CategoryInformationAdapter(dataModels, getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                switch(absListView.getId())
                {
                    case R.id.list:
                        // Sample calculation to determine if the last
                        // item is fully visible.
                        final int lastItem = firstVisibleItem + visibleItemCount;
                        if(lastItem == totalItemCount)
                        {
                            if(preLast!=lastItem)
                            {
                                preLast = lastItem;
                                progressBar.setVisibility(View.VISIBLE);
                                if (threadLoadThreads != null && threadLoadThreads.isAlive())
                                    threadLoadThreads.interrupt();
                                threadLoadThreads = new Thread(loadThreads);
                                threadLoadThreads.start();
                            }
                        }
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        if (threadLoadThreads != null && threadLoadThreads.isAlive())
            threadLoadThreads.interrupt();
        threadLoadThreads = new Thread(loadThreads);
        threadLoadThreads.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        block = -1;
        preLast = 0;
        adapter.clear();
        if (threadLoadThreads != null && threadLoadThreads.isAlive())
            threadLoadThreads.interrupt();
    }

    private void fragmentTransaction(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

}