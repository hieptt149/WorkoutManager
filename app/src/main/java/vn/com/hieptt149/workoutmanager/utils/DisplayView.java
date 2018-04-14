package vn.com.hieptt149.workoutmanager.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.widget.Toast;

import vn.com.hieptt149.workoutmanager.R;

/**
 * Created by Administrator on 03/31/2018.
 */

public class DisplayView {
    private static ProgressDialog progressDialog;
    private static AlertDialog.Builder builder;

    public static void showAlertDialog(Context context, String msg, DialogInterface.OnClickListener positiveButtonOnClick, DialogInterface.OnClickListener negativeButtonOnClick) {
        builder = new AlertDialog.Builder(context);
        builder.setMessage(msg)
                .setPositiveButton(R.string.positive_btn, positiveButtonOnClick)
                .setNegativeButton(R.string.negative_btn, negativeButtonOnClick)
                .show();
    }

    public static void showProgressDialog(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    public static void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 125);
        return noOfColumns;
    }
}
