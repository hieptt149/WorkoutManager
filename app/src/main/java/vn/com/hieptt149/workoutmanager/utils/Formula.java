package vn.com.hieptt149.workoutmanager.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 04/03/2018.
 */

public class Formula {

    /**
     * Tính lượng calo cần đốt cháy để duy trì cân năng hiện tại
     *
     * @param gender: giới tính
     * @param age:    tuổi
     * @param height: chiều cao (cm)
     * @param weight: cân nặng (kg)
     * @return (cal)
     */
    public static double calculateCaloriesBurnADay(boolean gender, int age, double height, double weight) {
        double bmr;
        if (gender) {
            bmr = 66 + (6.2 * weight * 2.20462262) + (12.7 * height * 0.032808399) - (6.76 * age);
        } else {
            bmr = 655.1 + (4.35 * weight * 2.20462262) + (4.7 * height * 3.2808399) - (4.7 * age);
        }
        return (bmr * 2.5);
    }

    /**
     * Tính lượng calo của bài tập
     *
     * @param metsRate: chỉ số METS
     * @param weight:   chiều cao của người tập (cm)
     * @param duration: thời gian tập (millisec)
     * @return (cal)
     */
    public static double calculateCaloriesBurned(double metsRate, double weight, long duration) {
        return (metsRate * weight * duration * 1000.0) / 3600000.0;
    }

    /**
     * Quy đổi thời gian thành định dạng mm:ss
     *
     * @param milliseconds: thời gian quy đổi (millisec)
     * @return
     */
    public static String msTimeFormatter(long milliseconds) {
        String ms = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
        return ms;
    }

    /**
     * Quy đổi thời gian thành định dang hh:mm:ss
     *
     * @param milliseconds: thời gian quy đổi (millisec)
     * @return
     */
    public static String hmsTimeFormatter(long milliseconds) {
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(milliseconds),
                TimeUnit.MILLISECONDS.toMinutes(milliseconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
        return hms;
    }

    /**
     * Ẩn keyboard
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
