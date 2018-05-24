package com.becitizen.app.becitizen.presentation;

import android.accounts.NetworkErrorException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.ControllerUserDomain;
import com.becitizen.app.becitizen.domain.entities.CategoryThread;
import com.becitizen.app.becitizen.exceptions.SharedPreferencesException;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CategoryThreadActivity extends Fragment  {

    private View rootView;
    boolean sortedByVotes;
    ArrayList<CategoryThread> dataModels;
    ArrayList<CategoryThread> dataChunk;
    private int block = -1;
    ListView listView;
    private int preLast;
    private static CategoryThreadAdapter adapter;
    private String category = "";
    private Thread threadLoadThreads;
    private Handler UIUpdater = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            adapter.addAll(dataChunk);
            progressBar.setVisibility(View.GONE);
            if (msg.what ==0 && adapter.getCount() == 0) {
                Toast toast = Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.empty), Toast.LENGTH_SHORT);
                toast.show();
            } else if (msg.what == 1) {
                Toast toast = Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.networkError), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    };
    private ProgressBar progressBar;

    private Runnable loadThreads = new Runnable() {
        public void run() {
            ++block;
            try {
                dataChunk = ControllerThreadPresentation.getUniqueInstance().getThreadsCategory(category, block, sortedByVotes);
                UIUpdater.sendEmptyMessage(0);
            } catch (NetworkErrorException e) {
                UIUpdater.sendEmptyMessage(1);
            }
        }
    };

    public CategoryThreadActivity() {
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_category_thread, container, false);

        sortedByVotes = false;

        listView = (ListView)rootView.findViewById(R.id.list);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);

        TextView categoryText = rootView.findViewById(R.id.categoryText);
        categoryText.setText(category);

        dataModels = new ArrayList<CategoryThread>();

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (ControllerUserDomain.getUniqueInstance().isLogged()) {
                        NewThreadActivity fragment = new NewThreadActivity();
                        fragment.setCategory(category);
                        fragmentTransaction(fragment, "CATEGORY_THREAD_ACTIVITY");
                    } else throw new SharedPreferencesException("User not logged in");
                } catch (SharedPreferencesException e) {
                    Toast toast = Toast.makeText(getApplicationContext(), "You have to be logged in", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        ImageButton sortButton = rootView.findViewById(R.id.threadsSortButton);
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortedByVotes = !sortedByVotes;
                block = -1;
                preLast = 0;
                adapter.clear();
                if (threadLoadThreads != null && threadLoadThreads.isAlive())
                    threadLoadThreads.interrupt();
                threadLoadThreads = new Thread(loadThreads);
                threadLoadThreads.start();

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
                        if (lastItem == totalItemCount) {
                            if(preLast!=lastItem) {
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