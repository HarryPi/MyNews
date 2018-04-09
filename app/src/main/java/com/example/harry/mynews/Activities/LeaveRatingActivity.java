package com.example.harry.mynews.Activities;

import android.support.v7.app.AppCompatActivity;
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

public class LeaveRatingActivity extends AppCompatActivity {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_rating);
        ((App) getApplication()).getMainComponent().inject(this);
        ButterKnife.bind(this);
        title.setText(getIntent().getStringExtra("title"));
        description.setText(getIntent().getStringExtra("description"));
    }

    @OnClick(R.id.leave_rating_source_submit)
    void submitReview() {
        sourceViewModel.updateSourceProviderRating(
                getIntent().getStringExtra("id"),
                ratingBar.getRating(),
                reviewField.getText().toString());
    }
}
