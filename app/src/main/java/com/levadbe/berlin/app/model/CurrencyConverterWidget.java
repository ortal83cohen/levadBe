package com.levadbe.berlin.app.model;

import com.levadbe.berlin.app.ui.WidgetListAdapter;

/**
 * Created by Ortal Cohen on 18/7/2017.
 */

public class CurrencyConverterWidget extends Widget {

    public String text;

    public CurrencyConverterWidget(String text) {
        super(WidgetListAdapter.WIDGET_TYPE_CURRENCY_CONVERTER);
        this.text = text;
    }


}
