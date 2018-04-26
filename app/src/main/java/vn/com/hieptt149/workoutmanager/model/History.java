package vn.com.hieptt149.workoutmanager.model;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;

/**
 * Created by Administrator on 04/02/2018.
 */

public class History implements Serializable {

    private String userId, workoutId;
    private String practiceDate;
    private int workoutTimes;
    private double currHeight, currWeight;

    public History() {
    }

    public History(int workoutTimes, double currHeight, double currWeight) {
        this.workoutTimes = workoutTimes;
        this.currHeight = currHeight;
        this.currWeight = currWeight;
    }

    public History(String userId, String workoutId, int workoutTimes, double currHeight,
                   double currWeight, String practiceDate) {
        this.userId = userId;
        this.workoutId = workoutId;
        this.workoutTimes = workoutTimes;
        this.currHeight = currHeight;
        this.currWeight = currWeight;
        this.practiceDate = practiceDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(String workoutId) {
        this.workoutId = workoutId;
    }

    public String getPracticeDate() {
        return practiceDate;
    }

    public void setPracticeDate(String practiceDate) {
        this.practiceDate = practiceDate;
    }
    @PropertyName("workout_times")
    public int getWorkoutTimes() {
        return workoutTimes;
    }

    @PropertyName("workout_times")
    public void setWorkoutTimes(int workoutTimes) {
        this.workoutTimes = workoutTimes;
    }

    @PropertyName("curr_height")
    public double getCurrHeight() {
        return currHeight;
    }

    @PropertyName("curr_height")
    public void setCurrHeight(double currHeight) {
        this.currHeight = currHeight;
    }

    @PropertyName("curr_weight")
    public double getCurrWeight() {
        return currWeight;
    }

    @PropertyName("curr_weight")
    public void setCurrWeight(double currWeight) {
        this.currWeight = currWeight;
    }
}
