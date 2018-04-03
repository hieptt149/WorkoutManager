package vn.com.hieptt149.workoutmanager.model;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 04/01/2018.
 */

public class Workout implements Serializable{

    public int id;
    public int userId;
    @PropertyName("title")
    public String title;
    @PropertyName("icon")
    public String icon;
    @PropertyName("exercises")
    public ArrayList<Exercise> lstUsersExercises;
    @PropertyName("total_time")
    public long totalTime;
    @PropertyName("cadio_rate")
    public int cadioRate;
    @PropertyName("strength_rate")
    public int strength_rate;
    @PropertyName("mobility_rate")
    public int mobility_rate;

    public Workout() {
    }

    public Workout(int id, int userId, String title, String icon, ArrayList<Exercise> lstUsersExercises, long totalTime, int cadioRate, int strength_rate, int mobility_rate) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.icon = icon;
        this.lstUsersExercises = lstUsersExercises;
        this.totalTime = totalTime;
        this.cadioRate = cadioRate;
        this.strength_rate = strength_rate;
        this.mobility_rate = mobility_rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public ArrayList<Exercise> getLstUsersExercises() {
        return lstUsersExercises;
    }

    public void setLstUsersExercises(ArrayList<Exercise> lstUsersExercises) {
        this.lstUsersExercises = lstUsersExercises;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public int getCadioRate() {
        return cadioRate;
    }

    public void setCadioRate(int cadioRate) {
        this.cadioRate = cadioRate;
    }

    public int getStrengthRate() {
        return strength_rate;
    }

    public void setStrengthRate(int strength_rate) {
        this.strength_rate = strength_rate;
    }

    public int getMobilityRate() {
        return mobility_rate;
    }

    public void setMobilityRate(int mobility_rate) {
        this.mobility_rate = mobility_rate;
    }
}
