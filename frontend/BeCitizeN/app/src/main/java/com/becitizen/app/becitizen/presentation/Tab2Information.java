package com.becitizen.app.becitizen.presentation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.becitizen.app.becitizen.R;

/**
 * Created by Nuria on 18/05/2018.
 */

public class Tab2Information extends Fragment{

    private View rootView;

    public Tab2Information() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab2_information, container, false);

        return rootView;
    }
}
