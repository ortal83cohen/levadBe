package com.query.social.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.query.social.app.client.JSONWeatherParser;
import com.query.social.app.client.WeatherHttpClient;
import com.query.social.app.model.ClockWidget;
import com.query.social.app.model.ForumWidget;
import com.query.social.app.model.MapWidget;
import com.query.social.app.model.NotificationWidget;
import com.query.social.app.model.Weather;
import com.query.social.app.model.WeatherWidget;
import com.query.social.app.model.Widget;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ortal Cohen on 30/5/2017.
 */

public class WidgetViewModel extends ViewModel {


    private final MutableLiveData<List<Widget>> widgets = new MutableLiveData<>();
    String time;
    private List<Widget> dinamicWidgets = new ArrayList<>();
    FirebaseDatabase database;

    public WidgetViewModel() {

        database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("GROUP_WIDGETS");
        myRef.addListenerForSingleValueEvent(valueEventListener);

        String city = "Berlin,Germany";
        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{city});


    }


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

    public LiveData<List<Widget>> getWidget() {
        if (widgets.getValue() == null) {
            List<Widget> mItems = new ArrayList<>();
            mItems.add(new ClockWidget());
            mItems.add(new ForumWidget());
            mItems.add(new WeatherWidget(new Weather()));
            mItems.add(new MapWidget());
            mItems.addAll(dinamicWidgets);
            mItems.add(new NotificationWidget("הודעה אישית 1"));
            mItems.add(new NotificationWidget("הודעה אישית 2"));
            mItems.add(new NotificationWidget("הודעה אישית 3"));
            mItems.add(new NotificationWidget("הודעה אישית 4"));
            widgets.setValue(mItems);
        }
        return widgets;
    }

    public void addGroupWidgets(List<String> strings) {
        DatabaseReference ref = database.getReference(strings.get(0));
        ref.addListenerForSingleValueEvent(valueEventListener);
    }

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ((new WeatherHttpClient()).getWeatherData(params[0]));

            try {
                weather = JSONWeatherParser.getWeather(data);

                // Let's retrieve the icon
                weather.iconData = ((new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;

        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            List<Widget> mItems = new ArrayList<>();
            mItems.add(new ClockWidget());
            mItems.add(new ForumWidget());
            mItems.add(new WeatherWidget(weather));
            mItems.add(new MapWidget());
            mItems.addAll(dinamicWidgets);
            mItems.add(new NotificationWidget("הודעה אישית 1"));
            mItems.add(new NotificationWidget("הודעה אישית 2"));
            mItems.add(new NotificationWidget("הודעה אישית 3"));
            mItems.add(new NotificationWidget("הודעה אישית 4"));
            widgets.setValue(mItems);
        }


    }

}


