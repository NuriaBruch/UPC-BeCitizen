package com.becitizen.app.becitizen.domain;

public class ControllerUserDomain {
    private static ControllerUserDomain uniqueInstance;
    private User currentUser;

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




}
