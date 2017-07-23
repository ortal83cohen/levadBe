package com.levadbe.berlin.app.model;

import com.levadbe.berlin.app.ui.WidgetListAdapter;

import java.util.UUID;

/**
 * Created by Ortal Cohen on 18/7/2017.
 */

public class AddGroupWidgetWidget extends Widget {

    public String text;

    public AddGroupWidgetWidget() {
        super(WidgetListAdapter.WIDGET_TYPE_ADD_GROUP_WIDGET, UUID.randomUUID().toString());
        this.text = "הוסף ויד'ט לקבוצות";
    }


}
