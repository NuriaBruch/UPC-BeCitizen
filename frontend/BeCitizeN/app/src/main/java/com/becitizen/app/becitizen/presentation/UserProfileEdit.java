package com.becitizen.app.becitizen.presentation;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;

import java.lang.reflect.Array;
import java.util.Calendar;

public class UserProfileEdit extends Fragment implements View.OnClickListener {
    private View rootView;

    private ControllerUserPresentation controllerUserPresentation;

    private TextInputEditText firstNameInput;
    private TextInputEditText lastNameInput;
    private TextInputEditText birthDateInput;
    private Spinner countryInput;
    private TextInputEditText biographyInput;
    private ImageButton ibSelectImage;
    private ImageButton ibDeleteUser;
    private Button bUpdate;

    private Calendar myCalendar = Calendar.getInstance();
    private int day = myCalendar.get(Calendar.DAY_OF_MONTH);
    private int month = myCalendar.get(Calendar.MONTH);
    private int year = myCalendar.get(Calendar.YEAR);

    private int selection;

    public UserProfileEdit() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_user_profile_edit, container, false);

        controllerUserPresentation = ControllerUserPresentation.getUniqueInstance();

        Bundle bundle = controllerUserPresentation.getLoggerUserData();

        firstNameInput = rootView.findViewById(R.id.firstNameInput);
        lastNameInput = rootView.findViewById(R.id.lastNameInput);
        birthDateInput = rootView.findViewById(R.id.birthDateInput);
            birthDateInput.setOnClickListener(this);
        countryInput = rootView.findViewById(R.id.countrySpinner);
        biographyInput = rootView.findViewById(R.id.biographyInput);

        ibSelectImage = rootView.findViewById(R.id.ibSelectImage);
            ibSelectImage.setOnClickListener(this);
        ibDeleteUser = rootView.findViewById(R.id.ibDeleteUser);
            ibDeleteUser.setOnClickListener(this);
        bUpdate = rootView.findViewById(R.id.bUpdate);
            bUpdate.setOnClickListener(this);

        firstNameInput.setText(bundle.getString("firstName"));
        lastNameInput.setText(bundle.getString("lastName"));
        birthDateInput.setText(bundle.getString("birthDate"));
        biographyInput.setText(bundle.getString("biography"));

        SpinnerAdapter spinnerAdapter = countryInput.getAdapter();

        setCountry(bundle.getString("country"));

        selection = bundle.getInt("image");
        setImage(selection);

        return rootView;
    }

    private void setCountry(String country) {
        String[] array = getResources().getStringArray(R.array.countries);
        boolean found = false;
        int i = 0;

        for (; i < array.length && !found; ++i) {
            if (array[i].equals(country)) {
                found = true;
                countryInput.setSelection(i);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.birthDateInput:
                obtainDate();
                break;
            case R.id.ibSelectImage:
                changeImage();
                break;
            case R.id.bUpdate:
                updateProfile();
                break;
            case R.id.ibDeleteUser:
                deleteUser();
                break;
        }
    }

    private void deleteUser() {
        int ret = controllerUserPresentation.deleteUser();

        if (ret == 0) {
            controllerUserPresentation.logout();
            goToLogin();
        }

        else {
            Toast notificacion=Toast.makeText(rootView.getContext(),getString(R.string.serverError),Toast.LENGTH_LONG);
            notificacion.show();
        }
    }

    private void goToLogin() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    private void obtainDate() {
        DatePickerDialog birthDatePicker = new DatePickerDialog(rootView.getContext(), new DatePickerDialog.OnDateSetListener() {
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

    private void setImage(int number) {
        switch (number) {
            case 1:
                ibSelectImage.setImageResource(R.drawable.userprofile1);
                break;

            case 2:
                ibSelectImage.setImageResource(R.drawable.userprofile2);
                break;

            case 3:
                ibSelectImage.setImageResource(R.drawable.userprofile3);
                break;

            case 4:
                ibSelectImage.setImageResource(R.drawable.userprofile4);
                break;

            case 5:
                ibSelectImage.setImageResource(R.drawable.userprofile5);
                break;

            case 6:
                ibSelectImage.setImageResource(R.drawable.userprofile6);
                break;

            case 7:
                ibSelectImage.setImageResource(R.drawable.userprofile7);
                break;

            case 8:
                ibSelectImage.setImageResource(R.drawable.userprofile8);
                break;

            default:
                ibSelectImage.setImageResource(R.drawable.userprofile1);
                break;
        }
    }

    public void changeImage() {
        Intent intent = new Intent(getActivity(), ImageSelection.class);
        startActivityForResult(intent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            String aux = data.getStringExtra(ImageSelection.RESULT_IMAGE);
            selection = Integer.parseInt(aux);
            setImage(selection);
        }
    }

    public void updateProfile() {
        String firstName = firstNameInput.getText().toString().trim();
        if (!validateStringInput(firstName)) {
            firstNameInput.setError(getString(R.string.errorMsgName));
            requestFocus(firstNameInput);
            return;
        }

        String lastName = lastNameInput.getText().toString().trim();
        if (!validateStringInput(lastName)) {
            lastNameInput.setError(getString(R.string.errorMsgName));
            requestFocus(lastNameInput);
            return;
        }

        String date = birthDateInput.getText().toString().trim();
        if (!validateBirthDate(date)) {
            return;
        }

        String biography = biographyInput.getText().toString().trim();
        if (!validateStringInput(biography)) {
            biographyInput.setError(getString(R.string.errorMsgName));
            requestFocus(biographyInput);
            return;
        }

        int res = controllerUserPresentation.editProfile(firstName, lastName, date, selection,
                countryInput.getSelectedItem().toString().trim(), biography);

        if (res == 0) {
            Toast notificacion=Toast.makeText(rootView.getContext(),getString(R.string.updateCorrect),Toast.LENGTH_LONG);
            notificacion.show();

            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            getActivity().getSupportFragmentManager().popBackStack();
            transaction.remove(this);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            transaction.commit();
        }

        else if (res == 1) {
            Toast notificacion=Toast.makeText(rootView.getContext(),getString(R.string.DBError),Toast.LENGTH_LONG);
            notificacion.show();
        }

        else {
            Toast notificacion=Toast.makeText(rootView.getContext(),getString(R.string.serverError),Toast.LENGTH_LONG);
            notificacion.show();
        }
    }

    private boolean validateStringInput(String input) {
        return !input.isEmpty();
    }

    private boolean validateBirthDate(String date) {
        String yearString = date.substring(6);
        if (date.isEmpty()) {
            birthDateInput.setError(getString(R.string.errorMsgName));
            return false;
        } else if (Integer.valueOf(yearString) > year - 18) {
            birthDateInput.setError(getString(R.string.errorMsgName));
            return false;
        }
        else {
            TextInputLayout birthDateLayout  = rootView.findViewById(R.id.birthDateLayout);
            birthDateLayout.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}
