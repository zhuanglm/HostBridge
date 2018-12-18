package com.epocal.testsettingsfeature;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.epocal.common.CU;
import com.epocal.common.types.SelenaFamilyType;
import com.epocal.common_ui.BaseActivity;
import com.epocal.testsettingsfeature.dataentrysettings.DataEntrySettingsFragment;
import com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.FieldSettingsFragment;
import com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.selenafamilies.SelenaEditValuesFragment;
import com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.testselection.TestSelectionFragment;
import com.epocal.testsettingsfeature.unitsandreportablerangesscreen.BGPlus.BGPlusFragment;
import com.epocal.testsettingsfeature.unitsandreportablerangesscreen.EPlus.EPlusFragment;
import com.epocal.testsettingsfeature.unitsandreportablerangesscreen.MPlus.MPlusFragment;
import com.epocal.testsettingsfeature.unitsandreportablerangesscreen.UnitsAndReportableRangesFragment;

/**
 * The Test Settings activity
 * <p>
 * Created by Zeeshan A Zakaria on 5/8/2017.
 */

public class TestSettingsActivity extends BaseActivity {

    public int mState = TEST_SETTINGS;
    FragmentTransaction mFt;
    FragmentManager mFm;
    Fragment mCurrentFragment = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showHomeBack();
        mFm = getFragmentManager();
        testSettingsNavigation(mState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            // Sub navigation under the Units and Reportable ranges
            if (mState == BG_PLUS_SETTINGS || mState == E_PLUS_SETTINGS || mState == M_PLUS_SETTINGS) {
                testSettingsNavigation(UNITS_AND_REPORTABLE_RANGES);
                return false;
            }

            // Sub navigation under the Field Settings
            if (mState == TEST_SELECTION || mState == SelenaFamilyType.DELIVERYSYSTEM.value || mState == SelenaFamilyType.RESPIRATORYMODE.value || mState == SelenaFamilyType.DRAWSITE.value || mState == SelenaFamilyType.ALLENSTYPE.value) {
                testSettingsNavigation(FIELD_SETTINGS);
                return false;

            } else if (mState == FIELD_SETTINGS || mState == CUSTOM_FIELDS) {
                testSettingsNavigation(DATA_ENTRY_SETTINGS);
                return false;

            } else if (mState != TEST_SETTINGS) {
                testSettingsNavigation(TEST_SETTINGS);
                return false;

            }

        }
        return super.onKeyDown(keyCode, event);
    }

    public void testSettingsNavigation(int state) {
        mState = state;
        mFt = mFm.beginTransaction();
        switch (state) {
            case TEST_SETTINGS:
                TestSettingsFragment testSettingsFragment = new TestSettingsFragment();
                mCurrentFragment = testSettingsFragment;
                mFt.replace(android.R.id.content, testSettingsFragment);
                mFt.commit();
                changeTitle(getString(R.string.module_test_settings));
                break;
            case GENERAL_TEST_SETTINGS:
                GeneralTestSettingsFragment generalTestSettingsFragment = new GeneralTestSettingsFragment();
                mCurrentFragment = generalTestSettingsFragment;
                mFt.replace(android.R.id.content, generalTestSettingsFragment);
                mFt.commit();
                changeTitle(CU.titleCase(getString(R.string.general_test_settings)));
                break;
            case DATA_ENTRY_SETTINGS:
                DataEntrySettingsFragment dataEntrySettingsFragment = new DataEntrySettingsFragment();
                mCurrentFragment = dataEntrySettingsFragment;
                mFt.replace(android.R.id.content, dataEntrySettingsFragment);
                mFt.commit();
                changeTitle(CU.titleCase(getString(R.string.data_entry_settings)));
                break;
            case UNITS_AND_REPORTABLE_RANGES:
                UnitsAndReportableRangesFragment unitsAndReportableRangesFragment = new UnitsAndReportableRangesFragment();
                mCurrentFragment = unitsAndReportableRangesFragment;
                mFt.replace(android.R.id.content, unitsAndReportableRangesFragment);
                mFt.commit();
                changeTitle(CU.titleCase(getString(R.string.units_and_reportable_ranges)));
                break;
            case RANGES:
                RangesFragment rangesFragment = new RangesFragment();
                mCurrentFragment = rangesFragment;
                mFt.replace(android.R.id.content, rangesFragment);
                mFt.commit();
                changeTitle(CU.titleCase(getString(R.string.ranges)));
                break;
            // Sub navigation for the Data Entry Settings fragment
            case FIELD_SETTINGS:
                FieldSettingsFragment fieldSettingsFragment = new FieldSettingsFragment();
                mCurrentFragment = fieldSettingsFragment;
                mFt.replace(android.R.id.content, fieldSettingsFragment);
                mFt.commit();
                changeTitle(CU.titleCase(getString(R.string.field_settings)));
                break;
            // Sub navigation for the Units and Reportable Ranges
            case BG_PLUS_SETTINGS:
                BGPlusFragment bgPlusFragment = new BGPlusFragment();
                mCurrentFragment = bgPlusFragment;
                mFt.replace(android.R.id.content, bgPlusFragment);
                mFt.commit();
                changeTitle(getString(R.string.analyte_setup_bgp));
                break;
            case E_PLUS_SETTINGS:
                EPlusFragment ePlusFragment = new EPlusFragment();
                mCurrentFragment = ePlusFragment;
                mFt.replace(android.R.id.content, ePlusFragment);
                mFt.commit();
                changeTitle(getString(R.string.analyte_setup_e));
                break;
            case M_PLUS_SETTINGS:
                MPlusFragment mPlusFragment = new MPlusFragment();
                mCurrentFragment = mPlusFragment;
                mFt.replace(android.R.id.content, mPlusFragment);
                mFt.commit();
                changeTitle(getString(R.string.analyte_setup_m));
                break;

            // Sub navigation for the Field Settings fragment
            case TEST_SELECTION:
                TestSelectionFragment testSelectionFragment = new TestSelectionFragment();
                mCurrentFragment = testSelectionFragment;
                mFt.replace(android.R.id.content, testSelectionFragment);
                mFt.commit();
                changeTitle(CU.titleCase(getString(R.string.test_selection)));
                break;

            default: // Selena families
                SelenaEditValuesFragment selenafamily = new SelenaEditValuesFragment();
                mCurrentFragment = selenafamily;
                Bundle bundle = new Bundle();
                bundle.putInt("selenaFamilyTypeValue", mState);
                selenafamily.setArguments(bundle);
                mFt.replace(android.R.id.content, selenafamily);
                mFt.commit();
                if (mState == SelenaFamilyType.DELIVERYSYSTEM.value)
                    changeTitle(CU.titleCase(getString(R.string.delivery_system_edit_values)));
                else if (mState == SelenaFamilyType.RESPIRATORYMODE.value)
                    changeTitle(CU.titleCase(getString(R.string.respiratory_mode_edit_values)));
                else if (mState == SelenaFamilyType.ALLENSTYPE.value)
                    changeTitle(CU.titleCase(getString(R.string.allens_test_edit_values)));
                else if (mState == SelenaFamilyType.DRAWSITE.value)
                    changeTitle(CU.titleCase(getString(R.string.draw_site_edit_values)));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(mState == TEST_SETTINGS)
                {
                    super.onBackPressed();
                }
                else if (mState == FIELD_SETTINGS)
                {
                    testSettingsNavigation(DATA_ENTRY_SETTINGS);
                }
                else if (mState == TEST_SELECTION)
                {
                    testSettingsNavigation(FIELD_SETTINGS);
                }
                else if (mState == SelenaFamilyType.DELIVERYSYSTEM.value)
                {
                    testSettingsNavigation(FIELD_SETTINGS);
                }
                else if (mState == SelenaFamilyType.RESPIRATORYMODE.value)
                {
                    testSettingsNavigation(FIELD_SETTINGS);
                }
                else if (mState == SelenaFamilyType.ALLENSTYPE.value)
                {
                    testSettingsNavigation(FIELD_SETTINGS);
                }
                else if (mState == SelenaFamilyType.DRAWSITE.value)
                {
                    testSettingsNavigation(FIELD_SETTINGS);
                }
                else if (mState == BG_PLUS_SETTINGS)
                {
                    testSettingsNavigation(UNITS_AND_REPORTABLE_RANGES);
                }
                else if (mState == E_PLUS_SETTINGS)
                {
                    testSettingsNavigation(UNITS_AND_REPORTABLE_RANGES);
                }
                else if (mState == M_PLUS_SETTINGS)
                {
                    testSettingsNavigation(UNITS_AND_REPORTABLE_RANGES);
                }
                else {
                    testSettingsNavigation(TEST_SETTINGS);
                }
                break;
        }
        return true;
    }
}