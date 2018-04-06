package vn.com.hieptt149.workoutmanager.addworkout.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.adapter.ExercisePreviewAdapter;
import vn.com.hieptt149.workoutmanager.addworkout.AddWorkoutActivityIntf;
import vn.com.hieptt149.workoutmanager.model.ConstantValue;
import vn.com.hieptt149.workoutmanager.model.Exercise;
import vn.com.hieptt149.workoutmanager.model.Workout;
import vn.com.hieptt149.workoutmanager.utils.TimeFormatter;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkoutDetailsFragment extends Fragment implements View.OnClickListener, WorkoutDetailsFragmentIntf {

    private AddWorkoutActivityIntf addWorkoutActivityIntf;
    private TextView tvAddWorkoutToolbarTitle, tvTotalExercise, tvTotalTime, tvClickToChoose, tvExerciseDescription;
    private ProgressBar pbCardio, pbStrength, pbMobility;
    private ImageView ivChooseWorkoutIcon;
    private EditText edtWorkoutTitle;
    private LinearLayout lnExercisesInfo;
    private RecyclerView rvPreviewSelectedExercise;
    private ScrollView svDescriptionContainer;
    private RelativeLayout rlBtnAddExerciseContainer;
    private Button btnAddExercise;
    private ExercisePreviewAdapter exercisePreviewAdapter;
    private ArrayList<Exercise> lstSelectedExercise = new ArrayList<>();
    private long totalTime;
    private int totalExercise, cadioRate = 0, strengthRate = 0, mobilityRate = 0, practiceTime = 60000;

    private static Workout usersWorkoutDetails;
    private static String tag;

    public static WorkoutDetailsFragment newInstance(Bundle bundle) {
        WorkoutDetailsFragment workoutDetailsFragment = new WorkoutDetailsFragment();
        if (bundle != null) {
            workoutDetailsFragment.setArguments(bundle);
            usersWorkoutDetails = (Workout) bundle.getSerializable("workout");
            tag = bundle.getString("tag");
        }
        return workoutDetailsFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        addWorkoutActivityIntf = (AddWorkoutActivityIntf) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        //Trường hợp user xem chi tiết workout
        if (tag.equals(ConstantValue.WORKOUT_DETAILS)) {
            tvAddWorkoutToolbarTitle.setText(usersWorkoutDetails.getTitle());
            lstSelectedExercise.addAll(usersWorkoutDetails.getLstUsersExercises());
            showUsersWorkoutDetails();
        }
        //Trường hợp tạo mới workout
        else if (tag.equals(ConstantValue.ADD_WORKOUT)) {
            tvAddWorkoutToolbarTitle.setText(R.string.add_workout);
            //Nếu người dùng đã thêm exercise
            if (lstSelectedExercise.size() != 0) {
                showUsersWorkoutDetails();
            }
        }
        rvPreviewSelectedExercise.setHasFixedSize(true);
        rvPreviewSelectedExercise.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        exercisePreviewAdapter = new ExercisePreviewAdapter(getContext(), lstSelectedExercise, this);
        rvPreviewSelectedExercise.setAdapter(exercisePreviewAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_exercise:
                Bundle bundle = new Bundle();
                bundle.putSerializable("lstselectedexercise", lstSelectedExercise);
                addWorkoutActivityIntf.openFragment(AddExerciseFragment.newInstance(bundle), ConstantValue.ADD_EXERCISE);
                break;
        }
    }

    @Override
    public void onExerciseItemClick(Exercise selectedExercise) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("selectedexercise", selectedExercise);
        addWorkoutActivityIntf.openFragment(ExerciseDetailsFragment.newInstance(bundle), ConstantValue.EXERCISE_DETAILS);
    }

    /**
     * Hiển thị chi tiết workout
     */
    private void showUsersWorkoutDetails() {
        for (int i = 0; i < lstSelectedExercise.size(); i++) {
            totalTime += practiceTime;
            cadioRate += lstSelectedExercise.get(i).getCadioRate();
            strengthRate += lstSelectedExercise.get(i).getStrengthRate();
            mobilityRate += lstSelectedExercise.get(i).getMobilityRate();
        }
        totalExercise = lstSelectedExercise.size();
        cadioRate = cadioRate / (lstSelectedExercise.size());
        strengthRate = strengthRate / (lstSelectedExercise.size());
        mobilityRate = mobilityRate / (lstSelectedExercise.size());
        pbCardio.post(new Runnable() {
            @Override
            public void run() {
                pbCardio.setProgress(cadioRate);
                pbStrength.setProgress(strengthRate);
                pbMobility.setProgress(mobilityRate);
            }
        });
        tvTotalExercise.setText(totalExercise + " exercise(s)");
        tvTotalTime.setText(TimeFormatter.msTimeFormatter(totalTime));
        if (usersWorkoutDetails != null) {
            int imgRes = getResources().getIdentifier(usersWorkoutDetails.getIcon(), "drawable", getContext().getPackageName());
            ivChooseWorkoutIcon.setImageResource(imgRes);
            edtWorkoutTitle.setText(usersWorkoutDetails.getTitle());
        }
    }

    public void addSelectedExercise(Exercise exercise) {
        Log.d("add", exercise.toString());
        lstSelectedExercise.add(exercise);
    }

    public void removeSelectedExercise(Exercise exercise) {
        Log.d("remove", exercise.toString());
        int tmpId = -1;
        for (int i = 0; i < lstSelectedExercise.size(); i++) {
            if (lstSelectedExercise.get(i).getId() == exercise.getId()) {
                tmpId = i;
            }
        }
        lstSelectedExercise.remove(tmpId);
    }

    private void initView(View view) {
        tvAddWorkoutToolbarTitle = getActivity().findViewById(R.id.tv_addworkout_toolbar_title);
        tvTotalExercise = view.findViewById(R.id.tv_total_exercise);
        tvTotalTime = view.findViewById(R.id.tv_total_time);
        pbCardio = view.findViewById(R.id.pb_cardio);
        pbStrength = view.findViewById(R.id.pb_strength);
        pbMobility = view.findViewById(R.id.pb_mobility);
        ivChooseWorkoutIcon = view.findViewById(R.id.iv_choose_icon);
        tvClickToChoose = view.findViewById(R.id.tv_click_to_choose);
        edtWorkoutTitle = view.findViewById(R.id.edt_title);
        lnExercisesInfo = view.findViewById(R.id.ln_exercises_info);
        rvPreviewSelectedExercise = view.findViewById(R.id.rv_preview_selected_exercise);
        svDescriptionContainer = view.findViewById(R.id.sv_description_container);
        tvExerciseDescription = view.findViewById(R.id.tv_exercise_description);
        rlBtnAddExerciseContainer = view.findViewById(R.id.rl_btn_add_exercise_container);
        btnAddExercise = view.findViewById(R.id.btn_add_exercise);
        btnAddExercise.setOnClickListener(this);
    }
}
