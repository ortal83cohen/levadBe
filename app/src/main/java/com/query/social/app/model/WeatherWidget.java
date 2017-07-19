package com.query.social.app.model;

import com.query.social.app.ui.WidgetListAdapter;

/**
 * Created by Ortal Cohen on 18/7/2017.
 */

public class WeatherWidget extends Widget {

    private Weather weather;

    public WeatherWidget(Weather weather) {
        super( WidgetListAdapter.WIDGET_TYPE_WHETHER);
        this.weather=weather;
    }

    public Weather getWeather() {
        return weather;
    }
}
