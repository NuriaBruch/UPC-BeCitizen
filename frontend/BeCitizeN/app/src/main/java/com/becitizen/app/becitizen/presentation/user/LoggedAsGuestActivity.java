package com.becitizen.app.becitizen.presentation.user;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.presentation.controllers.ControllerUserPresentation;

public class LoggedAsGuestActivity extends Fragment {

    private View rootView;

    public LoggedAsGuestActivity() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_logged_as_guest, container, false);

        try {
            if(!ControllerUserPresentation.getUniqueInstance().isLogged()) goToLogin();
            else if (!ControllerUserPresentation.getUniqueInstance().isLoggedAsGuest()) getActivity().onBackPressed();
        } catch (Exception e) {
            e.printStackTrace();
            goToLogin();
        }

        final Button logoutButton = (Button) rootView.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ControllerUserPresentation.getUniqueInstance().logout();
                showToast(getResources().getString(R.string.logout));
                goToLogin();
            }
        });

        return rootView;
    }

    private void showToast(String text) {
        Toast.makeText(rootView.getContext(), text, Toast.LENGTH_LONG).show();
    }

    private void goToLogin() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

}
