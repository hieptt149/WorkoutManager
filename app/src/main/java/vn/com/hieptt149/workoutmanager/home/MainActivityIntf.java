package vn.com.hieptt149.workoutmanager.home;


import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 04/02/2018.
 */

public interface MainActivityIntf {
    void showDialogFragment(Fragment targetFragment, DialogFragment dialogFragment, String tag);
}
