package com.example.harry.mynews.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.harry.mynews.Adapters.HeadlinesAdapter;
import com.example.harry.mynews.Adapters.RecyclerItemClickListener;
import com.example.harry.mynews.App;
import com.example.harry.mynews.Model.ListItem;
import com.example.harry.mynews.Model.StatusResponseObject;
import com.example.harry.mynews.R;
import com.example.harry.mynews.ViewModel.HeadlinesViewModel;
import com.firebase.ui.auth.AuthUI;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    private static final String LOGOUT_MESSAGE = "Are you sure you want to log out?";
    private static final String CONFIRM = "Yes";
    private static final String CANCEL = "No";

    //Recycler view attrs

    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recicler_view_id)
    RecyclerView recyclerView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Inject
    HeadlinesViewModel viewModel;
    @Inject
    HeadlinesAdapter adapter;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Injections
        ((App) getApplication()).getMainComponent().inject(this);
        ButterKnife.bind(this);

        viewModel.loadCountryWithPermissions(this);
        viewModel.loadTopHeadlines();

        setUpSideNav();
        setUpRecyclerView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                // Notice that the code above passes GravityCompat.START as the open drawer animation
                // gravity to openDrawer(). This ensures nav drawer open animation
                // behaves properly for both right-to-left and left-to-right layouts
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private DialogInterface.OnClickListener createConfirmDialog() {
        return (dialogInterface, flag) -> {
            switch (flag) {
                case DialogInterface.BUTTON_POSITIVE:
                    //logout
                    AuthUI.getInstance().signOut(this);
                    startActivity(new Intent(this, LogInActivity.class));
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    //cancel
                    break;
            }
        };
    }

    private void setUpSideNav() {

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        navigationView.setNavigationItemSelectedListener(item -> {
            onNavItemClickAction(item);
            return true;
        });
    }

    private void onNavItemClickAction(@NonNull MenuItem item) {
        item.setChecked(true); // This is to persist click state

        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            onLogOutAction();
        }
        else if (id == R.id.nav_sources) {

        }
        drawerLayout.closeDrawers();
    }

    private void onLogOutAction() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogInterface.OnClickListener dialogListener = createConfirmDialog();
        builder.setMessage(LOGOUT_MESSAGE)
                .setPositiveButton(CONFIRM, dialogListener)
                .setNegativeButton(CANCEL, dialogListener)
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final List<ListItem> items = new ArrayList<>();
        viewModel.getLoadedHeadlines()
                .subscribeOn(Schedulers.io())
                .subscribe(
                        (StatusResponseObject next) -> {
                            next.getArticles().forEach(a ->
                                    items.add(new ListItem(
                                            a.getTitle(),
                                            a.getDescription(),
                                            a.getUrlToImage(),
                                            a.getUrl())));
                            runOnUiThread(() -> adapter.notifyDataSetChanged());
                        }

                );
        adapter.setListItems(items);
        recyclerView.setAdapter(adapter);
        this.addRecyclerOnTouchEvent(items);
    }


    private void presentErrorToUser(Exception error) {
        Toast.makeText(this.getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
    }
    private void addRecyclerOnTouchEvent(List<ListItem> items){
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent = new Intent(MainActivity.this, HeadlineUrlActivity.class);
                                intent.putExtra("url", items.get(position).getUrlToArticle());
                                startActivity(intent);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                //nothing later on perhaps rating and commenting?
                            }
                        }));
    }
}
