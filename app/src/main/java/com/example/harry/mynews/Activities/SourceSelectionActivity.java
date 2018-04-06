package com.example.harry.mynews.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.harry.mynews.Adapters.NewsSourceAdapter;
import com.example.harry.mynews.Adapters.RecyclerItemClickListener;
import com.example.harry.mynews.App;
import com.example.harry.mynews.Model.NewsSourceItem;
import com.example.harry.mynews.Model.NewsSourceSourceResponseObject;
import com.example.harry.mynews.R;
import com.example.harry.mynews.ViewModel.NewsViewModel;
import com.example.harry.mynews.ViewModel.SourcesViewModel;
import com.example.harry.mynews.ViewModel.UserViewModel;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.schedulers.Schedulers;

public class SourceSelectionActivity extends BaseActivity {
    @BindView(R.id.news_source_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Inject
    SourcesViewModel sourcesViewModel;
    @Inject
    NewsSourceAdapter adapter;
    @Inject
    UserViewModel userViewModel;
    @Inject
    DatabaseReference database;
    @Inject
    NewsViewModel newsViewModel;
    private android.view.ActionMode actionMode;

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
        sourcesViewModel.getNewsSources(null, null)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        (NewsSourceSourceResponseObject next) -> {
                            next.getSources().forEach(a ->
                                    sourcesViewModel.getAllNewsSources().add(new NewsSourceItem(
                                            a.getId(),
                                            a.getLanguage(),
                                            a.getName(),
                                            a.getCountry())));
                            runOnUiThread(() -> adapter.notifyDataSetChanged());
                        }

                );
        adapter.setNewsSourceItem(sourcesViewModel.getAllNewsSources());
        recyclerView.setAdapter(adapter);
        addRecyclerOnTouchEvent(this);
    }

    private void addRecyclerOnTouchEvent(SourceSelectionActivity activity) {
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                addOrRemoveSource(view, position);
                                if (actionMode == null) {
                                    actionMode = activity.startActionMode(new android.view.ActionMode.Callback() {
                                        @Override
                                        public boolean onCreateActionMode(android.view.ActionMode actionMode, Menu menu) {
                                            MenuInflater inflater = actionMode.getMenuInflater();
                                            inflater.inflate(R.menu.actionmode, menu);
                                            return true;
                                        }

                                        @Override
                                        public boolean onPrepareActionMode(android.view.ActionMode actionMode, Menu menu) {
                                            return false;
                                        }

                                        @Override
                                        public boolean onActionItemClicked(android.view.ActionMode actionMode, MenuItem menuItem) {
                                            switch (menuItem.getItemId()) {
                                                case R.id.save:
                                                    activity.saveUserSources();
                                                    actionMode.finish();
                                                    return true;
                                            }
                                            return false;
                                        }

                                        @Override
                                        public void onDestroyActionMode(android.view.ActionMode actionMode) {
                                            activity.clearSources(activity);
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                            }
                        }));
    }
    private void clearSources(SourceSelectionActivity activity) {
        activity.actionMode = null;
        SparseBooleanArray arr = sourcesViewModel.getSelectedItems();
        for (int i = 0; i < arr.size(); i++) {
            int key = arr.keyAt(i);
            // get the object by the key.
            if (arr.get(key)) {
                recyclerView.findViewHolderForAdapterPosition(key)
                        .itemView.findViewById(R.id.news_source_cardView)
                        .setBackgroundColor(Color.WHITE);
            }
        }
        arr.clear();
    }
    private void saveUserSources() {
        List<String> sourcesById = new ArrayList<>();
        SparseBooleanArray arr = sourcesViewModel.getSelectedItems();

        for (int i = 0; i < arr.size(); i++) {
            int key = arr.keyAt(i);
            // get the object by the key.
            if (arr.get(key)) {
                sourcesById.add(sourcesViewModel.getAllNewsSources().get(key).getId());
            }
        }
        userViewModel.addSources(sourcesById);
    }

    private void addOrRemoveSource(View view, int position) {
        if (!sourcesViewModel.getSelectedItems().get(position, false)) {
            sourcesViewModel.getSelectedItems().put(position, true);
            view.findViewById(R.id.news_source_cardView).setBackgroundColor(Color.LTGRAY);
        } else {
            sourcesViewModel.getSelectedItems().put(position, false);
            view.findViewById(R.id.news_source_cardView).setBackgroundColor(Color.WHITE);
        }
    }
}
