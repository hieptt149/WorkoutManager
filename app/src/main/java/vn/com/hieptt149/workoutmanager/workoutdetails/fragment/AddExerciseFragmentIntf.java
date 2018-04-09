package vn.com.hieptt149.workoutmanager.workoutdetails.fragment;

import vn.com.hieptt149.workoutmanager.model.Exercise;

/**
 * Created by Administrator on 04/03/2018.
 */

public interface AddExerciseFragmentIntf {
    void onExerciseItemClick(Exercise selectedExercise);
    void addExercise(Exercise exercise);
    void removeExercise(Exercise exercise);
}
