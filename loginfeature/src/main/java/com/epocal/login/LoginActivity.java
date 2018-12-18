package com.epocal.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.epocal.common.eventmessages.EpocNavigationObject;
import com.epocal.common.globaldi.GlobalAppModule;
import com.epocal.common.realmentities.User;
import com.epocal.common.types.AuthorizationLogin;
import com.epocal.common.types.LogoutPowerOffType;
import com.epocal.common.types.UIScreens;
import com.epocal.datamanager.HostConfigurationModel;
import com.epocal.datamanager.UserModel;
import com.epocal.login.di.DaggerLoginScreenComponent;
import com.epocal.login.di.LoginScreenComponent;
import com.epocal.login.di.LoginScreenContract;
import com.epocal.login.di.LoginScreenModule;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import static android.view.KeyEvent.KEYCODE_BUTTON_R1;
import static com.epocal.common.Consts.AC;
import static com.epocal.common.Consts.BATTERY;
import static com.epocal.common.Consts.DEVICE_STATE;
import static com.epocal.common.Consts.POWER_TYPE;

public class LoginActivity extends AppCompatActivity implements LoginScreenContract.View {

    ProgressBar mPBar;
    Button mBtnLogin;
    EditText mEtOperatorId, mEtPassword;
    TextView mTvErrorMessage;
    int mDataLength = 0;
    AuthorizationLogin mAuthorizationLogin;
    boolean screenOffOnInactivityLogout = false;
    boolean shouldNavigateToHomeScreen = false;
    SharedPreferences mPrefs;
    boolean mScannerTriggered = false;

    @Inject
    LoginPresenter mPresenter;

