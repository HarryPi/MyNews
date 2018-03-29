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

import com.example.harry.mynews.Model.ListItem;
import com.example.harry.mynews.R;
import com.example.harry.mynews.Util.RoundedCornersTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by harry on 26/03/2018.
 */

public class NewsSourceAdapter extends RecyclerView.Adapter<NewsSourceAdapter.ViewHolder> {
    private Context context;
    private List<ListItem> listItems;

    @Inject
    public NewsSourceAdapter(Application application) {
        this.context = application.getApplicationContext();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListItem item = listItems.get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
    }

    public void setListItems(@NonNull List<ListItem> listItems) {
        this.listItems = listItems;
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cardview_title)
        TextView title;
        @BindView(R.id.cardview_description)
        TextView description;
        @BindView(R.id.cardviewImageView)
        ImageView articleImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
