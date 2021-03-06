package com.becitizen.app.becitizen.presentation.user;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.exceptions.ServerException;
import com.becitizen.app.becitizen.presentation.controllers.ControllerUserPresentation;

import org.json.JSONException;

public class ChangePasswordActivity extends Fragment  implements View.OnClickListener {

    View rootView;
    TextInputEditText oldPassword, newPassword, reNewPassword;
    Button send;

    public ChangePasswordActivity() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_change_password, container, false);

        oldPassword = (TextInputEditText) rootView.findViewById(R.id.oldPassword);
        newPassword = (TextInputEditText) rootView.findViewById(R.id.newPassword);
        reNewPassword = (TextInputEditText) rootView.findViewById(R.id.reNewPassword);
        send = (Button) rootView.findViewById(R.id.newPasswordButton);
        send.setOnClickListener(this);

        oldPassword.setTransformationMethod(new PasswordTransformationMethod());
        newPassword.setTransformationMethod(new PasswordTransformationMethod());
        reNewPassword.setTransformationMethod(new PasswordTransformationMethod());
        oldPassword.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        newPassword.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        reNewPassword.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

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
        } if (newPassword.getText().toString().trim().length() < 6) {
            Toast.makeText(getContext(), getResources().getString(R.string.errorPasswlenght), Toast.LENGTH_LONG).show();
        } else if (!newPassword.getText().toString().trim().equals(reNewPassword.getText().toString().trim())) {
            Toast.makeText(getContext(), getResources().getString(R.string.passDoNotMatch), Toast.LENGTH_LONG).show();
        } else {
            try {
                ControllerUserPresentation.getUniqueInstance().newPassword(
                        oldPassword.getText().toString().trim(),
                        newPassword.getText().toString().trim()
                );
                Toast.makeText(getContext(), getResources().getString(R.string.passUpdated), Toast.LENGTH_LONG).show();
                hideKeyboard();
                getActivity().onBackPressed();

            } catch (ServerException e) {
                if (e.getMessage().equals("User password doesn't match")) Toast.makeText(getContext(), getResources().getString(R.string.errorIncorrPassw), Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(getContext(), getResources().getString(R.string.serverError), Toast.LENGTH_LONG).show();
                    Log.e("ServerE", e.getMessage());
                }
            } catch (JSONException e) {
                Toast.makeText(getContext(), getResources().getString(R.string.JSONerror), Toast.LENGTH_LONG).show();
                Log.e("JSONe", e.getMessage());
            }
        }
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
