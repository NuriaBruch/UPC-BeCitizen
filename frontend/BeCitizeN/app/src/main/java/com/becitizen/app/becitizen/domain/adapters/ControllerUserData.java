package com.becitizen.app.becitizen.domain.adapters;

import android.os.AsyncTask;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

public class ControllerUserData {

    //URIs
    private static final String URI_FB_LOGIN = "http://becitizen.cf/loginFacebook";
    private static final String URI_EXISTS_EMAIL = "http://becitizen.cf/existsEmail";
    private static final String URI_REGISTER = "http://becitizen.cf/register";

    private static ControllerUserData instance = null;

    protected ControllerUserData() {
        // Exists only to defeat instantiation.
    }
    public static ControllerUserData getInstance() {
        if(instance == null) {
            instance = new ControllerUserData();
        }
        return instance;
    }

    public String facebookLogin() throws Exception {

        ControllerUserData controllerUserData = ControllerUserData.getInstance();

        String response, token;

        if((token = AccessToken.getCurrentAccessToken().getToken()) != null)
            response = doGetRequest(URI_FB_LOGIN + "?accessToken=" + token);
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

    public boolean existsMail(String mail) {
        // TODO gestionar error.
        try {
            JSONObject info = new JSONObject(doGetRequest(URI_EXISTS_EMAIL + "?email=" + mail));
            if (info.get("status").equals("Ok")) {
                return !info.get("found").equals("No");
            }
            return true;
        }
        catch (JSONException e) {
            return true;
        }
    }

    public boolean registerData(String email, String password, String username, String firstName,
                            String lastName, String birthDate, String country, boolean facebook, boolean google) {
        String uri = URI_REGISTER;
        uri += "?username=" + username;
        uri += "&password=" + password;
        uri += "&email=" + email;
        uri += "&name=" + firstName;
        uri += "&surname=" + lastName;
        uri += "&birthday=" + birthDate;
        uri += "&country=" + country;
        uri += "&facebook=" + facebook;
        uri += "&google=" + google;

        try {
            JSONObject info = new JSONObject(doGetRequest(uri));
            return info.get("status").equals("Ok");
        }
        catch (JSONException e) {
            return false;
        }
    }

    public String doGetRequest(String url) {
        sendUserDataToServer request = new sendUserDataToServer();
        String data = "";
        try {
            data = request.execute(new String[]{url}).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return data;
    }

    private class sendUserDataToServer extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... data) {

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(data[0]);
            String responseBody;

            try {
                HttpResponse response = httpClient.execute(httpGet);
                int statusCode = response.getStatusLine().getStatusCode();
                responseBody = EntityUtils.toString(response.getEntity());
                Log.w("Result", "Signed in as: " + responseBody);
            } catch (ClientProtocolException e) {
                Log.e(TAG, "Error sending ID token to backend.", e);
                return "Error sending ID token to backend.";
            } catch (IOException e) {
                Log.e(TAG, "Error sending ID token to backend.", e);
                return "Error sending ID token to backend.";
            }
            return responseBody;
        }
    }
}
