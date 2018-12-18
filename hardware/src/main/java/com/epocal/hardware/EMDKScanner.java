package com.epocal.hardware;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.barcode.BarcodeManager;
import com.symbol.emdk.barcode.ScanDataCollection;
import com.symbol.emdk.barcode.Scanner;
import com.symbol.emdk.barcode.ScannerException;
import com.symbol.emdk.barcode.ScannerResults;
import com.symbol.emdk.barcode.StatusData;

import java.util.ArrayList;

/**
 * This class handles the EMDK and Scanner Listeners
 * <p>
 * Created by Zeeshan A Zakaria on 6/27/2017.
 */

public class EMDKScanner implements EMDKManager.EMDKListener, Scanner.StatusListener, Scanner.DataListener {

    private static String TAG = "Host4";
    private Scanner mScanner = null;
    private AsyncStatusUpdate mAsyncStatusUpdate;
    private EMDKManager mEmdkManager;
    private BarcodeManager mBarcodeManager;
    private Context mContext;
    private ObserveBarcodeString mObserveBarcodeString;

    public EMDKScanner(Context context) {
        mContext = context;
        mObserveBarcodeString = ObserveBarcodeString.getInstance();
}

    @Override
    public void onOpened(EMDKManager emdkManager) {
        Log.w(TAG, "Opening the Scanner");
        mEmdkManager = emdkManager;
//        try {
//            initializeScanner();
//        } catch (ScannerException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onClosed() {
        Log.w(TAG, "Closing the mScanner");
        releaseEMDKManager();
    }

    @Override
    public void onData(ScanDataCollection scanDataCollection) {
        new AsyncDataUpdate().execute(scanDataCollection);
    }

    @Override
    public void onStatus(StatusData statusData) {
        String status = mAsyncStatusUpdate.getStatus().toString();
        Log.d(TAG, "Updating status: " + mAsyncStatusUpdate.getStatus());
        if (status.equals("PENDING") || status.equals("FINISHED")) { // Hacky way to make the scanner keep working.
            new AsyncStatusUpdate().execute(statusData);
        }
    }

    public void cancelRead() {
        try {
            mScanner.cancelRead();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Start the EMDK Manager which in return start the scanner
     */
    private void startEMDKManager() {
        // Clear the existing states
        mEmdkManager = null;
        mBarcodeManager = null;
        try {
//        The EMDKManager object will be created and returned in the callback.
            EMDKResults emdkResults = EMDKManager.getEMDKManager(mContext, this); // This causes the instrumentation test to fail
//        try {
//            initializeScanner();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
            System.out.println("EMDKResults.STATUS_CODE is: " + emdkResults.statusCode);
        } catch (RuntimeException e) {
            Log.d("EMDK","EMDK Scanner library not available");
        }
    }

    // Method to initialize and enable Scanner and its listeners
    private void initializeScanner() throws ScannerException {
        if (mScanner == null && mEmdkManager != null) {
            mBarcodeManager = (BarcodeManager) mEmdkManager.getInstance(EMDKManager.FEATURE_TYPE.BARCODE); // Get the Barcode Manager object
            mScanner = mBarcodeManager.getDevice(BarcodeManager.DeviceIdentifier.DEFAULT); // Get default mScanner defined on the device
            mScanner.addDataListener(this); // Add data and status listeners
            mScanner.addStatusListener(this);
            mScanner.triggerType = Scanner.TriggerType.HARD; // Hard trigger. When this mode is set, the user has to manually press the trigger on the device after issuing the read call.
            mScanner.enable(); // Enable the scanner
            mScanner.read(); // Starts an asynchronous Scan. The method will not turn ON the mScanner. It will, however, put the mScanner in a state in which the mScanner can be turned ON either by pressing a hardware trigger or can be turned ON automatically.
        }
    }

    private void releaseEMDKManager() {
        if (mEmdkManager != null) {
            mEmdkManager.release();
            mEmdkManager = null;
        }
    }

    /**
     * Stop the scanner
     */
    private void stopScanner() {
        try {
            if (mScanner != null && mEmdkManager != null) {
                // releases the mScanner hardware resources for other application to use. You must call this as soon as you're done with the scanning.
                mScanner.removeDataListener(this);
                mScanner.removeStatusListener(this);
                mScanner.disable();
            }
            mScanner = null;
        } catch (ScannerException e) {
            mScanner = null;
            e.printStackTrace();
        }
    }

    public void onResume() {
        mAsyncStatusUpdate = new AsyncStatusUpdate();
        releaseEMDKManager();
        stopScanner();
        startEMDKManager();
    }

    public void onStop() {
        stopScanner();
        releaseEMDKManager();
    }

    public void onDestroy() {
        releaseEMDKManager();
    }

    /**
     *
     */
    private class AsyncStatusUpdate extends AsyncTask<StatusData, Void, String> {

        @Override
        protected String doInBackground(StatusData... params) {
            String statusStr = "";
            // Get the current state of mScanner in background
            StatusData statusData = params[0];
            StatusData.ScannerStates state = statusData.getState();

            // Different states of Scanner
            switch (state) {
                case IDLE:
                    statusStr = "The scanner is enabled and its idle";
                    try {
                        // Reset the mScanner for next scanning
                        if (mScanner.isReadPending()) {
                            Log.d(TAG, "Reading...");
                            cancelRead();
                        }
                        // Puts the device in a state where it can scan barcodes
                        mScanner.read();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case SCANNING:
                    statusStr = "Scanning...";
                    break;
                case WAITING:
                    statusStr = "Waiting for trigger press...";
                    break;
                case DISABLED:
                    statusStr = "Scanner is not enabled";
                    break;
                default:
                    break;
            }

            return statusStr;
        }

        @Override
        protected void onPostExecute(String result) {
            // Update the status text view on UI thread with current mScanner
            // state
            Log.i(TAG, "STATUS IS: " + result);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    /**
     * AsyncTask that configures the scanned data on background thread and updated the result on UI
     * thread with scanned data and type of label
     */
    private class AsyncDataUpdate extends
            AsyncTask<ScanDataCollection, Void, String> {

        @Override
        protected String doInBackground(ScanDataCollection... params) {

            // Status string that contains both barcode data and type of barcode
            // that is being scanned
            String statusStr = "";

            try {
                ScanDataCollection scanDataCollection = params[0];

                // The ScanDataCollection object gives scanning result and the
                // collection of ScanData. So check the data and its status
                if (scanDataCollection != null && scanDataCollection.getResult() == ScannerResults.SUCCESS) {

                    ArrayList<ScanDataCollection.ScanData> scanData = scanDataCollection.getScanData();

                    // Iterate through scanned data and prepare the statusStr
                    for (ScanDataCollection.ScanData data : scanData) {
                        // Get the scanned data
                        statusStr = data.getData();
                    }
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // Return result to populate on UI thread
            return statusStr;
        }

        @Override
        protected void onPostExecute(String result) {
            mObserveBarcodeString.setBarcodeString(result); // This is read in by the presenter via Rx
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}