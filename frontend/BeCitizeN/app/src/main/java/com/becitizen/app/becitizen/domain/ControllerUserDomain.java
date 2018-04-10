package com.becitizen.app.becitizen.domain;


import com.becitizen.app.becitizen.exceptions.SharedPreferencesException;

import org.json.JSONException;
import org.json.JSONObject;


public class ControllerUserDomain {
    private static ControllerUserDomain uniqueInstance;
    private User currentUser;

    private String PREFS_KEY = "myPreferences";

    private ControllerUserDomain() {
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

    public String facebookLogin() throws Exception {

        String response = Facebook.login();
        JSONObject json = new JSONObject(response);

        if (json.getBoolean("loggedIn")) {
            //TODO: do login
        }

        return response;

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


        return preferences.getValue(PREFS_KEY, "userName");
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
}
