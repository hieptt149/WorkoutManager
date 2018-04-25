package vn.com.hieptt149.workoutmanager.workoutdetails.fragment;


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
import vn.com.hieptt149.workoutmanager.model.Timer;
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

    private void initAnimation() {
        animationTimer = new MyCountDownTimer(Long.MAX_VALUE, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                sbDuration.setProgress((int) animation);
                //Chạy tới cuối interval
                if (animation == 0 && currInterval <= lstTimer.size() - 1) {
                    animation = lstTimer.get(currInterval).getDuration();
                }
                //Chạy tới đầu interval
                if (animation == lstTimer.get(currInterval).getDuration()) {
                    if (currInterval == 0) {
                        ivNextExercise.setVisibility(View.VISIBLE);
                    }
                    if (currInterval == lstTimer.size() - 1) {
                        ivNextExercise.setVisibility(View.INVISIBLE);
                    }
                    sbDuration.setMax((int) lstTimer.get(currInterval).getDuration());
                    tvExerciseName.setText(lstTimer.get(currInterval).getExerciseName());
                }
                animation -= 100;
                //Chạy hết interval cuối cùng
                if (animation == 0 && currInterval == lstTimer.size() - 1) {
                    countDownTimer.cancel();
                    animationTimer.cancel();
                    refreshTimer();
                }
            }

            @Override
            public void onFinish() {

            }
        };
    }

    private static Status status;
    private static long exercisesDuration, restsDuration, timer, animation;
    private ArrayList<Timer> lstTimer;
    private static String workoutTitle;
    private static ArrayList<Exercise> lstExercise;

    @Override
    public void onStop() {
        super.onStop();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (animationTimer != null) {
            animationTimer.cancel();
        }
    }

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

                break;
            case R.id.tv_duration:
                if (status == Status.STOP) {
                    status = Status.START;
                    initCountDownTimer();
                    initAnimation();
                    countDownTimer.start();
                    animationTimer.start();
                } else if (status == Status.START) {
                    status = Status.PAUSE;
                    countDownTimer.pause();
                    animationTimer.pause();
                } else if (status == Status.PAUSE) {
                    status = Status.START;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            countDownTimer.resume();
                        }
                    }, animation - timer);
                    animationTimer.resume();
                }
                break;
        }
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
        createTimerList();
        refreshTimer();
        initCountDownTimer();
        initAnimation();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }

    private void initCountDownTimer() {
        countDownTimer = new MyCountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvDuration.setText(Formula.msTimeFormatter(timer));
                //Chạy tới cuối interval
                if (timer == 0 && currInterval < lstTimer.size() - 1) {
                    currInterval++;
                    timer = lstTimer.get(currInterval).getDuration();
                }
                timer -= 1000;
            }

            @Override
            public void onFinish() {

            }
        };
    }

    private enum Status {
        START, STOP, PAUSE
    }

    private void createTimerList() {
        lstTimer = new ArrayList<>();
        for (int i = 0; i < lstExercise.size(); i++) {
            if (i == 0) {
                lstTimer.add(0, new Timer(getString(R.string.ready), 3000));
                lstTimer.add(1, new Timer(lstExercise.get(0).getName(), exercisesDuration));
            } else {
                lstTimer.add(2 * i, new Timer(getString(R.string.next) + " " + lstExercise.get(i).getName(), restsDuration));
                lstTimer.add((2 * i) + 1, new Timer(lstExercise.get(i).getName(), exercisesDuration));
            }
        }
    }

    private void refreshTimer() {
        status = Status.STOP;
        currInterval = 0;
        countDownTimer = null;
        animationTimer = null;
        handler = new Handler();
        timer = lstTimer.get(currInterval).getDuration();
        animation = lstTimer.get(currInterval).getDuration();
        tvDuration.setText(Formula.msTimeFormatter(0));
        tvExerciseName.setText(workoutTitle);
        sbDuration.setProgress(0);
    }
}
