package com.becitizen.app.becitizen.domain;


import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Facebook {

    private static final String URI_FB_LOGIN = "";
    private CallbackManager callbackManager;
    private static boolean registered = false;

    private static String userId, token;

    private static User user;

    public static boolean login() {

        token = AccessToken.getCurrentAccessToken().getToken();
        userId = AccessToken.getCurrentAccessToken().getUserId();

        //TODO: send request to backend
        /*HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URI_FB_LOGIN + "?id_token=" + token);

        try {
            HttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            final String responseBody = EntityUtils.toString(response.getEntity());
            Log.w("Result", "Signed in as: " + responseBody);

        } catch (IOException e) {
            //TODO: Throw server exception
            e.printStackTrace();
        }
*/

        //TODO: response treatment
        registered = true;

        user = new User();
            // if(user is already regisrtered): Create user with our db data
            // else: Create user with the facebook data

        return registered;
    }


    public User getUser() {
        return user;
    }
}
