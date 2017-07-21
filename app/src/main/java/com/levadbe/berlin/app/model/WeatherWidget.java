package com.levadbe.berlin.app.model;

import com.levadbe.berlin.app.ui.WidgetListAdapter;

/**
 * Created by Ortal Cohen on 18/7/2017.
 */

public class WeatherWidget extends Widget {

    private Weather weather;

    public WeatherWidget(Weather weather) {
        super(WidgetListAdapter.WIDGET_TYPE_WHETHER);
        this.weather = weather;
    }

    public Weather getWeather() {
        return weather;
    }
}
