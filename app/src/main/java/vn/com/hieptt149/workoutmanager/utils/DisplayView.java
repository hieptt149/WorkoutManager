package vn.com.hieptt149.workoutmanager.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

/**
 * Created by Administrator on 03/31/2018.
 */

public class DisplayView {
    private static ProgressDialog progressDialog;

    public static void showProgressDialog(Activity activity){
        progressDialog = new ProgressDialog(activity);
        progressDialog = new ProgressDialog(activity);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    public static void dismissProgressDialog(){
        progressDialog.dismiss();
    }

    public static void showToast(Activity activity, String msg){
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }
}
