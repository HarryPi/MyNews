package com.example.harry.mynews.Adapters;


import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.harry.mynews.Model.NewsSourceItem;
import com.example.harry.mynews.R;
import com.example.harry.mynews.ViewModel.RateSourceViewModel;
import com.example.harry.mynews.ViewModel.SourcesViewModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by harry on 26/03/2018.
 */

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder> {
    private Context context;
    private List<NewsSourceItem> newsSourceItem;
    private IRatingClickListener listener;
    @Inject
    RateSourceViewModel rateSourceViewModel;

    public interface IRatingClickListener {
        public void addRating(NewsSourceItem item);
        public void seeAllRatings(String providerId);
    }

    @Inject
    public RatingAdapter(Application application) {
        this.context = application.getApplicationContext();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rate_news_source_list_row, parent, false);

        return new ViewHolder(view);

    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.setIsRecyclable(false);
        rateSourceViewModel.getUserRatingsFromMemoryOrReload(false).subscribe(
                ratedList -> {
                    NewsSourceItem item = newsSourceItem.get(position);
                    holder.name.setText(item.getName());
                    holder.country.setText(context.getString(R.string.country, item.getCountry()));
                    holder.language.setText(context.getString(R.string.language, item.getLanguage()));
                    holder.addCommentBtn.setOnClickListener(view -> listener.addRating(item));
                    holder.seeCommentsBtn.setOnClickListener(view -> listener.seeAllRatings(item.getId()));
                    if (ratedList.containsKey(item.getId())) {
                        holder.addCommentBtn.setText("Edit your review");
                        newsSourceItem.get(position).setReviewd(true);
                    }
                }
        );


    }

    public void setListener(IRatingClickListener listener) {
        this.listener = listener;
    }

    public void setNewsSourceItem(@NonNull List<NewsSourceItem> newsSourceItem) {
        this.newsSourceItem = newsSourceItem;
    }

    @Override
    public int getItemCount() {
        return newsSourceItem.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rate_news_source_cardview_name)
        TextView name;
        @BindView(R.id.rate_news_source_cardview_country)
        TextView country;
        @BindView(R.id.rate_news_source_cardview_language)
        TextView language;
        @BindView(R.id.rate_news_source_cardview_add_comment)
        TextView addCommentBtn;
        @BindView(R.id.rate_news_source_cardview_see_all_comments)
        TextView seeCommentsBtn;

        @BindView(R.id.rate_news_source_cardView)
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
