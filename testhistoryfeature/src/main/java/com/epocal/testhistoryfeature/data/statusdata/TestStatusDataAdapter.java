package com.epocal.testhistoryfeature.data.statusdata;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.epocal.common.realmentities.EqcInfo;
import com.epocal.common.realmentities.TestRecord;
import com.epocal.common.types.am.TestMode;
import com.epocal.common_ui.list.IListItem;
import com.epocal.common_ui.list.ListItem;
import com.epocal.common_ui.list.StyledTextListItem;
import com.epocal.common_ui.qaresults.SimpleTest;
import com.epocal.common_ui.qaresults.SimpleTestGridViewAdapter;
import com.epocal.testhistoryfeature.R;

import java.util.List;

/**
 * QAStatusDataAdapter - this class creates data and views to display in Details Screen Tab 3
 * - Status tab content.
 *
 * <h4>Class details:</h4>
 * <p>
 * The status is grouped in 7 categories and is displayed in the custom Expanded ListView.
 * The child view of the expanded List View holds non-uniform view and a unique view must be
 * created for each category. (each category has its own view.)
 * </p>
 *
 * <p>
 * QATestCategoryDataUtil object helps parse the data to each category
 * to facilitate adapter's view display.
 * </p>
 *
 * @see com.epocal.testhistoryfeature.ui.TestHistoryDetailsActivity
 *
 */
public class TestStatusDataAdapter extends BaseExpandableListAdapter {
    final private Context mContext;
    private final RelatedTestStatusCategoryDataUtil mRelatedTestStatusCategoryDataUtil;
    private final List<RelatedTestStatusCategoryData> mGroupDataList;

    public TestStatusDataAdapter(Context context, TestRecord testRecord, EqcInfo eqcInfo) {
        this.mContext = context;
        if (testRecord.getTestMode() == TestMode.BloodTest) {
            // Patient Test
            mRelatedTestStatusCategoryDataUtil = new RelatedTestStatusCategoryDataUtil(context, testRecord, eqcInfo);
        } else {
            // QA Test
            mRelatedTestStatusCategoryDataUtil = new QATestRelatedTestStatusCategoryDataUtil(context, testRecord, eqcInfo);
        }
        mGroupDataList = mRelatedTestStatusCategoryDataUtil.getCategoryList();
    }

    @Override
    public int getGroupCount() {
        return mGroupDataList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        RelatedTestStatusCategoryData groupData = mGroupDataList.get(groupPosition);
        return mRelatedTestStatusCategoryDataUtil.getChildrenCount(groupData.getCategory());
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroupDataList.get(groupPosition);
    }

