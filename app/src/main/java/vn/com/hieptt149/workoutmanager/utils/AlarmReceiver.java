package vn.com.hieptt149.workoutmanager.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import vn.com.hieptt149.workoutmanager.home.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {

    private SimpleDateFormat hhmmformat = new SimpleDateFormat("hh:mm a");

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {

                ReminderData reminderData = new ReminderData(context);

                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(hhmmformat.parse(reminderData.getTimeRemind()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                NotificationScheduler.setReminder(context, AlarmReceiver.class, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
                return;
            }
        }

        NotificationScheduler.showNotification(context, MainActivity.class, "You have 5 unwatched videos", "Watch them now?");

    }
}
