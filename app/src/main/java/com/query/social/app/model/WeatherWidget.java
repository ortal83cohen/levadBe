package com.query.social.app.model;

/**
 * Created by Ortal Cohen on 18/7/2017.
 */

public class WeatherWidget extends Widget {

    private Weather weather;

    public WeatherWidget(String text, int type, Weather weather) {
        super(text, type);
        this.weather=weather;
    }

    public Weather getWeather() {
        return weather;
    }
}
