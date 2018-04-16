package vn.com.hieptt149.workoutmanager.workoutdetails.fragment;


import android.content.SharedPreferences;
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
import vn.com.hieptt149.workoutmanager.utils.Formula;
import vn.com.hieptt149.workoutmanager.utils.MyCountDownTimer;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartWorkoutFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {

    private TextView tvAddWorkoutToolbarTitle, tvExerciseName, tvDuration;
    private ImageView ivExercisePreview, ivPreviousExercise, ivNextExercise;
    private CircularSeekBar sbDuration;
    private MyCountDownTimer countDownTimer;
    private boolean isStart, isWorkout = true;
    private long exerciseTimeCounted, restTimeCounted;

    private static String workoutTitle;
    private static ArrayList<Exercise> lstExercise;
    private static long totalTime, exercisesDuration, restsDuration;
    private static int currInterval;

    public StartWorkoutFragment() {
        // Required empty public constructor
    }

    public static StartWorkoutFragment newInstance(Bundle bundle) {
        StartWorkoutFragment startWorkoutFragment = new StartWorkoutFragment();
        if (bundle != null) {
            startWorkoutFragment.setArguments(bundle);
            workoutTitle = bundle.getString(ConstantValue.WORKOUT_TITLE);
            lstExercise = (ArrayList<Exercise>) bundle.getSerializable(ConstantValue.SELECTED_EXERCISE_LIST);
            totalTime = bundle.getLong(ConstantValue.TOTAL_TIME);
            exercisesDuration = bundle.getLong(ConstantValue.EXERCISES_DURATION);
            restsDuration = bundle.getLong(ConstantValue.RESTS_DURATION);
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
        init(view);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_previous_exercise:
                break;
            case R.id.iv_next_exercise:
                break;
            case R.id.tv_duration:
                if (!isStart) {
                    isStart = true;
                    countDownTimer.start();
                } else {
                    isStart = false;
                    countDownTimer.pause();
                }
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }

    private void init(View view) {
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
        exerciseTimeCounted = exercisesDuration;
        restTimeCounted = restsDuration;
        currInterval = 1;
        sbDuration.setMax((int) exercisesDuration);
        tvDuration.setText(Formula.msTimeFormatter(exerciseTimeCounted));
        countDownTimer = new MyCountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (isWorkout) {
                    exerciseTimeCounted -= 1000;
                    tvDuration.setText(Formula.msTimeFormatter(exerciseTimeCounted));
                    tvExerciseName.setText(lstExercise.get(currInterval - 1).getName());
                    if (exerciseTimeCounted == 0) {
                        if (currInterval < lstExercise.size()) {
                            currInterval++;
                        } else {
                            countDownTimer.cancel();
                        }
                        exerciseTimeCounted = exercisesDuration;
                        isWorkout = false;
                        tvDuration.setText(Formula.msTimeFormatter(restTimeCounted));
                    }
                } else {
                    restTimeCounted -= 1000;
                    tvDuration.setText(Formula.msTimeFormatter(restTimeCounted));
                    tvExerciseName.setText(lstExercise.get(currInterval - 1).getName());
                    if (restTimeCounted == 0) {
                        restTimeCounted = restsDuration;
                        isWorkout = false;
                        tvDuration.setText(Formula.msTimeFormatter(exerciseTimeCounted));
                    }
                }
            }

            @Override
            public void onFinish() {

            }
        };
    }
}
