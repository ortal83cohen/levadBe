package com.query.social.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Ortal Cohen on 30/5/2017.
 */

public class UserViewModel extends ViewModel {

    private final MutableLiveData<FirebaseUser> user = new MutableLiveData<>();

    public UserViewModel() {
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user.setValue(firebaseAuth.getCurrentUser());
            }
        });
    }
    public LiveData<FirebaseUser> getUser() {
        if (user.getValue() == null) {
            user.setValue(FirebaseAuth.getInstance().getCurrentUser());
        }
        return user;
    }


    public void updateUser() {
        user.setValue(FirebaseAuth.getInstance().getCurrentUser());
    }



}


