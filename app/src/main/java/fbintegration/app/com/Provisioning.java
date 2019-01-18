package fbintegration.app.com;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class Provisioning extends Activity {

    private TextView info;
    private Profile mProfil;

    private LoginButton mLoginButton;
    private CallbackManager callbackManager;

    private String LOG_TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.provisioning_layout);
        info = findViewById(R.id.info);
        mLoginButton = findViewById(R.id.provisioning_button);

        mLoginButton.setReadPermissions("user_events");
        mLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                goToSessionDataActivity();
            }

            @Override
            public void onCancel() {
                info.setText(getString(R.string.login_canceled));
            }

            @Override
            public void onError(FacebookException e) {
                info.setText(getString(R.string.login_error));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mProfil = Profile.getCurrentProfile();

        if (mProfil != null) {
            goToSessionDataActivity();
            return;
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void goToSessionDataActivity() {
        finish();
        Intent sessionData = new Intent();
        sessionData.setClass(getApplicationContext(), SessionDataActivity.class);
        startActivity(sessionData);
    }
}

