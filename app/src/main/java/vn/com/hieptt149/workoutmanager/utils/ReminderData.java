package vn.com.hieptt149.workoutmanager.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import vn.com.hieptt149.workoutmanager.model.ConstantValue;

public class ReminderData {
    private SharedPreferences sharedPreferences;

    public ReminderData(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getTimeRemind() {
        return sharedPreferences.getString(ConstantValue.TIME_REMIND, "08:00");
    }
}
