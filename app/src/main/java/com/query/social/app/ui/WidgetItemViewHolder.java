package com.query.social.app.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.query.social.app.helper.ItemTouchHelperViewHolder;
import com.query.social.app.model.Widget;

/**
 * Created by Ortal Cohen on 18/7/2017.
 */

public abstract class WidgetItemViewHolder extends RecyclerView.ViewHolder implements
        ItemTouchHelperViewHolder {

    protected boolean swipeable;
    protected boolean dragable;

    public WidgetItemViewHolder(View itemView, boolean swipeable,boolean dragable) {
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

    public void bind(Context context, Widget widget,OnWidgetClickListener onWidgetClickListener) {

    }
}

interface OnWidgetClickListener {
    void onWidgetClicked(View v);
}