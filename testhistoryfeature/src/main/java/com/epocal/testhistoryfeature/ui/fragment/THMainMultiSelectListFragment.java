package com.epocal.testhistoryfeature.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.epocal.common.realmentities.TestRecord;
import com.epocal.testhistoryfeature.R;
import com.epocal.testhistoryfeature.data.patienttestdata.IMultiSelectStateChangeListener;
import com.epocal.testhistoryfeature.data.patienttestdata.THMultiSelectDataAdapter;
import com.epocal.testhistoryfeature.data.patienttestdata.THMultiSelectGroupedDataAdapter;
import com.epocal.testhistoryfeature.data.type.ActionType;
import com.epocal.testhistoryfeature.data.type.SelectionType;
import com.epocal.testhistoryfeature.data.type.THDateRange;
import com.epocal.testhistoryfeature.ui.UIHelper;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;


/**
 * This class represents the selection screen of past test list.
 *
 * @since 2018-10-06
 *
 * <h4>Overview of Selection Screen</h4>
 * <p>
 * The user can select one or more rows of past Test in the list and perform the bulk actions on
 * selected tests.
 * </p>
 *
 * <h4>Selection methods</h4>
 * <ul>
 * <li>Select a single row by tapping the CheckBox in a row. Tap again to un-select the row.</li>
 * <li>If the list is grouped by the date range, selecting the header row selects all
 *     children rows in the group. Likewise, un-selecting the header row un-selects all children rows.
 *     <b>Note:</b> When the group contains the partial selection, the header row displays CheckBox with
 *     the minus icon indicating the indeterminate state. Tapping the minus icon will move
 *     the header to state to "Select" state to select all children rows.
 *     </li>
 * <li>Option menu provides the way to "Select All" / "Select None" for the entire list set.</li>
 * </ul>
 *
 * <h4>Options menu interactions</h4>
 * <ul>
 *    <li><b>Select All</b> -- select all rows in the list</li>
 *    <li><b>Select None</b> -- un-select all rows in the list</li>
 *    <li><b>Mark as Sent</b> -- change the sync-state (change to "Sent" state)</li>
 *    <li><b>Mark as Unsent</b> -- change the sync-state (change to "Unsent" state)</li>
 *    <li><b>Delete</b> -- delete the test(s) from the db. The confirmation dialog will pop-up
 *    to confirm the delete action.</li>
 * </ul>
 *
 * @see com.epocal.testhistoryfeature.ui.TestHistoryMainActivity
 *
 */
public class THMainMultiSelectListFragment extends Fragment implements IMultiSelectStateChangeListener {

    private static final String TAG = THMainMultiSelectListFragment.class.getSimpleName();
    private static final String INITIAL_SELECTION_TYPE = "initialSelectionType";
    private static final String INITIAL_SELECTION_ITEM_ID = "initialSelectionItemId";
    private static final String INITIAL_SELECTION_DATERANGE_TYPE = "initialSelectionDateRangeType";
    private static final String SHOULD_GROUP_BY_DATE = "shouldGroupByDate";
    private static final String FIRST_VISIBLE_ROW_INDEX = "firstVisibleRowIndex";
    private static final String SEARCH_TERM = "partialSearchTerm";
    private RealmResults<TestRecord> mDataSource;
    private ITHMainFragmentListener mCallback;
    private RecyclerView mListView;
    private boolean mShouldGroupByDate;
    private int mInitialFirstVisibleRowIndex;
    private TextView mTitleView;
    private ProgressBar mProgressBar;
    private View mProgressBarBackgroundView;
    private THMultiSelectGroupedDataAdapter mMultiSelectGroupedDataAdapter;
    private THMultiSelectDataAdapter mMultiSelectDataAdapter;
    private SelectionType mInitialSelectionType;
    private THDateRange mInitialDateRangeType;
    private long mInitialSelectionItemId;
    private List<Long> mCurrentSelectedItemIdList;
    private String mPartialStringMatch;
    private boolean mIsActionDeleteInProgress;

