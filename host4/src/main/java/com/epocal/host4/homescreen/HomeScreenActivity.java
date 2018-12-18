package com.epocal.host4.homescreen;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.epocal.common.epocobjects.QATestBuffer;
import com.epocal.common.eventmessages.EpocNavigationObject;
import com.epocal.common.realmentities.Log;
import com.epocal.common.realmentities.User;
import com.epocal.common.types.LogContext;
import com.epocal.common.types.LogLevel;
import com.epocal.common.types.TestType;
import com.epocal.common.types.UIScreens;
import com.epocal.common.types.am.TestMode;
import com.epocal.datamanager.UserModel;
import com.epocal.epoclog.LogServer;
import com.epocal.epoctest.TestContainerEvents;
import com.epocal.epoctest.TestManager;
import com.epocal.host4.BaseActivity;
import com.epocal.host4.PlaceHolderActivity;
import com.epocal.host4.R;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

import static com.epocal.common.Consts.ADMINISTRATOR;
import static com.epocal.common.Consts.ANONYMOUS;

public class HomeScreenActivity extends BaseActivity {

    RelativeLayout mRLTestHistory, mRLSettings, mRLNotifications, mRLSignOut, mRLRunPatientTest, mRLRunQATest, mRLAbout;
    TextView mTVRunPatientTest;
    ProgressBar mProgressBar;
    User mCurrentUser;

    @Override
    public void onPause() {
        super.onPause();
        TestManager.getInstance().getTestContainerEmitter().unsubscribe();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCurrentUser = new UserModel().getLoggedInUser();
        if (mCurrentUser == null) {
            EpocNavigationObject message = new EpocNavigationObject();
            message.setContext(HomeScreenActivity.this);
            message.setTargetscreen(UIScreens.LoginScreen);
            EventBus.getDefault().post(message);
        } else {
            setLinearLayouts();
        }
        TestManager.getInstance().getTestContainerEmitter().subscribeOn(new Consumer<TestContainerEvents>() {
            @Override
            public void accept(TestContainerEvents containerEvent) {
                updateTestButtons();
            }
        }, AndroidSchedulers.mainThread());
        updateTestButtons();
    }

    private void updateTestButtons() {
        if (TestManager.getInstance().areMultipleActiveTests()) {
            mTVRunPatientTest.setText(R.string.show_active_tests);
            mRLRunQATest.setEnabled(false);
            mRLRunQATest.setVisibility(View.GONE);
        } else {
            mTVRunPatientTest.setText(R.string.patient_test);
            mRLRunQATest.setEnabled(true);
            mRLRunQATest.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_screen);
        changeTitle("");
        if (getSupportActionBar()!=null) {
            getSupportActionBar().hide();
        }

        mProgressBar = findViewById(R.id.progress_bar_home_screen);
        mRLRunPatientTest =  findViewById(R.id.ll_run_patient_test);
        mRLRunQATest = findViewById(R.id.ll_run_qa_test);
        mRLTestHistory = findViewById(R.id.ll_test_history);
        mRLSettings = findViewById(R.id.ll_settings);
        //mRLNotifications =  findViewById(R.id.ll_notifications);
        mRLAbout = findViewById(R.id.ll_about);
        mRLSignOut = findViewById(R.id.ll_sign_out);
        mTVRunPatientTest = findViewById(R.id.tv_run_patient_test);

        mCurrentUser = new UserModel().getLoggedInUser();


        mRLRunPatientTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TestManager to decide how to start a patient test
                try {
                    if (TestManager.getInstance().areMultipleActiveTests()) {
                        TestManager.getInstance().navigateToMultiTestActivity(HomeScreenActivity.this, false);
                    } else {
                        Log epocLog = new Log();
                        epocLog.setAlert(false);
                        epocLog.setLogContext(LogContext.HOST);
                        epocLog.setLogLevel(LogLevel.Information);
                        epocLog.setMessage("Starting a Patient test");
                        LogServer.getInstance().log(epocLog);
                        TestManager.getInstance().startTest(HomeScreenActivity.this, TestMode.BloodTest, null, false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mRLRunQATest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log epocLog = new Log();
                    epocLog.setAlert(false);
                    epocLog.setLogContext(LogContext.HOST);
                    epocLog.setLogLevel(LogLevel.Information);
                    epocLog.setMessage("Starting a QA test");
                    LogServer.getInstance().log(epocLog);
                    TestManager.getInstance().startTest(HomeScreenActivity.this, TestMode.QA, TestType.Unknown, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mRLTestHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EpocNavigationObject message = new EpocNavigationObject();
                    message.setContext(HomeScreenActivity.this);
                    message.setTargetscreen(UIScreens.TestHistoryScreen);
                    EventBus.getDefault().post(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mRLSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to the launcher and start the HomeActivity
                try {
                    EpocNavigationObject message = new EpocNavigationObject();
                    message.setContext(HomeScreenActivity.this);
                    message.setTargetscreen(UIScreens.SettingsScreen);
                    EventBus.getDefault().post(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
//        mRLNotifications.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(HomeScreenActivity.this, PlaceHolderActivity.class));
//                overridePendingTransition(0, 0);
//            }
//        });
        mRLAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EpocNavigationObject message = new EpocNavigationObject();
                    message.setContext(HomeScreenActivity.this);
                    message.setTargetscreen(UIScreens.AboutScreen);
                    EventBus.getDefault().post(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mRLSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignOutScreen();
            }
        });
    }

    @Override
    public void onBackPressed() {
        goToSignOutScreen();
    }

    private void goToSignOutScreen() {
        new MaterialDialog.Builder(this)
                .title(R.string.warning)
                .content(R.string.msg_sign_out)
                .negativeText(R.string.no)
                .positiveText(R.string.yes)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        new CloseActivity().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }).show();
    }

    private void setLinearLayouts() {
        String currentUser = mCurrentUser.getUserId();
        // todo: rework this based on quality and testing permission of users
        switch (currentUser) {
            case ADMINISTRATOR: {
                mRLRunPatientTest.setVisibility(View.GONE);
                mRLRunQATest.setVisibility(View.GONE);
                break;
            }
            case ANONYMOUS: {
                mRLTestHistory.setVisibility(View.GONE);
                //mRLNotifications.setVisibility(View.GONE);
                // mRLRunQATest.setEnabled(true);
                break;
            }
            default: {
                mRLTestHistory.setVisibility(View.GONE);
                //mRLNotifications.setVisibility(View.GONE);
            }
        }
    }

    /**
     * A smoother exit using Progress spinner to give user some feedback.
     */
    private class CloseActivity extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
//                Thread.sleep(500);  // TODO: Forgot why was this pause put here. If it is not used, it should be deleted. [Zeeshan 18-Sep-2017]
                EpocNavigationObject message = new EpocNavigationObject();
                message.setContext(HomeScreenActivity.this);
                message.setTargetscreen(UIScreens.LoginScreen);
                EventBus.getDefault().post(message);
                UserModel userModel = new UserModel();
                User user = userModel.getLoggedInUser();
                if (user != null) {
                    userModel.logoutUser(user);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            finish();
        }
    }
}
