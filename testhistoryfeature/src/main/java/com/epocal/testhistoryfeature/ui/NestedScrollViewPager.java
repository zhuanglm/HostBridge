package com.epocal.testhistoryfeature.ui;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;

/**
 * This class is a custom ViewPager to overcome the issue found in scrolling RecyclerView
 * within CoordinatorLayout.
 *
 * <h4>Problem:</h4>
 * <p>
 * In Layout below, Toolbar contains (overflow) option menus on the right.
 * When option menu is open (menu pop-up) then dismissed by tapping the screen outside of pop-up,
 * then the next scroll gesture on the RecyclerView is ignored.
 * (RecyclerView does not scroll.)
 * In order to scroll RecyclerView again, it needs another touch (or scroll gesture) to the screen,
 * and the 2nd scroll gesture will scroll RecyclerView.
 * </p>
 *
 * <pre>
 * Layout file (DetailsActivity)
 * -----
 * CoordinatorLayout
 *   AppBarLayout
 *     Toolbar
 *     TabLayout
 *   ViewPager
 *     (fragment)RecyclerView
 * </pre>
 *
 * <h4>Investigation:</h4>
 * <pre>
 * The childview of Coordinator Layout (that is: FrameLayout) receives the following touch event
 * sequence upon the first scroll gesture when option menu pop-up is dismissed.
 *
 * Motion.ACTION_DOWN
 * Motion.ACTION_CANCEL
 *
 * Coordinator Layout is sending ACTION_CANCEL - for some unknown reason. This must be an error on
 * Coordinator Layout implementation.
 * </pre>
 *
 * <h4>Solution:</h4>
 * <p>
 * Set CoordinatorLayout.requestDisallowInterceptTouchEvent(true) to prevent CoordinatorLayout
 * to intercept touch event to CANCEL. NOTE: This call must be done from an immediate
 * child of CoordinatorLayout.
 * </p>
 *
 */
public class NestedScrollViewPager extends ViewPager {
    private ViewParent mParent;

    public NestedScrollViewPager(Context context) {
        super(context);
    }

    public NestedScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void setParent(ViewParent parentView) {
        mParent = parentView;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewParent p = getParent();
        if (p instanceof CoordinatorLayout) {
            setParent(p);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            // Prevent parent (CoordinatorLayout) to intercept and send ACTION_CANCEL
            requestUnblockScrollEvents();
        }
        return super.onInterceptTouchEvent(e);
    }

    private void requestUnblockScrollEvents() {
        if (mParent != null) {
            mParent.requestDisallowInterceptTouchEvent(true);
        }
    }
}
