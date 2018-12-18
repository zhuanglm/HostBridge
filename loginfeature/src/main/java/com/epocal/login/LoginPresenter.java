package com.epocal.login;

import com.epocal.common.epocobjects.IApp;
import com.epocal.common.globaldi.GlobalAppModule;
import com.epocal.common.realmentities.User;
import com.epocal.common.types.AuthorizationLogin;
import com.epocal.datamanager.UserModel;
import com.epocal.hardware.EMDKScanner;
import com.epocal.hardware.ObserveBarcodeString;
import com.epocal.login.di.DaggerLoginPresenterComponent;
import com.epocal.login.di.LoginPresenterComponent;
import com.epocal.login.di.LoginScreenContract;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.epocal.common.Consts.ADMINISTRATOR;
import static com.epocal.common.Consts.PASSWORD_MIN_LENGTH;
import static com.epocal.common.Consts.USERID_ANONYMOUS;


/**
 * The login presenter class
 * <p>
 * Created by Zeeshan A Zakaria on 3/21/2017.
 */

public class LoginPresenter implements LoginScreenContract.Presenter {

    private LoginScreenContract.View mView;
    private AuthorizationLogin mLoginAuthType;
    private Disposable mDisposable;
    private int mUserIDLength = 4;
    private LoginScreenContract.Logic mLoginLogic;
    private EMDKScanner mEMDKScanner;
    private boolean mPromptAdminPassword = false;

    @Inject
    IApp mApplication;

