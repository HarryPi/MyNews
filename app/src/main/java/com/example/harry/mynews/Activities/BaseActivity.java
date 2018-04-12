package com.example.harry.mynews.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.harry.mynews.App;
import com.example.harry.mynews.R;
import com.example.harry.mynews.ViewModel.UserViewModel;
import com.firebase.ui.auth.AuthUI;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

public class BaseActivity extends AppCompatActivity
{
    RelativeLayout fullLayout;
    FrameLayout frameLayout;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    List<Disposable> subscriptionsToDispose = new ArrayList<>();
    @Inject
    UserViewModel userViewModel;

    private static final String LOGOUT_MESSAGE = "Are you sure you want to log out?";
    private static final String CONFIRM = "Yes";
    private static final String CANCEL = "No";

    public void setUpSideNavAndToolbar() {

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        navigationView.setNavigationItemSelectedListener(item -> {
            onNavItemClickAction(item);
            return true;
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscriptionsToDispose.forEach(Disposable::dispose);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        ((App) getApplication()).getMainComponent().inject(this);
    }

    @Override
    public void setContentView(int layoutRes) {


        fullLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.activity_basic,null);
        frameLayout = fullLayout.findViewById(R.id.content_frame);
        getLayoutInflater().inflate(layoutRes, frameLayout, true);

        super.setContentView(fullLayout);

        // We do not use butterknife here as butterknife can ONLY BIND ONCE,
        // thus if we bind here no class that inherits from our base class can bind
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
    }

    private void onNavItemClickAction(@NonNull MenuItem item) {
        item.setChecked(true); // This is to persist click state

        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            onLogOutAction();
        }
        else if (id == R.id.nav_sources) {
            loadNewsSources();
        }
        else if (id == R.id.nav_headlines) {
            loadUserHeadlines();
        }
        else if (id == R.id.nav_wall) {
            loadWall();
        }
        drawerLayout.closeDrawers();
        item.setChecked(false);
    }

    private void loadWall() {
        startActivity(new Intent(BaseActivity.this, RateProvidersActivity.class));
    }

    private void loadUserHeadlines() {
        Intent intent = new Intent(BaseActivity.this, MainActivity.class);
        startActivity(intent);
    }
    private void loadNewsSources() {
        Intent intent = new Intent(BaseActivity.this, SourceSelectionActivity.class);
        startActivity(intent);
    }

    private void onLogOutAction() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogInterface.OnClickListener dialogListener = createConfirmDialog();
        builder.setMessage(LOGOUT_MESSAGE)
                .setPositiveButton(CONFIRM, dialogListener)
                .setNegativeButton(CANCEL, dialogListener)
                .show();
    }
    private DialogInterface.OnClickListener createConfirmDialog() {
        return (dialogInterface, flag) -> {
            switch (flag) {
                case DialogInterface.BUTTON_POSITIVE:
                    //logout
                    AuthUI.getInstance().signOut(this);
                    userViewModel.setFirebaseUser(null);
                    startActivity(new Intent(this, LogInActivity.class));
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    //cancel
                    break;
            }
        };
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                // This ensures nav drawer open animation
                // behaves properly for both right-to-left and left-to-right layouts
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
