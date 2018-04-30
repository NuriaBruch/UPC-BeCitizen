package com.becitizen.app.becitizen.domain.adapters;

import android.util.Log;

import com.becitizen.app.becitizen.exceptions.ServerException;
import com.facebook.AccessToken;
import com.facebook.FacebookException;

import org.json.JSONException;
import org.json.JSONObject;

public class ControllerUserData {

    //URIs
    private static final String URI_FB_LOGIN = "http://becitizen.cf/loginFacebook";
    private static final String URI_GOOGLE_LOGIN = "http://becitizen.cf/loginGoogle?idToken=";

    private static final String URI_EXISTS_EMAIL = "http://becitizen.cf/existsEmail";
    private static final String URI_REGISTER = "http://becitizen.cf/register";
    private static final String URI_LOGIN_MAIL = "http://becitizen.cf/loginMail";

    private static final String URI_DEACTIVATE_ACCOUNT = "http://becitizen.cf/deactivateAccount";
    private static final String URI_UPDATE_PROFILE = "http://becitizen.cf/updateProfile";

    private static ControllerUserData instance = null;

    /**
     * Constructora por defecto para evitar que sea instanciado
     */
    protected ControllerUserData() {
        // Exists only to defeat instantiation.
    }

    /**
     * Metodo para obtener la instancia del singleton
     *
     * @return La instancia de ControllerUserData
     */
    public static ControllerUserData getInstance() {
        if(instance == null) instance = new ControllerUserData();
        return instance;
    }

    /**
     * Metodo que envia la solicitud de hacer login con Facebook a nuestro servidor,
     * comprueba si ha sucedido algun error en el servidor y devuelve su respuesta.
     *
     * @return La respuesta de nuestro servidor al hacer login con Facebook
     * @throws ServerException Si se ha generado algún error en el servidor o no devuelve la respuesta esperada.
     */
    public String facebookLogin() throws ServerException {
        String response, token;

        if((token = AccessToken.getCurrentAccessToken().getToken()) != null)
            response = ServerAdapter.getInstance().doGetRequest(URI_FB_LOGIN + "?accessToken=" + token);
        else throw new FacebookException("You are not logged with Facebook");

        JSONObject resp = null;
        try {
            resp = new JSONObject(response);

            if (resp.getString("status").equals("Ok")) {

                return response;

            } else throw new ServerException("Server response status is not OK");

        } catch (JSONException e) {
            // TODO gestionar errors.
            e.printStackTrace();
            Log.d("Server", response);
            throw new ServerException("The server has not returned the expected JSONObject. \n");
        }
    }

    public String googleLogin(String token) {
        return ServerAdapter.getInstance().doGetRequest(URI_GOOGLE_LOGIN + token);
    }

    /**
     * Metodo que envia la solicitud para comprovar si un email esta registrado en nuestro servidor
     *
     * @param mail Email a comprobar si esta registrado
     * @return True si el email esta registrado en nuestro servidor, False de lo contrario
     */
    public boolean existsMail(String mail) {
        // TODO gestionar errors.
        try {
            JSONObject info = new JSONObject(ServerAdapter.getInstance().doGetRequest(URI_EXISTS_EMAIL + "?email=" + mail));
            if (info.get("status").equals("Ok")) {
                return !info.get("found").equals("No");
            }
            return true;
        }
        catch (JSONException e) {
            return true;
        }
    }

    /**
     * Metodo que envia la solicitud para registrar un usuario con los datos de los parametros en nuestro servidor
     *
     * @param email Email del usuario
     * @param password Contrasena del usuario
     * @param username Nombre de usuario
     * @param firstName Nombre del usuario
     * @param lastName Apellido del usuario
     * @param birthDate Fecha de nacimiento del usuario
     * @param country Pais del usuario
     * @param facebook True si el usuario se registra con Facebook
     * @param google True si el usuario se registra con Google
     * @return False si ha ocurrido algun error, True de lo contrario
     */
    public boolean registerData(String email, String password, String username, String firstName,
                            String lastName, String birthDate, String country, int image, boolean facebook, boolean google) {

        JSONObject json = new JSONObject();
        try {
            json.put("username", username);
            json.put("password", password);
            json.put("email", email);
            json.put("name", firstName);
            json.put("surname", lastName);
            json.put("birthday", birthDate);
            json.put("country", country);
            json.put("profilePicture", String.valueOf(image));
            json.put("facebook", String.valueOf(facebook));
            json.put("google", String.valueOf(google));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] dataRequest = {URI_REGISTER, json.toString()};
        try {
            JSONObject info = new JSONObject(ServerAdapter.getInstance().doPostRequest(dataRequest));
            if (info.get("status").equals("Ok")) {
               return true;
            }
            return false;
        }
        catch (JSONException e) {
            // TODO gestionar errors.
            return false;
        }

    }

    public String checkCredentials(String email, String password) {
        return ServerAdapter.getInstance().doGetRequest(URI_LOGIN_MAIL + "?email=" + email + "&password=" + password);
    }

    public int editProfile(String firstName, String lastName, String birthDate, int image, String country, String biography) {
        JSONObject json = new JSONObject();
        try {
            json.put("name", firstName);
            json.put("surname", lastName);
            json.put("biography", biography);
            json.put("birthday", birthDate);
            json.put("profilePicture", image);
            json.put("country", country);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*String[] dataRequest = {URI_UPDATE_PROFILE, json.toString()};
        try {
            JSONObject info = new JSONObject(ServerAdapter.getInstance().doPutRequest(dataRequest));
            if (info.get("status").equals("Ok")) {
                return true;
            }
            return false;
        }
        catch (JSONException e) {
            // TODO gestionar errors.
            return false;
        }*/

        return 0;
    }


    public boolean deactivateAccount(String username) {
        try {
            JSONObject info = new JSONObject(ServerAdapter.getInstance().doGetRequest(URI_DEACTIVATE_ACCOUNT + "?username=" + username));

            if (info.get("status").equals("Ok")) {
                return true;
            }
            return false;
        }
        catch (JSONException e) {
            return false;
        }
    }

    public int deleteUser() {
        return 0;
    }
}