    // we do a constructor injection here (of the view)
    @Inject
    public LoginPresenter(LoginScreenContract.View view, LoginScreenContract.Logic loginLogic, EMDKScanner emdkScanner) {

        mView = view;
        mLoginLogic = loginLogic;
        mEMDKScanner = emdkScanner;

        LoginPresenterComponent lpc = DaggerLoginPresenterComponent.builder()
                .globalAppModule(new GlobalAppModule())
                .build();
        lpc.inject(this);

        mDisposable = ObserveBarcodeString.getInstance().getBarcodeString()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String barcode) {
                        System.out.println("-----> Barcode is: " + barcode);
                        // Send to the view, i.e. login activity
                        mView.populateTextFromScanner(barcode);
                    }
                });
    }

    @Override
    public void start(AuthorizationLogin loginAuthType) {
        testConfigExpiryCheck();
        mLoginAuthType = loginAuthType;
        initScreen();
    }

    /**
     * Check when the test config is expiring and show the remaining days in a pop up dialog in the
     * view.
     */
    private void testConfigExpiryCheck() {
        // TODO: Translation would require these strings to be updated.
        int testConfigExpiryDays = mLoginLogic.getTestConfigExpiry(mApplication.getSoftwareExpirationDate().getTime());
        System.out.println("Expiry remaining: " + testConfigExpiryDays + " days");
        if (testConfigExpiryDays < 31 && testConfigExpiryDays > 0) {
            mView.showDialog("Test config is expiring in " + testConfigExpiryDays + " day" + (testConfigExpiryDays > 1 ? "s" : ""));
        } else if (testConfigExpiryDays < 0) {
            mView.showDialog("Test config is expired " + Math.abs(testConfigExpiryDays) + " day" + (Math.abs(testConfigExpiryDays) > 1 ? "s" : "") + " ago");
        }
    }

    /**
     * Initialize the screen, i.e. show the right fields as per the authentication settings
     */
    private void initScreen() {
        switch (mLoginAuthType) {
            case UserIdAndPassword:
                mView.showUserPasswordView();
                break;
            case UserIdOnly:
                mView.showUserIDView();
                break;
            case None:
                mUserIDLength = 1;
                mView.showUserIDView();
                break;
            default:
                break;
        }
    }

    /*
     * Return TRUE if last invalid login was in less than 5 minutes. This TRUE is needed to block
     * the user for 5 minutes.
     */
    private boolean lastInvalidLogin(Date attempt) {
        Date now = new Date();
        if (attempt == null) attempt = now;
        Long diff = now.getTime() - attempt.getTime();
        Long diffinMinutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        return diffinMinutes < 5;
    }

    @Override
    public void processUserIDUpdate(String userID) {
        mView.clearErrorMessageView(); // clear previous error as soon as the text changes
    }

    @Override
    public void processPasswordUpdate(String password) {
        mView.clearErrorMessageView(); // clear previous error as soon as the text changes
    }

    private void setUserAsLoggedIn(String userID) {
        UserModel userModel = new UserModel();

        userModel.openRealmInstance();
        userModel.setLoggedInByUserID(userID, true);
        userModel.setLastLoginByUserID(userID, new Date());
        userModel.closeRealmInstance();
    }

    @Override
    public boolean processLoginButtonClick(String userID, String password) {
        if (!validateUserIDText(userID)) {
            return false;
        }

        // Check 'administrator' login
        if (!mPromptAdminPassword) {
            mPromptAdminPassword = shouldPromptAdministratorPassword(userID);
            if (mPromptAdminPassword) {
                return false;
            }
        }

        if ((mLoginAuthType == AuthorizationLogin.UserIdAndPassword) || mPromptAdminPassword) {
            if (!validatePasswordText(password)) {
                return false;
            }
        }

        // doLogin
        String validatedUserId = getValidatedUserID(userID, password);
        // check if the user account was found.
        if (validatedUserId.isEmpty()) {
            showLoginErrorMessage();
            return false;
        }

        // check account - no account check needed for anony user.
        if (!validatedUserId.equals(USERID_ANONYMOUS)) {
            if (!validatedUserId.equals(userID)) {
                showLoginErrorMessage();
                return false;
            }

            if (!validateAccountStatus(validatedUserId)) {
                return false;
            }
        }

        // Login success
        setUserAsLoggedIn(validatedUserId);
        mView.emptyInputFields();
        return true;
    }

    public void processsUserIDFocusChanged(boolean userIDHasFocus) {
        if ((userIDHasFocus) && mPromptAdminPassword) {
            mView.resetAndHidePasswordView();
            mPromptAdminPassword = false;
        }
    }

    /**
     * Post scan operations that need to be run after the scan text has completed.
     */
    @Override
    public boolean processLoginScannerInput(String userID, String password) {
        boolean loginSuccess = false;
        boolean shouldProcessLoginButtonClick = false;
        if (mView.isUserIDViewFocused() && !userID.isEmpty()) {
            if (!mView.isPasswordViewHidden()) {
                mView.clearPasswordViewText();
                mView.moveFocusToPasswordView();
                return loginSuccess;
            }
            // if PasswordView was hidden, processLoginButtonClick will display PasswordView.
            shouldProcessLoginButtonClick = true;
        }
        if (mView.isPasswordViewFocused() && !password.isEmpty()) {
            shouldProcessLoginButtonClick = true;
        }

        if (shouldProcessLoginButtonClick) {
            loginSuccess = processLoginButtonClick(userID, password);
        }

        return loginSuccess;
    }

    private String getValidatedUserID(String userID, String password) {
        String validatedUserID = "";
        // admin login override when userID = 'administrator'

        UserModel userModel = new UserModel();
        userModel.openRealmInstance();

        User user = null;
        if (userID.equals(ADMINISTRATOR)) {
            if (password.equals(ADMINISTRATOR)) {
                user = userModel.authenticateUser(userID, password);
            }
        } else {
            // regular login
            switch (mLoginAuthType) {
                case None:
                    user = userModel.findUser(USERID_ANONYMOUS);
                    break;

                case UserIdOnly:
                    user = userModel.findUser(userID);
                    break;

                case UserIdAndPassword:
                    user = userModel.authenticateUser(userID, password);

                    // When password fails, UserModel will return the annonymous user.
                    // Catch this case by checking the user obj has the same userID as Login form userID.
                    if ((user != null) && !user.getUserId().equals(userID)) {
                        user = null;
                    }
                    break;
            }
        }

        if (user != null)
            validatedUserID = user.getUserId();

        userModel.closeRealmInstance();

        return validatedUserID;
    }


    private boolean validateUserIDText(String userID) {
        // 1. - Check non-empty string rule
        // 2. - Check min number of char rule
        if ((userID == null) || (userID.isEmpty()) || (userID.length() < mUserIDLength)) {
            showLoginErrorMessage();
            return false;
        }
        return true;
    }

    private boolean validatePasswordText(String password) {
        // 1. - Check non-empty string rule
        // 2. - Check min number of char rule
        if ((password == null) || (password.isEmpty()) || password.length() < PASSWORD_MIN_LENGTH) {
            showLoginErrorMessage();
            return false;
        }
        return true;
    }

    // Special override for "administrator" login - to display Password View if hidden.
    private boolean shouldPromptAdministratorPassword(String userID) {
        if (userID.equals(ADMINISTRATOR) && (mView.isPasswordViewHidden())) {
            mView.showPasswordView();
            return true;
        }
        return false;
    }

    private boolean validateAccountStatus(String userID) {
        UserModel userModel = new UserModel();
        userModel.openRealmInstance();

        boolean isUserValid = false;
        User user = userModel.getUser(userID);
        if (user == null) {
            mView.showErrorMessageView(mView.getErrorString(R.string.invalid_id));
        } else {
            boolean invalidLogin = lastInvalidLogin(user.getLastInvalidLogin());
            if (user.getInvalidLoginAttempts() >= 3 && invalidLogin) {
                mView.showErrorMessageView(mView.getErrorString(R.string.user_account_lockedout));
            } else if (!user.getEnabled()) {
                mView.showErrorMessageView(mView.getErrorString(R.string.user_account_disabled));
            } else if ((user.Expires() != null) && user.Expires().before(new Date())) {
                mView.showErrorMessageView(mView.getErrorString(R.string.user_account_expired));
            } else {
                isUserValid = true;
            }
        }

        userModel.closeRealmInstance();
        return isUserValid;
    }

    private void showLoginErrorMessage() {
        if (mView.isPasswordViewHidden()) {
            mView.showErrorMessageView(mView.getErrorString(R.string.invalid_id));
        } else {
            mView.clearPasswordViewText();
            mView.showErrorMessageView(mView.getErrorString(R.string.invalid_id_or_password));
        }
    }

    @Override
    public void cancelRead() {
        mEMDKScanner.cancelRead();
    }

    @Override
    public void emdkOnResume() {
        mEMDKScanner.onResume();
    }

    @Override
    public void emdkOnStop() {
        mEMDKScanner.onStop();
    }

    @Override
    public void emdkOnDestroy() {
        mEMDKScanner.onDestroy();
    }

    @Override
    public void disposeRx() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}