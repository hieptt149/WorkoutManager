package vn.com.hieptt149.workoutmanager.workoutdetails.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
    private MyCountDownTimer countDownTimer, animationTimer;
    private Handler handler;

    private static boolean isStart, isWorkout;
    private static String workoutTitle;
    private static ArrayList<Exercise> lstExercise;
    private static long exercisesDuration, restsDuration, exerciseTimeCounted, restTimeCounted, exerciseAnimationTime, restAnimationTime;
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
//            case R.id.iv_previous_exercise:
//                countDownTimer.cancel();
//                if (isWorkout) {
//                    isWorkout = false;
//                } else {
//
//                }
//                break;
            case R.id.iv_next_exercise:
                countDownTimer.cancel();
                animationTimer.cancel();
                if (isWorkout) {
                    isWorkout = false;
                    exerciseTimeCounted = exercisesDuration;
                    exerciseAnimationTime = exercisesDuration;
                    currInterval++;
                    countDownTimer.start();
                    animationTimer.start();
                } else {
                    isWorkout = true;
                    restTimeCounted = restsDuration;
                    restAnimationTime = restsDuration;
                    if (currInterval == lstExercise.size()) {
                        ivNextExercise.setVisibility(View.INVISIBLE);
                    }
                    countDownTimer.start();
                    animationTimer.start();
                }
                tvExerciseName.setText(lstExercise.get(currInterval - 1).getName());
                break;
            case R.id.tv_duration:
                if (!isStart) {
                    isStart = true;
                    if (isWorkout) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                countDownTimer.start();
                            }
                        }, exerciseAnimationTime - exerciseTimeCounted);
                    } else {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                countDownTimer.start();
                            }
                        }, restAnimationTime - restTimeCounted);
                    }
                    animationTimer.start();
                } else {
                    isStart = false;
                    countDownTimer.pause();
                    animationTimer.pause();
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
        handler = new Handler();
        refreshTimer();
        initCountDownTimer();
    }

    private void refreshTimer() {
        isStart = false;
        isWorkout = true;
        exerciseTimeCounted = exercisesDuration;
        restTimeCounted = restsDuration;
        exerciseAnimationTime = exercisesDuration;
        restAnimationTime = restsDuration;
        currInterval = 1;
        sbDuration.setMax((int) exercisesDuration);
        tvExerciseName.setText(lstExercise.get(currInterval - 1).getName());
        ivNextExercise.setVisibility(View.VISIBLE);
    }

    private void initCountDownTimer() {
        countDownTimer = new MyCountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (isWorkout) {
                    if (currInterval == lstExercise.size()) {
                        ivNextExercise.setVisibility(View.INVISIBLE);
                    }
                    if (exerciseTimeCounted == exercisesDuration){
                        sbDuration.setMax((int) exercisesDuration);
                    }
                    tvDuration.setText(Formula.msTimeFormatter(exerciseTimeCounted));
                    exerciseTimeCounted -= 1000;
                    if (exerciseTimeCounted == 0) {
                        exerciseTimeCounted = exercisesDuration;
                        exerciseAnimationTime = exercisesDuration;
                        if (currInterval < lstExercise.size()) {
                            isWorkout = false;
                            currInterval++;
                        }
                        //Khi đồng hồ chạy đến giây 0 của bài tập cuối
                        else {
                            refreshTimer();
                            countDownTimer.cancel();
                            animationTimer.cancel();
                        }
                    }
                } else {
                    if (restTimeCounted == restsDuration){
                        sbDuration.setMax((int) restsDuration);
                    }
                    tvDuration.setText(Formula.msTimeFormatter(restTimeCounted));
                    restTimeCounted -= 1000;
                    tvExerciseName.setText(lstExercise.get(currInterval - 1).getName());
                    if (restTimeCounted == 0) {
                        restTimeCounted = restsDuration;
                        restAnimationTime = restsDuration;
                        isWorkout = true;
                    }
                }
            }

            @Override
            public void onFinish() {

            }
        };
        animationTimer = new MyCountDownTimer(Long.MAX_VALUE, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (isWorkout) {
                    sbDuration.setProgress((int) exerciseAnimationTime);
                    exerciseAnimationTime -= 100;
                } else {
                    sbDuration.setProgress((int) restAnimationTime);
                    restAnimationTime -= 100;
                }
            }

            @Override
            public void onFinish() {

            }
        };
    }
}
