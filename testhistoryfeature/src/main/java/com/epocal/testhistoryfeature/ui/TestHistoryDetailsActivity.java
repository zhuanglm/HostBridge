package com.epocal.testhistoryfeature.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import com.epocal.common.realmentities.TestRecord;
import com.epocal.common.types.Permissions;
import com.epocal.common.types.am.TestMode;
import com.epocal.datamanager.PrinterModel;
import com.epocal.testhistoryfeature.BaseActivity;
import com.epocal.testhistoryfeature.R;
import com.epocal.testhistoryfeature.THDetailsContract;
import com.epocal.testhistoryfeature.data.patienttestdata.THDataHelper;
import com.epocal.testhistoryfeature.di.DaggerTestHistoryDetailsComponent;
import com.epocal.testhistoryfeature.di.TestHistoryDetailsComponent;
import com.epocal.testhistoryfeature.di.TestHistoryDetailsModule;
import com.epocal.testhistoryfeature.ui.fragment.ITHDetailsFragmentListener;
import com.epocal.testhistoryfeature.ui.fragment.THDetailsTestInputDataFragment;
import com.epocal.testhistoryfeature.ui.fragment.THDetailsTestQAStatusFragment;
import com.epocal.testhistoryfeature.ui.fragment.THDetailsTestResultsFragment;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 *  This class represents the Details Screen of Test History (when the user selects a row of
 *  test history search result list from the main screen) and displays the details information
 *  of the selected test.
 *
 * @since 2018-10-06
 *
 *  <h4>Overview of Details Screen</h4>
 *  <p>
 *  This screen displays the details of past test in 3 different tabs.
 *  <ul>
 *      <li><b>Tab 1</b> - Test Results - blood/qa test results</li>
 *      <li><b>Tab 2</b> - Entered Data - blood/qa test info inputted during the test.</li>
 *      <li><b>Tab 3</b> - Status - the status of the reader at the time of the test.</li>
 *  </ul>
 *  </p>
 *
 *  <h4>Class Details</h4>
 *  <p>The user can perform actions on test using ActionBar menu and tap the back arrow
 *  on the ActionBar to navigate back to the Main Screen.</p>
 *
 *  <h4>ActionBar Interactions</h4>
 *  <ul>
 *      <li><b>Mark as Sent</b> -- (admin user only) change the sync-state (change to "Sent" state)</li>
 *      <li><b>Mark as Unsent</b> -- (admin user only) change the sync-state (change to "Unsent" state)</li>
 *      <li><b>Delete</b> -- (admin user only) delete the test from the db. The confirmation dialog will pop-up
 *      to confirm the delete action.</li>
 *      <li><b>Print</b> -- not implemented</li>
 *  </ul>
 *
 *  <h4>Tab 2 - Extended Data</h4>
 *  <p>Exnteded Data - contains the user inputted data during the test. The content differs
 *  based on TestRecord testMode (Blood or QA).</p>
 *  <pre>
 *  (1) (testMode)BloodTest -- The data user inputted into an input form when running a Blood Test.
 *  The workflow used during Blood Test determines the set of input fields.
 *  See {@link com.epocal.epoctestprocedure.stepper}
 *  (2) (testMode)QA Test -- contains the user inputted data during the test and
 *  related fluid lot information.
 *  Fields: Lot number, Comments, Test type, Fluid Type, (EDM) reference, (EDM) expiry.
 *  </pre>
 *
 *  <h4>Tab 3 - Status</h4>
 *  <p>Status tab content is diplayed in the expanded list view. Status contains below
 *  categories of information. The categories listed differs based on TestRecord testMode.
 *  <pre>
 *  (1) (testMode)BloodTest -- lists all categories below.
 *  (2) (testMode)QA Test -- lists Related Electronic QC, Device Status and Reference Range categories.
 *  </pre>
 *  <ol>
 *     <li><b>Related QC Test</b> -- QC info read from the reader at the time
 *     of blood test.</li>
 *     <li><b>Related CV Test</b> -- CV info read from the reader at the time
 *     of blood test.</li>
 *     <li><b>Related Electronic QC (non-expandable)</b> -- EQC date and time at the time
 *     of blood test.</li>
 *     <li><b>Related Thermal QA (non-expandable)</b> -- ThermalQC info read from the reader at the time
 *     of blood test.</li>
 *     <li><b>Device Status</b> -- reader's status, software versions (host, reader) at the
 *     time of blood test.
 *     <ul>
 *         <li>Ambient Temperature</li>
 *         <li>Ambient Pressure</li>
 *         <li>Host Version</li>
 *         <li>Reader Version</li>
 *         <li>Test Config Version</li>
 *         <li>EVAD Version</li>
 *     </ul>
 *     </li>
 *     <li><b>Reference Ranges</b></li>
 *     <li><b>Critical Ranges</b></li>
 *     <li><b>Reportable Ranges</b></li>
 *  </ol>
 *  </p>
 *
 *  <h4>Note: Not implemented content for 2018-Nov release - reading below data not implemented
 *  in the reader communication module.</h4>
 *  <ul>
 *      <li>Tab2 - (testMode)QA Test - Reference and Expiry.</li>
 *      <li>Tab3 - Related QA Test and Thermal QC info are not saved in db and
 *      these categories displays "Record not found" as subtitle. Reader's EQC info is not saved
 *      in db but TestRecord.lastEqcDatetime is available and this date time is displayed.
 *      Only partial Device Status is available in db.</li>
 *  </ul>
 *
 *  <h4>User Permissions</h4>
 *  <p>The option menu "Mark as sent", "Mark as unsent" and "delete" are only avaialble
 *  for the admin user. (The non-admin user won't see these options).</p>
 *
 *  <h4>Note: Related packagages</h4>
 *  <ul>
 *      <li><b>com.epocal.testhistoryfeature.data.testresults</b> -- package contains adapter
 *      for Tab 1 - Test Results. Test Results list shares the same layout as in
 *      epoctestprocedure module. This common layout and the base adapter test result list is
 *      located in com.epocal.common_ui.testresults package.</li>
 *      <li><b>com.epocal.testhistoryfeature.data.patientdata</b> -- package contains adapter,
 *      model for list view for Tab 2 - Entered Data.</li>
 *      <li><b>com.epocal.testhistoryfeature.data.statusdata</b>-- package contains adapter,
 *      model for list view for Tab 3 - Status.</li>
 *  </ul>
 *
 * @see com.epocal.testhistoryfeature.data.patientdata
 * @see com.epocal.testhistoryfeature.data.statusdata
 * @see com.epocal.testhistoryfeature.data.testresults
 * @see TestHistoryMainActivity
 *
 */
public class TestHistoryDetailsActivity extends BaseActivity implements ITHDetailsFragmentListener {
    public static final String KEY_TEST_ID = "KEY_TEST_ID";

    private THDetailsTestResultsFragment mTestResultsFragment;
    private long       mTestID;
    private TestRecord mTestRecord;
    private Realm      mRealm;
    private boolean    mIsActionDeleteInProgress;
    private boolean    mIsAdminUser;
    private boolean    mIsPrinterAvailable;

    // Dagger Injection
    @Inject
    public THDetailsContract.Model     mModel;

    // Callbacks from Actions
    private Realm.Transaction.OnSuccess mOnSuccessActionDelete = new Realm.Transaction.OnSuccess() {
        @Override
        public void onSuccess() {
            // Update the ActionBar and Adapter
            mIsActionDeleteInProgress = false;
            finish();
        }
    };
    private Realm.Transaction.OnError mOnErrorActionDelete = new Realm.Transaction.OnError() {
        @Override
        public void onError(Throwable error) {
            UIHelper.showActionErrorDialog(TestHistoryDetailsActivity.this, error.getMessage());
            mIsActionDeleteInProgress = false;
        }
    };
    private Realm.Transaction.OnSuccess mOnSuccessActionGeneric = new Realm.Transaction.OnSuccess() {
        @Override
        public void onSuccess() {

        }
    };
    private Realm.Transaction.OnError mOnErrorActionGeneric = new Realm.Transaction.OnError() {
        @Override
        public void onError(Throwable error) {
            UIHelper.showActionErrorDialog(TestHistoryDetailsActivity.this, error.getMessage());
        }
    };

    private RealmChangeListener<TestRecord> mDataSourceChangeListener = new RealmChangeListener<TestRecord>() {
        @Override
        public void onChange(TestRecord element) {
            if (!mIsActionDeleteInProgress) {
                refresh();
            }
        }
    };
    DialogInterface.OnClickListener mActionDeleteConfirmationCallback = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mIsActionDeleteInProgress = true;
            mModel.delete(mTestRecord.getId(), mOnSuccessActionDelete, mOnErrorActionDelete);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unpackArguments();

        setContentView(R.layout.activity_test_history_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter sectionsPagerAdapter = setupSectionsPagerAdapter();
        // Set up the ViewPager with the sections adapter.
        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        //tabLayout.addOnTabSelectedListener(mTabSelectListener);

        mRealm = Realm.getDefaultInstance();
        checkPrinters(mRealm);

        TestHistoryDetailsComponent component = DaggerTestHistoryDetailsComponent.builder()
                .testHistoryDetailsModule(new TestHistoryDetailsModule(mRealm))
                .build();
        component.inject(this);

        mTestRecord = mModel.fetchTestRecord(mTestID);
        if (mTestRecord != null) {
            updateTitle(mTestRecord);
            mTestRecord.addChangeListener(mDataSourceChangeListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
        mModel = null;
        mTestRecord = null;
    }

    private void unpackArguments() {
        Intent intent = getIntent();
        if (intent.hasExtra(KEY_TEST_ID)) {
            mTestID = intent.getLongExtra(KEY_TEST_ID, 0);
        }
        mIsAdminUser = intent.hasExtra(Permissions.HOSTADMINISTRATOR.name());
    }

    private SectionsPagerAdapter setupSectionsPagerAdapter() {
        final String[] tabTitleItems = getResources().getStringArray(R.array.TestDetailsCategories);

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mTestResultsFragment = new THDetailsTestResultsFragment();
        adapter.addFragment(mTestResultsFragment, tabTitleItems[0]);
        adapter.addFragment(new THDetailsTestInputDataFragment(), tabTitleItems[1]);
        adapter.addFragment(new THDetailsTestQAStatusFragment(), tabTitleItems[2]);
        return adapter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test_history_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // Handle Back btn pressed on ActionBar
            finish();
            return true;
        } else if (id == R.id.action_print) {
            // TODO: Not implemented.
            return true;
        } else if (id == R.id.action_sent) {
            mModel.markSent(mTestRecord.getId(), mOnSuccessActionGeneric, mOnErrorActionGeneric);
            return true;
        } else if (id == R.id.action_unsent) {
            mModel.markUnsent(mTestRecord.getId(), mOnSuccessActionGeneric, mOnErrorActionGeneric);
            return true;
        } else if (id == R.id.action_delete) {
            UIHelper.showActionDeleteConfirmationDialog(TestHistoryDetailsActivity.this, 1, mActionDeleteConfirmationCallback);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called when the option menu has changed at run time.
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mIsAdminUser) {
            menu.findItem(R.id.action_delete).setVisible(true);
            if (mTestRecord != null) {
                if (THDataHelper.isTestRecordSent(mTestRecord)) {
                    menu.findItem(R.id.action_sent).setVisible(false);
                    menu.findItem(R.id.action_unsent).setVisible(true);
                } else {
                    menu.findItem(R.id.action_sent).setVisible(true);
                    menu.findItem(R.id.action_unsent).setVisible(false);
                }
            }
        } else {
            // non-admin user
            menu.findItem(R.id.action_delete).setVisible(false);
            menu.findItem(R.id.action_sent).setVisible(false);
            menu.findItem(R.id.action_unsent).setVisible(false);
        }

        if (mIsPrinterAvailable) {
            menu.findItem(R.id.action_print).setVisible(true);
        } else {
            menu.findItem(R.id.action_print).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public void refresh() {
        // TODO: Bug: After Selecting Sent/Unsent from options menu,
        // TODO: the screen needs scroll gesture twice to get list scrolling.
        // TODO: May be a device issue.
        invalidateOptionsMenu();
        if (mTestResultsFragment != null) {
            mTestResultsFragment.updateTestResults(mTestRecord);
        }
    }

    private void updateTitle(TestRecord testRecord) {
        String subjectIdString = (testRecord.getSubjectId() == null) ? "" : testRecord.getSubjectId();
        String title;
        if (mTestRecord.getTestMode() == TestMode.QA) {
            // QA Test (TestMode.QA) - display fluid lot id
            if (subjectIdString.isEmpty()) {
                title = getResources().getString(R.string.no_lot_number);
            } else {
                title = String.format(getResources().getString(R.string.lot_with_id), subjectIdString);
            }
        } else {
            // Patient Test - display SubjectId
            if (subjectIdString.isEmpty()) {
                title = getResources().getString(R.string.no_patient_id);
            } else {
                title = String.format(getResources().getString(R.string.patient_with_name), subjectIdString);
            }
        }
        changeTitle(title);
    }

    /**
     * Checks if the printer exists in db.
     * @param realm -- realm instance managed by this activity.
     */
    private void checkPrinters(Realm realm) {
         int numOfPrinters = PrinterModel.getAllPrinters(realm).size();
         mIsPrinterAvailable = (numOfPrinters > 0);
    }

    //////////////////////////////
    // ITHDetailsFragmentListener
    /////////////////////////////
    @Override
    public TestRecord getTestRecord() {
        return mTestRecord;
    }

    @Override
    public THDetailsContract.Model getDetailsModel() {
        return mModel;
    }

}

