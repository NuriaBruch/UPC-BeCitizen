package com.becitizen.app.becitizen.presentation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.becitizen.app.becitizen.R;

import java.util.ArrayList;
import java.util.List;

public class NewThreadActivity extends Fragment {

    private View rootView;

    private String[] categories;

    public NewThreadActivity() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_new_thread, container, false);

        //TODO: Do not have categories hardcoded.
        List<String> categories = new ArrayList<>();
        categories.add("Justice");
        categories.add("Test");
        categories.add("Education");
        //categories = ControllerThreadPresentation.getUniqueInstance().getCategories();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.row_forum_category,
                categories
        );

        Spinner spinner = (Spinner) rootView.findViewById(R.id.newThreadCategorySpinner);
        spinner.setAdapter(adapter);

        return rootView;
    }
}
