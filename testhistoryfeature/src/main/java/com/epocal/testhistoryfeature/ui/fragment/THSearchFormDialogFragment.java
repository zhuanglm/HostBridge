package com.epocal.testhistoryfeature.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.epocal.testhistoryfeature.R;
import com.epocal.testhistoryfeature.search.THSearchFilters;

import java.util.Map;

/**
 * This class displayed an advanced search form in the dialog.
 *
 * @see THSearchFormAdapter
 */
public class THSearchFormDialogFragment extends DialogFragment {
    private static final String ARG_SEARCH_FILTER = "ARG_SEARCH_FILTER";
    THSearchFilters mSearchFilters;
    ITHSearchFormDialogListener mListener;
    THSearchFormAdapter mSearchFilterDataAdapter;

    /**
     * Mandatory empty constructor to instanciate the fragment.
     */
    public THSearchFormDialogFragment() {
    }

    /**
     * Create a new SearchForm with given searchFilters (searchCriterion).
     * @param searchFilters -- list of search criteria
     * @return -- dialogFragment displays the advanced search filter panel.
     */
    public static THSearchFormDialogFragment newInstance(THSearchFilters searchFilters) {
        THSearchFormDialogFragment fragment = new THSearchFormDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SEARCH_FILTER, searchFilters);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mSearchFilters = getArguments().getParcelable(ARG_SEARCH_FILTER);
        }
    }

    public void setListener(ITHSearchFormDialogListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.form_search_title));
        builder.setPositiveButton(context.getResources().getString(R.string.okay), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //callback
                if (mListener != null) {
                    mListener.searchFilterUpdated(mSearchFilterDataAdapter.getFilters());
                }
            }
        });
        builder.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //callback
            }
        });
        builder.setView(createListView());
        return builder.create();
    }

    private View createListView() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.list_filter, null, false);
        RecyclerView listView = view.findViewById(R.id.rv_filter_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listView.setLayoutManager(layoutManager);
        mSearchFilterDataAdapter = new THSearchFormAdapter(mSearchFilters.getFilters(), getActivity());
        listView.setAdapter(mSearchFilterDataAdapter);
        // Below is needed to display softkeyboard in editText in listView in dialog.
        listView.post(new Runnable() {
            @Override
            public void run() {
                THSearchFormDialogFragment.this.getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            }
        });
        return view;
    }

    /**
     * The caller of THSearchFormDialogFragment must implement below callback interface.
     */
    interface ITHSearchFormDialogListener {
        void searchFilterUpdated(Map<String, String> filters);
    }
}
