package com.becitizen.app.becitizen.presentation;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.becitizen.app.becitizen.R;

public class UtilitiesMenu extends Fragment {

    private View rootView;

    public UtilitiesMenu() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_utilities_menu, container, false);

        return rootView;
    }
}
