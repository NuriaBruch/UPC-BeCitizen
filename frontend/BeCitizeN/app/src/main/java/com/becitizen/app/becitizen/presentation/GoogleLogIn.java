package com.becitizen.app.becitizen.presentation;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.LoginResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import org.json.JSONException;

public class GoogleLogIn {

    public static final int RC_SIGN_IN = 100;

    private GoogleSignInClient mGoogleSignInClient;

    public GoogleLogIn() {}

    /**
     * Configure sign-in to request the user's ID, email address, and basic
     * profile. ID and basic profile are included in DEFAULT_SIGN_IN.
     * Request only the user's ID token, which can be used to identify the
     * user securely to your backend.
     *
     * @param  activity the activity from which the login is used
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
     * Check for existing Google Sign In account, if the user is already signed in
     * the GoogleSignInAccount will be non-null.
     *
     * @param  activity the activity from which the login is used
     */
    public void start(Activity activity) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(activity);
        //updateUI(account);
    }

    /**
     * Start SignIn screen to get a result.
     *
     * @param  activity the activity from which the login is used
     */
    public void signIn(Activity activity) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public LoginResponse onResult(MainActivity activity, Intent data) {
        // The Task returned from this call is always completed, no need to attach
        // a listener.
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        return handleSignInResult(task, activity);
    }

    private LoginResponse handleSignInResult(Task<GoogleSignInAccount> completedTask, MainActivity activity) {
        GoogleSignInAccount account = null;
        try {
            account = completedTask.getResult(ApiException.class);

            //TODO: delete logs
            Log.w("Email", account.getEmail());
            Log.w("Display Name", account.getDisplayName());
            Log.w("Token", account.getIdToken());



            /*JSONObject data = new JSONObject(ControllerUserData.getInstance().doGetRequest("http://10.0.2.2:1337/loginGoogle?idToken=" + account.getIdToken()));

            if(data.get("status").equals("Ok")) {
                Bundle bundle = new Bundle();

                if (data.get("loggedIn").equals("False")) {
                    bundle.putString("username", null);
                    bundle.putString("mail", account.getEmail());
                    bundle.putString("firstName", account.getGivenName());
                    bundle.putString("lastName", account.getFamilyName());
                    bundle.putString("birthDate", null);
                    bundle.putString("country", null);

                    // It goes to the DataRegisterView and all the data of the user is sent.
                    activity.goToActivity(DataRegisterView.class, bundle, 0);
                } else {
                    activity.goToActivity(InsideActivity.class, bundle, Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                }
            } else {
                Toast toast = Toast.makeText(activity.getApplicationContext(), "Error", Toast.LENGTH_SHORT);
                toast.show();
            }
            //mGoogleSignInClient.signOut();
            // Signed in successfully, show authenticated UI.
            //updateUI(account);*/
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
        return ControllerUserPresentation.getUniqueInstance().googleLogin(account.getIdToken());
    }
}