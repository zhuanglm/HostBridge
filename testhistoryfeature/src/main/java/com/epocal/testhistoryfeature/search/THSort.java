package com.epocal.testhistoryfeature.search;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;

/**
 * This class represent a Sort criteria for Realm TestRecord object.
 * It holds the column name for the sort and sort order.
 */
public class THSort {
    public static final String ASC            = "ascending";
    public static final String DESC           = "descending";

    public static final String TESTDATE       = THSearchFilters.TESTDATE;

    protected String mSortKey;
    protected String mSortOrder;

    private static final List<String> mValidSortKeys = Arrays.asList(TESTDATE);

    public static List<String> getValidSortKeys() {
        return mValidSortKeys;
    }

    public THSort() {
        this(TESTDATE, DESC);
    }

    public THSort(String sortKey, String sortOrder) throws InputMismatchException {
        if (mValidSortKeys.contains(sortKey)) {
            this.mSortKey = sortKey;
        } else {
            throw new InputMismatchException(String.format("Wrong sortKey passed (%s). Only THSort.TESTDATE is supported.", sortKey));
        }

        if (ASC.equals(sortOrder) || (DESC.equals(sortOrder))) {
            this.mSortOrder = sortOrder;
        } else {
            throw new InputMismatchException(String.format("Wrong sortOrder passed (%s). Only THSort.ASC or THSort.DESC supported.", sortOrder));
        }
    }

    public String getSortKey() {
        return mSortKey;
    }

    public String getSortOrder() {
        return mSortOrder;
    }

    public boolean isAscending() {
        return (mSortOrder.equals(ASC));
    }
}
