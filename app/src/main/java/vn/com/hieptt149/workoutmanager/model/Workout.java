package vn.com.hieptt149.workoutmanager.model;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 04/01/2018.
 */

public class Workout implements Serializable {

    private String id;
    private String userId;
    private String title;
    private String icon;
    private ArrayList<Exercise> lstUsersExercises;
    private int cardioRate;
    private int strengthRate;
    private int mobilityRate;

    public Workout() {
    }

    public Workout(String title, String icon, ArrayList<Exercise> lstUsersExercises, int cardioRate, int strengthRate, int mobilityRate) {
        this.title = title;
        this.icon = icon;
        this.lstUsersExercises = lstUsersExercises;
        this.cardioRate = cardioRate;
        this.strengthRate = strengthRate;
        this.mobilityRate = mobilityRate;
    }

    public Workout(String id, String userId, String title, String icon, ArrayList<Exercise> lstUsersExercises, int cardioRate, int strengthRate, int mobilityRate, double caloriesBurn) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.icon = icon;
        this.lstUsersExercises = lstUsersExercises;
        this.cardioRate = cardioRate;
        this.strengthRate = strengthRate;
        this.mobilityRate = mobilityRate;
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

    @PropertyName("title")
    public String getTitle() {
        return title;
    }

    @PropertyName("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @PropertyName("icon")
    public String getIcon() {
        return icon;
    }

    @PropertyName("icon")
    public void setIcon(String icon) {
        this.icon = icon;
    }

    @PropertyName("exercises")
    public ArrayList<Exercise> getLstUsersExercises() {
        return lstUsersExercises;
    }

    @PropertyName("exercises")
    public void setLstUsersExercises(ArrayList<Exercise> lstUsersExercises) {
        this.lstUsersExercises = lstUsersExercises;
    }

    @PropertyName("cardio_rate")
    public int getCardioRate() {
        return cardioRate;
    }

    @PropertyName("cardio_rate")
    public void setCardioRate(int cardioRate) {
        this.cardioRate = cardioRate;
    }

    @PropertyName("strength_rate")
    public int getStrengthRate() {
        return strengthRate;
    }

    @PropertyName("strength_rate")
    public void setStrengthRate(int strengthRate) {
        this.strengthRate = strengthRate;
    }

    @PropertyName("mobility_rate")
    public int getMobilityRate() {
        return mobilityRate;
    }

    @PropertyName("mobility_rate")
    public void setMobilityRate(int mobilityRate) {
        this.mobilityRate = mobilityRate;
    }
}
