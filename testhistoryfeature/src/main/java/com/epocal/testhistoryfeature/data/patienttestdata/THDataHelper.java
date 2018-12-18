package com.epocal.testhistoryfeature.data.patienttestdata;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;

import com.epocal.common.Consts;
import com.epocal.common.realmentities.TestRecord;
import com.epocal.common.types.SyncState;
import com.epocal.common_ui.list.IListItem;
import com.epocal.common_ui.list.ISelectableListItem;
import com.epocal.testhistoryfeature.R;
import com.epocal.testhistoryfeature.data.type.SelectionType;
import com.epocal.testhistoryfeature.data.type.THDateRange;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import io.realm.RealmResults;

/**
 * Collection of static helper methods to parse and group data into the format
 * required by Data Adapters in this package.
 */
public class THDataHelper {
    static final int MAX_CHAR_LENGTH_TESTSUMMARYVIEWHOLDER = 48;
    static final int MAX_CHAR_LENGTH_SELECTABLETESTSUMMARYVIEWHOLDER = 44;
    static final String ELLIPSIS = "\u2026";

    static SpannableString formatHighlightMatchingString(String original, String textToMatch) {
        int startIndexOfMatch = original.toLowerCase().indexOf(textToMatch.toLowerCase());
        if (startIndexOfMatch == -1) return null;

        SpannableString outStr = new SpannableString(original);
        outStr.setSpan(new BackgroundColorSpan(Color.YELLOW), startIndexOfMatch, startIndexOfMatch+textToMatch.length(), 0);
        return outStr;
    }

    // MaxCharLength includes title (e.g) "title : value" - because the title is variable length.
    static SpannableString formatHighlightMatchingStringWithinCharLength(String title, String original, String textToMatch, int maxCharLength) {
        int startIndexOfMatch = original.toLowerCase().indexOf(textToMatch.toLowerCase());
        if (startIndexOfMatch == -1) return null;

        // Calculate the allowed length to fit original value such that the total length is within maxCharLength
        String prefix = title + " ";  // value is prefixed by "title[space]".
        int allowedValueLength = maxCharLength - prefix.length(); // allowedLength for value field.
        if (allowedValueLength >= original.length()) {
            // Original will fit. No length adjustment required.
            SpannableStringBuilder ssb = new SpannableStringBuilder(prefix);
            SpannableString spanedOriginal = new SpannableString(original);
            spanedOriginal.setSpan(new BackgroundColorSpan(Color.YELLOW), startIndexOfMatch, startIndexOfMatch + textToMatch.length(), 0);
            ssb.append(spanedOriginal);
            return SpannableString.valueOf(ssb);
        }

        // Need to truncate the original string such that the highlighted span is visible.
        int endIndexOfMatch = startIndexOfMatch + textToMatch.length();
        String front = original.substring(0, startIndexOfMatch);
        String tobeHilighted = original.substring(startIndexOfMatch, endIndexOfMatch);
        String back = original.substring(endIndexOfMatch);
        SpannableStringBuilder ssb = new SpannableStringBuilder(prefix);
        // If the front of highlight area is longer than 3 chars, replace it with ellipsis
        if (front.length() <= 3) {
            ssb.append(front);
        } else {
            ssb.append(ELLIPSIS);
        }
        // If after adding length of back, there is still remaining space, consider add front back.
        int remainingNoOfChars = maxCharLength - ssb.length() - tobeHilighted.length() - back.length();
        if ((remainingNoOfChars > 0) && (remainingNoOfChars <= front.length())) {
            // pull remainingNoOfChars from the end of front string
            String remainingFront = front.substring(front.length() - remainingNoOfChars);
            ssb.append(remainingFront);
        }
        ssb.append(tobeHilighted, new BackgroundColorSpan(Color.YELLOW), 0);
        ssb.append(back);
        // NOTE: If it needs to be truncated at end, it will be done by TextView.
        return SpannableString.valueOf(ssb);
    }

