package com.example.harry.mynews.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.harry.mynews.App;
import com.example.harry.mynews.R;
import com.example.harry.mynews.ViewModel.UserViewModel;
import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.Optional;

public class LogInActivity extends AppCompatActivity {
    private static final int RESPONSE_CODE = 123;

    @Inject
    UserViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ((App) getApplication()).getMainComponent().inject(this);
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                2
        );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESPONSE_CODE) {
            startActivity(new Intent(LogInActivity.this, MainActivity.class));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("PERMISSION", " permission granted");
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                        showMessageOkCancel("You need to allow access to location to get local headlines",
                                (dialog, which) -> {

                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            ActivityCompat.requestPermissions(
                                                    this,
                                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                                    2
                                            );
                                            break;
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            Toast.makeText(LogInActivity.this, "Storage permission required to continue", Toast.LENGTH_LONG)
                                                    .show();
                                            finishAffinity();
                                            break;
                                    }
                                });
                    } else {
                        Toast.makeText(this, "Go to settings and enable storage permissions", Toast.LENGTH_LONG)
                                .show();
                        finishAffinity();
                    }
                }
                // if there is not user when our app loads code goes here
                if (viewModel.getFirebaseUser() == null) {
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
                            RESPONSE_CODE);

                } else {
                    startActivity(new Intent(LogInActivity.this, MainActivity.class));
                }
        }
    }

    private void showMessageOkCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }
}
