package com.becitizen.app.becitizen.presentation;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.exceptions.ServerException;

import org.json.JSONException;

public class UserProfile extends Fragment implements View.OnClickListener {

    private View rootView;
    private ControllerUserPresentation controllerUserPresentation;
    private TextView tvUsername;
    private TextView tvName;
    private TextView tvBirthDate;
    private TextView tvCountry;
    private TextView tvBiography;
    private TextView tvRank;
    private ImageView ivUserImage;
    private FloatingActionButton fbPrivateMessage;
    private ImageButton ibEditProfile;
    private ImageButton ibSignOut;

    private boolean loggedUser;
    private String username;
    private boolean activeUser;
    Bundle userData;

    public UserProfile() {
        activeUser = true;
        username = "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_user_profile, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            loggedUser = bundle.getBoolean("loggeduser");
            if (!loggedUser) username = bundle.getString("username");
        }

        controllerUserPresentation = ControllerUserPresentation.getUniqueInstance();
        tvUsername = rootView.findViewById(R.id.tvUsername);
        tvRank = rootView.findViewById(R.id.tvRank);
        tvName = rootView.findViewById(R.id.tvName);
        tvBirthDate = rootView.findViewById(R.id.tvBirthday);
        tvCountry = rootView.findViewById(R.id.tvCountry);
        tvBiography = rootView.findViewById(R.id.tvBiography);
        ivUserImage = rootView.findViewById(R.id.ivUserImage);

        ibEditProfile = rootView.findViewById(R.id.ibEditProfile);
        ibEditProfile.setOnClickListener(this);

        ibSignOut = rootView.findViewById(R.id.ibSignOut);
        ibSignOut.setOnClickListener(this);

        fbPrivateMessage = rootView.findViewById(R.id.fbPrivateMessage);

        setValues();

        return rootView;
    }

    private void setValues() {

        if (loggedUser) {
            fbPrivateMessage.setVisibility(View.GONE);
        }
        else {
            ibEditProfile.setVisibility(View.GONE);
            ibSignOut.setVisibility(View.GONE);
        }

        try {
            userData = controllerUserPresentation.viewProfile(username);

            setTextView("username", tvUsername);

            String name = userData.get("firstName").toString();
            name += " " + userData.get("lastName").toString();
            tvName.setText(name);

            setTextView("birthDate", tvBirthDate);
            setTextView("country", tvCountry);
            setTextView("biography", tvBiography);
            setTextView("rank", tvRank);

            setImage(userData.getInt("image"));
        }

        catch (ServerException e) {
            ibEditProfile.setVisibility(View.GONE);
            ibSignOut.setVisibility(View.GONE);
            fbPrivateMessage.setVisibility(View.GONE);

            if (e.getMessage().equals("User deactivated")) {
                fbPrivateMessage.setVisibility(View.GONE);
                // TODO posar les coses per al usuari desactivat
            }

            else {
                Toast.makeText(rootView.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        catch (JSONException e) {
            Toast.makeText(rootView.getContext(), "JSON error", Toast.LENGTH_LONG).show();
        }
    }

    private void setTextView(String text, TextView tv) {
        if (userData.get(text) != null) tv.setText(userData.get(text).toString());
        else {
            tv.setVisibility(View.GONE);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibEditProfile:
                editProfile(rootView);
                break;
            case R.id.ibSignOut:
                signOut();
                break;
        }
    }

    private void signOut() {
        controllerUserPresentation.logout();
        Toast.makeText(rootView.getContext(), getResources().getString(R.string.logout), Toast.LENGTH_LONG).show();
        goToLogin();
    }

    private void goToLogin() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    public void editProfile(View view) {
        Fragment fragment = new UserProfileEdit();
        fragmentTransaction(fragment, "USER_EDIT_PROFILE");
    }

    private void fragmentTransaction(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }
}