    public void changeTitle(CharSequence title) {
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(title);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mPrefs = getSharedPreferences(DEVICE_STATE, Context.MODE_PRIVATE);

        forceLogoutUsers();

        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        changeTitle(getString(R.string.sign_in));
        mBtnLogin = findViewById(R.id.button_login);

        mAuthorizationLogin = new HostConfigurationModel().getUnmanagedHostConfiguration().getAuthorizationLogin();

        LoginScreenComponent lsc = DaggerLoginScreenComponent.builder()
                .loginScreenModule(new LoginScreenModule(this))
                .globalAppModule(new GlobalAppModule())
                .build();
        lsc.inject(this);

        mEtOperatorId = findViewById(R.id.edit_text_operator_id);
        mEtPassword = findViewById(R.id.edit_text_password);
        mTvErrorMessage = findViewById(R.id.text_view_error_message);
        mPBar = findViewById(R.id.progress_bar);

        setupLoginButtonClickListener();
        setupUserIDTextChangedListener();
        setupUserIDFocusChangeListener();
        setupPasswordTextChangedListener();

        mPresenter.start(mAuthorizationLogin);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case 1: // Restarting the EMDK manager
                mPresenter.emdkOnResume();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // InactivityLogout: Below are extra actions that LoginScreen needs to
        // handle for Inactivity Logout Feature.
        Bundle parm = getIntent().getExtras();
        if (parm != null) {
            boolean handleInactivity = parm.getBoolean("Handle_Inactivity", false);
            if (handleInactivity) {
                String currentPowerType = mPrefs.getString(POWER_TYPE, AC);
                LogoutPowerOffType logoutPowerOffType = new HostConfigurationModel().getUnmanagedHostConfiguration().getLogoutPowerOffType();
                switch (logoutPowerOffType) {

                    case ON_BATTERY:
                        if (currentPowerType.equals(BATTERY)) {
                            screenOffOnInactivityLogout = true;
                        }
                        break;
                    case ON_AC:
                        if (currentPowerType.equals(AC) || currentPowerType.equals(BATTERY)) {
                            screenOffOnInactivityLogout = true;
                        }
                        break;
                    case BOTH:
                        screenOffOnInactivityLogout = true;
                        break;
                    default: // BOTH
                        screenOffOnInactivityLogout = false;
                        break;
                }
            }


        }
        // InactivityLogout: While LoginScreen is displayed, stop Inactivity timer.
        inactivityBroadcast(false);
        // InactivityLogout: If InactivityLogout feature is the one that started the
        // LoginScreen, then one of the following flags may be set to handle
        // extra operations.

        if (screenOffOnInactivityLogout) {
            screenOffOnInactivityLogout = false;
            turnScreenOff(15000);
        } else {
            // Force keep screen ON
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        try {
            EpocNavigationObject message = new EpocNavigationObject();
            message.setContext(this);
            message.setTargetscreen(UIScreens.ENABLE_KIOSK_MODE);
            EventBus.getDefault().post(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mPresenter.emdkOnResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.emdkOnStop();

        if (shouldNavigateToHomeScreen) {
            killActivity();
        }
    }

    private void killActivity() {
        mPresenter.emdkOnDestroy();
        mPBar.setVisibility(View.GONE);
        finishAndRemoveTask(); // Kill the Activity so that the HomeScreenActivity becomes the first in the stack.
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = getCurrentFocus();
        if ((v != null) && (imm != null))
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * In the kiosk mode the back mButton should not work in this activity because otherwise it takes
     * the user to the previous activity which is still running and can't be killed without disabling
     * the kiosk mode too. So overriding the back key press here.
     *
     * @param keyCode the key code
     * @param event   the event
     * @return returning false to disable the key
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // KEYCODE_BUTTON_R1 is a hardware button to trigger the barcode scanner
        // onKeyDown receives this keyCode when the hardware button was pressed and the scan was successful.
        // onKeyDown DOES NOT receive this keyCode when the hardware button was pressed but the scan was NOT successful.
        if (keyCode == KEYCODE_BUTTON_R1) {
            mScannerTriggered = true;
        }
        return false;
    }

    /**
     * Inactivity Timer: Broadcasts message to the InactivityService which is taking care of user inactivity timer.
     *
     * @param value the flag to convey whether to logout the user (true) or stop the timer (false)
     */
    private void inactivityBroadcast(boolean value) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.epocal.host4.inactivity");
        broadcastIntent.putExtra("inactivity", value);
        sendBroadcast(broadcastIntent);
    }

    private void turnScreenOff(int delayInMilliSec) {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Settings.System.putString(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, String.valueOf(delayInMilliSec));
    }

    /**
     * Go to the home screen after the login
     */
    private void goToHomeScreen() {
        shouldNavigateToHomeScreen = true;
        mPBar.setVisibility(View.VISIBLE);
        hideKeyboard();
        // Stop the mScanner
        mPresenter.emdkOnStop();
        mPresenter.disposeRx();

        // Go back to the launcher and start the HomeActivity
        try {
            EpocNavigationObject message = new EpocNavigationObject();
            message.setContext(this);
            message.setTargetscreen(UIScreens.HomeScreen);
            EventBus.getDefault().post(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupLoginButtonClickListener() {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleLoginButtonClick();
            }
        });
    }

    private void setupUserIDTextChangedListener() {
        mEtOperatorId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO: Workaround to get ScannerTriggered event from onKeyDown.
                if (mScannerTriggered) {
                    mScannerTriggered = false;
                    if (s.length() > 1) {
                        // keep the last char. delete everything else
                        s.delete(0, s.length() - 1);
                    }
                }

                mPresenter.processUserIDUpdate(s.toString());
            }
        });
    }

    private void setupUserIDFocusChangeListener() {
        mEtOperatorId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mPresenter.processsUserIDFocusChanged(hasFocus);
            }
        });
    }

    private void setupPasswordTextChangedListener() {
        mEtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO: Workaround to get ScannerTriggered event from onKeyDown.
                if (mScannerTriggered) {
                    mScannerTriggered = false;
                    if (s.length() > 1) {
                        // keep the last char. delete everything else
                        s.delete(0, s.length() - 1);
                    }
                }

                mPresenter.processPasswordUpdate(s.toString());
            }
        });
    }

    private void handleLogin() {
        // User login success. Find where to route user
        UserModel userModel = new UserModel();
        userModel.openRealmInstance();

        User user = userModel.getLoggedInUser();
        if (user != null) {
            userModel.updateUserLastLoginTime(user.getUserId(), new Date());
            goToHomeScreen();
        }

        userModel.closeRealmInstance();
    }

    /* Disable Kiosk mode, only for the devs */
    private void handleDkm() {
        stopLockTask();
        EpocNavigationObject message = new EpocNavigationObject();
        message.setContext(this);
        message.setTargetscreen(UIScreens.DISABLE_KIOSK_MODE);
        EventBus.getDefault().post(message);
    }

    /**
     * When the login button is clicked, try login.
     */
    private void handleLoginButtonClick() {
        String userId = mEtOperatorId.getText().toString();
        String pw = mEtPassword.getText().toString();

        // Special handling for the dkm mode, i.e. disable kiosk mode
        if (userId.equals("dkm")) {
            handleDkm();
            return;
        }

        if (mPresenter.processLoginButtonClick(userId, pw)) {
            handleLogin();
        }
    }

