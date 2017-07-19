package com.query.social.app.model;

import com.query.social.app.ui.WidgetListAdapter;

/**
 * Created by Ortal Cohen on 18/7/2017.
 */

public class NotificationWidget extends Widget {

    public String text;

    public NotificationWidget(String text) {
        super(WidgetListAdapter.WIDGET_TYPE_NOTIFICATION);
        this.text=text;
    }


}
