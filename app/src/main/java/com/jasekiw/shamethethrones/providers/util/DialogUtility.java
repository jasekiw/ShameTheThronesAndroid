package com.jasekiw.shamethethrones.providers.util;

import android.app.AlertDialog;
import android.content.Context;

public class DialogUtility {
    /**
     * Shows an error dialog with the specified message
     * @param context
     * @param message
     */
    public static void showErrorDialog(Context context, String message) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle(message);
        builderSingle.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
        builderSingle.show();
    }
}
