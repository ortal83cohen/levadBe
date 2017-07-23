package com.levadbe.berlin.app.model;

import java.util.UUID;

/**
 * Created by Ortal Cohen on 18/7/2017.
 */

public abstract class Widget {

    public int type;
    public String id;

    public Widget(int type,String id) {
        this.type = type;
        this.id=id;
    }
}
