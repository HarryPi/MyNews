package com.example.harry.mynews.Adapters;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.harry.mynews.Model.HeadlineItem;
import com.example.harry.mynews.Model.NewsSourceItem;
import com.example.harry.mynews.R;
import com.example.harry.mynews.ViewModel.SourcesViewModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by harry on 26/03/2018.
 */

public class NewsSourceAdapter extends RecyclerView.Adapter<NewsSourceAdapter.ViewHolder> {
    private Context context;
    private List<NewsSourceItem> newsSourceItem;

    @Inject
    SourcesViewModel viewModel;

    @Inject
    public NewsSourceAdapter(Application application) {
        this.context = application.getApplicationContext();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_source_list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsSourceAdapter.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        NewsSourceItem item = newsSourceItem.get(position);
        holder.name.setText(item.getName());
        holder.country.setText(context.getString(R.string.country, item.getCountry()));
        holder.language.setText(context.getString(R.string.language, item.getLanguage()));
        if (viewModel.getSelectedItems().get(position, false)) {
            holder.cardView.setCardBackgroundColor(Color.LTGRAY);
        } else {
            holder.cardView.setBackgroundColor(Color.WHITE);
        }
    }

    public void setNewsSourceItem(@NonNull List<NewsSourceItem> newsSourceItem) {
        this.newsSourceItem = newsSourceItem;
    }

    @Override
    public int getItemCount() {
        return newsSourceItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.news_source_cardview_name)
        TextView name;
        @BindView(R.id.news_source_cardview_country)
        TextView country;
        @BindView(R.id.news_source_cardview_language)
        TextView language;
        @BindView(R.id.news_source_cardView)
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }

}
