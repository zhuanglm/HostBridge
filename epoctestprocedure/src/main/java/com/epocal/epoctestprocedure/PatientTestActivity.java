package com.epocal.epoctestprocedure;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.epocal.common.CU;
import com.epocal.common.epocobjects.EpocUIMessageType;
import com.epocal.common.epocobjects.WorkflowField;
import com.epocal.common.eventmessages.EpocMessageBoxInfo;
import com.epocal.common.eventmessages.UIChangeRequest;
import com.epocal.common.realmentities.TestRecord;
import com.epocal.common.realmentities.WorkFlow;
import com.epocal.common.types.UIScreens;
import com.epocal.epoctest.TestManager;
import com.epocal.epoctest.uidriver.QATestUIDriver;
import com.epocal.epoctest.uidriver.TestUIDriver;
import com.epocal.epoctest.uimessage.TestMessageContext;
import com.epocal.epoctestprocedure.fragments.TestMessageAndInstructionFragment;
import com.epocal.epoctestprocedure.fragments.TestResultsFragment;
import com.epocal.epoctestprocedure.fragments.dataentry.VerticalStepper;
import com.epocal.epoctestprocedure.fragments.dataentry.VerticalStepperInputLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

enum Direction {
    up,
    down,
    left,
    right;

    /**
     * Returns a direction given an angle.
     * Directions are defined as follows:
     * <p>
     * Up: [45, 135]
     * Right: [0,45] and [315, 360]
     * Down: [225, 315]
     * Left: [135, 225]
     *
     * @param angle an angle from 0 to 360 - e
     * @return the direction of an angle
     */
    public static Direction fromAngle(double angle) {
        if (inRange(angle, 45, 135)) {
            return Direction.up;
        } else if (inRange(angle, 0, 45) || inRange(angle, 315, 360)) {
            return Direction.right;
        } else if (inRange(angle, 225, 315)) {
            return Direction.down;
        } else {
            return Direction.left;
        }

    }

    /**
     * @param angle an angle
     * @param init  the initial bound
     * @param end   the final bound
     * @return returns true if the given angle is in the interval [init, end).
     */
    private static boolean inRange(double angle, float init, float end) {
        return (angle >= init) && (angle < end);
    }
}

