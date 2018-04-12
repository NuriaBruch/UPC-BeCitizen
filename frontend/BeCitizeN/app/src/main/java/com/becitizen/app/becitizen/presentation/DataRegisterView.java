package com.becitizen.app.becitizen.presentation;

import android.app.DatePickerDialog;
import android.content.Intent;
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
    TextInputEditText usernameInput;
    TextInputEditText lastNameInput;
    Spinner countryInput;
    TextInputEditText firstNameInput;
    ControllerUserPresentation controllerUserPresentation;
    Button submitButton;
    Calendar myCalendar = Calendar.getInstance();
    int day = myCalendar.get(Calendar.DAY_OF_MONTH);
    int month = myCalendar.get(Calendar.MONTH);
    int year = myCalendar.get(Calendar.YEAR);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_register_view);

        controllerUserPresentation = ControllerUserPresentation.getUniqueInstance();

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

        countryInput = findViewById(R.id.countrySpinner);
        boolean result = controllerUserPresentation.registerData(
                usernameInput.getText().toString().trim(),
                firstNameInput.getText().toString().trim(),
                lastNameInput.getText().toString().trim(),
                birthDateInput.getText().toString().trim(),
                countryInput.getSelectedItem().toString().trim()
        );

        if (result) {
            Intent i = new Intent(this,InsideActivity.class);
            startActivity(i);
        }

        else {
            usernameInput.setError(getString(R.string.errorUsername));
            requestFocus(usernameInput);
        }
    }

    private boolean validateUsername() {
        usernameInput = findViewById(R.id.usernameInput);
        if (usernameInput.getText().toString().trim().isEmpty()) {
            usernameInput.setError(getString(R.string.errorMsgName));
            requestFocus(usernameInput);
            return false;
        }
        else {
            TextInputLayout usernameLayout  = findViewById(R.id.usernameLayout);
            usernameLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateFirstName() {
        firstNameInput = findViewById(R.id.firstNameInput);
        if (firstNameInput.getText().toString().trim().isEmpty()) {
            firstNameInput.setError(getString(R.string.errorMsgName));
            requestFocus(firstNameInput);
            return false;
        }
        else {
            TextInputLayout firstNameLayout  = findViewById(R.id.firstNameLayout);
            firstNameLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateLastName() {
        lastNameInput = findViewById(R.id.lastNameInput);
        if (lastNameInput.getText().toString().trim().isEmpty()) {
            lastNameInput.setError(getString(R.string.errorMsgName));
            requestFocus(lastNameInput);
            return false;
        }
        else {
            TextInputLayout lastNameLayout  = findViewById(R.id.lastNameLayout);
            lastNameLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateBirthDate() {
        birthDateInput = findViewById(R.id.birthDateInput);
        String date = birthDateInput.getText().toString().trim();
        String yearString = date.substring(6);
        if (date.isEmpty()) {
            birthDateInput.setError(getString(R.string.errorMsgName));
            return false;
        } else if (Integer.valueOf(yearString) > year - 18) {
            birthDateInput.setError(getString(R.string.errorMsgName));
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
