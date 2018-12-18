package com.epocal.host4.dmsettingscreen;

import com.epocal.common.realmentities.DMSetting;
import com.epocal.datamanager.DMSettingModel;
import com.epocal.util.IntegerUtil;
import com.epocal.util.NetworkUtil;

public class DMSettingPresenter implements DMSettingScreenContract.Presenter  {

    DMSettingScreenContract.View mView;
    DMSettingModel mDMSettingModel;
    DMSetting setting;

    public DMSettingPresenter(DMSettingScreenContract.View view)
    {
        mView = view;
    }

    @Override
    public void load() {
        mDMSettingModel = new DMSettingModel();
        mDMSettingModel.openRealmInstance();
        setting = mDMSettingModel.getDMSetting();
        mView.setAddress(setting.getAddress());
        mView.setPort(setting.getPort());
        mView.setPresent(setting.getPresent());
    }

    @Override
    public boolean validateUI() {
        String address = mView.getAddress();
        String port = mView.getPort();
        if (NetworkUtil.isBluetoothAddress(address))
        {
            return true;
        }
        else
        {
            if (IntegerUtil.isInteger(port))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void save() {
        String address = mView.getAddress();
        String port = mView.getPort();
        boolean present = mView.isPresent();

        setting.setAddress(address);
        setting.setPort(port);
        setting.setPresent(present);
        mDMSettingModel.saveSetting(setting);
    }

    @Override
    public void unload() {
        mDMSettingModel.closeRealmInstance();
    }

}
