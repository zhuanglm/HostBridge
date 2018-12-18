package com.epocal.testhistoryfeature.data.patienttestdata;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.epocal.common.realmentities.TestRecord;
import com.epocal.common.types.am.TestMode;
import com.epocal.testhistoryfeature.R;

/**
 * This class represent view-model to display Patient Test Summary from TestRecord.
 */
class TestSummaryViewHolder extends RecyclerView.ViewHolder {
    private Context  mContext;
    private String   mUnknownStringValue;
    private TextView mTvTitle;
    private TextView mTvDateTime;
    private TextView mTvFieldValue1;
    private TextView mTvSyncStateValue;
    private TextView mTvFieldSeparator;
    //private ImageView mImageViewSyncState;

    TestSummaryViewHolder(View itemView, Context context) {
        super(itemView);
        mContext = context;
        mUnknownStringValue = mContext.getResources().getString(R.string.unknown);
        mTvTitle = itemView.findViewById(R.id.tv_title);
        mTvDateTime = itemView.findViewById(R.id.tv_datetime);
        mTvFieldValue1 = itemView.findViewById(R.id.tv_field1_value); // Display operatorID, or Reader SN
        mTvSyncStateValue = itemView.findViewById(R.id.tv_sync_state);
        mTvFieldSeparator = itemView.findViewById(R.id.field_separator);
        //mImageViewSyncState = itemView.findViewById(R.id.img_sync_state);
    }

    /**
     * Display a Row of RecyclerView of List<TestRecord>,
     * @param record --  TestRecord object contains the source of text displayed in this ViewHolder
     */
    void bind(TestRecord record) {
        // 1. - Title = Subject ID
        CharSequence subjectId = ((record.getSubjectId() == null) || (record.getSubjectId().isEmpty())) ? getNoSubjectIdTitle(record) : getSubjectIdTitle(record) + " " + record.getSubjectId();
        mTvTitle.setText(subjectId);

        // 2. - SubTitle = date at time <bullet> operator
        String datetime = (record.getTestDateTime() == null) ? "" : THDataHelper.formatDateTime(record.getTestDateTime());
        mTvDateTime.setText(datetime);

        // Op ID
        String operator = THDataHelper.getDisplayUserId(mContext, record);
        if ((operator == null) || operator.isEmpty()) operator = mUnknownStringValue;

        String operatorTitle = mContext.getString(R.string.operator_short);
        mTvFieldValue1.setText(THDataHelper.formatTitleAndValue(operatorTitle, operator));

        // Save current text color
        ColorStateList textColor = mTvFieldValue1.getTextColors();

        // 3. - Sync State
        if (THDataHelper.isTestRecordSent(record)) {
            mTvSyncStateValue.setText(R.string.sent);
            mTvSyncStateValue.setTextColor(textColor); // set to current text color
        } else {
            mTvSyncStateValue.setText(R.string.unsent);
            mTvSyncStateValue.setTextColor(ContextCompat.getColor(mContext, R.color.colorDarkRed));

        }

        // 3a - Alt Icon Sync State
//        if (THDataHelper.isTestRecordSent(record)) {
//            mImageViewSyncState.setImageResource(R.drawable.ic_cloud_done_green_24dp);
//        } else {
//            mImageViewSyncState.setImageResource(R.drawable.ic_cloud_off_red_24dp);
//        }
    }

    /**
     * Display a Row of RecyclerView of List<TestRecord>, with searchTerm (stringToMatch) highlighted.
     * This highlight is used to show the search term used in SearchResult list display.
     *
     * @param record --  TestRecord object contains the source of text displayed in this ViewHolder
     * @param stringToMatch -- contains the string value to be highlighted in yellow.
     *                      Highlight text fields are different based on TestRecord.testMode value.
     *                      Case TestMode.BloodTest:
     *                      Text is highlighted in the following text fields:
     *                        SubjectId, Operator ID, Reader S/N, or CarLot.
     *                      Case TestMode.QA:
     *                      Text is highlighted in the following text field:
     *                        Comments
     *
     * @param maxCharLength -- a guesstimate of the number of characters that fit in a row of
     *                      TextSummaryViewHolder's view.
     *                      mTvFieldValue1 has a variable width with (typically) a long text that is truncated.
     *                      This value is used to shift the highlighted text to the visible area of the text view.
     */
    void bind(TestRecord record, String stringToMatch, int maxCharLength) {
        // 1. - First, display the row without highlight.
        bind(record);

        // 2. Reset LayoutParam of mTvFieldValue1 - as it may set to layout_weight of 1 or 0 depends
        //    on the length of the text value.
        LinearLayout.LayoutParams defaultParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0f);
        mTvFieldValue1.setLayoutParams(defaultParam);

