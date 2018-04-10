package com.becitizen.app.becitizen.presentation;

import com.becitizen.app.becitizen.domain.ControllerUserDomain;
import com.becitizen.app.becitizen.domain.LoginResponse;

/**
 * Created by Nuria on 06/04/2018.
 */

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

    public int registerData(String username, String firstName, String lastName, String birthDate, String country) {
        return controllerUserDomain.registerData(username, firstName, lastName, birthDate, country);
    }

    public int checkCredentials(String email, String password) {
        return 0;
    }

    public LoginResponse facebookLogin() {
        return null;
    }

    public LoginResponse googleLogin(String idToken) {
        return null;
    }
}
