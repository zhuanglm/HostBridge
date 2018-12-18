package com.epocal.common.epocuiobjects.stepper;

import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

// http://stackoverflow.com/a/13381228/3891038 + modifications
class Animations {
    static void slideDown(final View v, final StepperAnimationCallback callback) {
        if (v.getVisibility() != View.VISIBLE) {

            v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            final int targetHeight = v.getMeasuredHeight();

            // Older versions of android (pre API 21) cancel animations for views with a height of 0.
            setHeight(v, 1);
            v.setVisibility(View.VISIBLE);
            Animation a = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    int newHeight = interpolatedTime == 1
                            ? WindowManager.LayoutParams.WRAP_CONTENT
                            : (int) (targetHeight * interpolatedTime);
                    setHeight(v, newHeight);
                    v.requestLayout();
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };

            a.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    if (callback != null)
                        callback.onSlideDownAnimationStart();
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    v.setVisibility(View.VISIBLE);
                    if (callback != null)
                        callback.onSlideDownAnimationEnd();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

            // 1dp/ms
            a.setDuration(((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density)) * 2);
            v.startAnimation(a);
        }
    }

    static void slideUp(final View v, final StepperAnimationCallback callback) {
        if (v.getVisibility() == View.VISIBLE) {

            final int initialHeight = v.getMeasuredHeight();

            Animation a = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    if (interpolatedTime != 1) {
                        int newHeight = initialHeight - (int) (initialHeight * interpolatedTime);
                        setHeight(v, newHeight);
                        v.requestLayout();
                    }
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }

            };

            a.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    if (callback != null)
                        callback.onSlideUpAnimationStart();
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    v.setVisibility(View.GONE);
                    if (callback != null)
                        callback.onSlideUpAnimationEnd();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

            // 1dp/ms
            a.setDuration(((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density)) * 2);
            v.startAnimation(a);
        }
    }

    private static void setHeight(View v, int newHeight) {
        //v.getLayoutParams().height = newHeight;
        v.setLayoutParams(new LinearLayout.LayoutParams(v.getLayoutParams().width, newHeight));
    }

}
