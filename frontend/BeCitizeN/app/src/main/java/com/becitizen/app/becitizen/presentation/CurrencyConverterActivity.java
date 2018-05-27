package com.becitizen.app.becitizen.presentation;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CurrencyConverterActivity extends Fragment {

    private View rootView;

    EditText editTextAmount;
    TextView textViewResultado;
    Spinner spinnerFrom, spinnerTo;
    List<String> currencyList;
    Button convertButton;
    TextView resultado;

    public CurrencyConverterActivity() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_currency_converter, container, false);

        currencyList = new ArrayList<>();
        linkStuff();
        getCurrencyList();
        Collections.sort(currencyList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.row_forum_category, currencyList);

        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        return rootView;
    }

    private void linkStuff(){
        editTextAmount = rootView.findViewById(R.id.editTextAmount);
        textViewResultado = rootView.findViewById(R.id.textViewResultado);
        spinnerFrom = rootView.findViewById(R.id.spinnerFrom);
        spinnerTo = rootView.findViewById(R.id.spinnerTo);
        convertButton = rootView.findViewById(R.id.convertButton);
        textViewResultado = rootView.findViewById(R.id.textViewResultado);
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.convertButton:
                        if(currencyList.size() <= 0) {
                            Context context = rootView.getContext();
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, R.string.ccErrorConvert, duration);
                            toast.show();
                            break;
                        }
                        String currencyFrom = spinnerFrom.getSelectedItem().toString();
                        String currencyTo = spinnerTo.getSelectedItem().toString();
                        String amount = editTextAmount.getText().toString();
                        if(amount.equals("")){
                            editTextAmount.setText("0");
                            amount = "0";
                        }
                        double resultado = 0;
                        try {
                            resultado = ControllerUtilitiesPresentation.getInstance().getConversion(currencyFrom,currencyTo,amount);
                        } catch (NetworkErrorException e) {
                            Toast toast = Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.networkError), Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        textViewResultado.setText(String.valueOf((float)resultado));
                        break;
                }
            }
        });
    }

    private void getCurrencyList(){
        try {
            ControllerUtilitiesPresentation.getInstance().getCurrencyList(currencyList);
        } catch (NetworkErrorException e) {
            Toast toast = Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.networkError), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
