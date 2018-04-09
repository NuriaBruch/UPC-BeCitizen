package com.becitizen.app.becitizen.presentation;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;

import java.util.regex.Pattern;

import static android.util.Patterns.EMAIL_ADDRESS;

public class ViewSignUpMail extends AppCompatActivity {

    private ControllerUserPresentation controllerUserPresentation;

    private TextInputEditText tietMail;
    private TextInputEditText tietPassw;
    private TextInputEditText tietPassw2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sign_up_mail);

        controllerUserPresentation = ControllerUserPresentation.getUniqueInstance();

        tietMail = findViewById(R.id.tietMail);
        tietPassw = findViewById(R.id.tietPassw);
        tietPassw.setTransformationMethod(new PasswordTransformationMethod());
        tietPassw2 = findViewById(R.id.tietPassw2);
        tietPassw2.setTransformationMethod(new PasswordTransformationMethod());
    }

    public void signUp(View view) {
        if (!validateEmail()) return;
        if (!validatePassw()) return;
        if (!validatePassw2()) return;

        controllerUserPresentation.createUser(tietMail.getText().toString(), tietPassw.getText().toString());
        Intent i = new Intent(this,DataRegisterView.class);
        startActivity(i);

    }

    private boolean validateEmail() {
        String email = tietMail.getText().toString();

        if (email.trim().isEmpty() || !EMAIL_ADDRESS.matcher(email).matches()) {
            tietMail.setError(getString(R.string.errorMsgName));
            requestFocus(tietMail);
            return false;
        }

        else if (controllerUserPresentation.existsMail(email)) {
            tietMail.setError(getString(R.string.errorMail));
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

    private boolean validatePassw2() {
        String pass = tietPassw.getText().toString();
        String pass2 = tietPassw2.getText().toString();

        if (pass2.trim().isEmpty()) {
            tietPassw2.setError(getString(R.string.errorMsgName));
            requestFocus(tietPassw2);
            return false;
        }

        else if (!pass2.equals(pass)) {
            tietPassw2.setError(getString(R.string.errorPassw));
            requestFocus(tietPassw2);
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
