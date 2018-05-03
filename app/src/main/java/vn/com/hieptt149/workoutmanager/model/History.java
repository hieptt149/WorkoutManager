package vn.com.hieptt149.workoutmanager.model;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;

/**
 * Created by Administrator on 04/02/2018.
 */

public class History implements Serializable {

    private String userId;
    private String practiceDate;
    private int workoutTimes;
    private double currHeight, currWeight;
    private double caloriesBurn;

    public History() {
    }

    public History(int workoutTimes, double currHeight, double currWeight, double caloriesBurn) {
        this.workoutTimes = workoutTimes;
        this.currHeight = currHeight;
        this.currWeight = currWeight;
        this.caloriesBurn = caloriesBurn;
    }

    public History(String userId, int workoutTimes, double currHeight,
                   double currWeight, String practiceDate, double caloriesBurn) {
        this.userId = userId;
        this.workoutTimes = workoutTimes;
        this.currHeight = currHeight;
        this.currWeight = currWeight;
        this.practiceDate = practiceDate;
        this.caloriesBurn = caloriesBurn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    @PropertyName("calories_burn")
    public double getCaloriesBurn() {
        return caloriesBurn;
    }

    @PropertyName("calories_burn")
    public void setCaloriesBurn(double caloriesBurn) {
        this.caloriesBurn = caloriesBurn;
    }
}
