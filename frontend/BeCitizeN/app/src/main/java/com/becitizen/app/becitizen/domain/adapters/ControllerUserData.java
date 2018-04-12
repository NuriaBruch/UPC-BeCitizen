package com.becitizen.app.becitizen.domain.adapters;

import android.os.AsyncTask;
import android.util.Log;

import com.becitizen.app.becitizen.exceptions.ServerException;
import com.facebook.AccessToken;
import com.facebook.FacebookException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

public class ControllerUserData {

    //URIs
    private static final String URI_FB_LOGIN = "http://becitizen.cf/loginFacebook";
    private static final String URI_EXISTS_EMAIL = "http://becitizen.cf/existsEmail";
    private static final String URI_REGISTER = "http://becitizen.cf/register";

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
        if(instance == null) {
            instance = new ControllerUserData();
        }
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
            response = doGetRequest(URI_FB_LOGIN + "?accessToken=" + token);
        else throw new FacebookException("You are not logged with Facebook");

        JSONObject resp = null;
        try {
            resp = new JSONObject(response);

            if (resp.getString("status").equals("Ok")) {

                return response;

            } else throw new ServerException("Server response status is not OK");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Server", response);
            throw new ServerException("The server has not returned the expected JSONObject. \n");
        }
    }

    /**
     * Metodo que envia la solicitud para comprovar si un email esta registrado en nuestro servidor
     *
     * @param mail Email a comprobar si esta registrado
     * @return True si el email esta registrado en nuestro servidor, False de lo contrario
     */
    public boolean existsMail(String mail) {
        // TODO gestionar error.
        try {
            JSONObject info = new JSONObject(doGetRequest(URI_EXISTS_EMAIL + "?email=" + mail));
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
                            String lastName, String birthDate, String country, boolean facebook, boolean google) {
        String uri = URI_REGISTER;
        uri += "?username=" + username;
        uri += "&password=" + password;
        uri += "&email=" + email;
        uri += "&name=" + firstName;
        uri += "&surname=" + lastName;
        uri += "&birthday=" + birthDate;
        uri += "&country=" + country;
        uri += "&facebook=" + facebook;
        uri += "&google=" + google;


        /* DESCOMENTAR I ACABAR QUAN FUNCIONI
        try {
            JSONObject info = new JSONObject(doPostRequest(uri));
            if (info.get("status").equals("Ok")) {
                if (info.get("".equals(""))) return true;
            }
            return false;
        }
        catch (JSONException e) {
            return false;
        }

        */

        return false;
    }

    /**
     * Metodo que permite hacer un get a la url indicada
     *
     * @param url Direccion en la que se quiere hacer el get
     * @return Respuesta obtenida con el get
     */
    public String doGetRequest(String url) {
        sendUserDataToServer request = new sendUserDataToServer();
        String data = "";
        try {
            data = request.execute(new String[]{url}).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Clase que permite realizar tareas en segundo plano, en este caso peticiones Http
     */
    private class sendUserDataToServer extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... data) {

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(data[0]);
            String responseBody;

            try {
                HttpResponse response = httpClient.execute(httpGet);
                int statusCode = response.getStatusLine().getStatusCode();
                responseBody = EntityUtils.toString(response.getEntity());
                Log.w("Result", "Signed in as: " + responseBody);
            } catch (ClientProtocolException e) {
                Log.e(TAG, "Error sending ID token to backend.", e);
                return "Error sending ID token to backend.";
            } catch (IOException e) {
                Log.e(TAG, "Error sending ID token to backend.", e);
                return "Error sending ID token to backend.";
            }
            return responseBody;
        }
    }

    /* INTENT DE POST

    public String doPostRequest(String url) {
        sendUserDataToServer request = new sendUserDataToServer();
        String data = "";
        try {
            data = request.execute(new String[]{url}).post();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return data;
    }

    private class sendPostToServer extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... data) {

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(data[0]);
            String responseBody;

            try {
                HttpResponse response = httpClient.execute(httpPost);
                int statusCode = response.getStatusLine().getStatusCode();
                responseBody = EntityUtils.toString(response.getEntity());
                Log.w("Result", "Signed in as: " + responseBody);
            } catch (ClientProtocolException e) {
                Log.e(TAG, "Error sending ID token to backend.", e);
                return "Error sending ID token to backend.";
            } catch (IOException e) {
                Log.e(TAG, "Error sending ID token to backend.", e);
                return "Error sending ID token to backend.";
            }
            return responseBody;
        }
    }*/
}