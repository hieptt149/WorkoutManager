package vn.com.hieptt149.workoutmanager.workout;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.addworkout.AddWorkoutActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkoutFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton fabAddWorkout;

    public static WorkoutFragment newInstance() {
        WorkoutFragment workoutFragment = new WorkoutFragment();
        return workoutFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fabAddWorkout = view.findViewById(R.id.fab_addworkout);
        fabAddWorkout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(getActivity(), AddWorkoutActivity.class));
    }
}
