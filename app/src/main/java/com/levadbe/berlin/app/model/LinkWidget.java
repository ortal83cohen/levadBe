package com.levadbe.berlin.app.model;

import com.levadbe.berlin.app.ui.WidgetListAdapter;

/**
 * Created by Ortal Cohen on 18/7/2017.
 */

public class LinkWidget extends Widget {

    public String text;
    public String link;

    public LinkWidget(String text, String link,String id) {
        super(WidgetListAdapter.WIDGET_TYPE_LINK,id);
        this.text = text;
        this.link = link;
    }


}
