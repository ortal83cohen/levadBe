package com.levadbe.berlin.app.model;

import com.levadbe.berlin.app.ui.WidgetListAdapter;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Ortal Cohen on 18/7/2017.
 */

public class ClockWidget extends Widget {

    public String text;

    public ClockWidget(String id) {
        super(WidgetListAdapter.WIDGET_TYPE_CLOCK,id);
        TimeZone tz = TimeZone.getTimeZone("GMT+01:00");
        Calendar c = Calendar.getInstance(tz);
        String time = String.format("%02d", c.get(Calendar.HOUR_OF_DAY)) + ":" +
                String.format("%02d", c.get(Calendar.MINUTE)) + ":" +
                String.format("%02d", c.get(Calendar.SECOND));

        this.text = time + "השעה בברלין עכשיו ";
    }


}
