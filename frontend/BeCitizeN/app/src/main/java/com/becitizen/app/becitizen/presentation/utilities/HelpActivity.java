package com.becitizen.app.becitizen.presentation.utilities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.becitizen.app.becitizen.R;

/**
 * Created by Nuria on 18/05/2018.
 */

public class HelpActivity extends Fragment implements View.OnClickListener{

    private View rootView;
    private CardView cvUser;
    private CardView cvInformation;
    private CardView cvFAQ;
    private CardView cvForum;
    private CardView cvMessaging;
    private CardView cvUtilities;

    public HelpActivity() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_help, container, false);

        cvUser = rootView.findViewById(R.id.cvUser);
        cvInformation = rootView.findViewById(R.id.cvInformation);
        cvFAQ = rootView.findViewById(R.id.cvFAQ);
        cvForum = rootView.findViewById(R.id.cvForum);
        cvMessaging = rootView.findViewById(R.id.cvMessaging);
        cvUtilities = rootView.findViewById(R.id.cvUtilities);

        cvUser.setOnClickListener(this);
        cvInformation.setOnClickListener(this);
        cvFAQ.setOnClickListener(this);
        cvForum.setOnClickListener(this);
        cvMessaging.setOnClickListener(this);
        cvUtilities.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        final Dialog dialog = new Dialog(rootView.getContext());
        switch (view.getId()) {
            case R.id.cvUser:
                dialog.setContentView(R.layout.tab1_user);
                break;

            case R.id.cvInformation:
                dialog.setContentView(R.layout.tab2_information);
                break;

            case R.id.cvFAQ:
                dialog.setContentView(R.layout.tab3_faq);
                break;

            case R.id.cvForum:
                dialog.setContentView(R.layout.tab4_forum);
                break;

            case R.id.cvMessaging:
                dialog.setContentView(R.layout.tab5_messaging);
                break;

            case R.id.cvUtilities:
                dialog.setContentView(R.layout.tab6_utilities);
                break;
        }
        dialog.show();
    }
}
