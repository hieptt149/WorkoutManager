package vn.com.hieptt149.workoutmanager.model;

public class Timer {
    private String exerciseName;
    private String preview;
    private long duration;

    public Timer() {
    }

    public Timer(String exerciseName, String preview, long duration) {
        this.exerciseName = exerciseName;
        this.preview = preview;
        this.duration = duration;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
