package vn.com.hieptt149.workoutmanager.workoutdetails.fragment;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.adapter.ExercisePreviewPagerAdapter;
import vn.com.hieptt149.workoutmanager.model.ConstantValue;
import vn.com.hieptt149.workoutmanager.model.Exercise;
import vn.com.hieptt149.workoutmanager.model.History;
import vn.com.hieptt149.workoutmanager.model.Timer;
import vn.com.hieptt149.workoutmanager.model.User;
import vn.com.hieptt149.workoutmanager.utils.CircularSeekBar;
import vn.com.hieptt149.workoutmanager.utils.Common;
import vn.com.hieptt149.workoutmanager.utils.CustomViewPager;
import vn.com.hieptt149.workoutmanager.utils.DisplayView;
import vn.com.hieptt149.workoutmanager.utils.MyCountDownTimer;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartWorkoutFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {

    private TextView tvAddWorkoutToolbarTitle, tvExerciseName, tvDuration;
    private ImageView ivPreviousExercise, ivNextExercise;
    private CustomViewPager vpExercisePreview;
    private CircularSeekBar sbDuration;
    private MyCountDownTimer countDownTimer;
    //            animationTimer;
    private Handler handler;
    private ArrayList<Timer> lstTimer;
    private DatabaseReference currUsersHistoryRef;
    private Calendar cal;
    private MediaPlayer mainAlarm, secondaryAlarm;
    private Vibrator vibrator;
    private AudioManager audioManager;
    private ArrayList<Fragment> lstExercisePreviewFragments;
    private FragmentPagerAdapter exercisePreviewPagerAdapter;
    private int gifRes;
    private double caloriesBurn;

    private enum Status {
        START, STOP, PAUSE
    }

    private static String workoutTitle;
    private static User currUser;
    private static Status status;
    private static long exercisesDuration, restsDuration, timer, animation;
    private static ArrayList<Exercise> lstExercise;
    private static int currInterval;
    private static ExercisePreviewFragment currExercisePreview;

    public StartWorkoutFragment() {
        // Required empty public constructor
    }

    public static StartWorkoutFragment newInstance(Bundle bundle) {
        StartWorkoutFragment startWorkoutFragment = new StartWorkoutFragment();
        if (bundle != null) {
            startWorkoutFragment.setArguments(bundle);
            currUser = (User) bundle.getSerializable(ConstantValue.CURRENT_USER);
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
            case R.id.iv_next_exercise:
                if (countDownTimer != null) {
                    countDownTimer.cancel();
//                    animationTimer.cancel();
                    currInterval++;
                    initCountDownTimer();
//                    initAnimation();
                    timer = lstTimer.get(currInterval).getDuration();
                    animation = lstTimer.get(currInterval).getDuration();
                    countDownTimer.start();
//                    animationTimer.start();
                }
                break;
            case R.id.tv_duration:
                if (status == Status.STOP) {
                    status = Status.START;
//                    initCountDownTimer();
//                    initAnimation();
                    currExercisePreview.playGifAnimation();
                    countDownTimer.start();
//                    animationTimer.start();
                } else if (status == Status.START) {
                    status = Status.PAUSE;
                    currExercisePreview.pauseGifAnimation();
                    countDownTimer.pause();
//                    animationTimer.pause();
                } else if (status == Status.PAUSE) {
                    status = Status.START;
                    currExercisePreview.playGifAnimation();
                    countDownTimer.resume();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            countDownTimer.resume();
//                        }
//                    }, animation - timer);
//                    animationTimer.resume();
                }
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
//        if (animationTimer != null) {
//            animationTimer.cancel();
//        }
    }

    private void init(View view) {
        tvAddWorkoutToolbarTitle = getActivity().findViewById(R.id.tv_addworkout_toolbar_title);
        vpExercisePreview = view.findViewById(R.id.vp_exercise_preview);
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
        vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        tvAddWorkoutToolbarTitle.setText(!workoutTitle.equals("") ? workoutTitle : getString(R.string.start_workout));
        createTimerList();
        createExercisePreviewList();
        refreshTimer();
        if (currUser != null) {
            calculateWorkoutsCaloriesBurned();
        }
        initCountDownTimer();
//        initAnimation();
    }

    /**
     * Khởi tạo timer
     */
    private void initCountDownTimer() {
        countDownTimer = new MyCountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvDuration.setText(Common.msTimeFormatter(timer));
                sbDuration.setProgress((int) timer);
                //Chạy tới cuối interval
                if (timer == 0 && currInterval < lstTimer.size() - 1) {
                    currInterval++;
                    if (audioManager.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
                        vibrator.vibrate(500);
                    } else {
                        mainAlarm.start();
                    }
                    timer = lstTimer.get(currInterval).getDuration();
                }
                if (timer < 3000 && timer >= 1000) {
                    if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
                        secondaryAlarm.start();
                    }
                }
                //Chạy tới đầu interval
                if (timer == lstTimer.get(currInterval).getDuration()) {
                    if (currInterval == 0) {
                        ivNextExercise.setVisibility(View.VISIBLE);
                    }
                    if (currInterval == lstTimer.size() - 1) {
                        ivNextExercise.setVisibility(View.INVISIBLE);
                    }
                    if (currInterval % 2 == 0 && currInterval != 0) {
                        currExercisePreview.pauseGifAnimation();
                        vpExercisePreview.setCurrentItem(currInterval / 2);
                        currExercisePreview = (ExercisePreviewFragment) exercisePreviewPagerAdapter.getItem(vpExercisePreview.getCurrentItem());
                        currExercisePreview.playGifAnimation();
                    }
                    if (currInterval != 0) {
                        sbDuration.setMax((int) lstTimer.get(currInterval).getDuration());
                    }
                    tvExerciseName.setText(lstTimer.get(currInterval).getExerciseName());
                }
                timer -= 1000;
                //Chạy hết interval cuối cùng
                if (timer < 0 && currInterval == lstTimer.size() - 1) {
                    countDownTimer.cancel();
                    mainAlarm.release();
                    secondaryAlarm.release();
                    if (currUser != null) {
                        createWorkoutHistory();
                    }
                    refreshTimer();
                }
            }

            @Override
            public void onFinish() {

            }
        };
    }

    /**
     * Khởi tạo animation cho timer
     */
