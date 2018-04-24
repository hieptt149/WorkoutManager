package vn.com.hieptt149.workoutmanager.model;

public class Timer {
    private String exerciseName;
    private long duration;

    public Timer() {
    }

    public Timer(String exerciseName, long duration) {
        this.exerciseName = exerciseName;
        this.duration = duration;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
