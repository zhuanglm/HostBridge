package com.epocal.epoctestprocedure.fragments.dataentry;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.epocal.common.epocobjects.AnalyteOption;
import com.epocal.epoctestprocedure.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * The custom test selection dialog fragment
 * <p>
 * Created by Zeeshan A Zakaria on 1/4/2018.
 */

public class CustomTestSelectionDialog extends DialogFragment implements Serializable {

    VerticalStepper mParent;
    ArrayList<AnalyteOption> mAnalytes;
    boolean isCalculationDone;

    public void setParent(VerticalStepper parent) {
        mParent = parent;
    }

    public static CustomTestSelectionDialog newInstance(ArrayList<AnalyteOption> analyteOptions, boolean isCalculationDone) {
        CustomTestSelectionDialog dialog = new CustomTestSelectionDialog();
        dialog.sortAnalytes(analyteOptions);
        dialog.isCalculationDone = isCalculationDone;
        return dialog;
    }

    public void sortAnalytes(ArrayList<AnalyteOption> analyteOptions) {
        Collections.sort(analyteOptions, new Comparator<AnalyteOption>() {
            @Override
            public int compare(AnalyteOption o1, AnalyteOption o2) {
                if (o1.getDisplayOrder() > o2.getDisplayOrder()) return 1;
                if (o1.getDisplayOrder() < o2.getDisplayOrder()) return -1;
                return 0;
            }
        });

        mAnalytes = analyteOptions;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_custom_test_selection, null);
        ListView itemList = (ListView) view.findViewById(R.id.list_view_test_selection);
        View titleView = getActivity().getLayoutInflater().inflate(R.layout.dialog_custom_title, null);
        TextView title = (TextView) titleView.findViewById(R.id.custom_dialog_title);
        title.setText(R.string.custom_test_panels);

        final CustomTestSelectionAdapter adapter = new CustomTestSelectionAdapter(getActivity(), mAnalytes, isCalculationDone);
        itemList.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.TPAlertDialogCustom);
        builder
                .setCustomTitle(titleView)
                .setView(view)
                .setPositiveButton(getActivity().getString(R.string.okay), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mParent.setCustomTestInclusions(adapter.mEnumSet);
                            }
                        }
                )
                .setNegativeButton(getActivity().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dismiss();
                            }
                        }
                )
        ;

        return builder.create();
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mParent.onDialogDismissed(this);
    }

    @Override
    public void onResume() {
        bottomCenterPositionFragment();
        super.onResume();
    }

    private void bottomCenterPositionFragment() {
        // Note: Dialog's height is 840dp will bring the top of dialog just below MessagePanel.
        final Window dialogWindow = getDialog().getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        lp.height = 840;
        lp.dimAmount = 0.2f;  // 20% dim
        dialogWindow.setAttributes(lp);
    }
}