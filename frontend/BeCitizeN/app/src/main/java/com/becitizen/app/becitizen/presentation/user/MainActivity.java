package com.becitizen.app.becitizen.presentation.user;

import android.accounts.NetworkErrorException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.enumerations.LoginResponse;
import com.becitizen.app.becitizen.exceptions.ServerException;
import com.becitizen.app.becitizen.presentation.SideMenuActivity;
import com.becitizen.app.becitizen.presentation.controllers.ControllerUserPresentation;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private GoogleLogIn googleLogIn;

    private LoginButton fbLoginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        googleLogIn = new GoogleLogIn();
        googleLogIn.configure(this);

        findViewById(R.id.sign_in_button).setOnClickListener(this);

        callbackManager = CallbackManager.Factory.create();

        fbLoginButton = (LoginButton) findViewById(R.id.fb_login_button);
        fbLoginButton.setReadPermissions(Arrays.asList("email", "user_birthday", "user_hometown"));
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                try {
                    handleExternalLoginResult(ControllerUserPresentation.getUniqueInstance().facebookLogin());
                } catch (ServerException e) {
                    // TODO revisar el getApplicationContext
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (NetworkErrorException e) {
                    Toast toast = Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.networkError), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onCancel() {

                showToast(getResources().getString(R.string.login_cancel));
            }

            @Override
            public void onError(FacebookException error) {

                showToast(getResources().getString(R.string.login_fail));

            }
        });
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
                try {
                    handleExternalLoginResult(googleLogIn.onResult(data));
                } catch (ServerException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
                break;
            case 64206:
                //Facebook
                callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleExternalLoginResult(LoginResponse loginresponse) {
        switch (loginresponse) {
            case ERROR:
                Toast toast = Toast.makeText(this.getApplicationContext(), "Error", Toast.LENGTH_SHORT);
                toast.show();
                break;
            case REGISTER:
                startActivity(new Intent(this, DataRegisterActivity.class));
                break;
            case LOGGED_IN:
                Intent intent = new Intent(this, SideMenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }

    public void signUpMailMainMenu(View view) {
        goToActivity(SignUpMailActivity.class, new Bundle(), 0);
    }

    public void logInGuestMainMenu(View view) {
        ControllerUserPresentation.getUniqueInstance().guestLogin();
        goToActivity(SideMenuActivity.class, new Bundle(), 0);
    }

    public void logInMailMainMenu(View view) {
        goToActivity(MailLoginActivity.class, new Bundle(), 0);
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }


    public void goToActivity(Class c, Bundle extras, int flags) {
        Intent intent = new Intent(this, c);
        intent.putExtras(extras);
        intent.addFlags(flags);
        startActivity(intent);
    }
}
