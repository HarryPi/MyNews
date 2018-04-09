package com.example.harry.mynews.ViewModel;

import android.util.Log;
import android.widget.LinearLayout;

import com.example.harry.mynews.Model.Firebase.RatingModel;
import com.example.harry.mynews.Model.Firebase.UserModel;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.security.acl.LastOwnerException;
import java.util.List;

import io.reactivex.subjects.BehaviorSubject;

public class UserViewModel {
    private FirebaseUser firebaseUser;
    private static final String TAG = UserViewModel.class.getSimpleName();
    private BehaviorSubject<UserModel> userModel = BehaviorSubject.create();
    private DatabaseReference databaseReference;

    private final String USERS = "users";
    private final String PREFERRED_SOURCES = "preferredSources";

    public UserViewModel(FirebaseUser firebaseUser, DatabaseReference databaseReference) {
        this.firebaseUser = firebaseUser;
        this.databaseReference = databaseReference;
    }

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public void setFirebaseUser(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
    }

    public void addSources(List<String> sources) {
        databaseReference.child(USERS).child(firebaseUser.getUid()).child(PREFERRED_SOURCES).setValue(sources);
    }
    public BehaviorSubject<UserModel> getUserRecourse() {
        databaseReference.child(USERS).child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               userModel.onNext(dataSnapshot.getValue(UserModel.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        });
        return userModel;
    }

}
