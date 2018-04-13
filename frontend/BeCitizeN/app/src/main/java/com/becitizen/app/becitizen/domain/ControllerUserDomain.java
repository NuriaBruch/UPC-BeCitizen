package com.becitizen.app.becitizen.domain;


import android.os.Bundle;
import android.util.Log;

import com.becitizen.app.becitizen.domain.adapters.ControllerUserData;
import com.becitizen.app.becitizen.domain.entities.User;
import com.becitizen.app.becitizen.domain.enumerations.LoginResponse;
import com.becitizen.app.becitizen.exceptions.SharedPreferencesException;
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
        return controllerUserData.existsMail(mail);
    }

    public void createUser (String mail, String password) {
        currentUser = new User (mail, password);
    }

    public boolean registerData(String username, String firstName, String lastName, String birthDate, String country) {
        currentUser.setUsername(username);
        currentUser.setFirstName(firstName);
        currentUser.setLastName(lastName);
        currentUser.setBirthDate(birthDate);
        currentUser.setCountry(country);

        boolean result = controllerUserData.registerData(currentUser.getMail(), currentUser.getPassword(), username,
                firstName, lastName, birthDate, country, currentUser.isFacebook(), currentUser.isGoogle());
        try {
            if (result)
                doLogin("mail", currentUser.getUsername());
        } catch (SharedPreferencesException e) {
            return false;
        }
        return result;
    }

    public LoginResponse facebookLogin() {

        JSONObject json = null;
        try {
            String response = ControllerUserData.getInstance().facebookLogin();
            json = new JSONObject(response);

            JSONObject info = json.getJSONObject("info");
            currentUser = new User(info.getString("email"), null);
            if(!info.isNull("username")) currentUser.setUsername(info.getString("username"));
            if(!info.isNull("name")) currentUser.setFirstName(info.getString("name"));
            if(!info.isNull("surname")) currentUser.setLastName(info.getString("surname"));
            if(!info.isNull("birthday")) currentUser.setBirthDate(info.getString("birthday"));
            if(!info.isNull("country")) currentUser.setCountry(info.getString("country"));
            currentUser.setFacebook(true);

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
        try {
            JSONObject response = new JSONObject(controllerUserData.doGetRequest("http://becitizen.cf/loginGoogle?idToken=" + account.getIdToken()));

            if (response.get("status").equals("Ok")) {
                JSONObject info = response.getJSONObject("info");
                currentUser = new User(info.getString("email"), null);
                if(!info.isNull("username")) currentUser.setUsername(info.getString("username"));
                if(!info.isNull("name")) currentUser.setFirstName(info.getString("name"));
                if(!info.isNull("surname")) currentUser.setLastName(info.getString("surname"));
                if(!info.isNull("birthday")) currentUser.setBirthDate(info.getString("birthday"));
                if(!info.isNull("country")) currentUser.setCountry(info.getString("country"));
                currentUser.setGoogle(true);

                if (!response.getBoolean("loggedIn"))
                    return LoginResponse.REGISTER;
                else {
                    doLogin("google", currentUser.getUsername());
                    return LoginResponse.LOGGED_IN;
                }
            } else {
                return LoginResponse.ERROR;
            }
            //mGoogleSignInClient.signOut();
            // Signed in successfully, show authenticated UI.
            //updateUI(account);*/
        }
        catch (JSONException | SharedPreferencesException e) {
            return LoginResponse.ERROR;
        }
    }

    public Bundle getUserDataRegister() {
        Bundle bundle = new Bundle();
        bundle.putString("firstName", currentUser.getFirstName());
        bundle.putString("lastName", currentUser.getLastName());
        bundle.putString("birthDate", currentUser.getBirthDate());
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

    public void guestLogin() {
        try {
            doLogin("guest", "");
        } catch (SharedPreferencesException e) {
            e.printStackTrace();
        }
    }
}
