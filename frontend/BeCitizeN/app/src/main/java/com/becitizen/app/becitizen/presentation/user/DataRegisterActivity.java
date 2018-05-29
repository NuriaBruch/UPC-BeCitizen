package com.becitizen.app.becitizen.presentation.user;

import android.accounts.NetworkErrorException;
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
import com.becitizen.app.becitizen.exceptions.ServerException;
import com.becitizen.app.becitizen.presentation.SideMenuActivity;
import com.becitizen.app.becitizen.presentation.controllers.ControllerUserPresentation;

import java.util.Calendar;

public class DataRegisterActivity extends AppCompatActivity implements View.OnClickListener {
    TextInputEditText birthDateInput;
    TextInputEditText usernameInput;
    TextInputEditText firstNameInput;
    TextInputEditText lastNameInput;
    Spinner countryInput;
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

        Bundle bundle = controllerUserPresentation.getUserDataRegister();

        usernameInput = findViewById(R.id.usernameInput);
        firstNameInput = findViewById(R.id.firstNameInput);
        lastNameInput = findViewById(R.id.lastNameInput);
        birthDateInput = findViewById(R.id.birthDateInput);
            birthDateInput.setOnClickListener(this);
        countryInput = findViewById(R.id.countrySpinner);

        firstNameInput.setText(bundle.getString("firstName"));
        lastNameInput.setText(bundle.getString("lastName"));
        birthDateInput.setText(bundle.getString("birthDate"));

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
                String formattedMonth = (m < 10)? "0" + String.valueOf(m):String.valueOf(m);
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


        try {
            boolean result = controllerUserPresentation.registerData(
                    usernameInput.getText().toString().trim(),
                    firstNameInput.getText().toString().trim(),
                    lastNameInput.getText().toString().trim(),
                    birthDateInput.getText().toString().trim(),
                    countryInput.getSelectedItem().toString()
            );

            if (result) {
                Intent i = new Intent(this, SideMenuActivity.class);
                startActivity(i);
            } else {
                usernameInput.setError(getString(R.string.errorUsername));
                requestFocus(usernameInput);
            }
        } catch (ServerException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (NetworkErrorException e) {
            Toast toast = Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.networkError), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private boolean validateUsername() {
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
