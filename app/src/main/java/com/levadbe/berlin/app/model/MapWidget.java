package com.levadbe.berlin.app.model;

import com.levadbe.berlin.app.ui.WidgetListAdapter;

/**
 * Created by Ortal Cohen on 18/7/2017.
 */

public class MapWidget extends Widget {

    private Weather weather;

    public MapWidget(String id) {
        super(WidgetListAdapter.WIDGET_TYPE_MAP,id);

    }

}
