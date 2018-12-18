package com.epocal.testhistoryfeature.data.statusdata;

import android.text.SpannableStringBuilder;

import com.epocal.common_ui.qaresults.SimpleTest;

/**
 * QATestCategory is a category of related (QA) Tests associated with the Patient Blood Test.
 *
 * <h4>Class details:</h4>
 * <p>
 * Each QA category has the following attributes:
 * <ul>
 *     <li><b>testName</b> -- the name of the QA Test Category.</li>
 *     <li><b>passed</b> -- a Boolean to indicate the overall status of QA Test Category.
 *     The status can be null, or "unknown" - if the test has never run or it's datetime is
 *     unknown.</li>
 *     <li><b>hasMoreInfo</b> -- a boolean to indicate if this category contains more information
 *     to be displayed. (used in ExpandedListView to display expand/collapse icon to display
 *     more information.)</li>
 * </ul>
 * </p>
 */
public class RelatedTestStatusCategoryData extends SimpleTest {
    private RelatedTestStatusCategory mCategory;
    private Boolean mHasMoreInfo;
    private SpannableStringBuilder mSubTitle;

    RelatedTestStatusCategoryData(RelatedTestStatusCategory category,
                                  String testName,
                                  SpannableStringBuilder subTitle,
                                  Boolean hasPassedTest,
                                  Boolean hasMoreInfo) {
        super(testName, hasPassedTest);
        this.mCategory = category;
        this.mHasMoreInfo = hasMoreInfo;
        this.mSubTitle = subTitle;
    }

    public String getTitle() {
        return getName();
    }

    SpannableStringBuilder getSubTitle() {
        return mSubTitle;
    }

    Boolean hasMoreInfo() {
        return mHasMoreInfo;
    }

    RelatedTestStatusCategory getCategory() {
        return mCategory;
    }
}


