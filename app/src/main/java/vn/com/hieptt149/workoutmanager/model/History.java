package vn.com.hieptt149.workoutmanager.model;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;

/**
 * Created by Administrator on 04/02/2018.
 */

public class History implements Serializable {

    private String id, userId, workoutId;
    private double caloriesBurn, currHeight, currWeight;
    private long practiceDate, timePracticed;

    public History() {
    }

    public History(double caloriesBurn, double currHeight, double currWeight, long practiceDate, long timePracticed) {
        this.caloriesBurn = caloriesBurn;
        this.currHeight = currHeight;
        this.currWeight = currWeight;
        this.practiceDate = practiceDate;
        this.timePracticed = timePracticed;
    }

    public History(String id, String userId, String workoutId, double caloriesBurn, double currHeight,
                   double currWeight, long practiceDate, long timePracticed) {
        this.id = id;
        this.userId = userId;
        this.workoutId = workoutId;
        this.caloriesBurn = caloriesBurn;
        this.currHeight = currHeight;
        this.currWeight = currWeight;
        this.practiceDate = practiceDate;
        this.timePracticed = timePracticed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @PropertyName("calories_burn")
    public double getCaloriesBurn() {
        return caloriesBurn;
    }

    @PropertyName("calories_burn")
    public void setCaloriesBurn(double caloriesBurn) {
        this.caloriesBurn = caloriesBurn;
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

    @PropertyName("practice_date")
    public long getPracticeDate() {
        return practiceDate;
    }

    @PropertyName("practice_date")
    public void setPracticeDate(long practiceDate) {
        this.practiceDate = practiceDate;
    }

    @PropertyName("time_practiced")
    public long getTimePracticed() {
        return timePracticed;
    }

    @PropertyName("time_practiced")
    public void setTimePracticed(long timePracticed) {
        this.timePracticed = timePracticed;
    }
}
