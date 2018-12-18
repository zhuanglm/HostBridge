package com.epocal.testhistoryfeature.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.epocal.common.Consts;
import com.epocal.common.realmentities.TestRecord;
import com.epocal.common.types.Permissions;
import com.epocal.common.types.am.TestMode;
import com.epocal.datamanager.TestRecordModel;
import com.epocal.testhistoryfeature.BaseActivity;
import com.epocal.testhistoryfeature.R;
import com.epocal.testhistoryfeature.THContract;
import com.epocal.testhistoryfeature.data.patienttestdata.ITestRecordListItemClickListener;
import com.epocal.testhistoryfeature.data.patienttestdata.ITestRecordListItemHeaderClickListener;
import com.epocal.testhistoryfeature.data.patienttestdata.THDataAdapter;
import com.epocal.testhistoryfeature.data.patienttestdata.THGroupedDataAdapter;
import com.epocal.testhistoryfeature.data.presetfilterdata.PresetFilterData;
import com.epocal.testhistoryfeature.data.presetfilterdata.PresetFilterDataAdapter;
import com.epocal.testhistoryfeature.data.type.ActionType;
import com.epocal.testhistoryfeature.data.type.SelectionType;
import com.epocal.testhistoryfeature.data.type.THDateRange;
import com.epocal.testhistoryfeature.di.DaggerTestHistoryComponent;
import com.epocal.testhistoryfeature.di.TestHistoryComponent;
import com.epocal.testhistoryfeature.di.TestHistoryModule;
import com.epocal.testhistoryfeature.ui.fragment.ITHMainFragmentListener;
import com.epocal.testhistoryfeature.ui.fragment.THMainMultiSelectListFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.functions.Predicate;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * This class represents the Main Screen of Test History.
 *
 * @since 2018-10-06
 *
 * <h4>Overview:</h4>
 * <p>
 * Test History feature enables the user to do the followings:
 *  <ul>
 *  <li> 1. list past Tests of type Blood Test or QA Test</li>
 *  <li> 2. search past Tests
 *  <li> 3. select one or multiple tests to perform actions:
 *       <ul><li>-- change the sync-state (change to one of "Sent" or "Unsent" state)</li>
 *           <li>-- delete the test(s) (delete from the db)</li></ul>
 *  <li> 4. view the Test Details info (e.g) Test results, Input data related to test,
 *  Status of the reader when the test was performed.
 *  <li> 5. in the details screen above, user can perform actions:
 *          <ul><li>-- change the sync-state (change to one of "Sent" or "Unsent" state)
 *              <li>-- delete the test (delete from the db)
 *              <li>-- print (not supported yet but shows in menu)</li></ul>
 *  </ul>
 *  </p>
 *
 *  <h4>Overview of Main Screen:</h4>
 *  <p>
 *  This class supports above 1 - 3 together with other supporting classes.
 *  Above 4 and 5 are supported by Details Screen (TestHistoryDetailsActivity).
 *  Main Screen displays the past Test of Blood Test or QA Test. Which kind is displayed depends
 *  on where it is navigated from.
 *  When the user navigates from Test History menu, it displays the Blood Test.
 *  When the user navigates from QA Test History menu, it displays the QA Test.
 *  </p>
 *
 *  <h4>Class Details:</h4>
 *  <p>
 *  There are two ways to list past tests.<br />
 *  <ul>
 *      <li><b>Using Pre-set Filters:</b> On ActionBar on top of the screen, there is a drop-down menu with
 *      the pre-set criteria for searching test. The available selections are:
 *      "All Patient Tests", "Sent Tests", and "Unsent Tests". The default is "All Patient Tests"
 *      and this is the criteria used for loading past tests when the user first navigates
 *      to Main Screen. Tests are grouped by the range of dates: "Today", "Yesterday",
 *      "Last Week" and "Older". Note: When past tests are QA Test, the label is changed to
 *      "All QA Tests" instead of "All Patient Tests".</li>
 *
 *      <li><b>Using Search View:</b> On ActionBar, there is a magnifying glass icon to expand the
 *      "Search" text area. When the user enters a text in the search text area, the past Test
 *      matching the search term will be displayed in the list view below Action Bar.
 *      Unlike using Pre-set filters, the search results are not grouped by the range of dates.
 *      The number of search results are displayed at the top of the search result list.
 *      The search term text is highlighted in yellow in search results.
 *
 *      <ul>
 *          <li><b>Search rules:</b></li>
 *          <li>Search will start when the input is 3 char or longer. </li>
 *          <li>When listing Blood tests, Search term will match any of
 *          the following columns: "subjectId", "userId", "cardLot" and "reader.serialNumber"
 *          in the order listed (precedence). Note: "subjectId" in context of Patient Test is
 *          "Patient Name".</li>
 *          <li>When listing QA tests, Search term will match any of the following
 *          columns: "subjectId", "userId", "comments", "cardLot" and "reader.serialNumber"
 *          in the order listed (precedence). Note: "subjectId"
 *          in context of QA Test is "Fluid Lot number".</li>
 *      </ul>
 *      <ul><li><b>Search term highlight rule:</b>
 *      The columns "userId", "cardLot", "reader.serialNumber", "comments" occupy the same TextView area.
 *      Only one matched column will be displayed, (e.g)
 *      if "cardLot" was the search term match, then "cardLot" will be displayed and not
 *      "userId" nor "reader.serialNumber".</li></ul>
 *      </li>
 *  </ul>
 *  </p>
 *
 *  <h4>ListView (search results) interactions:</h4>
 *  <p>
 *  ListView row supports the following gestures:
 *  <ul>
 *      <li>single row tap -- the screen will move to a Details Screen.</li>
 *      <li>long hold row -- (admin user only) the screen will move to a Test Selection screen.</li>
 *      <li>scroll -- scroll the list up/down.</li>
 *  </ul>
 *  </p>
 *
 *  <h4>ActionBar menu interactions: (admin user only)</h4>
 *  <p>
 *  ActionBar overflow menu contains the followings:
 *  <ul>
 *      <li>Select - the screen will move to a Test Selection screen with no row selected state.</li>
 *      <li>Select All - the screen will move to a Test Selection screen with
 *      all rows selected state.</li>
 *  </ul>
 *  </p>
 *
 *  <h4>Data Adapter for Recycler View</h4>
 *  <p>The Main Screen uses 4 different data adapters to display the list of Test Records.
 *  <ul>
 *  <li><b>THDataAdapter</b> -- is used to display the list of Test Records.</li>
 *  <li><b>THGroupedDataAdapter</b> -- is used to display the list of Test Records grouped by date range.</li>
 *  <li><b>THMultiSelectDataAdapter</b> -- same display as THDataAdapter with addition of CheckBox in each row.</li>
 *  <li><b>THMultiSelectGroupedDataAdapter</b> -- same display as THGroupedDataAdapter with addition of CheckBox in each row.</li>
 *  </ul>
 *  </p>
 *
 *  <h4>User Permissions</h4>
 *  <p>The option menu "select" and "select all" are only avaialble when the user logs in as the
 *  admin user. (The non-admin user won't see these options). Likewise, long hold
 *  search results row to move to Selection screen is only available for the admin user.</p>
 *
 *  <h4>Note:</h4>
 *  <p>
 *  com.epocal.testhistoryfeature.data.patienttestdata -- package contains adapter, viewholder, model for list view.
 *  </p>
 *
 *  <h4>Note2:</h4>
 *  <p>
 *  Since this class holds its own realm instance (in order to get a live result set update),
 *  do not use other class/method that has own realm instance. Multiple call to
 *  Realm.getDefaultInstance() cause problems in subsequent realm call.
 *  (e.g) UserModel().getLoggedInUser().getPermission(); call from onCreate() caused
 *  the search query to stop working.
 *  </p>
 *
 *  @see  com.epocal.testhistoryfeature.data.patienttestdata
 *  @see  THMainMultiSelectListFragment
 *  @see  TestHistoryDetailsActivity
 *
 */
public class TestHistoryMainActivity extends BaseActivity implements THContract.View, ITHMainFragmentListener {
    private static final String TAG= TestHistoryMainActivity.class.getSimpleName();

    private Button mBtnPopupMenu;
    private List<PresetFilterData> mPresetFilterList;
    private TextView mNoSearchResultTextView;
    private RecyclerView mListView;
    private LinearLayoutManager mListViewLayoutManager;
    private THGroupedDataAdapter mGroupedDataAdapter;
    private RealmResults<TestRecord> mTestHistoryData;
    private boolean mIsSearchViewMenuExpanded = false; // true - when searchView is expanded in ActionBar
    private String mCurrentPartialMatchString;
    private THMainMultiSelectListFragment mMultiSelectListFragment;
    private Realm mRealm;
    private boolean mIsQATest = false;
    private boolean mIsAdminUser;

    // MVP with Dagger
    @Inject
    public THContract.Presenter mPresenter;
    @Inject
    public THContract.Model     mModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unpackArguments();

        setContentView(R.layout.activity_test_history_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false); // Hide title
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show back-arrow on left
            getSupportActionBar().setElevation(0);
        }

        mBtnPopupMenu = findViewById(R.id.dropdown_menu_button);
        if (mIsQATest) {
            mBtnPopupMenu.setText(R.string.all_qa_tests);
        }
        setupDropdownMenu(mBtnPopupMenu);

        mNoSearchResultTextView = findViewById(R.id.tv_no_search_result);

        mListView = findViewById(R.id.recyclerview);
        setupSearchResultsListView(mListView);

        mRealm = Realm.getDefaultInstance();

        TestHistoryComponent component = DaggerTestHistoryComponent.builder()
                .testHistoryModule(new TestHistoryModule(this, mRealm))
                .build();
        component.inject(this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void unpackArguments() {
        Intent intent = getIntent();
        if (intent.hasExtra(Consts.KEY_TESTMODE)) {
            TestMode testMode = TestMode.fromInt(intent.getIntExtra(Consts.KEY_TESTMODE, TestMode.BloodTest.value));
            mIsQATest = (testMode == TestMode.QA);
        } else {
            // If mIsQATest cannot be determined in above, it defaults to false (Patient Test)
            mIsQATest = false;
        }
        mIsAdminUser = intent.hasExtra(Permissions.HOSTADMINISTRATOR.name());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mTestHistoryData == null) {
            mPresenter.load(mIsQATest); // initial data load
        } else {
            refresh();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
        mModel = null;
        mPresenter = null;
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.v(TAG, "search term = "+query);
            mIsSearchViewMenuExpanded = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test_history_main, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        setupSearchView(searchItem);
        return true;
    }

    /**
     * Process menu item select event.
     *
     * @param item -- menu item with select event.
     * @return true -- if item select event is consumed here.
     *         false - if item select event should be propagated.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // Handle Back btn pressed on ActionBar
            finish();
            return true;
        } else if (id == R.id.action_select) {
            if ((mTestHistoryData != null) && (mTestHistoryData.size() > 0)) {
                mPresenter.onSearchResultsSelectedForMultiselectMode(SelectionType.SELECT_NONE);
            }
            return true;
        } else if (id == R.id.action_select_all) {
            if ((mTestHistoryData != null) && (mTestHistoryData.size() > 0)) {
                mPresenter.onSearchResultsSelectedForMultiselectMode(SelectionType.SELECT_ALL);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called when the option menu has changed at run time.
     * Call invalidateOptionsMenu() where you want to update the options menu.
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (mIsAdminUser) {
            menu.findItem(R.id.action_select).setVisible(true);
            menu.findItem(R.id.action_select_all).setVisible(true);
        } else {
            menu.findItem(R.id.action_select).setVisible(false);
            menu.findItem(R.id.action_select_all).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    private void navigateToDetailsScreen(long id) {
        Intent intent = new Intent(TestHistoryMainActivity.this, TestHistoryDetailsActivity.class);
        intent.putExtra(TestHistoryDetailsActivity.KEY_TEST_ID, id);
        if (mIsAdminUser) {
            intent.putExtra(Permissions.HOSTADMINISTRATOR.name(), mIsAdminUser);
        }
        startActivity(intent);
    }

    private void showMultiSelectFragmentWithItemId(long id) {
        showMultiSelectFragment(SelectionType.SELECT_ONE_ITEM, id, THDateRange.NORANGE.value);
    }
    private void showMultiSelectFragmentWithDateRangeValue(int dateRangeValue) {
        showMultiSelectFragment(SelectionType.SELECT_DATE_RANGE, 0, dateRangeValue);
    }
    private void showMultiSelectFragment(SelectionType initialSelectionType, long initialItemId, int initialDateRangeEnumValue) {
        // Determine Grouped or not, and initial visible row position.
        boolean shouldGroupByDate = !mIsSearchViewMenuExpanded;
        int firstVisibleRowIndex = mListViewLayoutManager.findFirstVisibleItemPosition();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mMultiSelectListFragment = THMainMultiSelectListFragment
                .newInstance(
                        initialSelectionType.value,
                        initialItemId,
                        initialDateRangeEnumValue,
                        shouldGroupByDate,
                        firstVisibleRowIndex,
                        mCurrentPartialMatchString);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, mMultiSelectListFragment).addToBackStack(null).commit();
    }

    ///////////////////////////////////
    // RecyclerView for Search Results
    ///////////////////////////////////
    private void setupSearchResultsListView(RecyclerView listView) {
        mListViewLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(mListViewLayoutManager);
//        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        listView.addItemDecoration(decoration);
        mGroupedDataAdapter = new THGroupedDataAdapter(this, mTestHistoryData, mOnSearchResultSelected, mOnSearchResultGroupHeaderSelected);
        listView.setAdapter(mGroupedDataAdapter);
    }

    private ITestRecordListItemClickListener mOnSearchResultSelected = new ITestRecordListItemClickListener() {
        @Override
        public void onClickListItem(TestRecord record) {
            mPresenter.onTestSelected(record.getId());
        }

        @Override
        public void onLongClickListItem(TestRecord record) {
            // Selection Screen is only available for admin users.
            if (mIsAdminUser) {
                mPresenter.onTestSelectedForMultiselectMode(record.getId());
            }
        }
    };

    private ITestRecordListItemHeaderClickListener mOnSearchResultGroupHeaderSelected = new ITestRecordListItemHeaderClickListener() {
        @Override
        public void onLongClickHeaderListItem(int dateRangeValue) {
            // Selection Screen is only available for admin users.
            if (mIsAdminUser) {
                mPresenter.onHeaderSelectedForMultiselectMode(dateRangeValue);
            }
        }
    };

    ///////////////////////////////////
    // Dropdown Menu for Preset Filters
    ///////////////////////////////////
    /**
     * Hook up the dropdown menu (- display the preset filters -).
     * @param popupButton -- button to anchor the dropdown menu
     */
    private void setupDropdownMenu(final Button popupButton) {
        popupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildAndShowPopupMenuWindow(popupButton);
            }
        });
    }

    private List<PresetFilterData> getPresetFilterList() {
        if (mPresetFilterList == null) {
            mPresetFilterList = new ArrayList<>();
            int titleArrayResId = (mIsQATest) ? R.array.preset_filter_titles_qatest : R.array.preset_filter_titles;
            String[] presetFilterTitles = this.getResources().getStringArray(titleArrayResId);
            for (int i = 0; i < presetFilterTitles.length; i++) {
                // Initialize 1st item to be selected.
                mPresetFilterList.add(new PresetFilterData(presetFilterTitles[i], (i == 0), i));
            }
        }
        return mPresetFilterList;
    }

    private void buildAndShowPopupMenuWindow(View anchor) {
        PresetFilterDataAdapter adapter = new PresetFilterDataAdapter(this, getPresetFilterList());
        final ListPopupWindow popupWindow = new ListPopupWindow(this);
        popupWindow.setAnchorView(anchor);

        // Adjust the width of popup menu to 60% of the screen width (will fit the longest texts
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        popupWindow.setWidth((int)Math.round(displayMetrics.widthPixels*0.6));

        popupWindow.setAdapter(adapter);
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onPresetFilterChanged(position);
                popupWindow.dismiss();
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
        popupWindow.show();
    }

    private void onPresetFilterChanged(int index) {
        PresetFilterData selectedItem = null;
        // Update PresetFilterList
        for (int i = 0; i < mPresetFilterList.size(); i++) {
            PresetFilterData item = mPresetFilterList.get(i);
            if (i == index) {
                item.setSelected(true);
                mBtnPopupMenu.setText(item.getTitle());
                selectedItem = item;
            } else {
                item.setSelected(false);
            }
        }

        if (selectedItem != null) {

            switch (selectedItem.getFilterType()) {

                case PresetFilterData.FILTER_SENT:
                    mPresenter.loadBySyncState(true);
                    break;

                case PresetFilterData.FILTER_UNSENT:
                    mPresenter.loadBySyncState(false);
                    break;

                case PresetFilterData.FILTER_ALL:
                default:
                    mPresenter.load();
                    break;
            }
        }
    }

    ///////////////////////////////////
    // Search View in ActionBar
    ///////////////////////////////////
    private void setupSearchView(MenuItem searchItem) {

        SearchView searchView = (SearchView) searchItem.getActionView();
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }

        // Appearance - disable the text suggestion which removes the text underline.
        searchView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        // Update hint text
        if (mIsQATest) {
            searchView.setQueryHint(this.getResources().getText(R.string.search_hint_qa));
        }

        // Debounce
        io.reactivex.Observable<String> searchViewObservable = SearchViewTextObservable.fromView(searchView)
                .debounce(400, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        // Only emit when SearchView is active
                        return mIsSearchViewMenuExpanded;
                    }
                })
                .publish().autoConnect();
        mPresenter.observeSearchViewInput(searchViewObservable);

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                mIsSearchViewMenuExpanded = true;
                showNoSearchResult(); // Init search result panel to show no result.
                return true;  // Return true to expand action view
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                mIsSearchViewMenuExpanded = false;
                mPresenter.load();
                return true; // Return true to collapse action view
            }
        });
    }

    ///////////////////////////////
    // ITHMainFragmentListener
    ///////////////////////////////
    @Override
    public RealmResults<TestRecord> getTestRecords() {
        return mTestHistoryData;
    }

    @Override
    public void onFragmentClose() {
        getSupportFragmentManager().popBackStackImmediate();
        getSupportFragmentManager().beginTransaction().remove(mMultiSelectListFragment).commit();
        mMultiSelectListFragment = null;
        refresh();
    }

    @Override
    public void onActionSelect(ActionType actionType,
                               List<Long> idList,
                               Realm.Transaction.OnSuccess onSuccess,
                               Realm.Transaction.OnError onError)
    {
        switch (actionType) {
            case ACTION_MARK_SENT:
                mPresenter.actionSetSentAll(idList, onSuccess, onError);
                break;
            case ACTION_MARK_UNSENT:
                mPresenter.actionSetUnsentAll(idList, onSuccess, onError);
                break;
            case ACTION_DELETE:
                mPresenter.actionDeleteAll(idList, onSuccess, onError);
            default:
        }
    }

    ///////////////////////////////
    // THContract.View
    ///////////////////////////////

    @Override
    public void showNoSearchResult() {
        mNoSearchResultTextView.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
    }

    @Override
    public void showSearchResult(RealmResults<TestRecord> searchResult, String partialStringToMatch) {
        Log.v(TAG, String.format("Got %1$d Search Results ", (searchResult == null) ? 0 :searchResult.size()));
        mTestHistoryData = searchResult;
        mCurrentPartialMatchString = partialStringToMatch;
        if ((searchResult == null) || (searchResult.isEmpty())) {
            showNoSearchResult();
        } else {
            mNoSearchResultTextView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);

            // Redraw RecyclerView
            if (mIsSearchViewMenuExpanded) {
                THDataAdapter mDataAdapter = new THDataAdapter(this, mTestHistoryData, mOnSearchResultSelected, partialStringToMatch);
                mListView.setAdapter(mDataAdapter);
            } else {
                mGroupedDataAdapter = new THGroupedDataAdapter(this, mTestHistoryData, mOnSearchResultSelected, mOnSearchResultGroupHeaderSelected);
                mListView.setAdapter(mGroupedDataAdapter);
            }
        }
    }

    @Override
    public void showTestDetails(long id) {
        navigateToDetailsScreen(id);
    }

    @Override
    public void showMultiSelectSearchResult(long id) {
        showMultiSelectFragmentWithItemId(id);
    }

    @Override
    public void showMultiSelectSearchResultWithDateRange(int dateRangeValue) {
        showMultiSelectFragmentWithDateRangeValue(dateRangeValue);
    }

    @Override
    public void showMultiSelectSearchResultWithSelectionType(SelectionType selectionType) {
        showMultiSelectFragment(selectionType, 0, THDateRange.NORANGE.value);
    }

    private void refresh() {
        // NOTE: Refresh Adapter in case the object is deleted or status changed.
        showSearchResult(mTestHistoryData, mCurrentPartialMatchString);
    }

    // TODO: Remove Later
    private void setupFakeData() {
        if (mIsQATest) {
            new TestRecordModel().instrumentMockTestRecordTHQA();
        } else {
            new TestRecordModel().instrumentMockTestRecordTH();
        }
    }

}
