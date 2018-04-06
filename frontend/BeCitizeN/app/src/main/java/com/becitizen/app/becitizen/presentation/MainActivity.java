package com.becitizen.app.becitizen.presentation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.Facebook;
import com.becitizen.app.becitizen.domain.User;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private GoogleLogIn googleLogIn;

    private LoginButton fbLoginButton;
    private CallbackManager callbackManager;

    private String toastText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        googleLogIn = new GoogleLogIn();
        googleLogIn.configure(this);

        findViewById(R.id.sign_in_button).setOnClickListener(this);

        callbackManager = CallbackManager.Factory.create();

        fbLoginButton = (LoginButton) findViewById(R.id.fb_login_button);
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (Facebook.login()) {
                    //Code if the user is already registered in our database
                    // Facebook.getUser() returns a user with the data of our db
                    //TODO: do login

                    toastText = getResources().getString(R.string.login_success);

                    // It goes to the InsideActivity.
                    // Flags: If back is pressed in the InsideActivity, the app will exit and not return to this activity.
                    goToActivity(InsideActivity.class, new Bundle(), Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                } else {
                    // Code if the user is not yet registered in our database
                    // Facebook.getUser() returns a user with the data of user that could be obtain from FB
                    // TODO: go to register formular view and pass the user data as a parameter

                    toastText = getResources().getString(R.string.login_success);

                    User user = Facebook.getUser();

                    Bundle bundle = new Bundle();
                    bundle.putString("username", user.getUsername());
                    bundle.putString("mail", user.getMail());
                    bundle.putString("firstName", user.getFirstName());
                    bundle.putString("lastName", user.getLastName());
                    bundle.putString("birthDate", user.getBirthDate());
                    bundle.putString("country", user.getCountry());

                    // It goes to the DataRegisterView and all the data of the user is sent.
                    goToActivity(DataRegisterView.class, bundle, 0);
                }
            }

            @Override
            public void onCancel() {

                toastText = getResources().getString(R.string.login_cancel);
            }

            @Override
            public void onError(FacebookException error) {

                toastText = getResources().getString(R.string.login_fail);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleLogIn.start(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                googleLogIn.signIn(this);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GoogleLogIn.RC_SIGN_IN:
                // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
                googleLogIn.onResult(this, data);

                break;
            default:
                // FB
                Log.w("RequestCode", "### RequestCode: " + requestCode);
                Toast.makeText(this, toastText, Toast.LENGTH_LONG).show();
                callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void goToActivity(Class c, Bundle extras, int flags) {

        Intent intent = new Intent(this, c);
        intent.putExtras(extras);
        intent.addFlags(flags);
        startActivity(intent);
    }



}