    // Note: Do not use this method.
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition * 10 + childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        RelatedTestStatusCategoryData testCategoryInfo = (RelatedTestStatusCategoryData) getGroup(groupPosition);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.status_section_header, parent, false);

        TextView textView = view.findViewById(R.id.section_title);
        textView.setText(testCategoryInfo.getTitle());

        TextView subTitleView = view.findViewById(R.id.section_sub_title);
        subTitleView.setText(testCategoryInfo.getSubTitle());

        ImageView statusImageView = view.findViewById(R.id.status_icon);
        if (testCategoryInfo.passed() == null) {
            statusImageView.setVisibility(View.INVISIBLE);
        } else if (testCategoryInfo.passed()) {
            statusImageView.setVisibility(View.VISIBLE);
            statusImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_circle_status_green_24dp, null));
        } else {
            statusImageView.setVisibility(View.VISIBLE);
            statusImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_circle_status_red_24dp, null));
        }

        // Set GroupIndicator if children exists. If not, hide GroupIndicator
        ImageView imageView = view.findViewById(R.id.group_indicator);
        if (!testCategoryInfo.hasMoreInfo()) {
            imageView.setVisibility(View.GONE);
        } else {
            if (isExpanded) {
                imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_arrow_up_black_24dp, null));
            } else {
                imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_arrow_down_black_24dp, null));
            }
            imageView.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = null;
        RelatedTestStatusCategoryData groupHdrData = (RelatedTestStatusCategoryData) getGroup(groupPosition);

        // QA Status Expanded ListView has non-uniform child view and child data.
        switch (groupHdrData.getCategory()) {
            case QC:
                view = getQCTestView(parent);
                break;

            case CV:
                view = getCVTestView(parent);
                break;

            case DEVICE_INFO:
                view = getDeviceStatusView(childPosition, parent);
                break;

            case REFERENCE_RANGE:
                view = getReferenceRangeView(childPosition, parent);
                break;

            case CRITICAL_RANGE:
                view = getCriticalRangeView(childPosition, parent);
                break;

            case REPORTABLE_RANGE:
                view = getReportableRangeView(childPosition, parent);
                break;

            case EQC:
            case TQC:
            default:
        }
        if (view == null) {
            TextView textView = new TextView(mContext);
            textView.setText(R.string.overview);
            textView.setPadding(40, 2, 2, 2);
            textView.setTextColor(Color.MAGENTA);
            view = textView;
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    //// Inflate the Specific Child View

    /**
     * QCTestView has only one child which is displayed as a GridView
     * using SimpleTestGridViewAdapter.
     *
     * @param parent -- a parent of this view which is a row of parent ListView.
     * @return -- a child view of Related QATest category.
     */
    private View getQCTestView(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.status_qc_test, parent, false);

        GridView gridView = view.findViewById(R.id.grid_view);
        List<SimpleTest> data = mRelatedTestStatusCategoryDataUtil.getAnalyteQCTestList();
        SimpleTestGridViewAdapter adapter = new SimpleTestGridViewAdapter(mContext, data);
        gridView.setAdapter(adapter);
        return view;
    }

    /**
     * CVTestView has only one child which is displayed as a GridView
     * using SimpleTestGridViewAdapter.
     *
     * @param parent -- a parent of this view which is a row of parent ListView.
     * @return -- a child view of Related QATest category.
     */
    private View getCVTestView(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.status_qc_test, parent, false);

        GridView gridView = view.findViewById(R.id.grid_view);
        List<SimpleTest> data = mRelatedTestStatusCategoryDataUtil.getAnalyteCVTestList();
        SimpleTestGridViewAdapter adapter = new SimpleTestGridViewAdapter(mContext, data);
        gridView.setAdapter(adapter);
        return view;
    }

    private View getDeviceStatusView(int childPosition, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item_test_status_data, parent, false);

        ListItem item = mRelatedTestStatusCategoryDataUtil.getDeviceInfoList().get(childPosition);
        return bindListItemToView(view, item);
    }

    private View getReferenceRangeView(int childPosition, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item_test_status_data, parent, false);

        StyledTextListItem item = mRelatedTestStatusCategoryDataUtil.getReferenceRangeList().get(childPosition);
        return bindListItemToView(view, item);
    }

    private View getCriticalRangeView(int childPosition, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item_test_status_data, parent, false);

        StyledTextListItem item = mRelatedTestStatusCategoryDataUtil.getCriticalRangeList().get(childPosition);
        return bindListItemToView(view, item);
    }

    private View getReportableRangeView(int childPosition, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item_test_status_data, parent, false);

        StyledTextListItem item = mRelatedTestStatusCategoryDataUtil.getReportableRangeList().get(childPosition);
        return bindListItemToView(view, item);
    }

    private View bindListItemToView(View view, ListItem item) {
        TextView viewTitle = view.findViewById(R.id.tv_title);
        TextView viewValue = view.findViewById(R.id.tv_value);
        viewTitle.setText(item.getTitle());
        viewValue.setText(item.getValue());
        return view;
    }

    private View bindListItemToView(View view, StyledTextListItem item) {
        TextView viewTitle = view.findViewById(R.id.tv_title);
        TextView viewValue = view.findViewById(R.id.tv_value);
        viewTitle.setText(item.getTitle());
        viewValue.setText(item.getValue());
        return view;
    }
}
