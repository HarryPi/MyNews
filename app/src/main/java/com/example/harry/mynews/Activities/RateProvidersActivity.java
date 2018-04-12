package com.example.harry.mynews.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.harry.mynews.Adapters.RatingAdapter;
import com.example.harry.mynews.App;
import com.example.harry.mynews.Model.NewsSourceItem;
import com.example.harry.mynews.Model.NewsSourceSourceResponseObject;
import com.example.harry.mynews.R;
import com.example.harry.mynews.ViewModel.RateSourceViewModel;
import com.example.harry.mynews.ViewModel.SourcesViewModel;
import com.example.harry.mynews.ViewModel.UserViewModel;
import com.google.firebase.database.DatabaseReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.schedulers.Schedulers;

public class RateProvidersActivity extends BaseActivity implements RatingAdapter.IRatingClickListener {
    @BindView(R.id.news_source_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Inject
    SourcesViewModel sourcesViewModel;
    @Inject
    RatingAdapter adapter;
    @Inject
    UserViewModel userViewModel;
    @Inject
    DatabaseReference database;
    @Inject
    RateSourceViewModel
            rateSourceViewModel;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_selection);
        ((App) getApplication()).getMainComponent().inject(this);
        ButterKnife.bind(this);
        super.setUpSideNavAndToolbar();
        this.setUpRecyclerView();
    }

    @SuppressLint("CheckResult")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        subscriptionsToDispose.add(sourcesViewModel.getNewsSources(null, null)
                .subscribeOn(Schedulers.io())
                .doOnComplete(() -> runOnUiThread(() -> {
                    adapter.setListener(this);
                    adapter.setNewsSourceItem(sourcesViewModel.getAllNewsSources());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }))
                .subscribe(
                        (NewsSourceSourceResponseObject next) ->
                                next.getSources().forEach(a ->
                                {
                                    NewsSourceItem item = new NewsSourceItem(
                                            a.getId(),
                                            a.getLanguage(),
                                            a.getName(),
                                            a.getCountry(),
                                            a.getDescription());
                                    sourcesViewModel.getAllNewsSources().add(item);
                                })
                ));
    }

    @Override
    public void addRating(NewsSourceItem item) {
        Intent intent = new Intent(RateProvidersActivity.this, LeaveRatingActivity.class);
        intent.putExtra("id", item.getId());
        intent.putExtra("title", item.getName());
        intent.putExtra("description", item.getDescription());
        intent.putExtra("isReviewed", item.isReviewd());
        startActivity(intent);
    }

    @Override
    public void seeAllRatings(String providerId) {

    }
}
