package com.becitizen.app.becitizen.presentation.info;

import android.accounts.NetworkErrorException;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.entities.Information;
import com.becitizen.app.becitizen.presentation.controllers.ControllerInformationPresentation;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CategoryInformationActivity extends Fragment implements CategoryInformationAdapter.ItemClickListener {

    private View rootView;
    List<Information> dataModels;
    ArrayList<Information> dataChunk;
    private static CategoryInformationAdapter adapter;
    private String category = "";
    private String searchWords = "";
    private Thread threadLoadInformations;

    private Handler UIUpdater = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dataModels.addAll(dataChunk);
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
            if (msg.what ==0 && dataModels.size() == 0) {
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
                dataChunk = ControllerInformationPresentation.getUniqueInstance().getInformationsCategory(category);
                UIUpdater.sendEmptyMessage(0);
            } catch (NetworkErrorException e) {
                UIUpdater.sendEmptyMessage(1);
            }
        }
    };

    private Runnable loadInformationsSearch = new Runnable() {
        public void run() {
            try {
                dataChunk = ControllerInformationPresentation.getUniqueInstance().getInformationsCategorySearch(category, searchWords);
                UIUpdater.sendEmptyMessage(0);
            } catch (NetworkErrorException e) {
                UIUpdater.sendEmptyMessage(1);
            }
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

        TextView categoryText = rootView.findViewById(R.id.categoryText);
        categoryText.setText(category);


        dataModels = new ArrayList<Information>();

        progressBar.setVisibility(View.VISIBLE);
        if (threadLoadInformations != null && threadLoadInformations.isAlive())
            threadLoadInformations.interrupt();
        threadLoadInformations = new Thread(loadInformations);
        threadLoadInformations.start();


        ImageButton searchButton = rootView.findViewById(R.id.infoSearchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(rootView.getContext());
                dialog.setContentView(R.layout.category_thread_search);

                TextView searchCategory = dialog.findViewById(R.id.search_name_text);
                searchCategory.setText(category);

                Button searchButton = dialog.findViewById(R.id.search_button);

                final EditText wordsToSearch = dialog.findViewById(R.id.search_edit_text);

                dialog.show();

                searchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        searchWords = wordsToSearch.getText().toString().trim();
                        if (searchWords.isEmpty())
                            Toast.makeText(dialog.getContext(), R.string.searchCategoryEmptyText, Toast.LENGTH_LONG).show();
                        else {
                            dataModels.clear();
                            progressBar.setVisibility(View.VISIBLE);
                            if (threadLoadInformations != null && threadLoadInformations.isAlive())
                                threadLoadInformations.interrupt();
                            threadLoadInformations = new Thread(loadInformationsSearch);
                            threadLoadInformations.start();
                            dialog.dismiss();
                        }
                    }
                });

            }
        });



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