package com.example.harry.mynews.Adapters;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.harry.mynews.R;

import java.util.List;

import com.example.harry.mynews.Model.HeadlineItem;
import com.example.harry.mynews.Util.RoundedCornersTransform;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by harry on 15/03/2018.
 * Adapter to inflate news headlines on main activity
 */

public class HeadlinesAdapter extends RecyclerView.Adapter<HeadlinesAdapter.ViewHolder> {
    private Context context;
    private List<HeadlineItem> headlineItems;
    private Picasso picasso;

    @Inject
    public HeadlinesAdapter(Application application, Picasso picasso) {
        this.context = application.getApplicationContext();
        this.picasso = picasso;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HeadlinesAdapter.ViewHolder holder, int position) {
        HeadlineItem item = headlineItems.get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        holder.sourceName.setText(item.getSourceName());
        picasso.load(item.getImgUrl())
                .transform(new RoundedCornersTransform())
                .placeholder(R.drawable.loading_animation)
                .error(R.mipmap.ic_image)
                .fit()
                .into(holder.articleImage);
    }


    public void setHeadlineItems(@NonNull List<HeadlineItem> headlineItems) {
        this.headlineItems = headlineItems;
    }

    @Override
    public int getItemCount() {
        return headlineItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cardview_title)
        TextView title;
        @BindView(R.id.cardview_description)
        TextView description;
        @BindView(R.id.cardviewImageView)
        ImageView articleImage;
        @BindView(R.id.cardiview_source_provider_name)
        TextView sourceName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
