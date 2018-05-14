package com.becitizen.app.becitizen.presentation;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;

import java.util.ArrayList;
import java.util.List;

public class CurrencyConverterActivity extends AppCompatActivity{
    EditText editTextAmount;
    TextView textViewResultado;
    Spinner spinnerFrom, spinnerTo;
    List<String> currencyList;
    Button convertButton;
    TextView resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_converter);
        currencyList = new ArrayList<>();
        linkStuff();
        getCurrencyList();
        for(int i = 0; i < 10; ++i){
            currencyList.add(String.valueOf(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.row_forum_category, currencyList);

        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);
    }

    private void linkStuff(){
        editTextAmount = findViewById(R.id.editTextAmount);
        textViewResultado = findViewById(R.id.textViewResultado);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        convertButton = findViewById(R.id.convertButton);
        textViewResultado = findViewById(R.id.textViewResultado);
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.convertButton:
                        String currencyFrom = spinnerFrom.getSelectedItem().toString();
                        String currencyTo = spinnerTo.getSelectedItem().toString();
                        String amount = editTextAmount.getText().toString();
                        if(amount.equals("0")){
                            editTextAmount.setText("0");
                            amount = "0";
                        }
                        int resultado = ControllerUtilitiesPresentation.getInstance().getConversion(currencyFrom,currencyTo,amount);
                        textViewResultado.setText(resultado);
                        break;
                }
            }
        });
    }

    private void getCurrencyList(){
        ControllerUtilitiesPresentation.getInstance().getCurrencyList(currencyList);
    }

}
