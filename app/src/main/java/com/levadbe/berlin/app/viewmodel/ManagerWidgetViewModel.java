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
import java.util.UUID;

/**
 * Created by Ortal Cohen on 30/5/2017.
 */

public class ManagerWidgetViewModel extends WidgetViewModel {


    private final MutableLiveData<List<Widget>> widgets = new MutableLiveData<>();
    String time;
    FirebaseDatabase database;

    public ManagerWidgetViewModel() {

        database = FirebaseDatabase.getInstance();

    }

    public LiveData<List<Widget>> getWidget() {
        if (widgets.getValue() == null) {
            List<Widget> mItems = new ArrayList<>();
            mItems.add(new ForumWidget( UUID.randomUUID().toString()));
            mItems.add(new AddGroupWidgetWidget());
            widgets.setValue(mItems);
        }
        return widgets;
    }


    public void removeWidget(int position) {

    }


}


