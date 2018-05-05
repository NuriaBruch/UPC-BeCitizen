package com.becitizen.app.becitizen.presentation;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.entities.Thread;
import com.becitizen.app.becitizen.exceptions.ServerException;

import java.util.ArrayList;
import java.util.List;

public class NewThreadActivity extends Fragment {

    private View rootView;

    TextInputEditText titleEditText;
    TextInputEditText contentEditText;
    Spinner spinner;

    private String[] categories;

    public NewThreadActivity() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_new_thread, container, false);

        titleEditText = (TextInputEditText) rootView.findViewById(R.id.usernameInput);
        contentEditText = (TextInputEditText) rootView.findViewById(R.id.firstNameInput);
        spinner = (Spinner) rootView.findViewById(R.id.newThreadCategorySpinner);

        //TODO: Do not have categories hardcoded.
        List<String> categories = new ArrayList<>();
        categories.add("Justice");
        categories.add("Test");
        categories.add("Education");
        //categories = ControllerThreadPresentation.getUniqueInstance().getCategories();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.row_forum_category, categories);

        spinner.setAdapter(adapter);

        return rootView;
    }

    public void newThread() {
        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();
        String category = spinner.getSelectedItem().toString();

        Thread t = new Thread(title, content, category);

        try {
            ControllerThreadPresentation.getUniqueInstance().newThread(t);

        } catch (ServerException e) {
            Toast.makeText(rootView.getContext(), getResources().getText(R.string.serverError), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
