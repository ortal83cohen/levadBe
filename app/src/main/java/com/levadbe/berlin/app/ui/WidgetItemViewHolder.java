package com.levadbe.berlin.app.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.levadbe.berlin.app.helper.ItemTouchHelperViewHolder;
import com.levadbe.berlin.app.model.Widget;

interface OnWidgetClickListener {
    void onWidgetClicked(View v, Widget Widget);
}

/**
 * Created by Ortal Cohen on 18/7/2017.
 */

public abstract class WidgetItemViewHolder extends RecyclerView.ViewHolder implements
        ItemTouchHelperViewHolder {

    protected boolean swipeable;
    protected boolean dragable;

    public WidgetItemViewHolder(View itemView, boolean swipeable, boolean dragable) {
        super(itemView);
        this.swipeable = swipeable;
        this.dragable = dragable;
    }

    public boolean isSwipeable() {
        return swipeable;
    }

    public boolean isDragable() {
        return dragable;
    }

    public void bind(final Context context, final Widget widget, final OnWidgetClickListener onWidgetClickListener) {

    }
}