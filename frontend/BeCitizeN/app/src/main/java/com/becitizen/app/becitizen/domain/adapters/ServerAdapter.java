package com.becitizen.app.becitizen.domain.adapters;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

public class ServerAdapter {

    private static ServerAdapter instance;

    private ServerAdapter() {
    }

    public static ServerAdapter getInstance() {
        if(instance == null) instance = new ServerAdapter();
        return instance;
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
     * Metodo que permite hacer un post a la url indicada con
     * un objeto formado por los valores en los parametros
     *
     * @param dataRequest Conjunto de Strings que contienen la url seguida de los valors del JSONObject a postear
     * @return Respuesta obtenida con el post
     */
    public String doPostRequest(String[] dataRequest) {
        PostTask request = new PostTask();
        String data = "";
        try {
            data = request.execute(dataRequest).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Clase que permite realizar tareas en segundo plano, en este caso peticiones Http get
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

    /**
     * Clase que permite realizar tareas en segundo plano, en este caso peticiones Http post
     */
    private class PostTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... data) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(data[0]);
            String responseBody;

            try {
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("username", data[1]));
                nameValuePairs.add(new BasicNameValuePair("password", data[2]));
                nameValuePairs.add(new BasicNameValuePair("email", data[3]));
                nameValuePairs.add(new BasicNameValuePair("name", data[4]));
                nameValuePairs.add(new BasicNameValuePair("surname", data[5]));
                nameValuePairs.add(new BasicNameValuePair("birthday", data[6]));
                nameValuePairs.add(new BasicNameValuePair("country", data[7]));
                nameValuePairs.add(new BasicNameValuePair("facebook", data[8]));
                nameValuePairs.add(new BasicNameValuePair("google", data[9]));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                //execute http post
                HttpResponse response = httpclient.execute(httppost);
                int statusCode = response.getStatusLine().getStatusCode();
                responseBody = EntityUtils.toString(response.getEntity());

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
}
