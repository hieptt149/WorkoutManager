package vn.com.hieptt149.workoutmanager.model;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;

/**
 * Created by Administrator on 04/02/2018.
 */

public class History implements Serializable {

    public String id;
    public String userId;
    public String workoutId;
    @PropertyName("datetime")
    public long datetime;
    @PropertyName("time_workout")
    public long timeWorkout;

    public History() {
    }

    public History(String id, String userId, String workoutId, long datetime, long timeWorkout) {
        this.id = id;
        this.userId = userId;
        this.workoutId = workoutId;
        this.datetime = datetime;
        this.timeWorkout = timeWorkout;
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

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public long getTimeWorkout() {
        return timeWorkout;
    }

    public void setTimeWorkout(long timeWorkout) {
        this.timeWorkout = timeWorkout;
    }
}
