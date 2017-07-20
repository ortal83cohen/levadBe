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
import com.query.social.app.client.FixerAPIClient;
import com.query.social.app.client.FixerAPIInterface;
import com.query.social.app.client.FixerResponse;
import com.query.social.app.client.JSONWeatherParser;
import com.query.social.app.client.WeatherHttpClient;
import com.query.social.app.model.ClockWidget;
import com.query.social.app.model.ForumWidget;
import com.query.social.app.model.MapWidget;
import com.query.social.app.model.NotificationWidget;
import com.query.social.app.model.Weather;
import com.query.social.app.model.WeatherWidget;
import com.query.social.app.model.Widget;
import com.query.social.app.service.SharedPreferencesService;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ortal Cohen on 30/5/2017.
 */

public class WidgetViewModel extends ViewModel {

    private static SharedPreferencesService sharedPreferencesService;
    private final MutableLiveData<List<Widget>> widgets = new MutableLiveData<>();
    String time;
    private List<Widget> dynamicWidgets = new ArrayList<>();
    FirebaseDatabase database;

    public WidgetViewModel() {

        database = FirebaseDatabase.getInstance();
//        sharedPreferencesService = new SharedPreferencesService()
        DatabaseReference myRef = database.getReference("GROUP_WIDGETS");
        myRef.addListenerForSingleValueEvent(valueEventListener);

        String city = "Berlin,Germany";
        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{city});

        FixerAPIInterface apiInterface = FixerAPIClient.getClient().create(FixerAPIInterface.class);

        /**
         GET List Resources
         **/
        retrofit2.Call call = apiInterface.getCurrency("EUR,ILS");
        call.enqueue(new retrofit2.Callback() {
            @Override
            public void onResponse(retrofit2.Call call, retrofit2.Response response) {
                FixerResponse body = (FixerResponse)response.body();
                List<Widget> items = new ArrayList<>();
                items.addAll(widgets.getValue());
                items.add(new NotificationWidget(String.valueOf(body.rates.get("ILS"))));
                widgets.setValue(items);
            }

            @Override
            public void onFailure(retrofit2.Call call, Throwable t) {

            }
        });


    }


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if (dataSnapshot.getValue() != null) {
                dynamicWidgets.clear();
                dynamicWidgets.add(new NotificationWidget((String) dataSnapshot.getValue()));
                List<Widget> items = new ArrayList<>();
                items.addAll(widgets.getValue());
                items.addAll(dynamicWidgets);
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
            mItems.add(new NotificationWidget("הידעת, חלק מההודעות ניתנות לשינוי מקום או מחיקה (נסה אותי)"));
            mItems.add(new ClockWidget());
            mItems.add(new ForumWidget());
            mItems.add(new WeatherWidget(new Weather()));
            mItems.add(new MapWidget());
            mItems.addAll(dynamicWidgets);
            mItems.add(new NotificationWidget("הודעה אישית 1"));
            mItems.add(new NotificationWidget("הודעה אישית 2"));
            mItems.add(new NotificationWidget("הודעה אישית 3"));
            widgets.setValue(mItems);
        }
        return widgets;
    }

    public void addGroupWidgets(List<String> strings) {
        DatabaseReference ref = database.getReference(strings.get(0));
        ref.addListenerForSingleValueEvent(valueEventListener);
    }

    public void removeWidget(int position) {
        List<Widget> items = widgets.getValue();
        items.remove(position);
        widgets.setValue(items);
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
            mItems.add(new NotificationWidget("הידעת, חלק מההודעות ניתנות לשינוי מקום או מחיקה (נסה אותי)"));
            mItems.add(new ClockWidget());
            mItems.add(new ForumWidget());
            mItems.add(new WeatherWidget(weather));
            mItems.add(new MapWidget());
            mItems.addAll(dynamicWidgets);
            mItems.add(new NotificationWidget("הודעה אישית 1"));
            mItems.add(new NotificationWidget("הודעה אישית 2"));
            mItems.add(new NotificationWidget("הודעה אישית 3"));
            mItems.add(new NotificationWidget("הודעה אישית 4"));
            widgets.setValue(mItems);
        }


    }

}


