package com.example.harry.mynews.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.harry.mynews.Adapters.HeadlinesAdapter;
import com.example.harry.mynews.Adapters.RecyclerItemClickListener;
import com.example.harry.mynews.App;
import com.example.harry.mynews.Model.Firebase.UserModel;
import com.example.harry.mynews.Model.HeadlineItem;
import com.example.harry.mynews.Model.StatusResponseObject;
import com.example.harry.mynews.R;
import com.example.harry.mynews.ViewModel.NewsViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    //Recycler view attrs

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.headlines_progressbar)
    ProgressBar progressBar;
    @Inject
    NewsViewModel viewModel;
    @Inject
    HeadlinesAdapter adapter;

    Disposable newsSourcesSubscription;

    @SuppressLint("CheckResult")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        super.setUpSideNavAndToolbar();

        //Injections
        ((App) getApplication()).getMainComponent().inject(this);
        ButterKnife.bind(this);
        showProgress(true);
        subscribeToUserNewsRecourse();

    }
    private void showProgress(boolean hide) {
        if (hide) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();
        showProgress(true);
        subscribeToUserNewsRecourse();

    }

    @SuppressLint("CheckResult")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void subscribeToUserNewsRecourse() {
        newsSourcesSubscription = userViewModel.getUserRecourse().subscribe(
                onNext -> {
                    if (onNext == null | onNext.preferredSources == null) {
                        viewModel.loadCountryWithPermissions(this);
                        viewModel.loadTopHeadlinesByCountry();
                    } else {
                        getUserHeadlinesBySource(onNext);
                    }
                    setUpRecyclerView();
                    showProgress(false);
                }
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getUserHeadlinesBySource(UserModel model) {
        final StringBuilder sourcesString = new StringBuilder();
        model.preferredSources.forEach(s -> sourcesString.append(s).append(","));
        viewModel.loadTopHeadlinesBySources(sourcesString.toString());
    }

    @SuppressLint("CheckResult")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final List<HeadlineItem> items = new ArrayList<>();
        viewModel.getLoadedHeadlines()
                .subscribeOn(Schedulers.io())
                .subscribe(
                        (StatusResponseObject next) -> {
                            next.getArticles().forEach(a ->
                                    items.add(new HeadlineItem(
                                            a.getTitle(),
                                            a.getDescription(),
                                            a.getUrlToImage(),
                                            a.getUrl(),
                                            a.getSource().getName())));
                            runOnUiThread(() -> adapter.notifyDataSetChanged());
                        }

                );
        adapter.setHeadlineItems(items);
        recyclerView.setAdapter(adapter);
        this.addRecyclerOnTouchEvent(items);
    }


    private void presentErrorToUser(Exception error) {
        Toast.makeText(this.getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void addRecyclerOnTouchEvent(List<HeadlineItem> items) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        newsSourcesSubscription.dispose();
    }
}
