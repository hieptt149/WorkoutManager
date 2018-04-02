package vn.com.hieptt149.workoutmanager.model;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;

/**
 * Created by Administrator on 04/02/2018.
 */

public class History implements Serializable {

    public int id;
    public int userId;
    public int workoutId;
    @PropertyName("datetime")
    public long datetime;
    @PropertyName("time_workout")
    public int timeWorkout;

    public History() {
    }

    public History(int id, int userId, int workoutId, long datetime, int timeWorkout) {
        this.id = id;
        this.userId = userId;
        this.workoutId = workoutId;
        this.datetime = datetime;
        this.timeWorkout = timeWorkout;
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

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public int getTimeWorkout() {
        return timeWorkout;
    }

    public void setTimeWorkout(int timeWorkout) {
        this.timeWorkout = timeWorkout;
    }
}
