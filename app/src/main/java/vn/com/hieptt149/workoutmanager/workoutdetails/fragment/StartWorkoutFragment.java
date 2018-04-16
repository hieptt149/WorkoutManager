package vn.com.hieptt149.workoutmanager.workoutdetails.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.model.ConstantValue;
import vn.com.hieptt149.workoutmanager.model.Exercise;
import vn.com.hieptt149.workoutmanager.utils.CircularSeekBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartWorkoutFragment extends Fragment implements View.OnClickListener,View.OnTouchListener {

    private TextView tvAddWorkoutToolbarTitle, tvExerciseName, tvDuration;
    private ImageView ivExercisePreview, ivPreviousExercise, ivNextExercise;
    private CircularSeekBar sbDuration;

    private static String workoutTitle;
    private static ArrayList<Exercise> lstExercise;

    public StartWorkoutFragment() {
        // Required empty public constructor
    }

    public static StartWorkoutFragment newInstance(Bundle bundle) {
        StartWorkoutFragment startWorkoutFragment = new StartWorkoutFragment();
        if (bundle != null) {
            startWorkoutFragment.setArguments(bundle);
            workoutTitle = bundle.getString(ConstantValue.WORKOUT_TITLE);
            lstExercise = (ArrayList<Exercise>) bundle.getSerializable(ConstantValue.SELECTED_EXERCISE_LIST);
        }
        return startWorkoutFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_workout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_previous_exercise:
                break;
            case R.id.iv_next_exercise:
                break;
            case R.id.tv_duration:
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }

    private void initView(View view) {
        tvAddWorkoutToolbarTitle = getActivity().findViewById(R.id.tv_addworkout_toolbar_title);
        ivExercisePreview = view.findViewById(R.id.iv_exercise_preview);
        tvExerciseName = view.findViewById(R.id.tv_exercise_name);
        ivPreviousExercise = view.findViewById(R.id.iv_previous_exercise);
        sbDuration = view.findViewById(R.id.sb_duration);
        tvDuration = view.findViewById(R.id.tv_duration);
        ivNextExercise = view.findViewById(R.id.iv_next_exercise);
        tvDuration.setOnClickListener(this);
        ivPreviousExercise.setOnClickListener(this);
        ivNextExercise.setOnClickListener(this);
        sbDuration.setOnTouchListener(this);
    }
}
