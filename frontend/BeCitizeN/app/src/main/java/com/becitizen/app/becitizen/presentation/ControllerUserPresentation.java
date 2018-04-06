package com.becitizen.app.becitizen.presentation;

/**
 * Created by Nuria on 06/04/2018.
 */

public class ControllerUserPresentation {

    static ControllerUserPresentation uniqueInstance;

    //ControllerUserDomain controllerUserDomain;

    private ControllerUserPresentation() {
        //controllerUserDomain = ControllerUserDomain.getUniqueInstance();
    }

    public static ControllerUserPresentation getUniqueInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new ControllerUserPresentation();
        }

        return uniqueInstance;
    }

    public boolean existsMail(String email) {
        return false;/*controllerUserDomain.existsMail(email);*/
    }

    public void createUser(String mail, String password) {
        //controllerUserDomain.createUser(mail, password);
    }
}
