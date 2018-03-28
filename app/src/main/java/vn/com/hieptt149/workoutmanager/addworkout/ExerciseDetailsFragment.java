package vn.com.hieptt149.workoutmanager.addworkout;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
 * Created by Administrator on 03/28/2018.
 */

public class ExerciseDetailsFragment extends Fragment {

    private OpenDetailsFragmentCallback openDetailsFragmentCallback;
    private TextView tvAddWorkoutToolbarTitle, tvTotalExercise, tvTotalTime;
    private ProgressBar pbCardio, pbStrength, pbMobility;
    private ImageView ivChooseWorkoutIcon;
    private EditText edtWorkoutTitle;
    private RecyclerView rvPreviewSelectedExercise;
    private Button btnAddExercise;

    public static ExerciseDetailsFragment newInstance(){
        ExerciseDetailsFragment exerciseDetailsFragment = new ExerciseDetailsFragment();
        return exerciseDetailsFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        openDetailsFragmentCallback = (OpenDetailsFragmentCallback) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvAddWorkoutToolbarTitle = getActivity().findViewById(R.id.tv_addworkout_toolbar_title);
        tvTotalExercise = view.findViewById(R.id.tv_total_exercise);
        tvTotalTime = view.findViewById(R.id.tv_total_time);
        pbCardio = view.findViewById(R.id.pb_cardio);
        pbStrength = view.findViewById(R.id.pb_strength);
        pbMobility = view.findViewById(R.id.pb_mobility);
        ivChooseWorkoutIcon = view.findViewById(R.id.iv_choose_icon);
        edtWorkoutTitle = view.findViewById(R.id.edt_title);
        rvPreviewSelectedExercise = view.findViewById(R.id.rv_preview_selected_exercise);
        btnAddExercise = view.findViewById(R.id.btn_add_exercise);
//        tvAddWorkoutToolbarTitle.setText(R.string.add_workout);
    }
}
