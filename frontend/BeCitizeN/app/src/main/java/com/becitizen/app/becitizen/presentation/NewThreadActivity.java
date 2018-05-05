package com.becitizen.app.becitizen.presentation;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.entities.Thread;
import com.becitizen.app.becitizen.exceptions.ServerException;

import java.util.ArrayList;
import java.util.List;

public class NewThreadActivity extends Fragment implements View.OnClickListener {

    private View rootView;

    TextInputEditText titleEditText;
    TextInputEditText contentEditText;
    Spinner spinner;
    Button newThreadSubmit;

    List<String> categories = new ArrayList<>();

    public NewThreadActivity() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_new_thread, container, false);

        titleEditText = (TextInputEditText) rootView.findViewById(R.id.newThreadTitle);
        contentEditText = (TextInputEditText) rootView.findViewById(R.id.newThreadContent);
        spinner = (Spinner) rootView.findViewById(R.id.newThreadCategorySpinner);
        newThreadSubmit = (Button) rootView.findViewById(R.id.newThreadSubmit);

        newThreadSubmit.setOnClickListener(this);
        
        categories = ControllerThreadPresentation.getUniqueInstance().getCategories();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.row_forum_category, categories);

        spinner.setAdapter(adapter);

        return rootView;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.newThreadSubmit:
                newThread();
                break;
        }
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
