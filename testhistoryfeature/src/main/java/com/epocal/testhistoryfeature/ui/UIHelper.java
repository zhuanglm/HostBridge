package com.epocal.testhistoryfeature.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.epocal.testhistoryfeature.R;

/**
 * This class contains static utility methods called from the multiple locations.
 *
 * @since 2018-10-06
 */
public class UIHelper {

    public static void showActionErrorDialog(Context context, String errorMessage) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setTitle(R.string.error_action_title);
        alertBuilder.setMessage(R.string.error_action_delete);
        alertBuilder.setPositiveButton(R.string.okay, null);
        alertBuilder.show();
    }

    public static void showActionDeleteConfirmationDialog(Context context, int numSelected, DialogInterface.OnClickListener callback) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
        alertBuilder.setTitle(R.string.delete);
        alertBuilder.setMessage(context.getResources().getString(R.string.action_delete_confirmation, numSelected));
        alertBuilder.setPositiveButton(R.string.okay, callback);
        alertBuilder.setNegativeButton(R.string.cancel, null);
        alertBuilder.show();
    }
}