//    /**
//     * When scanner reads in input, try login.
//     * In somecases, it just changes the focus from userID field to password field.
//     */
//    private void handleScannerInputEvent() {
//        if (mPresenter.processLoginScannerInput(mEtOperatorId.getText().toString(), mEtPassword.getText().toString())) {
//            handleLogin();
//        }
//    }

    /**
     * LoginScreenContract.View implementations
     */
    public void emptyInputFields() {
        mEtOperatorId.setText("");
        mEtPassword.setText("");
    }

    public void showUserIDView() {
        clearErrorMessageView();
        mEtOperatorId.setText("");
        mEtPassword.setVisibility(View.GONE);
        mEtOperatorId.requestFocus();
    }

    public void showUserPasswordView() {
        clearErrorMessageView();
        mEtOperatorId.setText("");
        mEtPassword.setText("");
        mEtPassword.setVisibility(View.VISIBLE);
        mEtOperatorId.requestFocus();
    }

    public void showPasswordView() {
        mEtPassword.setText("");
        mEtPassword.setVisibility(View.VISIBLE);
        mEtPassword.requestFocus();
    }

    public void resetAndHidePasswordView() {
        clearErrorMessageView();
        mEtPassword.setText("");
        mEtPassword.setVisibility(View.GONE);
        mEtOperatorId.requestFocus();
    }

    @Override
    public void showDialog(String message) {
        new MaterialDialog.Builder(this)
                .title(R.string.warning)
                .positiveText(R.string.okay)
                .content(message)
                .show();
    }

    public void clearPasswordViewText() {
        mEtPassword.setText("");
    }

    public String getErrorString(int stringResourceId) {
        return getString(stringResourceId);
    }

    public void showErrorMessageView(String errorMsg) {
        mTvErrorMessage.setText(errorMsg);
        mTvErrorMessage.setVisibility(View.VISIBLE);
    }

    public void clearErrorMessageView() {
        mTvErrorMessage.setText("");
        mTvErrorMessage.setVisibility(View.GONE);
    }

    public boolean isPasswordViewHidden() {
        return !mEtPassword.isShown();
    }

    public boolean isPasswordViewFocused() {
        return mEtPassword.hasFocus();
    }

    public boolean isUserIDViewFocused() {
        return mEtOperatorId.hasFocus();
    }

    public void moveFocusToPasswordView() {
        mEtPassword.requestFocus();
    }

    private void forceLogoutUsers() {
        // Make sure all users are logged out on device!!
        UserModel userModel = new UserModel();
        ArrayList<User> users = userModel.getAllLoggedInUser();
        for (User user : users) {
            userModel.logoutUser(user);
        }
    }

    // TODO: 2018-01-04: This function is NOT CALLED.
    // TODO: This is called from Presenter's emdk but the emdk functionality is currently not working.
    // TODO: Left here for the emdk investigation purpose.
    @Override
    public void populateTextFromScanner(String barcodeString) {
        if (mDataLength++ > 50) {
            mEtOperatorId.getText().clear();
            mDataLength = 0;
        }

        if (mEtOperatorId.hasFocus()) {
            mEtOperatorId.setText(barcodeString);
        } else {
            mEtPassword.setText(barcodeString);
        }

        try {
            mPresenter.cancelRead();
        } catch (Exception e) {
            e.printStackTrace();
        }

        handleLoginButtonClick();
    }
}
