package com.query.social.app.service;

import android.text.Editable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Ortal Cohen on 24/6/2017.
 */

public class GoogleDatabaseService {
    FirebaseDatabase database;
    public GoogleDatabaseService() {
         database = FirebaseDatabase.getInstance();
    }

    public void saveNewQuestion(Editable text) {

        DatabaseReference myRef = database.getReference("QUESTIONS");

        myRef.setValue(text.toString());
    }
}
