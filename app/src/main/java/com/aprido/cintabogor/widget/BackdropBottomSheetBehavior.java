package com.aprido.cintabogor.widget;

import com.aprido.cintabogor.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by apridosandyasa on 10/5/16.
 */

public class BackdropBottomSheetBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {

    boolean mInit = false;
    int mCollapsedY;
    int mPeakHeight;
    int mAnchorPointY;
    int mCurrentChildY;

    public BackdropBottomSheetBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BottomSheetBehavior_Params);
        setPeekHeight(a.getDimensionPixelSize(R.styleable.BackdropBottomSheetBehavior_Params_behavior_backdrop_peekHeight, 0));
        a.recycle();
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof NestedScrollView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        if(!mInit){
            init(child, dependency);
            return false;
        }
        if((mCurrentChildY = (int) ((dependency.getY()-mAnchorPointY) * mCollapsedY / (mCollapsedY-mAnchorPointY))) <= 0)
            child.setY(mCurrentChildY = 0);
        else
            child.setY(mCurrentChildY);
        return true;
    }

    private void init(@NonNull View child, @NonNull View dependency){
        mCollapsedY = dependency.getHeight() - (2 * mPeakHeight);
        mAnchorPointY = child.getHeight();
        mCurrentChildY = (int) dependency.getY();
        //TODO
        if(mCurrentChildY == mAnchorPointY || mCurrentChildY == mAnchorPointY-1 ||mCurrentChildY == mAnchorPointY+1)
            child.setY(0);
        else child.setY(mCurrentChildY);
        mInit = true;
    }

    public void setPeekHeight(int peakHeight) {
        Log.d("TAG", "peakHeight " + peakHeight);
        this.mPeakHeight = peakHeight;
    }

    public static <V extends View> BackdropBottomSheetBehavior<V> from(V view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof CoordinatorLayout.LayoutParams)) {
            throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
        }
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) params)
                .getBehavior();
        if (!(behavior instanceof BackdropBottomSheetBehavior)) {
            throw new IllegalArgumentException(
                    "The view is not associated with BottomSheetBehaviorGoogleMapsLike");
        }
        return (BackdropBottomSheetBehavior<V>) behavior;
    }

}
