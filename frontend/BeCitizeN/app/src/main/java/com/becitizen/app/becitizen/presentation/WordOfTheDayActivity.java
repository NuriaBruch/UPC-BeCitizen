package com.becitizen.app.becitizen.presentation;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.exceptions.ServerException;

import org.json.JSONException;

public class WordOfTheDayActivity extends Fragment {

    private View rootView;

    private TextView wordToTranslate;
    private TextView wordTranslated;

    private ControllerUtilitiesPresentation controllerUtilitiesPresentation;

    public WordOfTheDayActivity() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_word_of_the_day, container, false);

        wordToTranslate = rootView.findViewById(R.id.wordToTranslateText);
        wordTranslated = rootView.findViewById(R.id.wordTranslatedText);

        controllerUtilitiesPresentation = ControllerUtilitiesPresentation.getInstance();

        try {
            String[] words = controllerUtilitiesPresentation.getWordOfTheDay();

            wordToTranslate.setText(words[0]);
            wordTranslated.setText(words[1]);

        } catch (JSONException e) {
            Toast.makeText(rootView.getContext(), "JSON error", Toast.LENGTH_LONG).show();
        } catch (ServerException e) {
            Toast.makeText(rootView.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return rootView;
    }
}
