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

import vn.com.hieptt149.workoutmanager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkoutDetailsFragment extends Fragment implements View.OnClickListener{

    private WorkoutDetailsOpenFragmentCallback workoutDetailsOpenFragmentCallback;
    private TextView tvAddWorkoutToolbarTitle, tvTotalExercise, tvTotalTime;
    private ProgressBar pbCardio, pbStrength, pbMobility;
    private ImageView ivChooseWorkoutIcon;
    private EditText edtWorkoutTitle;
    private RecyclerView rvSelectedExercise;
    private Button btnAddExercise;


    public static WorkoutDetailsFragment newInstance() {
        WorkoutDetailsFragment workoutDetailsFragment = new WorkoutDetailsFragment();
        return workoutDetailsFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        workoutDetailsOpenFragmentCallback = (WorkoutDetailsOpenFragmentCallback) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workout_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvAddWorkoutToolbarTitle = getActivity().findViewById(R.id.tv_addworkout_toolbar_title);
        tvTotalExercise = view.findViewById(R.id.tv_total_exercise);
        tvTotalTime = view.findViewById(R.id.tv_total_time);
        pbCardio = view.findViewById(R.id.pb_cardio);
        pbStrength = view.findViewById(R.id.pb_strength);
        pbMobility = view.findViewById(R.id.pb_mobility);
        ivChooseWorkoutIcon = view.findViewById(R.id.iv_choose_workout_icon);
        edtWorkoutTitle = view.findViewById(R.id.edt_workout_title);
        rvSelectedExercise = view.findViewById(R.id.rv_selected_exercise);
        btnAddExercise = view.findViewById(R.id.btn_add_exercise);
        btnAddExercise.setOnClickListener(this);
        tvAddWorkoutToolbarTitle.setText(R.string.add_workout);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add_exercise:
                workoutDetailsOpenFragmentCallback.openFragment(AddExerciseFragment.newInstance(),"addexercise");
                break;
        }
    }
}
