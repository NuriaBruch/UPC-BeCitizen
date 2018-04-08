package com.becitizen.app.becitizen.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.ControllerUserDomain;
import com.becitizen.app.becitizen.domain.GoogleLogIn;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

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
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                JSONObject json = null;
                try {
                    json = new JSONObject(ControllerUserDomain.getUniqueInstance().facebookLogin());
                    if (json.getBoolean("loggedIn")) {

                        showToast(getResources().getString(R.string.login_success));
                        goToActivity(InsideActivity.class, new Bundle(), Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                    } else {

                        showToast(getResources().getString(R.string.login_success));

                        JSONObject info = json.getJSONObject("info");

                        Bundle bundle = new Bundle();
                        bundle.putString("username", info.getString("username"));
                        bundle.putString("mail", info.getString("email"));
                        bundle.putString("firstName", info.getString("name"));
                        bundle.putString("lastName", info.getString("surname"));
                        bundle.putString("birthDate", info.getString("birthday"));
                        bundle.putString("country", info.getString("country"));

                        goToActivity(DataRegisterView.class, bundle, 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
                callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void goToActivity(Class c, Bundle extras, int flags) {

        Intent intent = new Intent(this, c);
        intent.putExtras(extras);
        intent.addFlags(flags);
        startActivity(intent);
    }

    public void signUpMail(View view) {
        goToActivity(ViewSignUpMail.class, new Bundle(), 0);
    }

    public void logInGuest(View view) {

    }

    public void logInMail(View view) {

    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
