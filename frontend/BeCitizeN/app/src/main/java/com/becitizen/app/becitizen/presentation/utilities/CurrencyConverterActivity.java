package com.becitizen.app.becitizen.presentation.utilities;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.support.v4.app.Fragment;
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
import com.becitizen.app.becitizen.exceptions.ServerException;
import com.becitizen.app.becitizen.presentation.controllers.ControllerUtilitiesPresentation;

import org.json.JSONException;

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
                        convert();
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
        } catch (JSONException e) {
            Toast.makeText(rootView.getContext(), getResources().getString(R.string.JSONerror), Toast.LENGTH_SHORT).show();
        } catch (ServerException e) {
            Toast.makeText(rootView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void convert() {
        if(currencyList.size() <= 0) {
            Context context = rootView.getContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, R.string.ccErrorConvert, duration);
            toast.show();
        }

        else {
            String currencyFrom = spinnerFrom.getSelectedItem().toString();
            String currencyTo = spinnerTo.getSelectedItem().toString();
            String amount = editTextAmount.getText().toString();

            if (!amount.equals("")) {

                try {
                    setResult(ControllerUtilitiesPresentation.getInstance().getConversion(currencyFrom, currencyTo, amount));
                }

                catch (NetworkErrorException e) {
                    Toast toast = Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.networkError), Toast.LENGTH_SHORT);
                    toast.show();
                    setResult(0);
                }

                catch (ServerException e) {
                    Toast.makeText(rootView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    setResult(0);
                }

                catch (JSONException e) {
                    Toast.makeText(rootView.getContext(), getResources().getString(R.string.JSONerror), Toast.LENGTH_SHORT).show();
                    setResult(0);
                }
            }
        }
    }

    private void setResult(double result) {
        textViewResultado.setText(String.valueOf((float) result));
    }

}
