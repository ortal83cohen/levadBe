package com.query.social.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.query.social.app.client.JSONWeatherParser;
import com.query.social.app.client.WeatherHttpClient;
import com.query.social.app.model.Weather;
import com.query.social.app.model.WeatherWidget;
import com.query.social.app.model.Widget;
import com.query.social.app.ui.WidgetListAdapter;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Ortal Cohen on 30/5/2017.
 */

public class WidgetViewModel extends ViewModel {

    private final MutableLiveData<List<Widget>> widgets = new MutableLiveData<>();
String time;
    public WidgetViewModel() {
//        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                user.setValue(firebaseAuth.getCurrentUser());
//            }
//        });
        String city = "Berlin,Germany";
        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{city});

        TimeZone tz = TimeZone.getTimeZone("GMT+01:00");
        Calendar c = Calendar.getInstance(tz);
         time = String.format("%02d" , c.get(Calendar.HOUR_OF_DAY))+":"+
                String.format("%02d" , c.get(Calendar.MINUTE))+":"+
               String.format("%02d" , c.get(Calendar.SECOND))+":"+
            String.format("%03d" , c.get(Calendar.MILLISECOND));
    }

    public LiveData<List<Widget>> getWidget() {
        if (widgets.getValue() == null) {
            List<Widget> mItems = new ArrayList<>();
            mItems.add(new Widget(time+"השעה בברלין עכשיו ", WidgetListAdapter.WIDGET_TYPE_CLOCK));
            mItems.add(new Widget("Forum", WidgetListAdapter.WIDGET_TYPE_FORM));
            mItems.add(new WeatherWidget("Whether", WidgetListAdapter.WIDGET_TYPE_WHETHER, new Weather()));
            widgets.setValue(mItems);
        }
        return widgets;
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
            mItems.add(new Widget(time+"השעה בברלין עכשיו ", WidgetListAdapter.WIDGET_TYPE_CLOCK));
            mItems.add(new Widget("Forum", WidgetListAdapter.WIDGET_TYPE_FORM));
            mItems.add(new WeatherWidget("Whether", WidgetListAdapter.WIDGET_TYPE_WHETHER, weather));
            widgets.setValue(mItems);
        }


    }

}


