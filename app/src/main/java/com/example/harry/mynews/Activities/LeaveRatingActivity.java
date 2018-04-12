package com.example.harry.mynews.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.harry.mynews.App;
import com.example.harry.mynews.R;
import com.example.harry.mynews.ViewModel.RateSourceViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LeaveRatingActivity extends BaseActivity {
    @BindView(R.id.leave_rating_source_description)
    TextView description;
    @BindView(R.id.leave_rating_source_title)
    TextView title;
    @BindView(R.id.leave_rating_source_ratingbar)
    RatingBar ratingBar;
    @BindView(R.id.leave_rating_source_review)
    EditText reviewField;
    @BindView(R.id.leave_rating_source_submit)
    Button submit;
    @Inject
    RateSourceViewModel
            sourceViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_rating);
        ((App) getApplication()).getMainComponent().inject(this);
        ButterKnife.bind(this);
        title.setText(getIntent().getStringExtra("title"));
        description.setText(getIntent().getStringExtra("description"));

        if (getIntent().getBooleanExtra("isReviewed", false)) {
            subscriptionsToDispose.add(sourceViewModel.getSingeRatingForProviderByCurrUser(getIntent().getStringExtra("id"), false).subscribe(
                    ratingModels -> {
                        ratingBar.setRating(ratingModels.getRating());
                        reviewField.setText(ratingModels.getComment());
                    }
            ));
        }
    }

    @OnClick(R.id.leave_rating_source_submit)
    void submitReview() {
        if (getIntent().getBooleanExtra("isReviewed", false)) {
            sourceViewModel.updateSourceProviderRating(
                    getIntent().getStringExtra("id"),
                    ratingBar.getRating(),
                    reviewField.getText().toString(), true);
        } else {
            sourceViewModel.updateSourceProviderRating(
                    getIntent().getStringExtra("id"),
                    ratingBar.getRating(),
                    reviewField.getText().toString(), false);
        }
        sourceViewModel.getUserRatings(true); // Reload user ratings
        sourceViewModel.reloadInMemorySourceRatings();
    }
}
