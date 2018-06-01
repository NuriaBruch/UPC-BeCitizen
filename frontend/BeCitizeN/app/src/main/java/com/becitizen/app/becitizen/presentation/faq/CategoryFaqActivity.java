package com.becitizen.app.becitizen.presentation.faq;

import android.accounts.NetworkErrorException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.entities.FaqEntry;
import com.becitizen.app.becitizen.domain.entities.Information;
import com.becitizen.app.becitizen.presentation.controllers.ControllerFaqPresentation;
import com.becitizen.app.becitizen.presentation.controllers.ControllerInformationPresentation;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CategoryFaqActivity extends Fragment implements CategoryFaqAdapter.ItemClickListener {

    private View rootView;
    List<FaqEntry> dataModels;
    ArrayList<FaqEntry> dataChunk;
    private static CategoryFaqAdapter adapter;
    private String category = "";
    private Thread threadLoadInformations;

    private Handler UIUpdater = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dataModels.addAll(dataChunk);
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
            if (msg.what == 0 && dataModels.size() == 0) {
                Toast toast = Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.empty), Toast.LENGTH_SHORT);
                toast.show();
            } else if (msg.what == 1) {
                Toast toast = Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.networkError), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    };
    private ProgressBar progressBar;

    private Runnable loadInformations = new Runnable() {
        public void run() {
            try {
                dataChunk = ControllerFaqPresentation.getUniqueInstance().getFaqsCategory(category);
                UIUpdater.sendEmptyMessage(0);
            } catch (NetworkErrorException e) {
                UIUpdater.sendEmptyMessage(1);
            }
        }
    };

    public CategoryFaqActivity() {
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_category_faq, container, false);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);

        dataModels = new ArrayList<FaqEntry>();

        progressBar.setVisibility(View.VISIBLE);
        if (threadLoadInformations != null && threadLoadInformations.isAlive())
            threadLoadInformations.interrupt();
        threadLoadInformations = new Thread(loadInformations);
        threadLoadInformations.start();

        RecyclerView recyclerView = rootView.findViewById(R.id.rvFaq);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CategoryFaqAdapter(getContext(), dataModels);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

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