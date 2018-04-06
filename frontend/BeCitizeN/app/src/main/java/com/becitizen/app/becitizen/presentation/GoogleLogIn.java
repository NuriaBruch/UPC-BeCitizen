package com.becitizen.app.becitizen.presentation;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.becitizen.app.becitizen.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import static android.content.ContentValues.TAG;

public class GoogleLogIn {

    public static final int RC_SIGN_IN = 100;

    private GoogleSignInClient mGoogleSignInClient;

    public GoogleLogIn() {}

    public void configure(Activity activity) {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Request only the user's ID token, which can be used to identify the
        // user securely to your backend. This will contain the user's basic
        // profile (name, profile picture URL, etc) so you should not need to
        // make an additional call to personalize your application.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.backendClientID))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
    }

    public void start(Activity activity) {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(activity);
        //updateUI(account);
    }

    public void signIn(Activity activity) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public Bundle onResult(Activity activity, Intent data) {
        // The Task returned from this call is always completed, no need to attach
        // a listener.
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        return handleSignInResult(task);
    }

    private Bundle handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        Bundle bundle = new Bundle();
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            //TODO: delete logs
            Log.w("Email", account.getEmail());
            Log.w("Display Name", account.getDisplayName());
            Log.w("Token", account.getIdToken());

            bundle.putString("username", null);
            bundle.putString("mail", account.getEmail());
            bundle.putString("firstName", account.getGivenName());
            bundle.putString("lastName", account.getFamilyName());
            bundle.putString("birthDate", null);
            bundle.putString("country", null);

            sendUserDataToServer request = new sendUserDataToServer();
            if (request.execute(new String[]{"http://10.0.2.2:1337/loginGoogle", account.getIdToken()}).equals("Success")) {
                mGoogleSignInClient.signOut();
                System.out.println("asaSS");
                // Signed in successfully, show authenticated UI.
                //updateUI(account);
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
        return bundle;
    }

    private class sendUserDataToServer extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... data) {

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(data[0] + "?idToken=" + data[1]);
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
            return "Success";
        }
    }
}