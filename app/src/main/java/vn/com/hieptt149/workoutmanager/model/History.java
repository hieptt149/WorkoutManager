package vn.com.hieptt149.workoutmanager.model;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;

/**
 * Created by Administrator on 04/02/2018.
 */

public class History implements Serializable {

    private String id;
    private String userId;
    private String workoutId;
    private long datetime;
    private long timeWorkout;

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

    @PropertyName("datetime")
    public long getDatetime() {
        return datetime;
    }

    @PropertyName("datetime")
    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    @PropertyName("time_workout")
    public long getTimeWorkout() {
        return timeWorkout;
    }

    @PropertyName("time_workout")
    public void setTimeWorkout(long timeWorkout) {
        this.timeWorkout = timeWorkout;
    }
}
