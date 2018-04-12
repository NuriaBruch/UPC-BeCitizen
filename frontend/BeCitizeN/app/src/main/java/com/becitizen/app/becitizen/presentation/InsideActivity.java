package com.becitizen.app.becitizen.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.ControllerUserDomain;
import com.becitizen.app.becitizen.domain.MySharedPreferences;

public class InsideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside);

        MySharedPreferences.init(this);

        try {
            if(!ControllerUserPresentation.getUniqueInstance().isLogged()) goToLogin();
            else {
                TextView loggedAs = (TextView) findViewById(R.id.textLoggedAs);
                String user = ControllerUserPresentation.getUniqueInstance().getLoggedUser();
                if(user.equals("guest")) user = getResources().getString(R.string.guest);
                loggedAs.setText(getResources().getString(R.string.loggedAs) + " " + user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            goToLogin();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logoutButton:
                ControllerUserPresentation.getUniqueInstance().logout();
                showToast(getResources().getString(R.string.logout));
                goToLogin();
                break;
        }
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private void goToLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

}
