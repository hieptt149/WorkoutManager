package vn.com.hieptt149.workoutmanager.model;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;

/**
 * Created by Administrator on 04/01/2018.
 */

public class Workout implements Serializable{

    public int id;
    public int userId;
    @PropertyName("title")
    public String title;
    @PropertyName("icon")
    public int icon;
    @PropertyName("exercises")
    public String lstUsersExercises;
    @PropertyName("total_time")
    public int totalTime;
    @PropertyName("cadio_rate")
    public int cadioRate;
    @PropertyName("strength_rate")
    public int strength_rate;
    @PropertyName("mobility_rate")
    public int mobility_rate;

    public Workout() {
    }

    public Workout(int id, int userId, String title, int icon, String lstUsersExercises, int totalTime, int cadioRate, int strength_rate, int mobility_rate) {
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

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getLstUsersExercises() {
        return lstUsersExercises;
    }

    public void setLstUsersExercises(String lstUsersExercises) {
        this.lstUsersExercises = lstUsersExercises;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getCadioRate() {
        return cadioRate;
    }

    public void setCadioRate(int cadioRate) {
        this.cadioRate = cadioRate;
    }

    public int getStrength_rate() {
        return strength_rate;
    }

    public void setStrength_rate(int strength_rate) {
        this.strength_rate = strength_rate;
    }

    public int getMobility_rate() {
        return mobility_rate;
    }

    public void setMobility_rate(int mobility_rate) {
        this.mobility_rate = mobility_rate;
    }
}
