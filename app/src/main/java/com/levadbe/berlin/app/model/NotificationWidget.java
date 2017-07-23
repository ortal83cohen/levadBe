package com.levadbe.berlin.app.model;

import com.levadbe.berlin.app.ui.WidgetListAdapter;

/**
 * Created by Ortal Cohen on 18/7/2017.
 */

public class NotificationWidget extends Widget {

    public String text;

    public NotificationWidget(String text,String id) {
        super(WidgetListAdapter.WIDGET_TYPE_NOTIFICATION,id);
        this.text = text;
    }


}
