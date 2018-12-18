package com.epocal.readersettings;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.epocal.common.CU;
import com.epocal.common.eventmessages.ReaderDevice;
import com.epocal.common.types.ReaderDiscoveryMode;
import com.epocal.common.types.TestType;
import com.epocal.common.types.am.TestMode;
import com.epocal.common_ui.BaseActivity;
import com.epocal.datamanager.ReaderModel;
import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestManager;
import com.epocal.network.bluetooth.BluetoothController;
import com.epocal.network.bluetooth.di.module.BluetoothControllerModule;
import com.epocal.readersettings.di.BluetoothComponent;
import com.epocal.readersettings.di.DaggerBluetoothComponent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

/**
 * Displays the list of readers and gives option to connect to them.
 * <p>
 * Created by Zeeshan A Zakaria on 7/11/2017.
 */

public class ReadersListActivity extends BaseActivity implements Consumer<TestEventInfo> {


    private ReaderDiscoveryMode mReaderDiscoveryMode;

    public ReaderDiscoveryMode getReaderDiscoveryMode() {
        return mReaderDiscoveryMode;
    }

    @Inject
    BluetoothController mBluetoothController;

    ArrayList<ReaderDevice> mDevices;
    BluetoothListAdapter mAdapter;
    ListView mListView;
    Runnable mRunnable;
    LinearLayout mLinearLayout;
    TextView mTextView, mTextViewTop;
    ProgressBar mProgressBarMain;
    Button mStartButton;
    String mTitle;
    private static final int TIMEOUT_SECONDS = 20000;
    TestType mTestType = TestType.Unknown;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReaderDiscoveryMode = getDiscoveryModeFromIntent();
        mTestType = getDiscoveryTypeFromIntent();

        setContentView(com.epocal.readersettings.R.layout.activity_readers_list);
        mTitle = CU.titleCase(getDiscoveryModeTitle(mReaderDiscoveryMode));
        changeTitle(mTitle);
        mLinearLayout = findViewById(com.epocal.readersettings.R.id.linear_layout_progress);
        mTextView = findViewById(com.epocal.readersettings.R.id.text_view_message);
        mTextViewTop = findViewById(com.epocal.readersettings.R.id.text_view_top);
        mProgressBarMain = findViewById(com.epocal.readersettings.R.id.progress_bar_main);
        mListView = findViewById(com.epocal.readersettings.R.id.listView_devices);
        mDevices = new ArrayList<>();
        if (mReaderDiscoveryMode == ReaderDiscoveryMode.QA_TEST) {
            //multitest disabled in this release. use normal bluetooth selector
            mAdapter = new BluetoothListAdapter(this, mDevices);

            /*
            mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            mAdapter = new QABluetoothListAdapter(this, mDevices);
            ((QABluetoothListAdapter)mAdapter).setAdapterListener(new QABluetoothListAdapter.AdapterListener() {
                @Override
                public void onItemChecked() {
                    mStartButton.setEnabled(((QABluetoothListAdapter)mAdapter).getSelectedReaders().size() > 0);
                }
            });
            mStartButton = findViewById(R.id.start_button);
            if (mStartButton != null) {
                mStartButton.setVisibility(View.VISIBLE);
                mStartButton.setEnabled(false);
                mStartButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startQATests();
                    }
                });
            }
            */
        } else {
            mAdapter = new BluetoothListAdapter(this, mDevices);
        }
        mListView.setAdapter(mAdapter);

        mRunnable = new Runnable() {
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        };

        EventBus.getDefault().register(this);

