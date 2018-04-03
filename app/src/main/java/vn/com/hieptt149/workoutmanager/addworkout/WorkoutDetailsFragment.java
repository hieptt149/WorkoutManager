package vn.com.hieptt149.workoutmanager.addworkout;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.adapter.ExercisePreviewAdapter;
import vn.com.hieptt149.workoutmanager.model.Exercise;
import vn.com.hieptt149.workoutmanager.model.Workout;
import vn.com.hieptt149.workoutmanager.utils.TimeFormatter;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkoutDetailsFragment extends Fragment implements View.OnClickListener,WorkoutDetailsFragmentIntf {

    private AddWorkoutActivityIntf addWorkoutActivityIntf;
    private TextView tvAddWorkoutToolbarTitle, tvTotalExercise, tvTotalTime, tvClickToChoose,tvExerciseDescription;
    private ProgressBar pbCardio, pbStrength, pbMobility;
    private ImageView ivChooseWorkoutIcon;
    private EditText edtWorkoutTitle;
    private LinearLayout lnExercisesInfo;
    private RecyclerView rvPreviewSelectedExercise;
    private ScrollView svDescriptionContainer;
    private RelativeLayout rlBtnAddExerciseContainer;
    private Button btnAddExercise;
    private ExercisePreviewAdapter exercisePreviewAdapter;
    private static Workout usersWorkoutDetails;
    private ArrayList<Exercise> lstSelectedExercise;

    public static WorkoutDetailsFragment newInstance(Bundle bundle) {
        WorkoutDetailsFragment workoutDetailsFragment = new WorkoutDetailsFragment();
        if (bundle != null) {
            workoutDetailsFragment.setArguments(bundle);
            usersWorkoutDetails = (Workout) bundle.getSerializable("workout");
        } else {
            usersWorkoutDetails = null;
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
        lstSelectedExercise = new ArrayList<>();
        rvPreviewSelectedExercise.setHasFixedSize(true);
        rvPreviewSelectedExercise.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        //Trường hợp user xem chi tiết workout
        if (usersWorkoutDetails != null) {
            lstSelectedExercise.addAll(usersWorkoutDetails.getLstUsersExercises());
            showUsersWorkoutDetails();
        }
        //Trường hợp tạo mới workout
        else {
            lstSelectedExercise = null;
            tvClickToChoose.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_exercise:
                Bundle bundle = new Bundle();
                bundle.putSerializable("lstselectedexercise",lstSelectedExercise);
                addWorkoutActivityIntf.openFragment(AddExerciseFragment.newInstance(bundle), "addexercise");
                break;
        }
    }

    @Override
    public void onExerciseItemClick(Exercise selectedExercise) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("selectedexercise",selectedExercise);
        addWorkoutActivityIntf.openFragment(ExerciseDetailsFragment.newInstance(bundle), "exercisedetails");
    }

    /**
     * Hiển thị chi tiết workout
     */
    private void showUsersWorkoutDetails() {
        pbCardio.setProgress(usersWorkoutDetails.getCadioRate());
        pbStrength.setProgress(usersWorkoutDetails.getStrengthRate());
        pbMobility.setProgress(usersWorkoutDetails.getMobilityRate());
        int imgRes = getResources().getIdentifier(usersWorkoutDetails.getIcon(), "drawable", getContext().getPackageName());
        ivChooseWorkoutIcon.setImageResource(imgRes);
        edtWorkoutTitle.setText(usersWorkoutDetails.getTitle());
        tvTotalExercise.setText(usersWorkoutDetails.getLstUsersExercises().size() + " exercise(s)");
        tvTotalTime.setText(TimeFormatter.msTimeFormatter(usersWorkoutDetails.getTotalTime()));
        exercisePreviewAdapter = new ExercisePreviewAdapter(getContext(),lstSelectedExercise,this);
        rvPreviewSelectedExercise.setAdapter(exercisePreviewAdapter);
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
        tvAddWorkoutToolbarTitle.setText(R.string.add_workout);
    }
}
