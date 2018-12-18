package com.epocal.readersettings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.epocal.common.eventmessages.ReaderDevice;
import com.epocal.common.realmentities.Reader;
import com.epocal.common.types.ReaderDiscoveryMode;
import com.epocal.datamanager.ReaderModel;

import java.util.ArrayList;

/**
 * The reader devices adapter
 *
 * Created by Zeeshan A Zakaria on 7/11/2017.
 */

class BluetoothListAdapter extends BaseAdapter {

    protected Context mContext;
    protected ArrayList<ReaderDevice> mReaderDevices;

    BluetoothListAdapter(Context context, ArrayList<ReaderDevice> readerDevices) {
        super();
        mContext = context;
        mReaderDevices = readerDevices;
    }

    @Override
    public int getCount() {
        return mReaderDevices.size();
    }

    @Override
    public Object getItem(int position) {
        return mReaderDevices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(R.layout.readers_list_item, parent, false);
        }
        final ReaderDevice readerDevice = mReaderDevices.get(position);

        TextView tvReaderName = (TextView) view.findViewById(R.id.text_view_reader_name);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.llProgressBar);
        final ImageView readerStatusImage = (ImageView) view.findViewById(R.id.image_view_reader_status);
        String deviceAlias = readerDevice.getDeviceAlias();
        String deviceId = readerDevice.getDeviceId();
        String readerName = deviceAlias + " #" + deviceId;
        tvReaderName.setText(readerName);

        setReaderStatusImage(readerDevice, readerStatusImage);

        final ReaderDiscoveryMode discoveryMode = ((ReadersListActivity)mContext).getReaderDiscoveryMode();

        // Set the entire row as clickable and override the onClick() method to perform the
        // appropriate action based on the current Discovery Mode
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (discoveryMode) {
                    case BLOOD_TEST:
                    case QA_TEST:
                        progressBar.setVisibility(View.VISIBLE);
                        ((ReadersListActivity)mContext).startTest(position);
                        break;

                    case DEDICATE_READER:
                        toggleDedicatedReader(readerDevice, readerStatusImage);
                        break;

                    default:
                        // DO NOTHING - The discovery mode is not set
                        break;
                }
            }
        });

        return view;
    }

    private void toggleDedicatedReader(ReaderDevice readerDevice, ImageView readerStatusImage) {
        // 1- get the reader's current dedicated state and toggle the setting
        boolean setAsDedicated = !(readerDevice.getDedicated());
        readerDevice.setDedicated(setAsDedicated);

        // 2- Save the updated reader's dedicated state to the DB
        Reader reader = readerDevice.toReader();
        if (reader != null) {
            ReaderModel rm = new ReaderModel();

            // 2a- try to just update the reader's dedicated state; but if it fails,
            // that means the reader was not in the DB and must be saved to the DB/
            if (!rm.dedicateReader(reader.getSerialNumber(), setAsDedicated)) {
                // The reader was not in the DB, we need to save this 'new' reader in the DB,
                // which will save the dedicated state as well.
                rm.saveReader(reader);
            }

            // 3- Set the reader status image according to the Reader's new isDedicated state
            setReaderStatusImage(reader, readerStatusImage);
        }
    }

    protected void setReaderStatusImage(ReaderDevice readerDevice, ImageView readerStatusImage) {
        setReaderStatusImage(readerDevice.toReader(), readerStatusImage);
    }

    protected void setReaderStatusImage(Reader reader, ImageView readerStatusImage) {
        int readerStatusImageId = (!reader.getDedicated()) ? R.mipmap.undedicated_reader : R.mipmap.dedicated_reader;

        readerStatusImage.setImageResource(readerStatusImageId);

        readerStatusImage.refreshDrawableState();
    }
}
