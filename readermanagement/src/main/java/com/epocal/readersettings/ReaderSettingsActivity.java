package com.epocal.readersettings;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.epocal.common.eventmessages.EpocNavigationObject;
import com.epocal.common.eventmessages.ExtraInfo;
import com.epocal.common.types.ReaderDiscoveryMode;
import com.epocal.common.types.UIScreens;
import com.epocal.common_ui.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import static com.epocal.common.CU.titleCase;

//import com.epocal.patienttestprocedure.ReadersListActivity;

/**
 * Reader Settings activity class
 * <p>
 * Created on 01/15/2017.
 * <p>
 * Based on class in hostsettings
 */

public class ReaderSettingsActivity extends BaseActivity implements IReaderSettingsView {

    private static final String TAG = ReaderSettingsActivity.class.getSimpleName();
    private RelativeLayout mLLReaderStatus, mLLReaderDedicate, mLLReaderPage, mLLReaderUpgrade, mLLReaderThermalQA, mLLReaderChangeNamePin;

    IReaderSettingsPresenter mReaderSettingsPresenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readermanager_settings);
        setReadOnly(false);
        changeTitle(titleCase(getString(R.string.reader_settings)));
        mReaderSettingsPresenter = new ReaderSettingsPresenter(this);
        showHomeBack();
        setUIElements();
        setUIElementsClicks();
        mReaderSettingsPresenter.loadView();
    }

    private void setUIElements() {
        mLLReaderStatus = findViewById(R.id.reader_setting_reader_status);
        mLLReaderDedicate = findViewById(R.id.reader_setting_reader_dedicate);
        mLLReaderPage = findViewById(R.id.reader_setting_reader_page);
        mLLReaderUpgrade = findViewById(R.id.reader_setting_reader_upgrade);
        mLLReaderThermalQA = findViewById(R.id.reader_setting_reader_thermalQA);
        mLLReaderChangeNamePin = findViewById(R.id.reader_setting_reader_change_name_pin);
    }

    private void setUIElementsClicks() {
        setReaderDedicateEvent();
    }

    private void setReaderDedicateEvent() {
        if (mLLReaderDedicate != null) {
            mLLReaderDedicate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleSelectDedicatedReadersClicked();
                }
            });
        }
    }

    /**
     * Handle selecting the Reader Status menu entry
     * TODO:  This is not currently used, there is no menu entry at this time.
     */
    @Override
    public void handleSelectReaderStatusClicked() {
        new MaterialDialog.Builder(this)
                .title(R.string.reader_settings_status_menu_01)
                .content(R.string.reader_settings_not_implemented)
                .positiveText(R.string.okay)
                .show();
    }

    /**
     * Handle selecting the Dedicate Reader menu entry
     */
    @Override
    public void handleSelectDedicatedReadersClicked() {
        //bm: navigation is not done from here.
        //this leads to reference entanglement. Please consult.
//        Intent intent = new Intent(this, ReadersListActivity.class);
//        intent.putExtra("SetDiscoveryMode", ReadersListActivity.DiscoveryMode.DEDICATE_READER.ordinal());
//        this.startActivity(intent);

        EpocNavigationObject message = new EpocNavigationObject();
        message.setContext(this);
        message.setExtraInfo1(new ExtraInfo("SetDiscoveryMode", ReaderDiscoveryMode.DEDICATE_READER.value));
        message.setTargetscreen(UIScreens.READER_DISCOVERY);
        EventBus.getDefault().post(message);
    }

    /**
     * Handle selecting the Page Reader menu entry
     * TODO:  This is not currently used, there is no menu entry at this time.
     */
    @Override
    public void handleSelectPageReaderClicked() {
        new MaterialDialog.Builder(this)
                .title(R.string.reader_settings_page_menu_01)
                .content(R.string.reader_settings_not_implemented)
                .positiveText(R.string.okay)
                .show();
    }

    /**
     * Handle selecting the Upgrade Reader menu entry
     * TODO:  This is not currently used, there is no menu entry at this time.
     */
    @Override
    public void handleSelectReaderUpgradeClicked() {
        new MaterialDialog.Builder(this)
                .title(R.string.reader_settings_upgrade_menu_01)
                .content(R.string.reader_settings_not_implemented)
                .positiveText(R.string.okay)
                .show();
    }

    /**
     * Handle selecting the Reader Thermal QA menu entry
     * TODO:  This is not currently used, there is no menu entry at this time.
     */
    @Override
    public void handleSelectReaderThermalQA() {
        new MaterialDialog.Builder(this)
                .title(R.string.reader_settings_thermal_qa_menu_01)
                .content(R.string.reader_settings_not_implemented)
                .positiveText(R.string.okay)
                .show();
    }

    /**
     * Handle selecting the Change Reader Name menu entry
     * TODO:  This is not currently used, there is no menu entry at this time.
     */
    @Override
    public void handleSelectChangeReaderName() {
        new MaterialDialog.Builder(this)
                .title(R.string.reader_settings_change_name_menu_01)
                .content(R.string.reader_settings_not_implemented)
                .positiveText(R.string.okay)
                .show();
    }

    @Override
    public void enableReaderStatus(boolean enabled) {
        showhideItems(mLLReaderStatus, enabled);
    }

    @Override
    public void enableDedicatedReaders(boolean enabled) {
        showhideItems(mLLReaderDedicate, enabled);
    }

    @Override
    public void enablePageReaderC(boolean enabled) {
        showhideItems(mLLReaderPage, enabled);
    }

    @Override
    public void enableReaderUpgrade(boolean enabled) {
        showhideItems(mLLReaderUpgrade, enabled);
    }

    @Override
    public void enableReaderThermalQA(boolean enabled) {
        showhideItems(mLLReaderThermalQA, enabled);
    }

    @Override
    public void enableChangeReaderName(boolean enabled) {
        showhideItems(mLLReaderChangeNamePin, enabled);
    }

    private void showhideItems(RelativeLayout layout, boolean enabled) {
        if (layout != null) {
            if (enabled) {
                layout.setVisibility(View.VISIBLE);
            } else {
                layout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
