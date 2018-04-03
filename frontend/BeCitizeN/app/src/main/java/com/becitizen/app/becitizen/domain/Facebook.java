package com.becitizen.app.becitizen.domain;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Facebook {

    private CallbackManager callbackManager;
    private static boolean registered = false;

    private static String userId, token;

    //private User fb_user;

    public static boolean login() {

        token = AccessToken.getCurrentAccessToken().getToken();
        userId = AccessToken.getCurrentAccessToken().getUserId();

        //TODO: send request to backend

        //TODO: response treatment
        registered = true;
        if(registered) {
            // if(user is already regisrtered): Create user with our db data
        } else {
            // else: Create user with the facebook data
        }

        return registered;
    }
}
