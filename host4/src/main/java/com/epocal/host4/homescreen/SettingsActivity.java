package com.epocal.host4.homescreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.epocal.common.eventmessages.EpocNavigationObject;
import com.epocal.common.realmentities.User;
import com.epocal.common.types.UIScreens;
import com.epocal.datamanager.UserModel;
import com.epocal.host4.BaseActivity;
import com.epocal.host4.R;

import org.greenrobot.eventbus.EventBus;

import static com.epocal.common.Consts.ADMINISTRATOR;
import static com.epocal.common.Consts.ANONYMOUS;

public class SettingsActivity extends BaseActivity {

    RelativeLayout mLLSetDateTime, mLLTestSettings, mLLHostSettings, mLLExitHost, mLLQAScheduleSettings, mLLDataManagement, mLLAbout, mLLReaderSettings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        changeTitle(getString(R.string.settings_s));
        showHomeBack();
        setUIElements();
        setUIElementsClicks();
        setUIElementsVisibility();
    }

    private void setUIElements() {
        mLLSetDateTime = findViewById(R.id.ll_set_date_time);
        mLLTestSettings = findViewById(R.id.ll_test_settings);
        mLLHostSettings = findViewById(R.id.ll_host_settings);
        mLLReaderSettings = findViewById(R.id.ll_reader_settings);
        mLLQAScheduleSettings = findViewById(R.id.ll_qa_schedule_settings);
        mLLDataManagement = findViewById(R.id.ll_data_management);
        mLLAbout = findViewById(R.id.ll_about);
        mLLExitHost = findViewById(R.id.ll_exit_host);
    }

    private void setUIElementsClicks() {
        setDateTime();
        readerSettings();
        testSettings();
        hostSettings();
        qAScheduleSettings();
        dataManagement();
        about();
        exitHost();
    }

    private void setUIElementsVisibility() {
        User user = new UserModel().getLoggedInUser();

        if (user == null)
            user = new UserModel().getAnonymousUser();

        String id = user.getUserId();

        switch (id) {
            case ADMINISTRATOR: {
                // Show all the items
                break;
            }
            case ANONYMOUS: {
                mLLSetDateTime.setVisibility(View.GONE);
                mLLTestSettings.setVisibility(View.GONE);
                mLLQAScheduleSettings.setVisibility(View.GONE);
                mLLDataManagement.setVisibility(View.GONE);
                mLLExitHost.setVisibility(View.GONE);
                break;
            }
            default: { //Any other user
                mLLSetDateTime.setVisibility(View.GONE);
                mLLTestSettings.setVisibility(View.GONE);
                mLLQAScheduleSettings.setVisibility(View.GONE);
                mLLDataManagement.setVisibility(View.GONE);
                mLLExitHost.setVisibility(View.GONE);
            }
        }
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void showSnackBar(String text) {
        Snackbar.make(getWindow().getDecorView().getRootView(), text, Snackbar.LENGTH_SHORT).show();
    }

    private void readerSettings() {
        mLLReaderSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EpocNavigationObject message = new EpocNavigationObject();
                message.setContext(SettingsActivity.this);
                message.setTargetscreen(UIScreens.ReaderSettingsScreen);
                EventBus.getDefault().post(message);
            }
        });
    }

    private void about() {
        mLLAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EpocNavigationObject message = new EpocNavigationObject();
                    message.setContext(SettingsActivity.this);
                    message.setTargetscreen(UIScreens.AboutScreen);
                    EventBus.getDefault().post(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void exitHost() {
        mLLExitHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLockTask();
                EpocNavigationObject message = new EpocNavigationObject();
                message.setContext(SettingsActivity.this);
                message.setTargetscreen(UIScreens.EXIT_HOST);
                EventBus.getDefault().post(message);
                killActivity();
            }
        });
    }

    private void setDateTime() {
        mLLSetDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
                //DialogFragmentDateAndTime dialog = DialogFragmentDateAndTime.newInstance();
                //dialog.show(getSupportFragmentManager(), "");
            }
        });
    }

    private void testSettings() {
        mLLTestSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EpocNavigationObject message = new EpocNavigationObject();
                message.setContext(SettingsActivity.this);
                message.setTargetscreen(UIScreens.TestSettingsScreen);
                EventBus.getDefault().post(message);
            }
        });
    }

    private void hostSettings() {
        mLLHostSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EpocNavigationObject message = new EpocNavigationObject();
                message.setContext(SettingsActivity.this);
                message.setTargetscreen(UIScreens.HostSettingsScreen);
                EventBus.getDefault().post(message);
            }
        });
    }

    private void qAScheduleSettings() {
//        bQAScheduleSettings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showSnackBar("Under development");
//            }
//        });
    }

    private void dataManagement() {
        mLLDataManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EpocNavigationObject message = new EpocNavigationObject();
                    message.setContext(SettingsActivity.this);
                    message.setTargetscreen(UIScreens.DMSettingScreen);
                    EventBus.getDefault().post(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
