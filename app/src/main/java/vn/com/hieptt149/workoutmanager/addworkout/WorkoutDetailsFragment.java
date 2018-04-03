package vn.com.hieptt149.workoutmanager.addworkout;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.model.Exercise;
import vn.com.hieptt149.workoutmanager.model.Workout;
import vn.com.hieptt149.workoutmanager.utils.TimeFormatter;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkoutDetailsFragment extends Fragment implements View.OnClickListener {

    private AddWorkoutActivityIntf addWorkoutActivityIntf;
    private TextView tvAddWorkoutToolbarTitle, tvTotalExercise, tvTotalTime, tvClickToChoose;
    private ProgressBar pbCardio, pbStrength, pbMobility;
    private ImageView ivChooseWorkoutIcon;
    private EditText edtWorkoutTitle;
    private RecyclerView rvPreviewSelectedExercise;
    private Button btnAddExercise;
    private static Workout usersWorkoutDetails;


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
        if (usersWorkoutDetails != null) {
            showUsersWorkoutDetails();
        } else {

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_exercise:
                addWorkoutActivityIntf.openFragment(AddExerciseFragment.newInstance(), "addexercise");
                break;
        }
    }

    private void showUsersWorkoutDetails() {
        pbCardio.setProgress(usersWorkoutDetails.getCadioRate());
        pbStrength.setProgress(usersWorkoutDetails.getStrengthRate());
        pbMobility.setProgress(usersWorkoutDetails.getMobilityRate());
        int imgRes = getResources().getIdentifier(usersWorkoutDetails.getIcon(), "drawable", getContext().getPackageName());
        ivChooseWorkoutIcon.setImageResource(imgRes);
        edtWorkoutTitle.setText(usersWorkoutDetails.getTitle());
        tvTotalExercise.setText(usersWorkoutDetails.getLstUsersExercises().size() + "");
        tvTotalTime.setText(TimeFormatter.msTimeFormatter(usersWorkoutDetails.getTotalTime()));
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
        rvPreviewSelectedExercise = view.findViewById(R.id.rv_preview_selected_exercise);
        btnAddExercise = view.findViewById(R.id.btn_add_exercise);
        btnAddExercise.setOnClickListener(this);
        tvAddWorkoutToolbarTitle.setText(R.string.add_workout);
    }
}
