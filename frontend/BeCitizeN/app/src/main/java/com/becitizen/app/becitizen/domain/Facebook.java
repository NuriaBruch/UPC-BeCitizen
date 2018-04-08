package com.becitizen.app.becitizen.domain;


import android.util.Log;

import com.becitizen.app.becitizen.domain.adapters.BackendConnection;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookException;

import org.json.JSONException;
import org.json.JSONObject;

public class Facebook {

    private static final String URI_FB_LOGIN = "http://10.0.2.2:1337/loginFacebook";
    private CallbackManager callbackManager;
    private static boolean registered = false;

    private static String token, response;

    public static String login() throws Exception {

        BackendConnection backendConnection = BackendConnection.getInstance();

        if((token = AccessToken.getCurrentAccessToken().getToken()) != null)
            response = backendConnection.doGetRequest(URI_FB_LOGIN + "?accessToken=" + token);
        else throw new FacebookException("You are not logged with Facebook");

        JSONObject resp = null;
        try {
            resp = new JSONObject(response);

            if (resp.getString("status").equals("Ok")) {

                return response;

            } else throw new Exception("Server response status is not OK");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Server", response);
            throw new Exception("The server has not returned the expected JSONObject. \n");
        }
    }
}
