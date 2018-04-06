package com.becitizen.app.becitizen.presentation;

import android.app.DatePickerDialog;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

public class DataRegisterView extends AppCompatActivity implements View.OnClickListener {
    TextInputEditText birthDateInput;
    Button submitButton;
    Calendar myCalendar = Calendar.getInstance();
    int day = myCalendar.get(Calendar.DAY_OF_MONTH);
    int month = myCalendar.get(Calendar.MONTH);
    int year = myCalendar.get(Calendar.YEAR);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_register_view);

        birthDateInput = findViewById(R.id.birthDateInput);
        birthDateInput.setOnClickListener(this);

        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.birthDateInput:
                obtainDate();
                break;
            case R.id.submitButton:
                submitData();
                break;
        }
    }

    private void obtainDate() {
        DatePickerDialog birthDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int m = month + 1; // starts with 0
                String formattedDay = (dayOfMonth < 10)? "0" + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                String formattedMonth = (month < 10)? "0" + String.valueOf(month):String.valueOf(month);
                birthDateInput.setText(formattedDay + "/" + formattedMonth + "/" + year);

            }
        }, year, month, day);
        birthDatePicker.show();
    }

    private void submitData() {
        if(!validateUsername())
            return;
        if (!validateFirstName())
            return;
        if (!validateLastName())
            return;
        if (!validateBirthDate()) {
            return;
        }

        Toast.makeText(getApplicationContext(), "You submitted your data", Toast.LENGTH_SHORT).show();
    }

    private boolean validateUsername() {
        TextInputEditText username = findViewById(R.id.usernameInput);
        if (username.getText().toString().trim().isEmpty()) {
            username.setError(getString(R.string.errorMsgName));
            requestFocus(username);
            return false;
        }
        else {
            TextInputLayout usernameLayout  = findViewById(R.id.usernameLayout);
            usernameLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateFirstName() {
        TextInputEditText firstName = findViewById(R.id.firstNameInput);
        if (firstName.getText().toString().trim().isEmpty()) {
            firstName.setError(getString(R.string.errorMsgName));
            requestFocus(firstName);
            return false;
        }
        else {
            TextInputLayout firstNameLayout  = findViewById(R.id.firstNameLayout);
            firstNameLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateLastName() {
        TextInputEditText lastName = findViewById(R.id.lastNameInput);
        if (lastName.getText().toString().trim().isEmpty()) {
            lastName.setError(getString(R.string.errorMsgName));
            requestFocus(lastName);
            return false;
        }
        else {
            TextInputLayout lastNameLayout  = findViewById(R.id.lastNameLayout);
            lastNameLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateBirthDate() {
        TextInputEditText birthDate = findViewById(R.id.birthDateInput);
        if (birthDate.getText().toString().trim().isEmpty()) {
            birthDate.setError(getString(R.string.errorMsgName));
            return false;
        }
        else {
            TextInputLayout birthDateLayout  = findViewById(R.id.birthDateLayout);
            birthDateLayout.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


}
