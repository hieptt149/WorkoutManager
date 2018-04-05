package vn.com.hieptt149.workoutmanager.addworkout;

import android.support.v4.app.Fragment;

import vn.com.hieptt149.workoutmanager.model.Exercise;

/**
 * Created by Administrator on 03/21/2018.
 */

public interface AddWorkoutActivityIntf {
    void openFragment(Fragment fragment,String tag);
    void addSelectedExercise(Exercise exercise);
    void removeSelectedExercise(Exercise exercise);
}
