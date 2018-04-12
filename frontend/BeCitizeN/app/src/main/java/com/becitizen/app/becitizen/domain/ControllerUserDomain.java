package com.becitizen.app.becitizen.domain;


import android.os.Bundle;
import android.util.Log;

import com.becitizen.app.becitizen.domain.adapters.ControllerUserData;
import com.becitizen.app.becitizen.exceptions.SharedPreferencesException;
import com.becitizen.app.becitizen.presentation.ControllerUserPresentation;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONException;
import org.json.JSONObject;


public class ControllerUserDomain {
    private static ControllerUserDomain uniqueInstance;
    private ControllerUserData controllerUserData;
    private User currentUser;

    private String PREFS_KEY = "myPreferences";

    private ControllerUserDomain() {
        controllerUserData = ControllerUserData.getInstance();
        currentUser = null;
    }

    public static ControllerUserDomain getUniqueInstance() {
        if (uniqueInstance == null)
            uniqueInstance = new ControllerUserDomain();
        return uniqueInstance;
    }

    public boolean existsMail(String mail) {
        return false;
    }

    public void createUser (String mail, String password) {
        currentUser = new User (mail, password);
    }

    public int registerData(String username, String firstName, String lastName, String birthDate, String country) {
        currentUser.setUsername(username);
        currentUser.setFirstName(firstName);
        currentUser.setLastName(lastName);
        currentUser.setBirthDate(birthDate);
        currentUser.setCountry(country);
        // llamar al adapter para registrar al usuario
        return 0;
    }

    public LoginResponse facebookLogin() {

        JSONObject json = null;
        try {
            String response = ControllerUserData.getInstance().facebookLogin();
            json = new JSONObject(response);

            JSONObject info = json.getJSONObject("info");
            currentUser = new User(info.getString("email"), null);
            currentUser.setUsername(info.getString("username"));
            currentUser.setFirstName(info.getString("name"));
            currentUser.setLastName(info.getString("surname"));
            currentUser.setBirthDate(info.getString("birthday"));
            currentUser.setCountry(info.getString("country"));

            if (json.getBoolean("loggedIn")) {
                doLogin("facebook", currentUser.getUsername());
                return LoginResponse.LOGGED_IN;
            }
            return LoginResponse.REGISTER;
        }
        catch (Exception e) {
            Log.e("Error", e.getMessage());
            return LoginResponse.ERROR;
        }
    }

    public LoginResponse googleLogin(GoogleSignInAccount account) {
        // TODO: poner uri en controller
        JSONObject info = null;
        try {
            info = new JSONObject(controllerUserData.doGetRequest("http://10.0.2.2:1337/loginGoogle?idToken=" + account.getIdToken()));

            if (info.get("status").equals("Ok")) {
                currentUser = new User(info.getString("email"), null);
                currentUser.setUsername(info.getString("username"));
                currentUser.setFirstName(info.getString("name"));
                currentUser.setLastName(info.getString("surname"));
                currentUser.setBirthDate(info.getString("birthday"));
                currentUser.setCountry(info.getString("country"));
                if (info.get("loggedIn").equals("False"))
                    return LoginResponse.REGISTER;
                else
                    return LoginResponse.LOGGED_IN;
            } else {
                return LoginResponse.ERROR;
            }
            //mGoogleSignInClient.signOut();
            // Signed in successfully, show authenticated UI.
            //updateUI(account);*/
        }
        catch (JSONException e) {
            return LoginResponse.ERROR;
        }
    }

    public Bundle getUserData() {
        Bundle bundle = new Bundle();
        bundle.putString("username", currentUser.getUsername());
        bundle.putString("firstName", currentUser.getFirstName());
        bundle.putString("lastName", currentUser.getLastName());
        bundle.putString("birthDate", currentUser.getBirthDate());
        bundle.putString("country", currentUser.getCountry());
        return bundle;
    }

    public boolean isLogged() throws SharedPreferencesException {
        MySharedPreferences preferences = MySharedPreferences.getInstance();
        return preferences.getValue(PREFS_KEY, "isLogged").equals("true");
    }

    public String getLoggedUser() throws SharedPreferencesException {
        MySharedPreferences preferences = MySharedPreferences.getInstance();
        JSONObject json = new JSONObject();

        boolean isLogged = preferences.getValue(PREFS_KEY, "isLogged").equals("true");
        try {
            json.put("isLogged", isLogged);
            if(isLogged) {
                String mode = preferences.getValue(PREFS_KEY, "mode");
                json.put("mode", mode);
                if (!mode.equals("guest")) json.put("userName", preferences.getValue(PREFS_KEY, "userName"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return json.toString();
    }

    private void doLogin(String mode, String userName) throws SharedPreferencesException {
        MySharedPreferences preferences = MySharedPreferences.getInstance();
        preferences.saveValue(PREFS_KEY, "isLogged", "true");
        preferences.saveValue(PREFS_KEY, "mode", mode);
        if(!mode.equals("guest")) preferences.saveValue(PREFS_KEY, "userName", userName);
    }

    private void doLogout() throws SharedPreferencesException {
        MySharedPreferences preferences = MySharedPreferences.getInstance();
        preferences.saveValue(PREFS_KEY, "isLogged", "false");
    }

    public void logout() {
        try {
            doLogout();
        } catch (SharedPreferencesException e) {
            e.printStackTrace();
        }
    }
}
