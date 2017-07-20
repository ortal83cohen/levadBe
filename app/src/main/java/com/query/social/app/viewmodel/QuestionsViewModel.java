package com.query.social.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.query.social.app.model.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ortal Cohen on 24/6/2017.
 */

public class QuestionsViewModel  extends ViewModel {
    private final DatabaseReference myRef;
    private FirebaseDatabase database;
    private final MutableLiveData<List<Question>> questions = new MutableLiveData<>();

    public LiveData<List<Question>> getQuestions() {
        return questions;
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
    public QuestionsViewModel() {
        questions.setValue(new ArrayList<Question>());
            database = FirebaseDatabase.getInstance();

             myRef = database.getReference("QUESTIONS");
            myRef.addListenerForSingleValueEvent(valueEventListener);


    }

    public void setFilter(String s) {
        myRef.orderByChild("mHeader").startAt(s).endAt(s.concat("\uf8ff")).limitToFirst(20).addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            //Get map of users in datasnapshot
            GenericTypeIndicator<HashMap<String, Question>> t = new GenericTypeIndicator<HashMap<String, Question>>() {
            };
            if (dataSnapshot.getValue(t) != null) {
                questions.setValue(new ArrayList<>(dataSnapshot.getValue(t).values()));
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            //handle databaseError
        }
    };
}
