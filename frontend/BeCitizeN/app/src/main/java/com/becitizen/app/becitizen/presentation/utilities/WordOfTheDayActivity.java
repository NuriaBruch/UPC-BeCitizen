package com.becitizen.app.becitizen.presentation.utilities;

import android.accounts.NetworkErrorException;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.exceptions.ServerException;
import com.becitizen.app.becitizen.presentation.controllers.ControllerUtilitiesPresentation;

import org.json.JSONException;

import static com.facebook.FacebookSdk.getApplicationContext;

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
        } catch (NetworkErrorException e) {
            Toast toast = Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.networkError), Toast.LENGTH_SHORT);
            toast.show();
        }

        return rootView;
    }
}
