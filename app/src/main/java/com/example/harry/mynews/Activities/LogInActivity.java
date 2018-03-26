package com.example.harry.mynews.Activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.harry.mynews.R;
import com.example.harry.mynews.Util.RequestCodeResolver;
import com.facebook.CallbackManager;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class LogInActivity extends AppCompatActivity {
    private static final String EMAIL = "email";
    private static RequestCodeResolver requestCodeResolver;

    // This is the facebook callback manager
    private CallbackManager callbackManager = CallbackManager.Factory.create();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        FirebaseUser user = fAuth.getCurrentUser();
        requestCodeResolver = new RequestCodeResolver(getResources());
        // if there is not user when our app loads code goes here
        if (user == null) {
             startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.EmailBuilder().build(),
                                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                                    new AuthUI.IdpConfig.FacebookBuilder().build()
                            ))
                            .build(),
                    requestCodeResolver.getNormalRegistrationCode());

        } else {
            startActivity(new Intent(LogInActivity.this, MainActivity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCodeResolver.getNormalRegistrationCode()) {
            startActivity(new Intent(LogInActivity.this, MainActivity.class));
        }
    }
}
