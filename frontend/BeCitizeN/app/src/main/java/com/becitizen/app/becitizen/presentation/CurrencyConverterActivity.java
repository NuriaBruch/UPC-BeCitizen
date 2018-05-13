package com.becitizen.app.becitizen.presentation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.becitizen.app.becitizen.R;

import java.util.List;

public class CurrencyConverterActivity extends AppCompatActivity {
    EditText editTextAmount;
    TextView textViewResultado;
    Spinner spinnerFrom, spinnerTo;
    List<String> currencyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_converter);
        linkStuff();
        getCurrencyList();
    }

    private void linkStuff(){
        editTextAmount = findViewById(R.id.editTextAmount);
        textViewResultado = findViewById(R.id.textViewResultado);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
    }

    private void getCurrencyList(){
        ControllerUtilitiesPresentation.getInstance().getCurrencyList(currencyList);
    }
}
