package com.levadbe.berlin.app.viewmodel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.levadbe.berlin.app.model.LinkWidget;
import com.levadbe.berlin.app.service.SharedPreferencesService;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.util.UUID;

/**
 * Created by Ortal Cohen on 30/5/2017.
 */

public class AddWidgetViewModel extends ViewModel {

    private final MutableLiveData<String> widgetTypeSpinner = new MutableLiveData<>();

    private final MutableLiveData<String> groupsSpinner = new MutableLiveData<>();

    private final MutableLiveData<String> header = new MutableLiveData<>();

    private final MutableLiveData<String> link = new MutableLiveData<>();

    private final DatabaseReference myRef;

    private SharedPreferencesService sharedPreferencesService;
    private FirebaseDatabase database;


    public void init(Context context) {
        sharedPreferencesService = new SharedPreferencesService(context);
    }

    public void setWidgetTypeSpinner(String s) {
        widgetTypeSpinner.setValue(s);
    }

    public void setGroupsSpinner(String s) {
        groupsSpinner.setValue(s);
    }

    public void setHeader(String s) {
        header.setValue(s);
    }

    public void setLink(String s) {
        link.setValue(s);
    }


    public LiveData getHeader() {
        return header;
    }

    public LiveData getLink() {
        return link;
    }


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            //handle databaseError
        }
    };


    public AddWidgetViewModel() {

        database = FirebaseDatabase.getInstance();
         myRef = database.getReference("GROUP_WIDGETS");
//        myRef.addListenerForSingleValueEvent(valueEventListener);


    }

    public void saveGroupWidget(){
        myRef.child(groupsSpinner.getValue()).child(header.getValue()).setValue(new LinkWidget(header.getValue(),link.getValue(), UUID.randomUUID().toString()));
    }


}


