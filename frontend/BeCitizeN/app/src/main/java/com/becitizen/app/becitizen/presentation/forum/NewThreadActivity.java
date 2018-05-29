package com.becitizen.app.becitizen.presentation.forum;

import android.accounts.NetworkErrorException;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.becitizen.app.becitizen.presentation.controllers.ControllerForumPresentation;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class NewThreadActivity extends Fragment implements View.OnClickListener {

    private View rootView;

    private TextInputEditText titleEditText;
    private TextInputEditText contentEditText;
    private Spinner spinner;
    private Button newThreadSubmit;

    private String default_category;

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

        try {
            categories = ControllerForumPresentation.getUniqueInstance().getCategories();
        } catch (NetworkErrorException e) {
            Toast toast = Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.networkError), Toast.LENGTH_SHORT);
            toast.show();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.row_forum_category, categories);

        spinner.setAdapter(adapter);

        int i = categories.indexOf(default_category);
        if (i >= 0) spinner.setSelection(i);

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
            ControllerForumPresentation.getUniqueInstance().newThread(t);

            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            getActivity().getSupportFragmentManager().popBackStack();
            transaction.remove(this);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            transaction.commit();

        } catch (ServerException e) {
            Toast.makeText(rootView.getContext(), getResources().getText(R.string.serverError), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (NetworkErrorException e) {
            Toast toast = Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.networkError), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void setCategory(String category) {
        this.default_category = category;
    }

    private void fragmentTransaction(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }
}
