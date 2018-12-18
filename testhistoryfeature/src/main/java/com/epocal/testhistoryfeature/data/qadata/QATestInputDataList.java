package com.epocal.testhistoryfeature.data.qadata;

import android.content.Context;

import com.epocal.common.realmentities.TestRecord;
import com.epocal.common_ui.list.IListItem;
import com.epocal.common_ui.list.ListItem;
import com.epocal.testhistoryfeature.R;

import java.util.ArrayList;
import java.util.List;

public class QATestInputDataList {
    private final Context mContext;
    private final QATestInputData mQAData;
    private final List<IListItem<String>> mList;

    QATestInputDataList(Context context, TestRecord testRecord) {
        mContext = context;
        mQAData = new QATestInputData(context, testRecord);
        mList = new ArrayList<>();
        buildLotSection();
    }

    private void buildLotSection() {
        // Fluid Lot number
        mList.add(new ListItem(mContext.getString(R.string.lot_with_hash), mQAData.fluidLot));
        // TestType
        mList.add(new ListItem(mContext.getString(R.string.test_type), mQAData.testType));
        // FluidType
        mList.add(new ListItem(mContext.getString(R.string.fluid_type), mQAData.fluidType));
        // Ref number
        mList.add(new ListItem(mContext.getString(R.string.fluid_reference), mQAData.referenceId));
        // Expiry
        mList.add(new ListItem(mContext.getString(R.string.fluid_expiry), mQAData.expiryDate));
        // Comments
        mList.add(new ListItem(mContext.getString(R.string.comments), mQAData.comments));
    }

    public List<IListItem<String>> getList() {
        return mList;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("QATestInputDataList: START \n");
        if ((mList != null) && (mList.size() > 0)) {
            sb.append("size = ").append(mList.size()).append("\n");
            for (IListItem item : mList) {
                if (item.isSectionHeader()) {
                    sb.append("Section Header = ").append(item.getTitle());
                } else {
                    sb.append("  ").append(item.getTitle()).append(" = ").append(item.getValue());
                }
                sb.append("\n");
            }
        }
        sb.append("QATestInputDataList: END \n");
        return sb.toString();
    }
}
