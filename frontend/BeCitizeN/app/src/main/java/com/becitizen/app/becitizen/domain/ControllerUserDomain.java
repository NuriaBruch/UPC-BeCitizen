package com.becitizen.app.becitizen.domain;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.becitizen.app.becitizen.domain.adapters.ControllerUserData;
import com.becitizen.app.becitizen.domain.entities.User;
import com.becitizen.app.becitizen.domain.enumerations.LoginResponse;
import com.becitizen.app.becitizen.exceptions.ServerException;
import com.becitizen.app.becitizen.exceptions.SharedPreferencesException;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONException;
import org.json.JSONObject;


public class ControllerUserDomain {
    private static ControllerUserDomain uniqueInstance;
    private ControllerUserData controllerUserData;
    private User currentUser;

    private String PREFS_KEY = "myPreferences";

    /**
     * Constructora por defecto para evitar que sea instanciado
     *
     **/
    private ControllerUserDomain() {
        controllerUserData = ControllerUserData.getInstance();
        currentUser = new User();
    }

    /**
     * Metodo para obtener la instancia del singleton
     *
     * @return La instancia de ControllerUserDomain
     */
    public static ControllerUserDomain getUniqueInstance() {
        if (uniqueInstance == null)
            uniqueInstance = new ControllerUserDomain();
        return uniqueInstance;
    }

    /**
     * Retorna si un email ya esta registrado en nuestro servidor
     *
     * @param mail Email a comprovar
     * @return True si el email esta en el servidor, false de lo contrario
     */
    public boolean existsMail(String mail) {
        return controllerUserData.existsMail(mail);
    }

