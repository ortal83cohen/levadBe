package com.levadbe.berlin.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.levadbe.berlin.app.model.AddGroupWidgetWidget;
import com.levadbe.berlin.app.model.ForumWidget;
import com.levadbe.berlin.app.model.NotificationWidget;
import com.levadbe.berlin.app.model.Widget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ortal Cohen on 30/5/2017.
 */

public class ManagerWidgetViewModel extends WidgetViewModel {


    private final MutableLiveData<List<Widget>> widgets = new MutableLiveData<>();
    String time;
    FirebaseDatabase database;
    private List<Widget> dinamicWidgets = new ArrayList<>();
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if (dataSnapshot.getValue() != null) {
                dinamicWidgets.add(new NotificationWidget((String) dataSnapshot.getValue()));
                List<Widget> items = new ArrayList<>();
                items.addAll(widgets.getValue());
                items.addAll(dinamicWidgets);
                widgets.setValue(items);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            //handle databaseError
        }
    };


    public ManagerWidgetViewModel() {

        database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("GROUP_WIDGETS");
        myRef.addListenerForSingleValueEvent(valueEventListener);


    }

    public LiveData<List<Widget>> getWidget() {
        if (widgets.getValue() == null) {
            List<Widget> mItems = new ArrayList<>();
            mItems.add(new ForumWidget());
            mItems.add(new AddGroupWidgetWidget());
            widgets.setValue(mItems);
        }
        return widgets;
    }


    public void removeWidget(int position) {

    }


}


