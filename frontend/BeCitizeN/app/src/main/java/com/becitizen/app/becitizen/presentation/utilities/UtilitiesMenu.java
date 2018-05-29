package com.becitizen.app.becitizen.presentation.utilities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;

public class UtilitiesMenu extends Fragment implements View.OnClickListener {

    private View rootView;

    private CardView cvCurrency;
    private CardView cvTranslator;
    private CardView cvWord;

    public UtilitiesMenu() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_utilities_menu, container, false);

        cvCurrency = rootView.findViewById(R.id.cvCurrency);
        cvTranslator = rootView.findViewById(R.id.cvTranslator);
        cvWord = rootView.findViewById(R.id.cvWord);

        cvCurrency.setOnClickListener(this);
        cvTranslator.setOnClickListener(this);
        cvWord.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cvCurrency:
                Fragment fragment = new CurrencyConverterActivity();
                fragmentTransaction(fragment, "CURRENCY_CONVERTER");
                break;

            case R.id.cvTranslator:
                Toast.makeText(rootView.getContext(), "Translator not implemented yet", Toast.LENGTH_LONG).show();
                break;

            case R.id.cvWord:
                Fragment fragmentWord = new WordOfTheDayActivity();
                fragmentTransaction(fragmentWord, "WORD_OF_THE_DAY");
                break;
        }
    }

    private void fragmentTransaction(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }
}
