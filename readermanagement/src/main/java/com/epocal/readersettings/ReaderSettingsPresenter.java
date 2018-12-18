package com.epocal.readersettings;

import com.epocal.common.realmentities.User;
import com.epocal.datamanager.UserModel;

import static com.epocal.common.Consts.ADMINISTRATOR;
import static com.epocal.common.Consts.ANONYMOUS;

/**
 * The Reader Settings mReaderSettingsPresenter class
 * <p>
 * Created on 01/15/2017.
 * <p>
 * Based on class in hostsettings
 */

class ReaderSettingsPresenter implements IReaderSettingsPresenter {

    private IReaderSettingsView readerSettingsView;
    private User mUser;

    ReaderSettingsPresenter(IReaderSettingsView readerSettingsView) {
        this.readerSettingsView = readerSettingsView;
        mUser = new UserModel().getLoggedInUser();
        if (mUser == null) {
            mUser = new UserModel().getAnonymousUser();
        }
    }

    private void setUIElementsVisibility() {
        User user = new UserModel().getLoggedInUser();

        if (user == null)
            user = new UserModel().getAnonymousUser();

        String id = user.getUserId();

        switch (id) {
            case ADMINISTRATOR: {
                readerSettingsView.enableReaderStatus(false);
                readerSettingsView.enableDedicatedReaders(true);
                readerSettingsView.enablePageReaderC(false);
                readerSettingsView.enableReaderUpgrade(false);
                readerSettingsView.enableReaderThermalQA(false);
                readerSettingsView.enableChangeReaderName(false);
                break;
            }
            case ANONYMOUS: {
                readerSettingsView.enableReaderStatus(false);
                readerSettingsView.enableDedicatedReaders(true);
                readerSettingsView.enablePageReaderC(false);
                readerSettingsView.enableReaderUpgrade(false);
                readerSettingsView.enableReaderThermalQA(false);
                readerSettingsView.enableChangeReaderName(false);
                break;
            }
            default: { //Any other user
                readerSettingsView.enableReaderStatus(false);
                readerSettingsView.enableDedicatedReaders(true);
                readerSettingsView.enablePageReaderC(false);
                readerSettingsView.enableReaderUpgrade(false);
                readerSettingsView.enableReaderThermalQA(false);
                readerSettingsView.enableChangeReaderName(false);
            }
        }
    }

    @Override
    public void loadView() {
        setUIElementsVisibility();
    }
}
