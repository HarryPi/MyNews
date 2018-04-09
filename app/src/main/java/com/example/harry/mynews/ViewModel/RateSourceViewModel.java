package com.example.harry.mynews.ViewModel;

import com.example.harry.mynews.Model.Firebase.RatingModel;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RateSourceViewModel {
    private static final String TAG = UserViewModel.class.getSimpleName();
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private final String SOURCE_PROVIDERS = "sourceProviders";
    private final String REVIEWS = "reviews";


    public RateSourceViewModel(FirebaseUser user, DatabaseReference databaseReference) {
        this.user = user;
        this.databaseReference = databaseReference;
    }

    public void updateSourceProviderRating(String providerId, float rating, String comment) {
        RatingModel model = new RatingModel(comment, user.getDisplayName(), rating, user.getUid());
        databaseReference.child(SOURCE_PROVIDERS).child(providerId).push().setValue(model); // push so that it wont overwrite node
    }
}

class ReviewsHolder {
    private List<RatingModel> reviews;

    public ReviewsHolder() {
    }

    public ReviewsHolder(List<RatingModel> reviews) {
        this.reviews = reviews;
    }

    public List<RatingModel> getReviews() {
        return reviews;
    }

    public void setReviews(List<RatingModel> reviews) {
        this.reviews = reviews;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("reviews", reviews);

        return result;
    }

}
