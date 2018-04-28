package com.becitizen.app.becitizen.presentation;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.becitizen.app.becitizen.R;

public class UserProfile extends Fragment {

    private View rootView;

    public UserProfile() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_user_profile, container, false);
        //super.onCreate(savedInstanceState);

        return rootView;
    }
}
