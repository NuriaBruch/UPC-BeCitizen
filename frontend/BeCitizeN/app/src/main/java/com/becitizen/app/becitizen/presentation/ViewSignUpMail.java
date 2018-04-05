package com.becitizen.app.becitizen.presentation;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;

import java.util.regex.Pattern;

public class ViewSignUpMail extends AppCompatActivity /*implements View.OnClickListener*/ {

    private TextInputEditText tietMail;
    private TextInputEditText tietPassw;
    private TextInputEditText tietPassw2;


    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sign_up_mail);

        tietMail = findViewById(R.id.tietMail);
        tietPassw = findViewById(R.id.tietPassw);
        tietPassw2 = findViewById(R.id.tietPassw2);
    }

    public void signUp(View view) {
        if (!validateEmail()) return;
        if (!validatePassw()) return;
        if (!validatePassw2()) return;

        Toast notificacion = Toast.makeText(this,"You subbmitted your data" ,Toast.LENGTH_SHORT);
        notificacion.show();

        Intent i=new Intent(this,DataRegisterView.class);
        i.putExtra("email", tietMail.getText().toString());
        i.putExtra("password", tietPassw.getText().toString());
        startActivity(i);

    }

    private boolean validateEmail() {
        String email = tietMail.getText().toString();
        boolean b = true;

        if (email.trim().isEmpty() || !EMAIL_ADDRESS_PATTERN.matcher(email).matches()) {
            tietMail.setError(getString(R.string.errorMsgName));
            requestFocus(tietMail);
            b = false;
        }

        return b;
    }

    private boolean validatePassw() {
        String pass = tietPassw.getText().toString();
        boolean b = true;

        if (pass.trim().isEmpty()) {
            tietPassw.setError(getString(R.string.errorMsgName));
            requestFocus(tietPassw);
            b = false;
        }

        return b;
    }

    private boolean validatePassw2() {
        String pass = tietPassw.getText().toString();
        String pass2 = tietPassw2.getText().toString();
        boolean b = true;

        if (pass2.trim().isEmpty() || !pass2.equals(pass)) {
            tietPassw2.setError(getString(R.string.errorMsgName));
            requestFocus(tietPassw2);
            b = false;
        }

        return b;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
