package com.becitizen.app.becitizen.presentation.faq;

import android.accounts.NetworkErrorException;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
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
    private SwipeRefreshLayout srl;
    private static CategoryFaqAdapter adapter;
    private String category = "";
    private Thread threadLoadFaqs;
    private String searchWords = "";
    private boolean searching = false;

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
            srl.setRefreshing(false);
        }
    };
    private ProgressBar progressBar;

    private Runnable loadFaqs = new Runnable() {
        public void run() {
            try {
                if (searchWords.equals(""))
                    dataChunk = ControllerFaqPresentation.getUniqueInstance().getFaqsCategory(category);
                else
                    dataChunk = ControllerFaqPresentation.getUniqueInstance().getFaqsCategoryWord(category, searchWords);
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
        ControllerFaqPresentation.getUniqueInstance().setContext(getApplicationContext());

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);

        dataModels = new ArrayList<FaqEntry>();

        ImageButton searchButton = rootView.findViewById(R.id.threadsSearchButton);
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
                            searching = true;
                            dataModels.clear();
                            if (threadLoadFaqs != null && threadLoadFaqs.isAlive())
                                threadLoadFaqs.interrupt();
                            threadLoadFaqs = new Thread(loadFaqs);
                            threadLoadFaqs.start();
                            dialog.dismiss();
                        }
                    }
                });

            }
        });

        srl = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        srl.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Runnable loadCategories = new Runnable() {
                            public void run() {
                                try {
                                    ControllerFaqPresentation.getUniqueInstance().getCategoriesForceRefresh();
                                    dataModels.clear();
                                    if (threadLoadFaqs != null && threadLoadFaqs.isAlive())
                                        threadLoadFaqs.interrupt();
                                    threadLoadFaqs = new Thread(loadFaqs);
                                    threadLoadFaqs.start();
                                } catch (NetworkErrorException e) {
                                    UIUpdater.sendEmptyMessage(1);
                                }
                            }
                        };

                        Thread threadLoadCategories = new Thread(loadCategories);
                        threadLoadCategories.start();
                    }
                }
        );

        progressBar.setVisibility(View.VISIBLE);
        if (threadLoadFaqs != null && threadLoadFaqs.isAlive())
            threadLoadFaqs.interrupt();
        threadLoadFaqs = new Thread(loadFaqs);
        threadLoadFaqs.start();

        RecyclerView recyclerView = rootView.findViewById(R.id.rvFaq);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CategoryFaqAdapter(getContext(), dataModels);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (threadLoadFaqs != null && threadLoadFaqs.isAlive())
            threadLoadFaqs.interrupt();
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