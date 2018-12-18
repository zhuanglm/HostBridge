package com.epocal.epoctestprocedure.fragments.dataentry;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import com.epocal.common.epocuiobjects.stepper.VerticalStepperFormLayout;

/**
 * This is derivative work (extending) VerticalStepperFormLayout from
 * https://github.com/ernestoyaquello/vertical-stepper-form
 * Modification: added a method to scroll to focused view to improve
 * usability. (otherwise, the step with multiple sub-views get hidden
 * (overlapped) by soft-keyboard.)
 *
 * ---
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class VerticalStepperInputLayout extends VerticalStepperFormLayout {
    public VerticalStepperInputLayout(Context context) {
        super(context);
    }

    public VerticalStepperInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalStepperInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // http://stackoverflow.com/questions/2150078/how-to-check-visibility-of-software-keyboard-in-android
    @Override
    protected void setObserverForKeyboard() {
        llContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                //r will be populated with the coordinates of your view that area still visible.
                llContent.getWindowVisibleDisplayFrame(r);

                int heightDiff = llContent.getRootView().getHeight() - (r.bottom - r.top);
                if (heightDiff > 100) { // if more than 100 pixels, it is probably a keyboard...
                    scrollToFocusedView();
                }
            }
        });
    }

    /**
     * Move focused view to visible spot (if overlapped by keyboard)
     */
    protected void scrollToFocusedView() {
        View stepview = stepLayouts.get(activeStep);
        View focusview = stepview.findFocus();
        if (focusview != null) {
            Rect rect = new Rect(focusview.getLeft(), focusview.getTop(), focusview.getRight(), focusview.getBottom());
            focusview.requestRectangleOnScreen(rect, true);
        }
    }

//    protected void addConfirmationStepToStepsList() {
//     // Do nothing. This method overrides the one in the original library, to remove the Confirmation step.
//        String confirmationStepText = context.getString(ernestoyaquello.com.verticalstepperform.R.string.vertical_form_stepper_form_last_step);
//        steps.add(confirmationStepText);
//        steps.size();
//        steps.remove(steps.size());
//    }
}
