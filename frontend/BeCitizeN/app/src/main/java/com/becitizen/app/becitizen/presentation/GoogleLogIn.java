package com.becitizen.app.becitizen.presentation;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.enumerations.LoginResponse;
import com.becitizen.app.becitizen.exceptions.ServerException;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import static com.facebook.FacebookSdk.getApplicationContext;

public class GoogleLogIn {

    public static final int RC_SIGN_IN = 100;

    private GoogleSignInClient mGoogleSignInClient;

    public GoogleLogIn() {}

    /**
     * Configurar la sign-in request para pedir el user token id.
     *
     * @param  activity la actividad donde se utiliza el login
     */
    public void configure(Activity activity) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.backendClientID))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
    }

    /**
     * Abrir ventana de sign-in con Google
     *
     * @param  activity la actividad donde se utiliza el login
     */
    public void signIn(Activity activity) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * Procesa el resultado del login.
     *
     * @param  data los datos resultantes del intent de login
     */
    public LoginResponse onResult(Intent data) throws ServerException {
        // The Task returned from this call is always completed, no need to attach
        // a listener.
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        GoogleSignInAccount account = null;
        try {
            account = task.getResult(ApiException.class);
        } catch (ApiException e) {
            return LoginResponse.ERROR;
        }
        try {
            return ControllerUserPresentation.getUniqueInstance().googleLogin(account);
        } catch (NetworkErrorException e) {
            Toast toast = Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.networkError), Toast.LENGTH_SHORT);
            toast.show();
            return null;
        }
    }
}