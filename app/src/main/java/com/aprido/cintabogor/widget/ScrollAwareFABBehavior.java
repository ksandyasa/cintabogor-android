package com.aprido.cintabogor.widget;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by apridosandyasa on 10/5/16.
 */

public class ScrollAwareFABBehavior extends FloatingActionButton.Behavior {

    float offset;

    public ScrollAwareFABBehavior(Context context, AttributeSet attrs) {
        super();
        offset = 0;
    }

    @Override
    public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final FloatingActionButton child,
                                       final View directTargetChild, final View target, final int nestedScrollAxes) {
        // Ensure we react to vertical scrolling
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return dependency instanceof NestedScrollView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {

        if (offset == 0)
            setOffsetValue(parent);

        if (dependency.getY() <=0)
            return false;

        if (child.getY() <= (offset + child.getHeight()) && child.getVisibility() == View.VISIBLE)
            child.hide();
        else if (child.getY() > offset && child.getVisibility() != View.VISIBLE)
            child.show();

        return false;
    }

    private void setOffsetValue(CoordinatorLayout coordinatorLayout) {

        for (int i = 0; i < coordinatorLayout.getChildCount(); i++) {
            View child = coordinatorLayout.getChildAt(i);

            if (child instanceof AppBarLayout) {

                if (child.getTag() != null &&
                        child.getTag().toString().contentEquals("modal-appbar") ) {
                    offset = child.getY()+child.getHeight();
                    break;
                }
            }
        }
    }
}