    @SuppressLint("DefaultLocale")
    public static String formatDateTime(Date date) {
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);
        int day   = calendar.get(Calendar.DATE);
        int year  = calendar.get(Calendar.YEAR);
        int hour  = calendar.get(Calendar.HOUR_OF_DAY);
        int min   = calendar.get(Calendar.MINUTE);
//        int sec   = calendar.get(Calendar.SECOND);
//        // Format with seconds e.g: "08/17/2018 05:15:12"
//        return String.format("%02d/%02d/%d %02d:%02d:%02d", month, day, year, hour, min, sec);

        // Format e.g: "08/17/2018 05:15"
        return String.format("%02d/%02d/%d %02d:%02d", month, day, year, hour, min);
    }

    private static Date getStartOfDay(Date date) {
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    static StringBuilder formatTitleAndValue(String title, String value) {
        return new StringBuilder(title).append(" ").append(value);
    }

    static List<IListItem<String>> groupByDate(Context context, RealmResults<TestRecord> testRecordList) {
        List<IListItem<String>> groupedList = new ArrayList<>(); // return array

        // Input check.
        if ((context == null) || (testRecordList == null) || (testRecordList.size() == 0)) {
            return groupedList; // return an empty list
        }

        // 1. - Create selectable item from testRecordList
        List<TestRecordListItem> testRecordListItems = createTestRecordList(testRecordList);

        // 2. - Group TestRecordListItem by Date
        List<TestRecordListItem> todayList = new ArrayList<>();
        List<TestRecordListItem> yesterdayList = new ArrayList<>();
        List<TestRecordListItem> lastweekList = new ArrayList<>();
        List<TestRecordListItem> lastyearList = new ArrayList<>();
        List<TestRecordListItem> unknownDateList = new ArrayList<>();
        // Perform sort
        sortTestRecordByDateRange(testRecordListItems, todayList, yesterdayList, lastweekList, lastyearList, unknownDateList);

        // 3. Assemble the list with Header items
        if (!todayList.isEmpty()) {
            groupedList.add(new TestRecordListItemHeader(context.getString(R.string.today), THDateRange.TODAY.value));
            groupedList.addAll(todayList);
        }
        if (!yesterdayList.isEmpty()) {
            groupedList.add(new TestRecordListItemHeader(context.getString(R.string.yesterday), THDateRange.YESTERDAY.value));
            groupedList.addAll(yesterdayList);
        }
        if (!lastweekList.isEmpty()) {
            groupedList.add(new TestRecordListItemHeader(context.getString(R.string.lastweek), THDateRange.LASTWEEK.value));
            groupedList.addAll(lastweekList);
        }
        if (!lastyearList.isEmpty()) {
            groupedList.add(new TestRecordListItemHeader(context.getString(R.string.older), THDateRange.OLDER.value));
            groupedList.addAll(lastyearList);
        }
        if (!unknownDateList.isEmpty()) {
            groupedList.add(new TestRecordListItemHeader(context.getString(R.string.unknown_date), THDateRange.NORANGE.value));
            groupedList.addAll(unknownDateList);
        }
        return groupedList;
    }

    /**
     * Create a list of TestRecordListItem from a list of TestRecord with two conditions:
     * -- testRecord that satisfy partialStringMatch condition.
     * -- set new item's selection state is to "false"
     *
     * @param testRecordList -- list of TestRecord (source data) to convert to list of TestRecordListItem.
     * @param partialStringMatch -- partial string (search string) used to fetch the list of TestRecord.
     * @return -- list of TestRecordListItem.
     */
    static List<TestRecordListItem> createTestRecordListWithPartialStringMatch(RealmResults<TestRecord> testRecordList, String partialStringMatch) {
        return createMultiSelectTestRecordListWithPartialStringMatch(testRecordList, false, partialStringMatch);
    }

    /**
     * Create a list of TestRecordListItem from a list of TestRecord.
     * TestRecordListItem has few additional fields for UI display including a item (row) selection
     * state, and item type (header or child).
     * @param testRecordList -- a list of TestRecord
     * @param initialSelectedId -- an itemId (TestRecord.id) to mark as selected.
     * @return -- a list of TestRecordListItem
     */
    private static List<TestRecordListItem> createMultiSelectTestRecordList(RealmResults<TestRecord> testRecordList, long initialSelectedId) {
        List<TestRecordListItem> list = new ArrayList<>();

        // Input check.
        if ((testRecordList == null) || (testRecordList.size() == 0)) {
            return list; // return an empty list
        }

        for (TestRecord record : testRecordList) {
            list.add(new TestRecordListItem(record, (initialSelectedId == record.getId())));
        }

        return list;
    }

    /**
     * Create a list of TestRecordListItem from a list of TestRecords, with two conditions:
     * -- testRecord that satisfy partialStringMatch condition.
     * -- set new item's selection state is to "true" when TestRecord.id = initialSelectedId
     *
     * @param testRecordList -- list of TestRecord (source data) to convert to list of TestRecordListItem.
     * @param initialSelectedId -- TestRecord.id to set selection state to "true". All other item selection state is "false"
     * @param partialStringMatch -- partial string (search string) used to fetch the list of TestRecord.
     * @return -- list of TestRecordListItem.
     */
    static List<TestRecordListItem> createMultiSelectTestRecordListWithPartialStringMatch(
            RealmResults<TestRecord> testRecordList,
            long initialSelectedId,
            String partialStringMatch)
    {
        List<TestRecordListItem> list = new ArrayList<>();

        // Input check.
        if ((testRecordList == null) || (testRecordList.size() == 0)) {
            return list; // return an empty list
        }

        for (TestRecord record : testRecordList) {
            // Condition check
            if (isSatisfyCondition(record, partialStringMatch)) {
                list.add(new TestRecordListItem(record, (initialSelectedId == record.getId())));
            }
        }

        return list;
    }

    /**
     * Create a list of TestRecordListItem from a list of TestRecord.
     * TestRecordListItem has few additional fields for UI display including a item (row) selection
     * state, and item type (header or child). Selection state is 'false'.
     * @param testRecordList -- a list of TestRecord
     * @return -- a list of TestRecordListItem
     */
    private static List<TestRecordListItem> createMultiSelectTestRecordList(RealmResults<TestRecord> testRecordList) {
        return createMultiSelectTestRecordList(testRecordList, false);
    }

    /**
     * Create a list of TestRecordListItem from a list of TestRecord.
     * TestRecordListItem has few additional fields for UI display including a item (row) selection
     * state, and item type (header or child).
     *
     * @param testRecordList -- a list of TestRecord
     * @param shouldBeSelected -- selection state of ListItem to be set.
     * @return -- a list of TestRecordListItem
     */
    private static List<TestRecordListItem> createMultiSelectTestRecordList(RealmResults<TestRecord> testRecordList, boolean shouldBeSelected) {
        List<TestRecordListItem> list = new ArrayList<>();

        // Input check.
        if ((testRecordList == null) || (testRecordList.size() == 0)) {
            return list; // return an empty list
        }

        for (TestRecord record : testRecordList) {
            list.add(new TestRecordListItem(record, shouldBeSelected));
        }

        return list;
    }

    /**
     * Create a list of TestRecordListItem from a list of TestRecords, with two conditions:
     * -- testRecord that satisfy partialStringMatch condition.
     * -- set new item's selection state is to "shouldBeSelected" input.
     *
     * @param testRecordList -- list of TestRecord (source data) to convert to list of TestRecordListItem.
     * @param shouldBeSelected -- selection state of newly created item
     * @param partialStringMatch -- partial string (search string) used to fetch the list of TestRecord.
     * @return -- list of TestRecordListItem.
     */
    static List<TestRecordListItem> createMultiSelectTestRecordListWithPartialStringMatch(
            RealmResults<TestRecord> testRecordList,
            boolean shouldBeSelected,
            String partialStringMatch)
    {
        List<TestRecordListItem> list = new ArrayList<>();

        // Input check.
        if ((testRecordList == null) || (testRecordList.size() == 0)) {
            return list; // return an empty list
        }

        for (TestRecord record : testRecordList) {
            // Condition check
            if (isSatisfyCondition(record, partialStringMatch)) {
                list.add(new TestRecordListItem(record, shouldBeSelected));
            }
        }

        return list;
    }

    /**
     /**
     * Create a list of TestRecordListItem from a list of TestRecord.
     * TestRecordListItem has few additional fields for UI display including a item (row) selection
     * state, and item type (header or child).
     * All items in a list are marked as not-selected.
     *
     * @param testRecordList -- a list of TestRecord
     * @return -- a list of TestRecordListItem
     */
    private static List<TestRecordListItem> createTestRecordList(RealmResults<TestRecord> testRecordList) {
        return createMultiSelectTestRecordList(testRecordList);
    }

    /**
     * Group the list of TestRecordListItem (contains TestRecord with the selection state) by date.
     * Input testRecordList is first converted to the list of TestRecordListItem (each item contains
     * TestRecord and selection state). Then the list of TestRecordListItem is grouped by Date.
     * (today, yesterday, last week, older).
     * Used by THMultiSelectGroupedDataAdapter
     *
     * @param context           -- context to build string resource
     * @param testRecordList    -- a list of TestRecord
     * @param initialSelectedId -- an item to mark as selected in a list. One of ALL, NONE. or record.id
     * @param headerRowIndexes  -- the list of header item positions (index) in the return list.
     * @return groupedList -- testRecordListItems grouped by Date.
     */
    static List<ISelectableListItem> groupByDate(
            Context context,
            RealmResults<TestRecord> testRecordList,
            List<Integer> headerRowIndexes,
            SelectionType initialSelectionType,
            long initialSelectedId,
            THDateRange initialSelectedDateRange)
    {

        List<ISelectableListItem> groupedList = new ArrayList<>(); // return array

        // Input Check
        if ((context == null) || (testRecordList == null) || (testRecordList.size() == 0)) {
            return groupedList; // return an empty list
        }

        // 1. - Create selectable testRecordList - for selection state, only handle one_item case.
        List<TestRecordListItem> testRecordListItems;
        if (initialSelectionType == SelectionType.SELECT_ALL) {
            testRecordListItems = createMultiSelectTestRecordList(testRecordList, true);
        } else if (initialSelectionType == SelectionType.SELECT_ONE_ITEM) {
            testRecordListItems = createMultiSelectTestRecordList(testRecordList, initialSelectedId);
        } else {
            testRecordListItems = createMultiSelectTestRecordList(testRecordList, false);
        }

        // 2. - Group TestRecordListItem by Date
        List<TestRecordListItem> todayList = new ArrayList<>();
        List<TestRecordListItem> yesterdayList = new ArrayList<>();
        List<TestRecordListItem> lastweekList = new ArrayList<>();
        List<TestRecordListItem> lastyearList = new ArrayList<>();
        List<TestRecordListItem> unknownDateList = new ArrayList<>();
        EnumMap<THDateRange, Integer> dateRangeToHeaderIndexMap = new EnumMap<>(THDateRange.class);
        // Perform sort
        sortTestRecordByDateRange(testRecordListItems, todayList, yesterdayList, lastweekList, lastyearList, unknownDateList);

        if (headerRowIndexes == null) headerRowIndexes = new ArrayList<>();
        int itemIndex = 0; // first header index starts at 0

        // 3. - Assemble the list with Header items
        if (!todayList.isEmpty()) {
            groupedList.add(new TestRecordListItemHeader(context.getString(R.string.today), false, todayList.size(), THDateRange.TODAY.value));
            groupedList.addAll(todayList);
            headerRowIndexes.add(itemIndex); // index for header -- starts with index 0
            dateRangeToHeaderIndexMap.put(THDateRange.TODAY, itemIndex);
            itemIndex += todayList.size() + 1;    // increment index by adding children's size and header size (1)
        }
        if (!yesterdayList.isEmpty()) {
            groupedList.add(new TestRecordListItemHeader(context.getString(R.string.yesterday), false, yesterdayList.size(), THDateRange.YESTERDAY.value));
            groupedList.addAll(yesterdayList);
            headerRowIndexes.add(itemIndex);  // index for header
            dateRangeToHeaderIndexMap.put(THDateRange.YESTERDAY, itemIndex);
            itemIndex += yesterdayList.size() + 1; // increment index by adding children's size and header size (1)
        }
        if (!lastweekList.isEmpty()) {
            groupedList.add(new TestRecordListItemHeader(context.getString(R.string.lastweek), false, lastweekList.size(), THDateRange.LASTWEEK.value));
            groupedList.addAll(lastweekList);
            headerRowIndexes.add(itemIndex); // index for header
            dateRangeToHeaderIndexMap.put(THDateRange.LASTWEEK, itemIndex);
            itemIndex += lastweekList.size() + 1; // increment index by adding children's size and header size (1)
        }
        if (!lastyearList.isEmpty()) {
            groupedList.add(new TestRecordListItemHeader(context.getString(R.string.older), false, lastyearList.size(), THDateRange.OLDER.value));
            groupedList.addAll(lastyearList);
            headerRowIndexes.add(itemIndex); // index for header
            dateRangeToHeaderIndexMap.put(THDateRange.OLDER, itemIndex);
            itemIndex += lastyearList.size() + 1; // increment index by adding children's size and header size (1)
        }
        if (!unknownDateList.isEmpty()) {
            groupedList.add(new TestRecordListItemHeader(context.getString(R.string.unknown_date), false, unknownDateList.size(), THDateRange.NORANGE.value));
            groupedList.addAll(unknownDateList);
            headerRowIndexes.add(itemIndex); // index for header
            dateRangeToHeaderIndexMap.put(THDateRange.NORANGE, itemIndex);
            //itemIndex += unknownDateList.size() + 1; // increment index by adding children's size and header size (1)
        }

        // 4. - Handle SELECT_DATE_RANGE case.
        if (initialSelectionType == SelectionType.SELECT_DATE_RANGE) {
            Integer headerIndex = dateRangeToHeaderIndexMap.get(initialSelectedDateRange);
            // NOTE: null index means no match found.
            if (headerIndex != null) {
                ISelectableListItem headerItem = groupedList.get(headerIndex);
                headerItem.setSelected(true); // Mark header selected
                for (int i = headerIndex + 1; i < groupedList.size(); i++) {
                    // Mark all child items until the next header
                    ISelectableListItem nextItem = groupedList.get(i);
                    if (nextItem.isSectionHeader())
                        break; // if nextItem is header, we are done!
                    nextItem.setSelected(true); // set the state for all child items until the next header.
                }
            }
        }
        return groupedList;
    }

    /**
     * This method sort a list of TestRecordListItem int the date ranges:
     * - today
     * - yesterday
     * - lastweek,
     * - lastyear
     * - unknown date (when an item does not have a datetime)
     *
     * @param testRecordListItems -- "in" parameter - a list of TestRecordListItem
     * @param todayList           -- "out" parameter. a list of items whose date is today
     * @param yesterdayList       -- "out" parameter. a list of items whose date is yesterday
     * @param lastweekList        -- "out" parameter. a list of items whose date is last week
     * @param lastyearList        -- "out" parameter. a list of items whose date is last year.
     * @param unknownDateList     -- "out" parameter. a list of items whose date is null.
     */
    private static void sortTestRecordByDateRange(List<TestRecordListItem> testRecordListItems, // IN
                                          List<TestRecordListItem> todayList,           // OUT
                                          List<TestRecordListItem> yesterdayList,       // OUT
                                          List<TestRecordListItem> lastweekList,        // OUT
                                          List<TestRecordListItem> lastyearList,        // OUT
                                          List<TestRecordListItem> unknownDateList)     // OUT
    {
        // Group TestRecordListItem by Date range
        Date today = getStartOfDay(new Date());
        Date yesterday = new Date(today.getTime() - TimeUnit.DAYS.toMillis(1));
        Date lastweek = new Date(today.getTime() - TimeUnit.DAYS.toMillis(7));

        // Input check
        todayList = (todayList == null) ? new ArrayList<TestRecordListItem>() : todayList;
        yesterdayList = (yesterdayList == null) ? new ArrayList<TestRecordListItem>() : yesterdayList;
        lastweekList = (lastweekList == null) ? new ArrayList<TestRecordListItem>() : lastweekList;
        lastyearList = (lastyearList == null) ? new ArrayList<TestRecordListItem>() : lastyearList;
        unknownDateList = (unknownDateList == null) ? new ArrayList<TestRecordListItem>() : unknownDateList;

        for (TestRecordListItem item : testRecordListItems) {
            if (item.getTestRecord().getTestDateTime() == null) {
                // unknown date
                unknownDateList.add(item);
            } else if (item.getTestRecord().getTestDateTime().after(getStartOfDay(today))) {
                // today
                todayList.add(item);
            } else if (item.getTestRecord().getTestDateTime().after(yesterday)) {
                // yesterday
                yesterdayList.add(item);
            } else if (item.getTestRecord().getTestDateTime().after(lastweek)) {
                // last week
                lastweekList.add(item);
            } else {
                // older
                lastyearList.add(item);
            }
        }
    }

    /**
     * TestRecord can have 4 different values for SyncState.
     * However, TestHistory will only display either Sent or Unsent state in UI.
     * Below is the logic to map the state to either Sent or Unsent state.
     * boolean  true indicate Sent state and false indicate Unsent state.
     *
     * @param record - TestRecord object
     * @return boolean - indicating if TestRecord is Sent or Unsent for UI display purpose.
     */
    public static boolean isTestRecordSent(TestRecord record) {
        SyncState currentState = record.getSyncState();
        boolean isSent;
        switch (currentState) {
            case SentSuccessfully:
                isSent = true; // Sent state
                break;

            case Unknown:
            case Unsent:
            case SentNotAccepted:
            default:
                isSent = false; // Unsent state
                break;
        }
        return isSent;
    }

    public static String getDisplayUserId(Context context, TestRecord testRecord) {
        if ((context == null) || (testRecord == null) ||
                (testRecord.getUser() == null) || (testRecord.getUser().getUserId() == null))
        {
            return null;
        }

        return (testRecord.getUser().getUserId().equals(Consts.ANONYMOUS)) ? context.getString(R.string.guest_user) : testRecord.getUser().getUserId();
    }

    /**
     * This method checks for a bad entry in partialStringMatch search results. (When user uses SearchView
     * to fetch the TestRecord list matching a partial search string.)
     *
     * When search string contains a part of "anonymous" opID, (e.g) "non",
     * then exclude matched opID = "anonymous" - because "anonymous" shouldn't be a searchable op ID
     * by string match.
     *
     * However, we'd like to include other opID (non-anonymous opID)(e.g) opID = "Nonna"
     * as well as when other fields contains the match (e.g) Comments = "anonymous".
     *
     * NOTE: string match is case insensitive.
     *
     * @param record -- TestRecord object
     * @param partialStringMatch -- partial string (search string) to match in the fields of TestRecord.
     * @return true -- when TestRecord satisfy the partialStringMatch condition.
     *         false -- when TestRecord does not satisfy the partialStringMatch condition.
     */
    private static boolean isSatisfyCondition(TestRecord record, String partialStringMatch) {
        if ((record == null) || (partialStringMatch == null)) return true;

        partialStringMatch = partialStringMatch.toLowerCase();
        boolean matchFound = false;

        // Subject Id
        if (record.getSubjectId() != null) {
            matchFound = record.getSubjectId().toLowerCase().contains(partialStringMatch);
        }
        if (!matchFound) {
            //cardLot.lotNumber
            if ((record.getCardLot() != null) && (record.getCardLot().getLotNumber() != null)) {
                matchFound = record.getCardLot().getLotNumber().toLowerCase().contains(partialStringMatch);
            }
        }
        if (!matchFound) {
            //reader.serialNumber
            if ((record.getReader() != null) && (record.getReader().getSerialNumber() != null)) {
                matchFound = record.getReader().getSerialNumber().toLowerCase().contains(partialStringMatch);
            }
        }
        if (!matchFound) {
            //testDetail.comment
            if ((record.getTestDetail() != null) && (record.getTestDetail().getComment() != null)) {
                matchFound = record.getTestDetail().getComment().toLowerCase().contains(partialStringMatch);
            }
        }
        if (!matchFound) {
            // Op ID check
            if ((record.getUser() != null) && (record.getUser().getUserId() != null)) {
                String lowerUserId = record.getUser().getUserId().toLowerCase();
                matchFound = lowerUserId.contains(partialStringMatch);
                if (matchFound) {
                    // If search string contains a part of "anonymous" opID, (e.g) "non",
                    // and matched opID is "anonymous", exclude this entry.
                    // But we'd like to include other opID (e.g) "Nonna".
                    return (!Consts.ANONYMOUS.contains(partialStringMatch)) || (!Consts.ANONYMOUS.equals(lowerUserId));
                }
            }
        }
        return true;
    }
}
