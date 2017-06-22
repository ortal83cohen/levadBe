package com.query.social.app.ui;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Ortal Cohen on 22/6/2017.
 */

public class ScrollAwareBehavior  extends CoordinatorLayout.Behavior<View> {
    public ScrollAwareBehavior(Context context, AttributeSet attrs) {
        super();
    }

    public ScrollAwareBehavior() {
        super();
    }

    @CoordinatorLayout.DefaultBehavior(ScrollAwareBehavior.class)
    public class FancyFrameLayout extends View {
        public FancyFrameLayout(Context context) {
            super(context);
        }
    }


    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof  RecyclerView && child instanceof FloatingActionButton;
    }


    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {

//        if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
//            child.hide();
//        } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
//            child.show();
//        }

        return  false;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
            ((FloatingActionButton)child).hide(new FloatingActionButton.OnVisibilityChangedListener() {
                @Override
                public void onHidden(FloatingActionButton fab) {
                    super.onShown(fab);
                    fab.setVisibility(View.INVISIBLE);
                }
            });
        } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
            ((FloatingActionButton)child).show();
        }

    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return true;
    }
    //    @Override
//    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
//                                       FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
//        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL ||
//                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target,
//                        nestedScrollAxes);
//    }
//
//    @Override
//    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child,
//                               View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
//        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed,
//                dyUnconsumed);
//
//        if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
//            child.hide();
//        } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
//            child.show();
//        }
//    }

    // ...
}