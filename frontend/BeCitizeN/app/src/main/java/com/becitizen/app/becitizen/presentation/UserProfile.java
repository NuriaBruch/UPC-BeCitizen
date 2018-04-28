package com.becitizen.app.becitizen.presentation;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.becitizen.app.becitizen.R;

public class UserProfile extends Fragment {

    private View rootView;
    private ControllerUserPresentation controllerUserPresentation;
    private TextView tvUsername;
    private TextView tvName;
    private TextView tvBirthDate;
    private TextView tvCountry;
    private TextView tvBiography;
    private TextView tvMail;
    private TextView tvRank;
    private ImageView ivUserImage;

    private boolean loggedUser;
    Bundle userData;

    public UserProfile() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_user_profile, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            loggedUser = bundle.getBoolean("loggeduser");
        }

        controllerUserPresentation = ControllerUserPresentation.getUniqueInstance();
        tvUsername = rootView.findViewById(R.id.tvUsername);
        tvRank = rootView.findViewById(R.id.tvRank);
        tvName = rootView.findViewById(R.id.tvName);
        tvBirthDate = rootView.findViewById(R.id.tvBirthday);
        tvCountry = rootView.findViewById(R.id.tvCountry);
        tvMail = rootView.findViewById(R.id.tvMail);
        tvBiography = rootView.findViewById(R.id.tvBiography);
        ivUserImage = rootView.findViewById(R.id.ivUserImage);

        setValues();

        return rootView;
    }

    private void setValues() {

        if (loggedUser) {
            userData = controllerUserPresentation.getLoggerUserData();
        }

        setTextView("username", tvUsername);

        String name = userData.get("firstName").toString();
        name += " " + userData.get("lastName").toString();
        tvName.setText(name);

        setTextView("birthDate", tvBirthDate);
        setTextView("country", tvCountry);
        setTextView("biography", tvBiography);
        setTextView("mail", tvMail);
        setTextView("rank", tvRank);

        setImage(userData.getInt("image"));
    }

    private void setTextView(String text, TextView tv) {
        if (userData.get(text) != null) tv.setText(userData.get(text).toString());
        else {
            tv.setVisibility(View.INVISIBLE);
        }
    }

    private void setImage(int number) {
        switch (number) {
            case 1:
                ivUserImage.setImageResource(R.drawable.userprofile1);
                break;

            case 2:
                ivUserImage.setImageResource(R.drawable.userprofile2);
                break;

            case 3:
                ivUserImage.setImageResource(R.drawable.userprofile3);
                break;

            case 4:
                ivUserImage.setImageResource(R.drawable.userprofile4);
                break;

            case 5:
                ivUserImage.setImageResource(R.drawable.userprofile5);
                break;

            case 6:
                ivUserImage.setImageResource(R.drawable.userprofile6);
                break;

            case 7:
                ivUserImage.setImageResource(R.drawable.userprofile7);
                break;

            case 8:
                ivUserImage.setImageResource(R.drawable.userprofile8);
                break;

            default:
                ivUserImage.setImageResource(R.drawable.userprofile1);
                break;
        }
    }
}