//    private void initAnimation() {
//        animationTimer = new MyCountDownTimer(Long.MAX_VALUE, 100) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                sbDuration.setProgress((int) animation);
//                //Chạy tới cuối interval
//                if (animation == 0 && currInterval <= lstTimer.size() - 1) {
//                    animation = lstTimer.get(currInterval).getDuration();
//                }
//                //Chạy tới đầu interval
//                if (animation == lstTimer.get(currInterval).getDuration()) {
//                    if (currInterval == 0) {
//                        ivNextExercise.setVisibility(View.VISIBLE);
//                    }
//                    if (currInterval == lstTimer.size() - 1) {
//                        ivNextExercise.setVisibility(View.INVISIBLE);
//                    }
//                    if (currInterval % 2 == 0 && currInterval != 0) {
//                        currExercisePreview.pauseGifAnimation();
//                        vpExercisePreview.setCurrentItem(currInterval / 2);
//                        currExercisePreview = (ExercisePreviewFragment) exercisePreviewPagerAdapter.getItem(vpExercisePreview.getCurrentItem());
//                        currExercisePreview.playGifAnimation();
//                    }
//                    sbDuration.setMax((int) lstTimer.get(currInterval).getDuration());
//                    tvExerciseName.setText(lstTimer.get(currInterval).getExerciseName());
//                }
//                animation -= 100;
//                //Chạy hết interval cuối cùng
//                if (timer < 0 && animation == 0 && currInterval == lstTimer.size() - 1) {
//                    animationTimer.cancel();
//                    mainAlarm.release();
//                    secondaryAlarm.release();
//                    if (currUser != null) {
//                        createWorkoutHistory();
//                    }
//                    refreshTimer();
//                }
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//        };
//    }

    /**
     * Khởi tạo lại các giá trị của timer
     */
    private void refreshTimer() {
        status = Status.STOP;
        vpExercisePreview.setCurrentItem(0);
        currInterval = 0;
        countDownTimer = null;
//        animationTimer = null;
        handler = new Handler();
        mainAlarm = MediaPlayer.create(getActivity(), R.raw.main_alarm);
        secondaryAlarm = MediaPlayer.create(getActivity(), R.raw.secondary_alarm);
        currExercisePreview = (ExercisePreviewFragment) exercisePreviewPagerAdapter.getItem(vpExercisePreview.getCurrentItem());
        timer = lstTimer.get(currInterval).getDuration();
        animation = lstTimer.get(currInterval).getDuration();
        tvDuration.setText(Common.msTimeFormatter(0));
        tvExerciseName.setText(!workoutTitle.equals("") ? workoutTitle : getString(R.string.start_workout));
        sbDuration.setMax((int) lstTimer.get(currInterval).getDuration());
    }

    private void createTimerList() {
        lstTimer = new ArrayList<>();
        for (int i = 0; i < lstExercise.size(); i++) {
            if (i == 0) {
                lstTimer.add(0, new Timer(getString(R.string.ready), lstExercise.get(0).getPreview(), 3000));
                lstTimer.add(1, new Timer(lstExercise.get(0).getName(), lstExercise.get(0).getPreview(), exercisesDuration));
            } else {
                lstTimer.add(2 * i, new Timer(getString(R.string.next) + " " + lstExercise.get(i).getName(), lstExercise.get(i).getPreview(), restsDuration));
                lstTimer.add((2 * i) + 1, new Timer(lstExercise.get(i).getName(), lstExercise.get(i).getPreview(), exercisesDuration));
            }
        }
    }

    private void createExercisePreviewList() {
        lstExercisePreviewFragments = new ArrayList<>();
        for (Exercise exercise : lstExercise) {
            gifRes = getResources().getIdentifier(exercise.getPreview(), "drawable", getContext().getPackageName());
            Bundle bundle = new Bundle();
            bundle.putInt(ConstantValue.GIF_RESOURCE, gifRes);
            ExercisePreviewFragment exercisePreviewFragment = new ExercisePreviewFragment();
            exercisePreviewFragment.setArguments(bundle);
            lstExercisePreviewFragments.add(exercisePreviewFragment);
        }
        exercisePreviewPagerAdapter = new ExercisePreviewPagerAdapter(getChildFragmentManager(), lstExercisePreviewFragments);
        vpExercisePreview.setAdapter(exercisePreviewPagerAdapter);
    }

    /**
     * Thêm dữ liệu vào lịch sử workout
     */
    private void createWorkoutHistory() {
        cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.setTime(new Date());
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        currUsersHistoryRef = FirebaseDatabase.getInstance().getReference()
                .child(ConstantValue.HISTORY).child(currUser.getId()).child(String.valueOf(cal.getTimeInMillis()));
        currUsersHistoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    History workoutHistory = dataSnapshot.getValue(History.class);
                    workoutHistory.setWorkoutTimes(workoutHistory.getWorkoutTimes() + 1);
                    workoutHistory.setCurrHeight(currUser.getHeight());
                    workoutHistory.setCurrWeight(currUser.getWeight());
                    workoutHistory.setCaloriesBurn(workoutHistory.getCaloriesBurn() + caloriesBurn);
                    currUsersHistoryRef.setValue(workoutHistory);
                } else {
                    currUsersHistoryRef.setValue(new History(1, currUser.getHeight(), currUser.getWeight(), caloriesBurn));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                DisplayView.showToast(getContext(), "Can't save your history to server. Please check your connection and try again");
            }
        });
    }

    private void calculateWorkoutsCaloriesBurned() {
        if (lstExercise != null) {
            for (Exercise exercise : lstExercise) {
                caloriesBurn = caloriesBurn + Common.calculateCaloriesBurned(exercise.getMetsRate(), currUser.getWeight(), exercisesDuration);
            }
        }
    }
}
