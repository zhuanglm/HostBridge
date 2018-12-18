package com.epocal.datamanager;

import com.epocal.common.realmentities.BarcodeSetting;
import com.epocal.common.types.BarcodeSymbologiesType;
import com.epocal.common.types.InputFieldType;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class BarcodeSettingModel {
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

    public ArrayList<BarcodeSetting> getAllBarcodeSettings() {
        ArrayList<BarcodeSetting> allBarcodeSettings = new ArrayList<>();

        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        try {
            RealmResults<BarcodeSetting> allBarcodeSettingsInRealm = realm.where(BarcodeSetting.class).findAll();
            allBarcodeSettings.addAll(allBarcodeSettingsInRealm);
        } catch (Exception ex) {
            // todo handle exception
        }

        if (mRealm == null)
            realm.close();

        return allBarcodeSettings;
    }

    public void updateBarcodeSetting(final BarcodeSetting input) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                BarcodeSetting ds = realm.where(BarcodeSetting.class).equalTo("InputField", input.getInputField()).findFirst();
                ds.setInputFieldType(input.getInputFieldType());
                ds.setCropBegin(input.getCropBegin());
                ds.setCropEnd(input.getCropEnd());
                ds.setSymbologies(input.getSymbologies());
            }
        });

        if (mRealm == null)
            realm.close();

        postBarcodeSettingsChange();
    }

    public BarcodeSetting getBarcodeSetting(InputFieldType inputFieldType) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;
        BarcodeSetting barcodeSetting = realm.copyFromRealm(realm.where(BarcodeSetting.class).equalTo("InputField", inputFieldType.value).findFirst());

        if (mRealm == null)
            realm.close();

        if (true) {
            return barcodeSetting;
        } else {
            return Clone(barcodeSetting);
        }
    }

    public BarcodeSetting getBarcodeSetting(boolean managed) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;
        BarcodeSetting barcodeSetting = getBarcodeSetting(realm, managed);

        if (mRealm == null)
            realm.close();

        return barcodeSetting;
    }

    private BarcodeSetting getBarcodeSetting(Realm realm, boolean managed) {
        BarcodeSetting ds = realm.where(BarcodeSetting.class).findFirst();
        if (managed) {
            return ds;
        } else {
            return Clone(ds);
        }
    }

    public void setCropBegin(int cropBegin, boolean managed) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        BarcodeSetting barcodeSetting = getBarcodeSetting(realm, managed);

        realm.beginTransaction();
        barcodeSetting.setCropBegin(cropBegin);
        realm.commitTransaction();

        if (mRealm == null)
            realm.close();
    }

    public int getCropBegin() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        int cropBegin = getBarcodeSetting(realm, true).getCropBegin();

        if (mRealm == null)
            realm.close();

        return cropBegin;
    }

    public void setCropEnd(int cropEnd, boolean managed) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        BarcodeSetting barcodeSetting = getBarcodeSetting(realm, managed);

        realm.beginTransaction();
        barcodeSetting.setCropEnd(cropEnd);
        realm.commitTransaction();

        if (mRealm == null)
            realm.close();
    }

    public int getCropEnd() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        int cropEnd = getBarcodeSetting(realm, true).getCropEnd();

        if (mRealm == null)
            realm.close();

        return cropEnd;
    }

    public void setSymbologies(long symbologies, boolean managed) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        BarcodeSetting barcodeSetting = getBarcodeSetting(realm, managed);

        realm.beginTransaction();
        barcodeSetting.setSymbologies(symbologies);
        realm.commitTransaction();

        if (mRealm == null)
            realm.close();
    }

    public long getSymbologies() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        long symbologies = getBarcodeSetting(realm, true).getSymbologies();

        if (mRealm == null)
            realm.close();

        return symbologies;
    }

    public void addSymbologies(InputFieldType symbologies, boolean managed) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        BarcodeSetting barcodeSetting = getBarcodeSetting(realm, managed);

        realm.beginTransaction();
        barcodeSetting.setSymbologies( barcodeSetting.getSymbologies() | symbologies.value);
        realm.commitTransaction();

        if (mRealm == null)
            realm.close();
    }

    public ArrayList<BarcodeSymbologiesType> getSymbologiesList() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        long symbologies = getBarcodeSetting(realm, true).getSymbologies();

        if (mRealm == null)
            realm.close();

        long sym = getSymbologies();
        ArrayList<BarcodeSymbologiesType> list = new ArrayList<BarcodeSymbologiesType>();
        for (BarcodeSymbologiesType item : BarcodeSymbologiesType.values()) {
            if ((sym & item.value) == item.value) {
                list.add(item);
            }
        }
        return list;
    }

    public boolean isSymbologiesEnable(BarcodeSymbologiesType symbologiesType) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        long symbologies = getBarcodeSetting(realm, true).getSymbologies();

        if (mRealm == null)
            realm.close();

        long sym = getSymbologies();
        ArrayList<BarcodeSymbologiesType> list = new ArrayList<BarcodeSymbologiesType>();
        for (BarcodeSymbologiesType item : BarcodeSymbologiesType.values()) {
            if(symbologiesType.value == item.value) {
                if ((sym & item.value) == item.value) {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        return false;
    }

    public void resetToFactoryDefault() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                BarcodeSetting ds = realm.where(BarcodeSetting.class)
                        .findFirst();
                ds.resetFactoryDefault();
            }
        });
        if (mRealm == null)
            realm.close();
    }

    private void postBarcodeSettingsChange() {
        //EventBus.getDefault().post(new DeviceSettingsChangeNotification());
    }

    public BarcodeSetting Clone(BarcodeSetting input) {
        BarcodeSetting ds = new BarcodeSetting();
        ds.setInputFieldType(input.getInputFieldType());
        ds.setSymbologies(input.getSymbologies());
        ds.setCropBegin(input.getCropBegin());
        ds.setCropEnd(input.getCropEnd());

        return ds;
    }
}