    private Realm.Transaction.OnError mOnErrorActionDelete = new Realm.Transaction.OnError() {
        @Override
        public void onError(Throwable error) {
            UIHelper.showActionErrorDialog(getActivity(), error.getMessage());
            mIsActionDeleteInProgress = false;
            showLoadingIcon(false);
        }
    };
    private Realm.Transaction.OnSuccess mOnSuccessActionGeneric = new Realm.Transaction.OnSuccess() {
        @Override
        public void onSuccess() {
            closeFragmentSelf();
        }
    };
    private Realm.Transaction.OnError mOnErrorActionGeneric = new Realm.Transaction.OnError() {
        @Override
        public void onError(Throwable error) {
            UIHelper.showActionErrorDialog(getActivity(), error.getMessage());
        }
    };

    private RealmChangeListener<RealmResults<TestRecord>> mDataSourceChangeListener = new RealmChangeListener<RealmResults<TestRecord>>() {
        @Override
        public void onChange(@NonNull RealmResults<TestRecord> element) {
            Log.d(TAG, "***** Realm Change Listner fired!");
            if (!mIsActionDeleteInProgress) {
                refresh();
            }
        }
    };
    DialogInterface.OnClickListener mActionDeleteConfirmationCallback = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mIsActionDeleteInProgress = true;
            showLoadingIcon(true);
            mCallback.onActionSelect(ActionType.ACTION_DELETE, mCurrentSelectedItemIdList, mOnSuccessActionGeneric, mOnErrorActionDelete);
        }
    };

    public THMainMultiSelectListFragment() {
    }

    /**
     * This method instanciates THMainMultiSelectListFragment with parameters to determine its initial
     * selection state.
     *
     * When initialSelectionTypeEnumValue is SELECT_DATE_RANGE - use initialDateRangeEnumValue
     * When initialSelectionTypeEnumValue is SELECT_ONE_ITEM - use initialItemId
     * When initialSelectionTypeEnumValue is SELECT_ALL or SELECT_NONE, ignore initialDateRangeEnumValue and initialItemId
     *
     * @param initialSelectionTypeEnumValue -- initial selection type
     * @param initialItemId -- initial selected item id. Valid only when SelectionType is SELECT_ONE_ITEM
     * @param initialDateRangeEnumValue -- initial selected date range. Valid only when SelectionType is SELECT_DATE_RANGE
     * @param shouldGroupByDate -- true = group search results by date range. false = no grouping.
     * @param firstVisibleRowIndex -- 1st visible row index of search results in Main screen.
     * @param partialStringMatch -- search string entered in Search view of Main screen.
     * @return -- new THMainMultiSelectListFragment object
     */
    public static THMainMultiSelectListFragment newInstance(
            int initialSelectionTypeEnumValue,
            long initialItemId,
            int initialDateRangeEnumValue,
            boolean shouldGroupByDate,
            int firstVisibleRowIndex,
            String partialStringMatch)
    {
        Bundle bundle = new Bundle();
        bundle.putInt(INITIAL_SELECTION_TYPE, initialSelectionTypeEnumValue);
        bundle.putLong(INITIAL_SELECTION_ITEM_ID, initialItemId);
        bundle.putInt(INITIAL_SELECTION_DATERANGE_TYPE, initialDateRangeEnumValue);
        bundle.putBoolean(SHOULD_GROUP_BY_DATE, shouldGroupByDate);
        bundle.putInt(FIRST_VISIBLE_ROW_INDEX, firstVisibleRowIndex);
        bundle.putString(SEARCH_TERM, partialStringMatch);

        THMainMultiSelectListFragment fragment = new THMainMultiSelectListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            int initialSelectionTypeEnumValue = bundle.getInt(INITIAL_SELECTION_TYPE, SelectionType.SELECT_NONE.value);
            mInitialSelectionType = SelectionType.fromInt(initialSelectionTypeEnumValue);
            int initialDateRangeEnumValue = bundle.getInt(INITIAL_SELECTION_DATERANGE_TYPE, THDateRange.NORANGE.value);
            mInitialDateRangeType = THDateRange.fromInt(initialDateRangeEnumValue);
            mInitialSelectionItemId = bundle.getLong(INITIAL_SELECTION_ITEM_ID);
            mShouldGroupByDate = bundle.getBoolean(SHOULD_GROUP_BY_DATE);
            mInitialFirstVisibleRowIndex = bundle.getInt(FIRST_VISIBLE_ROW_INDEX);
            mPartialStringMatch = bundle.getString(SEARCH_TERM);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // allow fragement to add options menu
        readBundle(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_multiselect, container, false);
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        mTitleView = rootView.findViewById(R.id.tv_toolbar_title);
        mProgressBar = rootView.findViewById(R.id.progressbar);
        mProgressBarBackgroundView = rootView.findViewById(R.id.progressbar_background);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        ImageButton closeButton = rootView.findViewById(R.id.close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFragmentSelf();
            }
        });

        mListView = rootView.findViewById(R.id.multiselect_recyclerview);
        setupMultiSelectListView(mListView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Load initial adapter data to display the content
        if (mCallback != null) {
            mDataSource = mCallback.getTestRecords();
            // TODO: Test Change Listener
            mDataSource.addChangeListener(mDataSourceChangeListener);
            refreshScreen();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDataSource.removeChangeListener(mDataSourceChangeListener);
        mDataSource = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (ITHMainFragmentListener) context;
        } catch (ClassCastException ex) {
            throw new ClassCastException(TAG + " Parent Activity must implement ITHMainFragmentListener interface.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mCallback != null) {
            mCallback = null;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_frag_multiselect_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // Handle menu select on fragment menu
        if (id == R.id.frag_action_select_all) {
            if (mShouldGroupByDate) {
                mMultiSelectGroupedDataAdapter.selectAll(true);
            } else {
                mMultiSelectDataAdapter.selectAll(true);
            }
        } else if (id == R.id.frag_action_select_none) {
            if (mShouldGroupByDate) {
                mMultiSelectGroupedDataAdapter.selectAll(false);
            } else {
                mMultiSelectDataAdapter.selectAll(false);
            }
        } else if (id == R.id.frag_action_mark_sent) {
            if (mCallback != null) {
                showProgressBarShort();
                mCallback.onActionSelect(ActionType.ACTION_MARK_SENT, mCurrentSelectedItemIdList, mOnSuccessActionGeneric, mOnErrorActionGeneric);
            }
        } else if (id == R.id.frag_action_mark_unsent) {
            if (mCallback != null) {
                showProgressBarShort();
                mCallback.onActionSelect(ActionType.ACTION_MARK_UNSENT, mCurrentSelectedItemIdList, mOnSuccessActionGeneric, mOnErrorActionGeneric);
            }
        } else if (id == R.id.frag_action_delete) {
            if (mCallback != null) {
                UIHelper.showActionDeleteConfirmationDialog(getActivity(), mCurrentSelectedItemIdList.size(), mActionDeleteConfirmationCallback);
            }
        }
        return true;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // Activitie's onPrepareOptionsMenu is called prior to this point.
        Log.v(TAG, "Fragment: onPrepareOptionsMenu");
        // Hide Parent activities menu
        menu.findItem(R.id.search).setVisible(false);
        menu.findItem(R.id.action_select).setVisible(false);
        menu.findItem(R.id.action_select_all).setVisible(false);

        // Set Show/Hide of SelectAll/SelectNone
        if (isNoneSelected()) {
            menu.findItem(R.id.frag_action_select_none).setVisible(false);
            menu.findItem(R.id.frag_action_select_all).setVisible(true);
        } else if (isAllSelected()) {
            menu.findItem(R.id.frag_action_select_none).setVisible(true);
            menu.findItem(R.id.frag_action_select_all).setVisible(false);
        } else {
            // Partially selected
            menu.findItem(R.id.frag_action_select_none).setVisible(true);
            menu.findItem(R.id.frag_action_select_all).setVisible(true);
        }

        // Show Action menu only if any items selected.
        if (isNoneSelected()) {
            menu.findItem(R.id.frag_action_delete).setVisible(false);
            menu.findItem(R.id.frag_action_mark_sent).setVisible(false);
            menu.findItem(R.id.frag_action_mark_unsent).setVisible(false);
        } else {
            menu.findItem(R.id.frag_action_delete).setVisible(true);
            menu.findItem(R.id.frag_action_mark_sent).setVisible(true);
            menu.findItem(R.id.frag_action_mark_unsent).setVisible(true);
        }
    }

    private void setupMultiSelectListView(RecyclerView listView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listView.setLayoutManager(layoutManager);
//        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
//        listView.addItemDecoration(decoration);
        // adapter data is not loaded at this point.
    }

    private void updateTitleView(int numberOfSelectedRows) {
        mTitleView.setText(String.format(getContext().getResources().getString(R.string.no_of_selected_items), numberOfSelectedRows));
    }

    private boolean isNoneSelected() {
        if (mCurrentSelectedItemIdList == null) {
            // List not initialized yet. Get initial selection status.
            return (mInitialSelectionType == SelectionType.SELECT_NONE);
        } else {
            // List is initialized. Get the size.
            return (mCurrentSelectedItemIdList.size() == 0);
        }
    }

    private boolean isAllSelected() {
        if (mCurrentSelectedItemIdList == null) {
            // List not initialized yet. Get initial selection status.
            return (mInitialSelectionType == SelectionType.SELECT_ALL);
        } else {
            // List is initialized. Get the size.
            int currentDataSize = (mDataSource == null) ? 0 : mDataSource.size();
            return (mCurrentSelectedItemIdList.size() == currentDataSize);
        }
    }

    // Refresh listView by notifying adapters. Do not use this when delete action.
    // Only update Adapters.
    private void refresh() {
        if (mShouldGroupByDate) {
            mMultiSelectGroupedDataAdapter.notifyDataSetChanged();
        } else {
            mMultiSelectDataAdapter.notifyDataSetChanged();
        }
    }

//    private void refreshScreenAfterDelete() {
//
//        mInitialSelectionType = SelectionType.SELECT_NONE;
//        RecyclerView.LayoutManager layoutManager = mListView.getLayoutManager();
//        if (layoutManager instanceof LinearLayoutManager) {
//            mInitialFirstVisibleRowIndex = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
//        } else {
//            mInitialFirstVisibleRowIndex = 0;
//        }
//        refreshScreen();
//    }

    private void closeFragmentSelf() {
        if (mCallback != null) {
            mCallback.onFragmentClose();
        }
    }


    /**
     * Refresh screen based on:
     *  - mDataSource
     *  - mInitialSelectionItemId
     *  - mInitialFirstVisibleRowIndex
     */
    private void refreshScreen() {
        showLoadingIcon(false);
        if (mShouldGroupByDate) {
            mMultiSelectGroupedDataAdapter = new THMultiSelectGroupedDataAdapter(
                    getActivity(),
                    mDataSource,
                    mInitialSelectionType,
                    mInitialSelectionItemId,
                    mInitialDateRangeType,
                    this);
            mListView.setAdapter(mMultiSelectGroupedDataAdapter);
            mCurrentSelectedItemIdList = mMultiSelectGroupedDataAdapter.getCurrentSelectedItemIdList();
        } else {
            mMultiSelectDataAdapter = new THMultiSelectDataAdapter(
                    getActivity(),
                    mDataSource,
                    mInitialSelectionType,
                    mInitialSelectionItemId,
                    this,
                    mPartialStringMatch);
            mListView.setAdapter(mMultiSelectDataAdapter);
            mCurrentSelectedItemIdList = mMultiSelectDataAdapter.getCurrentSelectedItemIdList();
        }
        if ((mInitialFirstVisibleRowIndex != RecyclerView.NO_POSITION) &&
                (mInitialFirstVisibleRowIndex > 0))
        {
            // Scroll to row
            mListView.scrollToPosition(mInitialFirstVisibleRowIndex);
        }
        updateTitleView(mCurrentSelectedItemIdList.size());
    }

    private void showProgressBarShort() {
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBarBackgroundView.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.GONE);
                mProgressBarBackgroundView.setVisibility(View.GONE);
            }
        }, 600);
    }

    private void showLoadingIcon(boolean shouldShow) {
        if (shouldShow) {
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBarBackgroundView.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mProgressBarBackgroundView.setVisibility(View.GONE);
        }
    }

    //////////////////////////////////
    // IMultiSelectStateChangeListener
    //////////////////////////////////
    @Override
    public void onSelectonStateChanged(List<Long> selectedItemIdList) {
        mCurrentSelectedItemIdList.clear();
        mCurrentSelectedItemIdList.addAll(selectedItemIdList);
        updateTitleView(selectedItemIdList.size());
        getActivity().invalidateOptionsMenu(); // Update SelectAll / SelectNone menu
    }
}
