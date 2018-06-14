package com.becitizen.app.becitizen.presentation.utilities;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.presentation.controllers.ControllerUtilitiesPresentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class TranslatorActivity extends Fragment implements View.OnClickListener{

    private View rootView;

    EditText editTextToTranslate;
    TextView textViewTranslated;
    Spinner spinnerFrom, spinnerTo;
    LinkedList<String> languagesList;
    LinkedList<String> codesList;
    LinkedList<String> ordered;
    Button translateButton;

    ControllerUtilitiesPresentation controllerUtilitiesPresentation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_translator, container, false);

        controllerUtilitiesPresentation = ControllerUtilitiesPresentation.getInstance();

        languagesList = new LinkedList<>();
        codesList = new LinkedList<>();
        editTextToTranslate = rootView.findViewById(R.id.editTextToTranslate);
        textViewTranslated = rootView.findViewById(R.id.translated);
        spinnerFrom = rootView.findViewById(R.id.spinnerFromT);
        spinnerTo = rootView.findViewById(R.id.spinnerToT);
        translateButton = rootView.findViewById(R.id.translateButton);
        translateButton.setOnClickListener(this);

        getLanguagesList();

        ordered = (LinkedList)languagesList.clone();
        Collections.sort(ordered);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(rootView.getContext(), R.layout.row_forum_category, ordered);

        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        return rootView;
    }

    private void getLanguagesList() {
        try {
            controllerUtilitiesPresentation.getLanguagesList(languagesList, codesList);
        } catch (NetworkErrorException e) {
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.networkError), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.translateButton:
                doTranslation();
                break;
        }
    }

    private void doTranslation() {
        if(languagesList.size() <= 0) {
            Toast.makeText(rootView.getContext(), R.string.errorTranslate, Toast.LENGTH_SHORT).show();
        }

        else {
            String textFrom = editTextToTranslate.getText().toString();

            if (!textFrom.isEmpty()) {

                if (textFrom.length() > 250) {
                    Toast.makeText(rootView.getContext(), getApplicationContext().getResources().getString(R.string.networkError), Toast.LENGTH_SHORT ).show();
                }

                else {
                    String sFrom = spinnerFrom.getSelectedItem().toString();
                    String sTo = spinnerTo.getSelectedItem().toString();

                    int indexFrom = languagesList.indexOf(sFrom);
                    int indexTo = languagesList.indexOf(sTo);

                    String translateFrom = codesList.get(indexFrom);
                    String translateTo = codesList.get(indexTo);

                    try {
                        String resultado = controllerUtilitiesPresentation.getTranslation(translateFrom, translateTo, textFrom);
                        textViewTranslated.setText(resultado);
                    } catch (NetworkErrorException e) {
                        Toast.makeText(rootView.getContext(), getApplicationContext().getResources().getString(R.string.errorTranslator), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            else textViewTranslated.setText("");
        }

    }
}
