package com.epocal.readersettings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.epocal.common.eventmessages.ReaderDevice;
import com.epocal.readersettings.BluetoothListAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class QABluetoothListAdapter extends BluetoothListAdapter {
    static final int maximumQATests = 4;

    public interface AdapterListener {
        public void onItemChecked();
    }

    private Set<ReaderDevice> selectedReaders = new HashSet<>();
    private AdapterListener adapterListener;

    public void setAdapterListener(AdapterListener adapterListener) {
        this.adapterListener = adapterListener;
    }

    public QABluetoothListAdapter(Context context, ArrayList<ReaderDevice> readerDevices) {
        super(context, readerDevices);
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(R.layout.qa_readers_list_item, parent, false);
        }
        final ReaderDevice readerDevice = mReaderDevices.get(position);
        final CheckedTextView tvReaderName = (CheckedTextView) view.findViewById(R.id.text_view_reader_name);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.llProgressBar);
        final ImageView readerStatusImage = (ImageView) view.findViewById(R.id.image_view_reader_status);
        String deviceAlias = readerDevice.getDeviceAlias();
        String deviceId = readerDevice.getDeviceId();
        final String readerName = deviceAlias + " #" + deviceId;
        tvReaderName.setText(readerName);

        setReaderStatusImage(readerDevice, readerStatusImage);

        if (tvReaderName != null) {
            boolean checked = false;
            for (Iterator<ReaderDevice> it = selectedReaders.iterator(); it.hasNext(); ) {
                if (readerDevice.equals(it.next())) {
                    checked = true;
                }
            }
            tvReaderName.setChecked(checked);
            tvReaderName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedReaders.size() >= maximumQATests && !tvReaderName.isChecked())
                        return;

                    tvReaderName.setChecked(!tvReaderName.isChecked());
                    if(tvReaderName.isChecked())
                        selectedReaders.add(readerDevice);
                    else
                        selectedReaders.remove(readerDevice);

                    if (adapterListener != null)
                        adapterListener.onItemChecked();
                }
            });
        }

        return view;
    }

    public List<ReaderDevice> getSelectedReaders() {
        return new ArrayList<>(selectedReaders);
    }
}
