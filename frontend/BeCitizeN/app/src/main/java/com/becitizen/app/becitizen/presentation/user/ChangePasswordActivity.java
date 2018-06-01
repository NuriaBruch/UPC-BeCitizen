package com.becitizen.app.becitizen.presentation.user;

import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.presentation.controllers.ControllerUserPresentation;

public class ChangePasswordActivity extends Fragment  implements View.OnClickListener {

    View rootView;
    TextInputEditText oldPassword, newPassword, reNewPassword;
    Button send;

    public ChangePasswordActivity() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_user_profile, container, false);

        oldPassword = (TextInputEditText) rootView.findViewById(R.id.oldPassword);
        newPassword = (TextInputEditText) rootView.findViewById(R.id.newPassword);
        reNewPassword = (TextInputEditText) rootView.findViewById(R.id.reNewPassword);
        send = (Button) rootView.findViewById(R.id.newPasswordButton);
        send.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.newPasswordButton:
                newPassword();
                break;
        }
    }

    private void newPassword() {

        if (oldPassword.getText().toString().trim().isEmpty() ||
                newPassword.getText().toString().trim().isEmpty() ||
                 reNewPassword.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), getResources().getString(R.string.allFieldsRequired), Toast.LENGTH_LONG).show();
        }
        else if (!newPassword.getText().toString().trim().equals(reNewPassword.getText().toString().trim())) {
            Toast.makeText(getContext(), getResources().getString(R.string.passDoNotMatch), Toast.LENGTH_LONG).show();
        } else {
            ControllerUserPresentation.getUniqueInstance().newPassword(
                    oldPassword.getText().toString().trim(),
                    newPassword.getText().toString().trim()
            );
            Toast.makeText(getContext(), getResources().getString(R.string.passUpdated), Toast.LENGTH_LONG).show();
        }
    }
}