        // 3. Process stringMatch highlight.
        int MIN_CHAR_LENGTH = 10;
        maxCharLength = (maxCharLength < MIN_CHAR_LENGTH) ? MIN_CHAR_LENGTH : maxCharLength;

        // Highlight partialMatch text - if any
        if ((stringToMatch != null) && (stringToMatch.length() > 0))
        {
            SpannableString highlightedString;

            if (record.getSubjectId() != null) {
                SpannableString titleSpanString = new SpannableString(getSubjectIdTitle(record) + " ");
                highlightedString = THDataHelper.formatHighlightMatchingString(record.getSubjectId(), stringToMatch);
                if (highlightedString != null) {
                    mTvTitle.setText(TextUtils.concat(titleSpanString, highlightedString));
                    return;
                }
            }

            // Highlight subtitle (2nd line) in the row

            // actualMaxCharLength -- a guesstimate of the number of characters that fit in a mTvFieldValue1 view.
            int noOfCharsUsedUp = mTvDateTime.getText().length() + mTvSyncStateValue.getText().length() + (mTvFieldSeparator.getText().length() * 2);
            int actualMaxCharLength = maxCharLength - noOfCharsUsedUp;
            highlightedString = highlightSubtitle(record, stringToMatch, actualMaxCharLength);
            if (highlightedString != null) {
                // If it contains '...' - it is truncated in front.
                // If its length is greater than maxCharLength, it will be truncated by TextView.
                if (highlightedString.toString().contains(THDataHelper.ELLIPSIS) || (highlightedString.length() > actualMaxCharLength)) {
                    // truncated! - set layout_weight = 1 to take up all available remaining space.
                    // if text is too long, TextView will truncate it at end.
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1f);
                    mTvFieldValue1.setLayoutParams(params);
                }
                mTvFieldValue1.setText(highlightedString);
            }
        }
    }

    private SpannableString highlightSubtitle(TestRecord record, String stringToMatch, int maxCharLength) {
        SpannableString highlightedString = null;
        // Patient Blood Test Mode - search match in: userId, CardLot, Reader S/N
        // QA Test Mode - search match in: userId, comments, CardLot, Reader S/N
        String operator = THDataHelper.getDisplayUserId(mContext, record);
        if (operator != null) {
            String title = mContext.getResources().getString(R.string.operator_short);
            highlightedString = THDataHelper
                    .formatHighlightMatchingStringWithinCharLength(title,
                            operator, stringToMatch, maxCharLength);
        }
        if ((highlightedString == null) && (record.getTestMode() == TestMode.QA)) {
            if ((record.getTestDetail() != null) && (record.getTestDetail().getComment() != null)) {
                String title = mContext.getString(R.string.comments);
                highlightedString = THDataHelper
                        .formatHighlightMatchingStringWithinCharLength(title,
                                record.getTestDetail().getComment(), stringToMatch, maxCharLength);
            }
        }
        if (highlightedString == null) {
            if ((record.getReader() != null) && (record.getReader().getSerialNumber() != null)) {
                String title = mContext.getResources().getString(R.string.reader_sn);
                highlightedString = THDataHelper
                        .formatHighlightMatchingStringWithinCharLength(title,
                                record.getReader().getSerialNumber(), stringToMatch, maxCharLength);
            }
        }
        if (highlightedString == null) {
            if ((record.getCardLot() != null) && (record.getCardLot().getLotNumber() != null)) {
                String title = mContext.getResources().getString(R.string.cardlot);
                highlightedString = THDataHelper
                        .formatHighlightMatchingStringWithinCharLength(title,
                                record.getCardLot().getLotNumber(), stringToMatch, maxCharLength);
            }
        }
        return highlightedString;
    }

    private CharSequence getSubjectIdTitle(TestRecord record) {
        if (record.getTestMode() == TestMode.QA) {
            return mContext.getString(R.string.lot);
        } else {
            return mContext.getString(R.string.patient);
        }
    }

    private CharSequence getNoSubjectIdTitle(TestRecord record) {
        if (record.getTestMode() == TestMode.QA) {
            return mContext.getString(R.string.no_lot_number);
        } else {
            return mContext.getString(R.string.no_patient_id);
        }
    }
}
