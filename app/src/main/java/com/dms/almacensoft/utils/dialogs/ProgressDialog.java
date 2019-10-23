package com.dms.almacensoft.utils.dialogs;

import android.content.Context;
import android.os.Build;

/**
 * {@link ProgressDialog} permite llamar un ProgressDialog en cualquier clase del aplicativo pueda
 * pasar el contexto necesario, tal y como Activities y Fragments.
 */

public class ProgressDialog {

    private static android.app.ProgressDialog progressDialog;

    public static void show(Context context, String message) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        int style;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            style = android.R.style.Theme_Material_Light_Dialog;
        } else {
            style = android.app.ProgressDialog.THEME_HOLO_LIGHT;
        }
        progressDialog = new android.app.ProgressDialog(context, style);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void dismiss() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}