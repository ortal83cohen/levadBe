package com.query.social.app.service;

import android.text.Editable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.query.social.app.model.Question;

import java.util.List;

/**
 * Created by Ortal Cohen on 24/6/2017.
 */

public class GoogleDatabaseService {
    FirebaseDatabase database;
    public GoogleDatabaseService() {
         database = FirebaseDatabase.getInstance();
    }

    public void saveNewQuestion(Question question) {

        DatabaseReference myRef = database.getReference("QUESTIONS");

        myRef.push().setValue(question);
    }


  public List<Question> getQuestionsList(){
      DatabaseReference myRef = database.getReference("QUESTIONS");
      myRef.addListenerForSingleValueEvent(
              new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot dataSnapshot) {
                      //Get map of users in datasnapshot
                     dataSnapshot.getValue();

                  }

                  @Override
                  public void onCancelled(DatabaseError databaseError) {
                      //handle databaseError
                  }
              });
      return null;
  }
}
