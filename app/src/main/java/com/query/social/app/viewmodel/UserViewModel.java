package com.query.social.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.query.social.app.helper.Strings;
import com.query.social.app.service.SharedPreferencesService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ortal Cohen on 30/5/2017.
 */

public class UserViewModel extends ViewModel {

    private final MutableLiveData<FirebaseUser> user = new MutableLiveData<>();
    private final MutableLiveData<List<String>> userGroups = new MutableLiveData<>();
    private SharedPreferencesService sharedPreferencesService;

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

    public void init(Context context) {
        sharedPreferencesService = new SharedPreferencesService(context);
        ArrayList<String> groups = new ArrayList<>();
        groups.add(sharedPreferencesService.getGroup());
        if(!Strings.isNullOrEmpty(sharedPreferencesService.getGroup()))
        userGroups.setValue(groups);
    }

    public void updateUser() {
        user.setValue(FirebaseAuth.getInstance().getCurrentUser());
    }

    public void addToGroup(List<String> groups) {
        if (userGroups.getValue() == null) {
            userGroups.setValue(groups);
        } else {
            List<String> userGroup = userGroups.getValue();
            userGroup.addAll(groups);
            userGroups.setValue(userGroup);
        }
        sharedPreferencesService.saveGroup(groups.get(0));
    }

//    public void saveGoup(Question question) {
//
//        DatabaseReference myRef = database.getReference("QUESTIONS");
//
//        myRef.push().setValue(question);
//    }

    public LiveData<List<String>> getGroups() {
        return userGroups;
    }
}


