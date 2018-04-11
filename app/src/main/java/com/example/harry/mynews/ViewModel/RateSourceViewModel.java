package com.example.harry.mynews.ViewModel;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.util.Log;

import com.example.harry.mynews.Model.Firebase.RatingModel;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RateSourceViewModel {
    private static final String TAG = UserViewModel.class.getSimpleName();
    private FirebaseUser user;
    private DatabaseReference databaseReference;

    private final String SOURCE_PROVIDERS = "sourceProviders";
    private final String REVIEWS = "value";
    private final String USERS = "users";


    public RateSourceViewModel(FirebaseUser user, DatabaseReference databaseReference) {
        this.user = user;
        this.databaseReference = databaseReference;
    }

    public void updateSourceProviderRating(String providerId, float rating, String comment) {
        RatingModel model = new RatingModel(comment, user.getDisplayName(), rating, user.getUid());
        databaseReference.child(SOURCE_PROVIDERS).child(providerId).push().setValue(model); // push so that it wont overwrite node
        databaseReference.child(SOURCE_PROVIDERS).child(providerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getChildren().forEach(child -> {
                    RatingModel model = child.getValue(RatingModel.class);
                    Log.d(TAG, model.toString());
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

class ReviewsHolder {
    private List<RatingModel> value;

    public ReviewsHolder() {
    }

    public ReviewsHolder(List<RatingModel> value) {
        this.value = value;
    }

    public List<RatingModel> getValue() {
        return value;
    }

    public void setValue(List<RatingModel> value) {
        this.value = value;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("value", value);

        return result;
    }

}