        BluetoothComponent bc = DaggerBluetoothComponent.builder()
                .bluetoothControllerModule(new BluetoothControllerModule(getApplicationContext()))
                .build();
        bc.inject(this);
        showHomeBack();
    }

    private ReaderDiscoveryMode getDiscoveryModeFromIntent() {
        Intent myIntent = getIntent();
        int discoverModeOrdinal = myIntent.getIntExtra("SetDiscoveryMode", ReaderDiscoveryMode.UNKNOWN.value);
        return ReaderDiscoveryMode.fromInt(discoverModeOrdinal);
    }

    private TestType getDiscoveryTypeFromIntent() {
        Intent myIntent = getIntent();
        int discoverTypeOrdinal = myIntent.getIntExtra("SetDiscoveryType", TestType.Unknown.value);
        return TestType.fromInt(discoverTypeOrdinal);
    }

    private String getDiscoveryModeTitle(ReaderDiscoveryMode readerDiscoveryMode) {
        String title;
        switch (readerDiscoveryMode) {
            case BLOOD_TEST:
                title = getString(com.epocal.readersettings.R.string.reader_discovery_title_patient_test);
                break;

            case QA_TEST:
                title = getString(com.epocal.readersettings.R.string.reader_discovery_title_qa_test);
                break;

            case STATUS_READER:
                title = getString(com.epocal.readersettings.R.string.reader_discovery_title_status);
                break;

            case DEDICATE_READER:
                title = getString(com.epocal.readersettings.R.string.reader_discovery_title_dedicate);
                break;

            case PAGE_READER:
                title = getString(com.epocal.readersettings.R.string.reader_discovery_title_page);
                break;

            case UPGRADE_READER:
                title = getString(com.epocal.readersettings.R.string.reader_discovery_title_upgrade);
                break;

            case READER_THERMAL_QA:
                title = getString(com.epocal.readersettings.R.string.reader_discovery_title_thermal_qa);
                break;

            case CHANGE_READER_NAME:
                title = getString(com.epocal.readersettings.R.string.reader_discovery_title_change_name);
                break;

            case UNKNOWN:
            default:
                title = getString(com.epocal.readersettings.R.string.reader_discovery_title_unknown_mode);
                break;
        }

        return title;
    }

    public void startTest(int position) {
        mListView.setEnabled(false);
        mListView.setAlpha(.5f);
        mProgressBarMain.setVisibility(View.INVISIBLE);
        mTextViewTop.setText(getString(com.epocal.readersettings.R.string.reader_discovery_connecting));
        ReaderDevice readerDevice = (ReaderDevice) mAdapter.getItem(position);
        try {
            switch (mReaderDiscoveryMode) {

                case BLOOD_TEST:
                    TestManager.getInstance().startSingleTest(ReadersListActivity.this, TestMode.BloodTest, TestType.Blood, readerDevice);
                    break;
                case QA_TEST:
                    TestManager.getInstance().startSingleTest(ReadersListActivity.this, TestMode.QA, mTestType, readerDevice);
                default:
                    Log.d("HOST4", "Cannot start test: No testmode info received");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace(); //TODO: deal with the exception
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deviceDiscoveredEvent(ReaderDevice readerDevice) {
        // Only add a reader to the list if it is not already listed
        // NOTE: Active readers are added to the list from the DB prior to discovery
        if (addReaderDevice(readerDevice)) {
            mDevices.add(readerDevice);
        }
        runOnUiThread(mRunnable);
    }

    private boolean addReaderDevice(ReaderDevice readerDevice) {
        String currentReaderAddress = readerDevice.getDeviceAddress();

        boolean retval = true;
        for (ReaderDevice rdv : mDevices) {
            if (rdv.getDeviceAddress().equals(currentReaderAddress)) {
                retval = false;
                break;
            }
        }
        return retval;
    }

    @Override
    public void onPause() {
        super.onPause();
        mBluetoothController.terminateDeviceDiscovery();
        finish();
        Log.d("HOST4", "~ Bluetooth discovery stopped ~");
    }

    @Override
    public void onResume() {
        super.onResume();
        discoverReaders();
    }

    @Override
    public void accept(TestEventInfo testEventInfo) {
        if (testEventInfo.getTestEventInfoType() != TestEventInfoType.STATUS_INFO) {
            return;
        }
        final String status = testEventInfo.getTestStatusType().toString();
        Log.d("HOST4", "~~ Enum is: " + status);

        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTextView.setText(status);

//                    if (status.equals(TestStatusType.CONNECTED.toString())) {
//                        Intent intent = new Intent(ReadersListActivity.this, PatientTestActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 1, Menu.NONE, "Refresh").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case 0: // start multiple tests
                startQATests();
                return true;
            case 1: // Restarting the bluetooth discovery
                mTextViewTop.setText(getString(com.epocal.readersettings.R.string.reader_discovery_looking_for_readers));
                discoverReaders();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private void startQATests() {
        try {
            if (mAdapter instanceof QABluetoothListAdapter) {
                QABluetoothListAdapter qaAdapter = (QABluetoothListAdapter) mAdapter;
                if (qaAdapter.getSelectedReaders().size() == 0) {
                    new MaterialDialog.Builder(this)
                            .content("Please select one or more readers")
                            .positiveText("close")
                            .positiveColor(getResources().getColor(com.epocal.readersettings.R.color.primaryBlueNew))
                            .show();
                } else if (qaAdapter.getSelectedReaders().size() == 1 && TestManager.getInstance().getActiveTestCount(qaAdapter.getSelectedReaders().get(0).getDeviceAddress()) == 0) {
                    TestManager.getInstance().startSingleTest(this, TestMode.QA, mTestType, qaAdapter.getSelectedReaders().get(0));
                } else {
                    TestManager.getInstance().startMultipleTests(this, qaAdapter.getSelectedReaders(), TestMode.QA, mTestType, true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Discover the Readers until the timeout value is reached and then stop the discovery.
     */
    private void discoverReaders() {
        new AsyncTask<Void, Void, Void>() {

            protected void onPreExecute() {
                mDevices.clear();
                addActiveReadersToList();
                mListView.setEnabled(true);
                mListView.setAlpha(1f);
                mProgressBarMain.setVisibility(View.VISIBLE);
                boolean enabled = mBluetoothController.isEnabled();
                if (!enabled) mBluetoothController.setupBluetooth(true);
                mBluetoothController.terminateDeviceDiscovery();
                boolean started = mBluetoothController.performDeviceDiscovery();
                Log.d("HOST4", "~ Bluetooth discovery started: " + started + " ~");
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(TIMEOUT_SECONDS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(Void unused) {
                mProgressBarMain.setVisibility(View.GONE);
                int numberOfReaders = mDevices.size();
                String text;
                if (numberOfReaders == 0) {
                    text = getString(com.epocal.readersettings.R.string.reader_discovery_no_readers_detected);
                } else {
                    text = getString(com.epocal.readersettings.R.string.reader_discovery_readers_detected, numberOfReaders);
                }

                mTextViewTop.setText(text);
                mBluetoothController.terminateDeviceDiscovery();
            }
        }.execute();
    }

    private void addActiveReadersToList() {
        // show every active reader in realm
        ArrayList<ReaderDevice> readerDeviceList = new ReaderModel().getActiveReadersOrderedByDedicatedFirst();
        mDevices.addAll(readerDeviceList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
