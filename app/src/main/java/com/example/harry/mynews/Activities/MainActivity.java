package com.example.harry.mynews.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.harry.mynews.Adapters.HeadlinesAdapter;
import com.example.harry.mynews.Adapters.RecyclerItemClickListener;
import com.example.harry.mynews.App;
import com.example.harry.mynews.Model.ListItem;
import com.example.harry.mynews.Model.StatusResponseObject;
import com.example.harry.mynews.R;
import com.example.harry.mynews.ViewModel.HeadlinesViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    //Recycler view attrs

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Inject
    HeadlinesViewModel viewModel;
    @Inject
    HeadlinesAdapter adapter;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        super.setUpSideNav();

        //Injections
        ((App) getApplication()).getMainComponent().inject(this);
        ButterKnife.bind(this);

        viewModel.loadCountryWithPermissions(this);
        viewModel.loadTopHeadlines();

        setUpRecyclerView();

    }

    @SuppressLint("CheckResult")
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

    private void addRecyclerOnTouchEvent(List<ListItem> items) {
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