    /**
     * Crea un usuario con el email y contrasena recibidos
     *
     * @param mail Email
     * @param password Contrasena
     */
    public void createUser (String mail, String password) {
        currentUser = new User (mail, password);
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
    public boolean registerData(String username, String firstName, String lastName, String birthDate, String country) {
        currentUser.setUsername(username);
        currentUser.setFirstName(firstName);
        currentUser.setLastName(lastName);
        currentUser.setBirthDate(birthDate);
        currentUser.setCountry(country);
        currentUser.setImage((int) (Math.random() * 8) + 1);

        boolean result = controllerUserData.registerData(currentUser.getMail(), currentUser.getPassword(), username,
                firstName, lastName, birthDate, country, currentUser.getImage(), currentUser.isFacebook(), currentUser.isGoogle());
        try {
            if (result) {
                if (!currentUser.isFacebook() && !currentUser.isGoogle()) {
                    // Fem el login amb mail per obtenir el token de la sessió del usuari
                    if (checkCredentials(currentUser.getMail(), currentUser.getPassword())) doLogin("mail", currentUser.getUsername());
                }

                else {
                    doLogin("mail", currentUser.getUsername());
                }
            }
        } catch (SharedPreferencesException e) {
            // TODO gestionar errors.
            return false;
        }
        return result;
    }

    /**
     * Metodo que identifica un usuario en nuestro servidor con Facebook
     *
     * @return ERROR si ha ocurrido algun error, LOGGED_IN si el usuario ya esta registrado en nuestro servidor o REGISTER si el usuario no esta registrado en nuestro servidor
     */
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
            if(!info.isNull("biography")) currentUser.setBiography(info.getString("biography"));
            if(!info.isNull("rank")) currentUser.setRank(info.getString("rank"));
            currentUser.setFacebook(true);

            if (json.getBoolean("loggedIn")) {
                doLogin("facebook", currentUser.getUsername());
                return LoginResponse.LOGGED_IN;
            }
            return LoginResponse.REGISTER;
        }
        catch (Exception e) {
            // TODO gestionar errors.
            Log.e("Error", e.getMessage());
            return LoginResponse.ERROR;
        }
    }

    /**
     * Metodo que identifica un usuario en nuestro servidor con Google
     *
     * @param account Cuenta de Google que identifica al usuario
     * @return ERROR si ha ocurrido algun error, LOGGED_IN si el usuario ya esta registrado en nuestro servidor o REGISTER si el usuario no esta registrado en nuestro servidor
     */
    public LoginResponse googleLogin(GoogleSignInAccount account) {
        try {
            JSONObject response = new JSONObject(controllerUserData.googleLogin(account.getIdToken()));

            if (response.get("status").equals("Ok")) {
                JSONObject info = response.getJSONObject("info");
                currentUser = new User(info.getString("email"), null);
                if(!info.isNull("username")) currentUser.setUsername(info.getString("username"));
                if(!info.isNull("name")) currentUser.setFirstName(info.getString("name"));
                if(!info.isNull("surname")) currentUser.setLastName(info.getString("surname"));
                if(!info.isNull("birthday")) currentUser.setBirthDate(info.getString("birthday"));
                if(!info.isNull("country")) currentUser.setCountry(info.getString("country"));
                if(!info.isNull("biography")) currentUser.setBiography(info.getString("biography"));
                if(!info.isNull("rank")) currentUser.setRank(info.getString("rank"));
                currentUser.setGoogle(true);

                if (!response.getBoolean("loggedIn"))
                    return LoginResponse.REGISTER;
                else {
                    doLogin("google", currentUser.getUsername());
                    return LoginResponse.LOGGED_IN;
                }
            } else {
                // TODO gestionar errors.
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

    /**
     * Retorna los datos obtenidos de Google o Facebook
     * si los hay
     *
     * @return Bundle que contiene los datos para el formulario de registro
     */
    public Bundle getUserDataRegister() {
        Bundle bundle = new Bundle();
        bundle.putString("firstName", currentUser.getFirstName());
        bundle.putString("lastName", currentUser.getLastName());
        bundle.putString("birthDate", currentUser.getBirthDate());
        return bundle;
    }

    /**
     * Determina si hay algun usuario identificado
     *
     * @return True si el usuario esta identificado, false de lo contrario
     * @throws SharedPreferencesException Si MySharedPreferences no se ha inicializado
     */
    public boolean isLogged() throws SharedPreferencesException {
        MySharedPreferences preferences = MySharedPreferences.getInstance();
        return preferences.getValue(PREFS_KEY, "isLogged").equals("true");
    }

    /**
     * Devuelve el usuario identificado
     *
     * @return "guest" si se ha identificado como invitado o el username si se ha identificado con email, Google o Facebook
     * @throws SharedPreferencesException Si MySharedPreferences no se ha inicializado
     */
    public String getLoggedUser() throws SharedPreferencesException {
        MySharedPreferences preferences = MySharedPreferences.getInstance();
        JSONObject json = new JSONObject();

        boolean isLogged = preferences.getValue(PREFS_KEY, "isLogged").equals("true");
        try {
            json.put("isLogged", isLogged);
            if(isLogged) {
                String mode = preferences.getValue(PREFS_KEY, "mode");
                json.put("mode", mode);
                if (!mode.equals("guest")) {
                    json.put("userName", preferences.getValue(PREFS_KEY, "userName"));
                    currentUser.setUsername(preferences.getValue(PREFS_KEY, "userName"));
                    controllerUserData.setToken(preferences.getValue(PREFS_KEY, "token"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return json.toString();
    }

    /**
     * Guarda en MySharedPreferences que el usuario esta identificado,
     * el modo como se ha identificado y el nombre de usuario.
     *
     * @param mode Modo de identificacion: facebook, google, guest, mail
     * @param userName Nombre de usuario
     * @throws SharedPreferencesException Si MySharedPreferences no se ha inicializado
     */
    private void doLogin(String mode, String userName) throws SharedPreferencesException {
        MySharedPreferences preferences = MySharedPreferences.getInstance();
        preferences.saveValue(PREFS_KEY, "isLogged", "true");
        preferences.saveValue(PREFS_KEY, "mode", mode);
        if(!mode.equals("guest")) {
            preferences.saveValue(PREFS_KEY, "userName", userName);
            preferences.saveValue(PREFS_KEY, "token", controllerUserData.getToken());
        }
    }

    /**
     * Guarda en MySharedPreferences que el usuario no esta identificado.
     *
     * @throws SharedPreferencesException Si MySharedPreferences no se ha inicializado
     */
    private void doLogout() throws SharedPreferencesException {
        MySharedPreferences preferences = MySharedPreferences.getInstance();
        preferences.saveValue(PREFS_KEY, "isLogged", "false");
    }

    /**
     * Guarda en MySharedPreferences que el usuario no esta identificado.
     *
     */
    public void logout() {
        try {
            doLogout();
        } catch (SharedPreferencesException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo que identifica un usuario como invitado
     *
     */
    public void guestLogin() {
        try {
            doLogin("guest", "");
        } catch (SharedPreferencesException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retorna si un par {email, password} ya esta registrado en nuestro servidor
     *
     * @param email Email a comprobar
     * @param password Contrasena a comprobar
     * @return True si el par {email, password} esta en el servidor, false de lo contrario
     */
    public boolean checkCredentials(String email, String password) {
        try {
            JSONObject response = new JSONObject(controllerUserData.checkCredentials(email, password));

            if (response.get("status").equals("Ok")) {
                JSONObject info = response.getJSONObject("info");
                currentUser = new User(email, null);
                if(!info.isNull("username")) currentUser.setUsername(info.getString("username"));
                if(!info.isNull("name")) currentUser.setFirstName(info.getString("name"));
                if(!info.isNull("surname")) currentUser.setLastName(info.getString("surname"));
                if(!info.isNull("birthday")) currentUser.setBirthDate(info.getString("birthday"));
                if(!info.isNull("country")) currentUser.setCountry(info.getString("country"));
                if(!info.isNull("biography")) currentUser.setBiography(info.getString("biography"));
                if(!info.isNull("rank")) currentUser.setRank(info.getString("rank"));
                doLogin("mail", currentUser.getUsername());
                return true;

            }
            return false;
        } catch (JSONException e) {
            return false;
        } catch (SharedPreferencesException e) {
            return false;
        }
    }

    public void initializeMySharedPreferences(Context context) {
        MySharedPreferences.init(context);
    }

    public boolean deactivateAccount() {
        return controllerUserData.deactivateAccount(currentUser.getUsername());
    }

    public int editProfile(String firstName, String lastName, String birthDate, int image, String country, String biography) {
        // Poster sha de mirar el Json aqui en lloc de a controllerUserData
        int i = controllerUserData.editProfile(firstName, lastName, birthDate, image, country, biography);
        if (i == 0) {
            currentUser.setFirstName(firstName);
            currentUser.setLastName(lastName);
            currentUser.setBirthDate(birthDate);
            currentUser.setCountry(country);
            currentUser.setBiography(biography);
            currentUser.setImage(image);
        }
        return i;
    }

    public Bundle getLoggedUserData() {
        Bundle bundle = new Bundle();

        bundle.putString("username", currentUser.getUsername());
        bundle.putString("firstName", currentUser.getFirstName());
        bundle.putString("lastName", currentUser.getLastName());
        bundle.putString("birthDate", currentUser.getBirthDate());
        bundle.putString("country", currentUser.getCountry());
        bundle.putString("biography", currentUser.getBiography());
        bundle.putString("rank", currentUser.getRank());
        bundle.putInt("image", currentUser.getImage());

        return bundle;
    }

    public Bundle viewProfile(String username) throws ServerException {

        Bundle bundle = new Bundle();

        if (username.isEmpty()) username = currentUser.getUsername();

        /*try {
            JSONObject response = new JSONObject(controllerUserData.viewProfile(username));

            if (response.get("status").equals("Ok")) {
                JSONObject info = response.getJSONObject("info");
                bundle.putString("username",username);
                if(!info.isNull("name")) bundle.putString("firstName", info.getString("name"));
                if(!info.isNull("surname")) bundle.putString("lastName", info.getString("surname"));
                if(!info.isNull("birthday")) bundle.putString("birthDate", info.getString("birthday"));
                if(!info.isNull("country")) bundle.putString("country", info.getString("country"));
                if(!info.isNull("biography")) bundle.putString("biography", info.getString("biography"));
                if(!info.isNull("rank")) bundle.putString("rank", info.getString("rank"));
                if(!info.isNull("profilePicture")) bundle.putInt("image", info.isNull("profilePicture"));

                if (username.equals(currentUser.getUsername())) {
                    currentUser.setFirstName(info.getString("name"));
                    currentUser.setLastName(info.getString("surname"));
                    currentUser.setBirthDate(info.getString("birthday"));
                    currentUser.setCountry(info.getString("country"));
                    currentUser.setBiography(info.getString("biography"));
                    currentUser.setRank(info.getString("rank"));
                }

                return bundle;
            }

            else if (response.get("status").equals("E1")){
                throw new ServerException("Server Error");
            }

            else if (response.get("status").equals("E2")){
                throw new ServerException("User not found");
            }

            else {
                throw new ServerException("User deactivated");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            throw new ServerException("JSONObject error");
        }*/

        bundle.putString("username", username);
        bundle.putString("firstName", "nuria");
        bundle.putString("lastName", "bruch");
        bundle.putString("birthDate", "16/10/1997");
        bundle.putString("country", "British Virgin Islands");
        bundle.putString("biography", "holiholi allioli");
        bundle.putString("rank", "coal");
        bundle.putInt("image", 5);

        if (username.equals(currentUser.getUsername())) {
            currentUser.setFirstName("nuria");
            currentUser.setLastName("bruch");
            currentUser.setBirthDate("16/10/1997");
            currentUser.setCountry("British Virgin Islands");
            currentUser.setBiography("holiholi allioli");
            currentUser.setRank("coal");
            currentUser.setImage(5);
        }

        return bundle;
    }

    public JSONObject getThreadsCategory(String category) {
        try {
            JSONObject response = new JSONObject(controllerUserData.getThreadsCategory(category));
            return response;
        }
        catch (JSONException e) {
            return null;
        }
    }

    public JSONObject getCategories() {
        try {
            JSONObject response = new JSONObject(controllerUserData.getCategories());
            return response;
        }
        catch (JSONException e) {
            return null;
        }
    }
}
