package com.becitizen.app.becitizen.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;

import com.becitizen.app.becitizen.R;

import static android.util.Patterns.EMAIL_ADDRESS;


public class MailLoginActivity extends AppCompatActivity {

    private ControllerUserPresentation controllerUserPresentation;

    private TextInputEditText tietMail;
    private TextInputEditText tietPassw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        controllerUserPresentation = ControllerUserPresentation.getUniqueInstance();

        tietMail = findViewById(R.id.tietMail);
        tietPassw = findViewById(R.id.tietPassw);
        tietPassw.setTransformationMethod(new PasswordTransformationMethod());

    }

    public void logInMail(View view) {
        if (!validateEmail()) return;
        if (!validatePassw()) return;

        String email = tietMail.getText().toString().trim();
        boolean i = controllerUserPresentation.checkCredentials(email, tietPassw.getText().toString().trim());

        if(i) {
            Intent intent = new Intent(this, InsideActivity.class);
            startActivity(intent);
        }

        else {
            if (controllerUserPresentation.existsMail(email)) {
                tietPassw.setError(getString(R.string.errorIncorrPassw));
                requestFocus(tietPassw);
            }

            else {
                tietMail.setError(getString(R.string.errorIncorrEmail));
                requestFocus(tietMail);
            }
        }

    }

    private boolean validateEmail() {
        String email = tietMail.getText().toString();

        if (email.trim().isEmpty()) {
            tietMail.setError(getString(R.string.errorMsgName));
            requestFocus(tietMail);
            return false;
        }

        return true;
    }

    private boolean validatePassw() {
        String pass = tietPassw.getText().toString();

        if (pass.trim().isEmpty()) {
            tietPassw.setError(getString(R.string.errorMsgName));
            requestFocus(tietPassw);
            return false;
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
