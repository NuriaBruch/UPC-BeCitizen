package com.becitizen.app.becitizen.presentation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;

import java.util.regex.Pattern;

public class ViewSignUpMail extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private EditText etPassword2;

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

        etEmail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);
        etPassword2 = (EditText)findViewById(R.id.etPassword2);
    }

    public void signUp(View view) {
        String email = etEmail.getText().toString();
        String pass = etPassword.getText().toString();
        String pass2 = etPassword2.getText().toString();

        if (email.isEmpty() || pass.isEmpty() || pass2.isEmpty()) {
            Toast notificacion = Toast.makeText(this,"It is obligatory to fill in all the fields" ,Toast.LENGTH_SHORT);
            notificacion.show();
        }

        else if (!EMAIL_ADDRESS_PATTERN.matcher(email).matches()) {
            Toast notificacion = Toast.makeText(this,"Enter a valid email" ,Toast.LENGTH_SHORT);
            notificacion.show();
        }

        else if (!pass.equals(pass2)) {
            Toast notificacion = Toast.makeText(this,"Passwords do not match" ,Toast.LENGTH_SHORT);
            notificacion.show();
        }

        else {
            // Aqui falta cridar a la vista de dades
        }

    }
}
