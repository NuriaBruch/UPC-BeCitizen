package com.becitizen.app.becitizen.domain;


import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Facebook {

    private static final String URI_FB_LOGIN = "http://localhost:1337/loginFacebook";
    private CallbackManager callbackManager;
    private static boolean registered = false;

    private static String userId, token;

    private static User user;

    public static boolean login() {

        token = AccessToken.getCurrentAccessToken().getToken();
        userId = AccessToken.getCurrentAccessToken().getUserId();

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URI_FB_LOGIN + "?id_token=" + token);

        HttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = EntityUtils.toString(response.getEntity());
            Log.w("Result", "Signed in as: " + responseBody);

        } catch (IOException e) {
            //TODO: Throw server exception
            e.printStackTrace();
        }

        user = new User();

        try {

            /*
            Possible errors:
                - The response is not a JSONObject
                - Do not contain loggedIn
                - Do not contain info
                - Do not contain email, then we cannot identify the user
            */

            JSONObject resp = new JSONObject(response.toString());

            registered = resp.getBoolean("loggedIn");
            JSONObject info = resp.getJSONObject("info");

            if(!info.isNull("email")) user.setMail(info.getString("email"));
            if(!info.isNull("username")) user.setUsername(info.getString("username"));
            if(!info.isNull("name")) user.setFirstName(info.getString("name"));
            if(!info.isNull("surname")) user.setLastName(info.getString("surname"));
            if(!info.isNull("biography")) user.setBiography(info.getString("biography"));
            if(!info.isNull("birthday")) user.setBirthDate(info.getString("birthday"));
            if(!info.isNull("country")) user.setCountry(info.getString("country"));
            if(!info.isNull("rank")) user.setRank(Rank.valueOf(info.getString("rank")));

        } catch (JSONException e) {
            e.printStackTrace();
        }

            // if(regisrtered): user with our db data
            // else: user with the facebook data

        return registered;
    }


    public static User getUser() {
        return user;
    }
}
