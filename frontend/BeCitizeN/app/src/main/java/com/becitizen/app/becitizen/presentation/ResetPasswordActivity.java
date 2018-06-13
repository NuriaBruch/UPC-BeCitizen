package com.becitizen.app.becitizen.presentation;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.controllers.ControllerUserDomain;
import com.becitizen.app.becitizen.exceptions.ServerException;

import org.json.JSONException;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText emailView;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        emailView = (TextInputEditText) findViewById(R.id.resetEmail);
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.resetButton:
                resetPassword();
        }
    }

    private void resetPassword() {
        email = emailView.getText().toString();

        if (email.trim().isEmpty()) {
            // Verify email is not empty

            emailView.setError(getString(R.string.errorMsgName));
            requestFocus(emailView);

        } else if (email.contains("@")) {
            // Verify email contains @

            emailView.setError(getString(R.string.errorMsgName));
            requestFocus(emailView);
        } else {
            // Ask confirmation
            askConfirmation();
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void askConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.resetPassword))
                .setMessage(getResources().getString(R.string.resetExplanation))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {
                            ControllerUserDomain.getUniqueInstance().resetPassword(email);

                            Toast.makeText(getApplicationContext(), R.string.resetSuccessfully, Toast.LENGTH_LONG).show();

                        } catch (ServerException e) {
                            if (e.getMessage().equalsIgnoreCase("User not found"))
                                emailView.setError(getString(R.string.noUserWithEmail));
                            else Toast.makeText(getApplicationContext(), R.string.serverError, Toast.LENGTH_LONG).show();
                            requestFocus(emailView);

                            Log.e("ServerE", e.getMessage());

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), R.string.JSONerror, Toast.LENGTH_LONG).show();

                            Log.e("ServerE", e.getMessage());

                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }
}

