package com.epocal.login.di;


import com.epocal.common.types.AuthorizationLogin;

public interface LoginScreenContract {
    interface View {
        void showUserIDView();
        void showUserPasswordView();
        void showPasswordView();
        void resetAndHidePasswordView();
        void showDialog(String message);
        void clearPasswordViewText();
        void populateTextFromScanner(String barcodeString);
        void emptyInputFields();
        String getErrorString(int errorStringId);
        void showErrorMessageView(String errorMsg);
        void clearErrorMessageView();
        boolean isPasswordViewHidden();
        boolean isPasswordViewFocused();
        boolean isUserIDViewFocused();
        void moveFocusToPasswordView();
    }

    interface Presenter {
        void start(AuthorizationLogin loginAuthType);
        void processUserIDUpdate(String userID);
        void processPasswordUpdate(String password);
        void cancelRead();
        void emdkOnResume();
        void emdkOnStop();
        void emdkOnDestroy();
        void disposeRx();

        boolean processLoginButtonClick(String userID, String password);
        void processsUserIDFocusChanged(boolean userIDHasFocus);
        boolean processLoginScannerInput(String userID, String password);
    }


    interface Logic {
        int getTestConfigExpiry(long expiryDateInDays);
    }
}