public class PatientTestActivity extends BaseActivity implements TestMessageAndInstructionFragment.OnMessagePanelSelectedListener, TestResultsFragment.OnTestResultSelectedListener, IPatientTestView {
    private static final String TAG = PatientTestActivity.class.getSimpleName();
    private FragmentManager mFm1, mFm2;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    String mReaderBTA;
    String mReaderAlias;
    public TestUIDriver mTestUIDriver;
    private TestMessageAndInstructionFragment mMessageAndInstructionFrag;
    private TestResultsFragment mTestResultsFragment;
    private static final int BOTTOM_SHEET_ANIMATION_DURATION_MS = 400;
    private RelativeLayout mBottomSheetViewGroup;
    private int mBottomSheetInitialPositionOffsetInDp = 320;
    private boolean isVerticalStepperExpanded = false;
    private ObjectAnimator mBottomSheetAnimator;
    private VerticalStepperInputLayout mVerticalStepperFormLayout;
    private VerticalStepper mVerticalStepper;
    public TestRecord mTestRecord;
    private boolean displayingResults = false;
    private TextView mTextViewDataEntry;
    private PatientTestPresenter mPresenter;
    private MaterialDialog mMaterialDialog;
    private WorkFlow mWorkFlow;
    private LinearLayout mLinearLayout;
    private GestureDetectorCompat mDetector;
    private boolean stepperOpened = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_test);
        changeTitle(CU.titleCase(getString(R.string.patient_test)));
        mLinearLayout = (LinearLayout) findViewById(R.id.llProgressBar);

        mPresenter = new PatientTestPresenter(this);
        mBottomSheetViewGroup = (RelativeLayout) findViewById(R.id.bottom_sheet);
        mTextViewDataEntry = (TextView) findViewById(R.id.text_view_data_entry);
        setDataEntryClick();
        mVerticalStepperFormLayout = (VerticalStepperInputLayout) findViewById(R.id.vertical_stepper_form);
        mBottomSheetAnimator = setupBottomSheetAnimator();

        // Set up the fragments
        mFm1 = getFragmentManager();
        mFm2 = getFragmentManager();
        mMessageAndInstructionFrag = new TestMessageAndInstructionFragment();
        mTestResultsFragment = new TestResultsFragment();
        mFm1.beginTransaction().add(R.id.container_message_and_data_entry, mMessageAndInstructionFrag).commit();
        mFm2.beginTransaction().add(R.id.container_test_results, mTestResultsFragment, "ResultsFragment").commit();
        displayTestMessageAndInstructionFragment();
        //

        Intent intent = getIntent();
        mReaderBTA = intent.getStringExtra("Reader_BTA"); // to get the testUIDriver
        mReaderAlias = intent.getStringExtra("Reader_Alias"); // to display
        mTestUIDriver = TestManager.getInstance().getTestDriver(mReaderBTA);
        if (mTestUIDriver == null) {
            //possible if app is killed while suspended. should probably go back to login screen instead of home
            Log.d(TAG, "null test driver, navigating to home screen!");
            TestManager.getInstance().navigate(UIScreens.HomeScreen, mReaderBTA, mReaderAlias);
            finish();
        }
        else {
            if (mTestUIDriver instanceof QATestUIDriver) {
                changeTitle(mTestUIDriver.getReaderDevice().getDeviceAlias() + " (" + mTestUIDriver.getReaderDevice().getDeviceId() + ")");
            }
            mTestUIDriver.setVisible(true);
            showHideCancelTestIcon(true);
            mWorkFlow = mTestUIDriver.getTestFlow();
            createVerticalStepper();
            mVerticalStepper.disableDocumentResults();
        }
    }

    private void createVerticalStepper() {
        android.util.Log.d(TAG, "Workflow object" + mWorkFlow.toString());
        mVerticalStepper = new VerticalStepper(this, mVerticalStepperFormLayout, mWorkFlow, mTestUIDriver, getFragmentManager(), mPresenter);
        mVerticalStepper.buildForm();
        mVerticalStepper.validateAllMandatorySteps();
        mVerticalStepper.enableMandatorySteps();
        mVerticalStepper.updateOptionalSteps();
//        mVerticalStepper.disableDocumentResults();
//        mVerticalStepper.validatePatientId(); // Patient ID was treated separately because it is an edge cases. But this line might not be needed any longer.
    }

    @Override
    public void handleTestEvent(TestUIDriver patientTestUIDriver) {
        android.util.Log.i(TAG, "Received Test Event in PatientTestActivity -> " + patientTestUIDriver.getDevMessage());
        mPresenter.processPatientTestUIDriver(patientTestUIDriver);
    }

    private void handleMessages(final EpocMessageBoxInfo epocMessageBoxInfo) {
        if (epocMessageBoxInfo.isToRemove()) {
            // try to remove
            // try to remove
            if (mMaterialDialog != null) {
                try {
                    mMaterialDialog.dismiss();
                    mMaterialDialog = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return;
        }
        String message = getString(epocMessageBoxInfo.getMessageStringResId());
        String positiveText = getString(epocMessageBoxInfo.getPositiveTextResId());
        String negativeText = getString(epocMessageBoxInfo.getNegativeTextResId());
        String neutralText = "";
        mMaterialDialog = new MaterialDialog.Builder(this)
                .content(message)
                .positiveText(positiveText)
                .positiveColor(getResources().getColor(R.color.primaryBlueNew))
                .negativeText(negativeText)
                .negativeColor(getResources().getColor(R.color.primaryBlueNew))
                .neutralText(neutralText)
                .neutralColor(getResources().getColor(R.color.primaryBlueNew))
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        try {
                            epocMessageBoxInfo.getNegativeAction().run();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        try {

                            epocMessageBoxInfo.getPositiveAction().run();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        try {

                            epocMessageBoxInfo.getMiddleAction().run();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .show();

    }

    @Override
    public void handleMessages(TestMessageContext testMessageContext) {
       if (displayingResults && testMessageContext.getEpocUIMessageType().equals(EpocUIMessageType.ERROR))
           displayTestMessageAndInstructionFragment();
       mMessageAndInstructionFrag.setMessages(testMessageContext);
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
        EventBus.getDefault().register(this);
        mTestUIDriver.getTestDriverEmmitter().subscribeOn(new Consumer<TestUIDriver>() {
            @Override
            public void accept(TestUIDriver testUIDriver) throws Exception {
                handleTestEvent(testUIDriver);
            }
        }, AndroidSchedulers.mainThread());

        mTestUIDriver.getMessageBoxRequestor().subscribeOn(new Consumer<EpocMessageBoxInfo>() {
            @Override
            public void accept(EpocMessageBoxInfo epocMessageBoxInfo) throws Exception {
                handleMessages(epocMessageBoxInfo);
            }
        }, AndroidSchedulers.mainThread());
        setInitialPositionBottomSheetView();
        mVerticalStepperFormLayout.setEnableInteraction(isVerticalStepperExpanded);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
        mTestUIDriver.getTestDriverEmmitter().unsubscribe();
        mTestUIDriver.getMessageBoxRequestor().unsubscribe();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // showProgressDialog(getString(R.string.loading));
        if(mTestUIDriver.isCalculationDone()) {
            enableDocumentResults();
            displayTestResultFragment();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UIChangeRequest uiChangeRequest){
        switch (uiChangeRequest.getReason()){

            case Unknown:
                break;
            case UPDATE_TESTSELECTION:
                this.setTestPanels();
                break;
            case CARDINSERTED_NEWTEST_DONTKEEPDATA:
                displayTestMessageAndInstructionFragment();
                LinearLayout ll = (LinearLayout) mVerticalStepperFormLayout.findViewById(R.id.content);
                ll.removeAllViews();
                createVerticalStepper();
                mVerticalStepper.disableDocumentResults();
                break;
            case CARDINSERTED_NEWTEST_KEEPDATA:
                displayTestMessageAndInstructionFragment();
                mVerticalStepper.updatePatientData(mTestUIDriver.getTestData());
                mVerticalStepper.updateFieldsMakeAllEditable();
                mVerticalStepper.disableDocumentResults();
                break;
            case RECALCULATION_STARTED:
                displayTestMessageAndInstructionFragment();
                showProgressDialog();
                break;
            case CALCULATION_STARTED:
                break;
            case CALCULATION_FINISHED:
                break;
            case EDITABILITY_CHANGED:
                // request editable fields from testUIDriver
               ArrayList<WorkflowField> nonEditableFields =  mTestUIDriver.getNonEditableFields();
//                ArrayList<WorkflowField> nonEditableFields =  mTestUIDriver.getNonEditableFieldsMock();
                mVerticalStepper.updateFieldsEditability(nonEditableFields);
                break;
        }

    }

    private void setDataEntryClick() {
        mDetector = new GestureDetectorCompat(mTextViewDataEntry.getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return super.onDoubleTap(e);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                // Grab two events located on the plane at e1=(x1, y1) and e2=(x2, y2)
                // Let e1 be the initial event
                // e2 can be located at 4 different positions, consider the following diagram
                // (Assume that lines are separated by 90 degrees.)
                //
                //
                //         \ A  /
                //          \  /
                //       D   e1   B
                //          /  \
                //         / C  \
                //
                // So if (x2,y2) falls in region:
                //  A => it's an UP swipe
                //  B => it's a RIGHT swipe
                //  C => it's a DOWN swipe
                //  D => it's a LEFT swipe
                //

                float x1 = e1.getX();
                float y1 = e1.getY();

                float x2 = e2.getX();
                float y2 = e2.getY();

                Direction direction = getDirection(x1,y1,x2,y2);
                if (direction == Direction.down && isVerticalStepperExpanded)
                    toggleVerticalStepper();
                else if (direction == Direction.up && !isVerticalStepperExpanded)
                    toggleVerticalStepper();

                return super.onFling(e1, e2, velocityX, velocityY);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                toggleVerticalStepper();
                return super.onSingleTapConfirmed(e);
            }

            /**
             * Given two points in the plane p1=(x1, x2) and p2=(y1, y1), this method
             * returns the direction that an arrow pointing from p1 to p2 would have.
             * @param x1 the x position of the first point
             * @param y1 the y position of the first point
             * @param x2 the x position of the second point
             * @param y2 the y position of the second point
             * @return the direction
             */
            public Direction getDirection(float x1, float y1, float x2, float y2){
                double angle = getAngle(x1, y1, x2, y2);
                return Direction.fromAngle(angle);
            }

            /**
             *
             * Finds the angle between two points in the plane (x1,y1) and (x2, y2)
             * The angle is measured with 0/360 being the X-axis to the right, angles
             * increase counter clockwise.
             *
             * @param x1 the x position of the first point
             * @param y1 the y position of the first point
             * @param x2 the x position of the second point
             * @param y2 the y position of the second point
             * @return the angle between two points
             */
            public double getAngle(float x1, float y1, float x2, float y2) {

                double rad = Math.atan2(y1-y2,x2-x1) + Math.PI;
                return (rad*180/Math.PI + 180)%360;
            }
        });

        mTextViewDataEntry.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                mDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    private void setInitialPositionBottomSheetView() {
        mBottomSheetViewGroup.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                //makes sure it the data entry sheet doesn't go offscreen on smaller screens and snaps to the bottom

                final int screenHeight = getResources().getDisplayMetrics().heightPixels;

                //get the absolute position on screen
                final int[] bottomViewLocation = new int[2];
                mBottomSheetViewGroup.getLocationOnScreen(bottomViewLocation);

                // detect if it will be off screen when animation occurs
                int translationYLocation = CU.dp2px(mBottomSheetViewGroup.getContext(),mBottomSheetInitialPositionOffsetInDp) + bottomViewLocation[1];
                if (translationYLocation > screenHeight) {
                    int barHeight = mTextViewDataEntry.getHeight();

                    //snap it to the bottom of the screen
                    mBottomSheetInitialPositionOffsetInDp = CU.px2dp(mBottomSheetViewGroup.getContext(), screenHeight - bottomViewLocation[1] - barHeight);
                    mBottomSheetAnimator = setupBottomSheetAnimator();
                    mBottomSheetViewGroup.setTranslationY(CU.dp2px(mBottomSheetViewGroup.getContext(), mBottomSheetInitialPositionOffsetInDp));
                } else {
                    mBottomSheetViewGroup.setTranslationY(CU.dp2px(mBottomSheetViewGroup.getContext(), mBottomSheetInitialPositionOffsetInDp));
                }
                mBottomSheetViewGroup.removeOnLayoutChangeListener(this);
            }
        });

    }

    private ObjectAnimator setupBottomSheetAnimator() {
        float propertyStart = 0f;
        float propertyEnd = CU.dp2px(this, mBottomSheetInitialPositionOffsetInDp);
        String propertyName = "translationY";
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mBottomSheetViewGroup, propertyName, propertyStart, propertyEnd);
        objectAnimator.setDuration(BOTTOM_SHEET_ANIMATION_DURATION_MS);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        return objectAnimator;
    }

    public boolean isVerticalStepperExpanded() {
        return isVerticalStepperExpanded;
    }

    private void toggleVerticalStepper() {

        float translatedY = mBottomSheetViewGroup.getTranslationY();
        if (translatedY == 0.0f) {
            if (mVerticalStepper != null) {
                mVerticalStepper.hideSoftKeyboard();
            }
            mBottomSheetAnimator.start();
            isVerticalStepperExpanded = false;
        } else {
            if (!stepperOpened) {
                mBottomSheetAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mVerticalStepperFormLayout.goToStep(0);
                        mBottomSheetAnimator.removeAllListeners();
                    }
                });
            }
            mBottomSheetAnimator.reverse();
            isVerticalStepperExpanded = true;
        }
        stepperOpened = true;
        mVerticalStepperFormLayout.setEnableInteraction(isVerticalStepperExpanded);
    }

    public void onMessagePanelSelected() {
        if (isVerticalStepperExpanded) {
            toggleVerticalStepper();
        }
    }

    @Override
    public void openDocumentResultsStep() {
        if(!isVerticalStepperExpanded)
            toggleVerticalStepper();
        if (mVerticalStepper.areAllMandatoryStepsCompleted()) {
            mVerticalStepper.openDocumentResults();
        }
    }

    public void onTestResultSelected() {
        if (isVerticalStepperExpanded) {
            toggleVerticalStepper();
        }
    }

    @Override
    public TestRecord getTestRecord() {
        return mTestRecord;
    }

    @Override
    public void displayTestMessageAndInstructionFragment() {
        displayingResults = false;
        if (!mMessageAndInstructionFrag.isVisible()) {
            mFm2.beginTransaction().hide(mTestResultsFragment).commit();
            mFm1.beginTransaction().show(mMessageAndInstructionFrag).commit();
        }
    }

    public void displayTestResultFragment() {
        displayingResults = true;
        if (!mTestResultsFragment.isVisible()) {
            mFm2.beginTransaction().show(mTestResultsFragment).commit();
            mFm1.beginTransaction().hide(mMessageAndInstructionFrag).commit();
        }
        mTestRecord = mTestUIDriver.getTestData();
        mTestResultsFragment.updateTestResults();
    }

    @Override
    public void refreshDataEntries() {
//        mTestUIDriver = (PatientTestUIDriver) TestManager.getInstance().getTestDriver(mReaderBTA);
//        mVerticalStepper.updatePatientData(mTestUIDriver.getTestData());
    }

    @Override
    public void onStop() {
        // hideProgressDialog();
        super.onStop();
    }

    /**
     * For dev testing purposes, creating the actionbar options menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        menu.add(0, 0, 0, "++Results").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
//        menu.add(0, 1, 0, "++Reload").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                mTestUIDriver.terminateWithOption(this);
                break;
            // this is a test
            case 1:
                LinearLayout ll = (LinearLayout) mVerticalStepperFormLayout.findViewById(R.id.content);
                ll.removeAllViews();
                createVerticalStepper();
                mVerticalStepper.disableDocumentResults();
                break;
            default:
                if (displayingResults) {
                    displayingResults = false;
                    displayTestMessageAndInstructionFragment();
                } else {
                    displayingResults = true;
                    displayTestResultFragment();
                }
        }
        return false;
    }

    /*************************************/

    @Override
    public void showProgressDialog() {
        mLinearLayout.setVisibility(View.VISIBLE);
        mLinearLayout.setClickable(true);
        mLinearLayout.setFocusable(true);
    }

    @Override
    public void hideProgressDialog() {
        mLinearLayout.setClickable(false);
        mLinearLayout.setFocusable(false);
        mLinearLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mTestUIDriver.terminateWithOption(this);
        }
        return false;
    }

    @Override
    public void enableDocumentResults() {
        mVerticalStepper.enableDocumentResults();
    }

    @Override
    public void setTestPanels() {
        mVerticalStepper.setTestPanels();
    }

    @Override
    public void updateTestRecord() {
        mTestRecord = mTestUIDriver.getTestData();
    }


    @Override
    public void showLogcatMessage(String message) {
        Log.w("Host4", message);
    }

    @Override
    public void updateActivityTitle(String title) {
        changeTitle(title);
    }

    @Override
    public void closeActivity() {

        mTestUIDriver.terminateWithOption(this);
    }

    @Override
    public void mockTestPanels() {
 //       mVerticalStepper.setMockTestPanels();
    }

    @Override
    public boolean canShowResults() {
        return mVerticalStepper.areAllMandatoryStepsCompleted() && mTestUIDriver.isCalculationDone();
    }
}
