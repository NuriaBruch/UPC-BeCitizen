package com.becitizen.app.becitizen.presentation;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.Bundle;

import com.becitizen.app.becitizen.domain.ControllerUserDomain;
import com.becitizen.app.becitizen.domain.entities.CategoryThread;
import com.becitizen.app.becitizen.domain.enumerations.LoginResponse;
import com.becitizen.app.becitizen.exceptions.ServerException;
import com.becitizen.app.becitizen.exceptions.SharedPreferencesException;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ControllerUserPresentation {

    static ControllerUserPresentation uniqueInstance;

    ControllerUserDomain controllerUserDomain;

    /**
     * Constructora privada para que no se instancie
     * y inicializar valores
     */
    private ControllerUserPresentation() {
        controllerUserDomain = ControllerUserDomain.getUniqueInstance();
    }

    /**
     * Metodo para obtener la instancia del singleton
     *
     * @return Instancia
     */
    public static ControllerUserPresentation getUniqueInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new ControllerUserPresentation();
        }

        return uniqueInstance;
    }

    /**
     * Retorna si un email ya esta registrado en nuestro servidor
     *
     * @param email Email a comprovar
     * @return True si el email esta en el servidor, false de lo contrario
     */
    public boolean existsMail(String email) throws ServerException, NetworkErrorException {
        return controllerUserDomain.existsMail(email);
    }

    /**
     * Crea un usuario con el email y contrasena recibidos
     *
     * @param mail Email
     * @param password Contrasena
     */
    public void createUser(String mail, String password) {
        controllerUserDomain.createUser(mail, password);
    }

    /**
     * Registra un usuario con los parametros pasados
     *
     * @param username Nombre de usuario
     * @param firstName Nombre
     * @param lastName Apellido
     * @param birthDate Fecha de nacimiento
     * @param country Pais
     * @return False si ha ocurrido algun error, true de lo contrario
     */
    public boolean registerData(String username, String firstName, String lastName, String birthDate, String country) throws ServerException, NetworkErrorException {
        return controllerUserDomain.registerData(username, firstName, lastName, birthDate, country);
    }

    /**
     * Comprueba que el existe el usuario identificado con el email y contrasena
     *
     * @param email Email
     * @param password Contrasena
     * @return Cierto si las credenciales son correctas, false de lo contrario
     */
    public boolean checkCredentials(String email, String password) throws ServerException, NetworkErrorException {
        return controllerUserDomain.checkCredentials(email,password);
    }

    /**
     * Metodo que identifica un usuario en nuestro servidor con Facebook
     *
     * @return ERROR si ha ocurrido algun error, LOGGED_IN si el usuario ya esta registrado en nuestro servidor o REGISTER si el usuario no esta registrado en nuestro servidor
     */
    public LoginResponse facebookLogin() throws ServerException, NetworkErrorException {
        return controllerUserDomain.facebookLogin();
    }

    /**
     * Metodo que identifica un usuario en nuestro servidor con Google
     *
     * @param account Cuenta de Google que identifica al usuario
     * @return ERROR si ha ocurrido algun error, LOGGED_IN si el usuario ya esta registrado en nuestro servidor o REGISTER si el usuario no esta registrado en nuestro servidor
     */
    public LoginResponse googleLogin(GoogleSignInAccount account) throws ServerException, NetworkErrorException {
        return controllerUserDomain.googleLogin(account);
    }

    /**
     * Retorna los datos obtenidos de Google o Facebook
     * si los hay
     *
     * @return Bundle que contiene los datos para el formulario de registro
     */
    public Bundle getUserDataRegister() {
        return controllerUserDomain.getUserDataRegister();
    }

    /**
     * Hace logout
     *
     */
    public void logout() {
        controllerUserDomain.logout();
    }

    /**
     * Determina si hay algun usuario identificado
     *
     * @return True si el usuario esta identificado, false de lo contrario
     * @throws Exception
     */
    public boolean isLogged() throws Exception {
            return controllerUserDomain.isLogged();
    }

    /**
     * Devuelve el usuario identificado
     *
     * @return "guest" si se ha identificado como invitado o el username si se ha identificado con email, Google o Facebook
     * @throws Exception
     */
    public String getLoggedUser() throws Exception {

        JSONObject json = new JSONObject(controllerUserDomain.getLoggedUser());
        String mode = json.getString("mode");
        if (mode.equals("guest")) return mode;
        else return json.getString("userName");
    }

    /**
     * Metodo que identifica un usuario como invitado
     *
     */
    public void guestLogin() {
        controllerUserDomain.guestLogin();
    }

    public void initializeMySharedPreferences(Context context) {
        ControllerUserDomain.getUniqueInstance().initializeMySharedPreferences(context);
    }

    public boolean deactivateAccount() throws ServerException, JSONException, NetworkErrorException {
        return controllerUserDomain.deactivateAccount();
    }

    public boolean editProfile(String firstName, String lastName, String birthDate, int image, String country, String biography) throws ServerException, JSONException, NetworkErrorException {
        return controllerUserDomain.editProfile(firstName, lastName, birthDate, image, country, biography);
    }

    public Bundle getLoggedUserData() {
        return controllerUserDomain.getLoggedUserData();
    }


    public Bundle viewProfile(String username) throws ServerException, JSONException, NetworkErrorException {
        return controllerUserDomain.viewProfile(username);
    }

    public boolean checkUsername(String username) {
        return controllerUserDomain.checkUsername(username);
    }

    public void blockUser(String mail) throws ServerException, JSONException, NetworkErrorException {
        controllerUserDomain.blockUser(mail);
    }

    public void unblockUser(String mail) throws ServerException, JSONException, NetworkErrorException {
        controllerUserDomain.unblockUser(mail);
    }
}
