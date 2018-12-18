package com.epocal.datamanager;

import com.epocal.common.realmentities.DMSetting;
import com.epocal.datamanager.realmrepository.PrimaryKeyFactory;

import io.realm.Realm;

public class DMSettingModel {

    Realm mRealm = null;

    public void openRealmInstance() {
        if (mRealm == null)
            mRealm = Realm.getDefaultInstance();
    }

    public void closeRealmInstance() {
        if (mRealm != null) {
            mRealm.close();
            mRealm = null;
        }
    }

    public DMSetting getDMSetting() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;
        DMSetting setting = realm.copyFromRealm(realm.where(DMSetting.class).findFirst());

        if (mRealm == null)
            realm.close();

        return setting;
    }

    public void saveSetting(final DMSetting unmanagedDMSetting) {
        if (unmanagedDMSetting.getId() == 0) {
            createNewDMSetting(unmanagedDMSetting);
        } else if (unmanagedDMSetting.getId() > 0) {
            updateDMSetting(unmanagedDMSetting);
        }
    }

    private void createNewDMSetting(final DMSetting unmanagedDMSetting) {
        // here we got an unmanaged User with all fields validated
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;
        //create by administrator
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                DMSetting setting = realm.createObject(DMSetting.class, PrimaryKeyFactory.getInstance().nextKey(DMSetting.class));
                if(setting != null) {
                    setting.setAddress(unmanagedDMSetting.getAddress());
                    setting.setPort(unmanagedDMSetting.getPort());
                    setting.setPresent(unmanagedDMSetting.getPresent());
                }
            }
        });

        if (mRealm == null)
            realm.close();
    }

    private void updateDMSetting(final DMSetting unmanagedDMSetting) {
        // here we got an unmanaged User with all fields validated
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;
        //create administrator
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                DMSetting setting = realm.where(DMSetting.class).findFirst();
                if (setting != null) {
                    setting.setAddress(unmanagedDMSetting.getAddress());
                    setting.setPort(unmanagedDMSetting.getPort());
                    setting.setPresent(unmanagedDMSetting.getPresent());
                }
            }
        });

        if (mRealm == null)
            realm.close();
    }
}
