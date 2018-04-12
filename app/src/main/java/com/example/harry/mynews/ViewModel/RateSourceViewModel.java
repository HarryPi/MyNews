package com.example.harry.mynews.ViewModel;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.harry.mynews.Model.Firebase.RatingModel;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.subjects.BehaviorSubject;

public class RateSourceViewModel {
    private static final String TAG = UserViewModel.class.getSimpleName();
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private BehaviorSubject<HashMap<String, RatingModel>> userRatings = BehaviorSubject.create();
    private HashMap<String, RatingModel> ratedByUserProviders;
    private HashMap<String, Float> inMemorySourceRatings = new HashMap<>();
    private final String SOURCE_PROVIDERS = "sourceProviders";
    private final String REVIEWS = "value";
    private final String USERS = "users";
    private final String PROVIDER_ID = "providerId";


    public RateSourceViewModel(FirebaseUser user, DatabaseReference databaseReference) {
        this.user = user;
        this.databaseReference = databaseReference;
    }

    /**
     * Gets the users reviews if not loaded in memory
     * or reloads in case user has made a new review so that the view can get the new data
     * if no data is loaded will force load data by setting @reload as true in a recursive call
     *
     * @param reload Indicates if the data should be reloaded
     */
    public BehaviorSubject<HashMap<String, RatingModel>> getUserRatings(boolean reload) {
            ratedByUserProviders = new HashMap<>();
            databaseReference.child(USERS).child(user.getUid()).child(REVIEWS).addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dataSnapshot.getChildren().forEach(child ->
                    {
                        RatingModel model = child.getValue(RatingModel.class);
                        if (model != null) {
                            ratedByUserProviders.put(model.getProviderId(), model);
                        }
                    });
                    userRatings.onNext(ratedByUserProviders);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, databaseError.getMessage());
                }
            });
        return userRatings;
    }

    public BehaviorSubject<RatingModel> getSingeRatingForProviderByCurrUser(String providerId, boolean fromMemoryIfAvailable) {
        BehaviorSubject<RatingModel> subject = BehaviorSubject.create();
        if (fromMemoryIfAvailable) {
            if (!ratedByUserProviders.isEmpty()) {
                if (ratedByUserProviders.containsKey(providerId))
                    subject.onNext(ratedByUserProviders.get(providerId));
                else
                    getSingeRatingForProviderByCurrUser(providerId, false);
            } else {
                getSingeRatingForProviderByCurrUser(providerId, false);
            }
        } else {
            databaseReference
                    .child(USERS)
                    .child(user.getUid())
                    .child(REVIEWS)
                    .orderByChild(PROVIDER_ID)
                    .equalTo(providerId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            dataSnapshot.getChildren().forEach(child ->
                            {
                                RatingModel model = child.getValue(RatingModel.class);
                                subject.onNext(model);
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d(TAG, databaseError.getMessage());
                        }
                    });

        }
        return subject;
    }

    public void updateSourceProviderRating(String providerId, float rating, String comment, boolean isReviewed) {
        RatingModel model = new RatingModel(comment, user.getDisplayName(), rating, user.getUid(), providerId);
        if (isReviewed) {
            HashMap<String, Object> child = new HashMap<>();
            child.put("comment", model.getComment());
            child.put("rating", model.getRating());
            databaseReference
                    .child(SOURCE_PROVIDERS)
                    .child(providerId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            dataSnapshot.getChildren().forEach(node -> {
                                RatingModel model = node.getValue(RatingModel.class);
                                if (model.getUserId().equals(user.getUid())) {
                                    model.setComment(comment);
                                    model.setRating(rating);
                                    node.getRef().updateChildren(model.toMap());
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d(TAG, databaseError.getMessage());
                        }
                    });
        } else {
            // make sure if review for provider exists update that instead
            // check to happen elsewhere
            databaseReference.child(SOURCE_PROVIDERS).child(providerId).push().setValue(model); // push so that it wont overwrite node
            databaseReference.child(USERS).child(user.getUid()).child(REVIEWS).push().setValue(model);
            // We create a circular reference to maintain a better data structural integrity
            // And easier access when querying
        }
    }
    public void reloadInMemorySourceRatings () {
        inMemorySourceRatings.clear();
    }

    /**
     * Returns an observable object that will return the rating once done
     * @param sourceProviderId The source provider id
     * @param fromMemoryIfAvailable Will check if value exists before fetching it from database */
    public Observable<Float> getRatingsForSourceProvider(String sourceProviderId, boolean fromMemoryIfAvailable) {
        return Observable.create( sub -> {
            if (fromMemoryIfAvailable) {
                if (inMemorySourceRatings.containsKey(sourceProviderId)) {
                    sub.onNext(inMemorySourceRatings.get(sourceProviderId));
                    sub.onComplete();
                } else
                    getRatingFromDatabase(sub, sourceProviderId);
            } else {
                getRatingFromDatabase(sub, sourceProviderId);
            }

        });
    }
    private void getRatingFromDatabase(ObservableEmitter<Float> sub, String sourceProviderId) {
        databaseReference
                .child(SOURCE_PROVIDERS)
                .child(sourceProviderId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        AtomicReference<Float> rating = new AtomicReference(0.0f);
                        AtomicInteger size = new AtomicInteger(0);
                        dataSnapshot.getChildren().forEach(child -> {
                            RatingModel model = child.getValue(RatingModel.class);
                            rating.getAndUpdate(r -> r + model.getRating());
                            size.getAndIncrement();
                        });
                        rating.getAndUpdate(r -> r / size.get());
                        inMemorySourceRatings.put(sourceProviderId, rating.get());
                        sub.onNext(rating.get());
                        sub.onComplete();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }
}
