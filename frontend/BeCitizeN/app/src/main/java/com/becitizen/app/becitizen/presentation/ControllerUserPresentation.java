package com.becitizen.app.becitizen.presentation;

import android.os.Bundle;

import com.becitizen.app.becitizen.domain.ControllerUserDomain;
import com.becitizen.app.becitizen.domain.enumerations.LoginResponse;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONObject;

public class ControllerUserPresentation {

    static ControllerUserPresentation uniqueInstance;

    ControllerUserDomain controllerUserDomain;

    private ControllerUserPresentation() {
        controllerUserDomain = ControllerUserDomain.getUniqueInstance();
    }

    public static ControllerUserPresentation getUniqueInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new ControllerUserPresentation();
        }

        return uniqueInstance;
    }

    public boolean existsMail(String email) {
        return controllerUserDomain.existsMail(email);
    }

    public void createUser(String mail, String password) {
        controllerUserDomain.createUser(mail, password);
    }

    public boolean registerData(String username, String firstName, String lastName, String birthDate, String country) {
        return controllerUserDomain.registerData(username, firstName, lastName, birthDate, country);
    }

    public boolean checkCredentials(String email, String password) {
        return controllerUserDomain.checkCredentials(email,password);
    }

    public LoginResponse facebookLogin() {
        return controllerUserDomain.facebookLogin();
    }

    public LoginResponse googleLogin(GoogleSignInAccount account) {
        return controllerUserDomain.googleLogin(account);
    }

    public Bundle getUserDataRegister() {
        return controllerUserDomain.getUserDataRegister();
    }

    public void logout() {
        controllerUserDomain.logout();
    }

    public boolean isLogged() throws Exception {
            return controllerUserDomain.isLogged();
    }

    public String getLoggedUser() throws Exception {

        JSONObject json = new JSONObject(controllerUserDomain.getLoggedUser());
        String mode = json.getString("mode");
        if (mode.equals("guest")) return mode;
        else return json.getString("userName");
    }

    public void guestLogin() {
        controllerUserDomain.guestLogin();
    }
}
