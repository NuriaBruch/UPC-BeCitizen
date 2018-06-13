package com.becitizen.app.becitizen.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.exceptions.SharedPreferencesException;
import com.becitizen.app.becitizen.presentation.controllers.ControllerUserPresentation;
import com.becitizen.app.becitizen.presentation.faq.FaqCategoriesActivity;
import com.becitizen.app.becitizen.presentation.forum.ForumCategoriesActivity;
import com.becitizen.app.becitizen.presentation.info.InformationCategoriesActivity;
import com.becitizen.app.becitizen.presentation.msg.AllConversationsActivity;
import com.becitizen.app.becitizen.presentation.user.MainActivity;

public class MenuActivity extends Fragment implements View.OnClickListener {
    private View rootView;

    private CardView cvInformation;
    private CardView cvForum;
    private CardView cvFAQ;
    private CardView cvMessaging;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_menu, container, false);

        cvInformation = rootView.findViewById(R.id.cvInformation);
        cvForum = rootView.findViewById(R.id.cvForum);
        cvFAQ = rootView.findViewById(R.id.cvFAQ);
        cvMessaging = rootView.findViewById(R.id.cvMessaging);

        cvInformation.setOnClickListener(this);
        cvForum.setOnClickListener(this);
        cvFAQ.setOnClickListener(this);
        cvMessaging.setOnClickListener(this);


        return rootView;
    }

    @Override
    public void onClick(View view) {
        Fragment fragment;
        switch (view.getId()) {
            case R.id.cvInformation:
                fragment = new InformationCategoriesActivity();
                fragmentTransaction(fragment, "INFORMATION_CATEGORY_ACTIVITY");
                break;

            case R.id.cvForum:
                fragment = new ForumCategoriesActivity();
                fragmentTransaction(fragment, "FORUM_CATEGORY_ACTIVITY");
                break;

            case R.id.cvFAQ:
                fragment = new FaqCategoriesActivity();
                fragmentTransaction(fragment, "FAQ_CATEGORY_ACTIVITY");
                break;

            case R.id.cvMessaging:
                try {
                    if (ControllerUserPresentation.getUniqueInstance().isLoggedAsGuest()) {
                        fragment = new LoggedAsGuestActivity();
                        fragmentTransaction(fragment, "LOGGED_AS_GUEST_ACTIVITY");
                    } else {
                        fragment = new AllConversationsActivity();
                        fragmentTransaction(fragment, "ALL_CONVERSATIONS_ACTIVITY");
                    }
                } catch (SharedPreferencesException e) {
                    e.printStackTrace();
                    //Initialize MySharedPreferences
                    ControllerUserPresentation.getUniqueInstance().initializeMySharedPreferences(rootView.getContext());
                    goToLogin();
                }
                break;

        }
    }

    private void fragmentTransaction(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    private void goToLogin() {
        Intent intent = new Intent(rootView.getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

}
