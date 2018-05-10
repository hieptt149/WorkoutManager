package vn.com.hieptt149.workoutmanager.workoutdetails.fragment;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.model.ConstantValue;
import vn.com.hieptt149.workoutmanager.model.Exercise;
import vn.com.hieptt149.workoutmanager.workoutdetails.AddWorkoutActivityIntf;

/**
 * Created by Administrator on 03/28/2018.
 */

public class ExerciseDetailsFragment extends Fragment {

    private AddWorkoutActivityIntf addWorkoutActivityIntf;
    private TextView tvAddWorkoutToolbarTitle, tvTotalExercise, tvTotalTime, tvClickToChoose, tvExerciseDescription, tvExerciseName;
    private ProgressBar pbCardio, pbStrength, pbMobility;
    private ImageView ivExercisePreview,ivStart, ivSave, ivDelete;
    private EditText edtWorkoutTitle;
    private LinearLayout lnWorkoutInfo, lnExerciseDetail, lnExercisesInfo;
    private RecyclerView rvPreviewSelectedExercise;
    private Button btnAddExercise;
//    private int imgRes;
    private byte[] bytes;
    private InputStream inputStream;
    private static Exercise selectedExercise;

    public static ExerciseDetailsFragment newInstance(Bundle bundle) {
        ExerciseDetailsFragment exerciseDetailsFragment = new ExerciseDetailsFragment();
        if (bundle != null) {
            selectedExercise = (Exercise) bundle.getSerializable(ConstantValue.SELECTED_EXERCISE);
        }
        return exerciseDetailsFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        addWorkoutActivityIntf = (AddWorkoutActivityIntf) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        showExerciseDetails();
    }

    private void showExerciseDetails() {
        tvAddWorkoutToolbarTitle.setText(selectedExercise.getName());
        lnWorkoutInfo.setVisibility(View.GONE);
        lnExercisesInfo.setVisibility(View.GONE);
        tvExerciseDescription.setVisibility(View.VISIBLE);
        btnAddExercise.setVisibility(View.GONE);
        tvClickToChoose.setVisibility(View.GONE);
        edtWorkoutTitle.setEnabled(false);
        ivExercisePreview.setTag(null);
        tvExerciseName.setText(selectedExercise.getName());
        pbCardio.setProgress(selectedExercise.getCardioRate());
        pbStrength.setProgress(selectedExercise.getStrengthRate());
        pbMobility.setProgress(selectedExercise.getMobilityRate());
//        imgRes = getResources().getIdentifier(selectedExercise.getPreview(),"drawable",getContext().getPackageName());
//        RequestOptions options = new RequestOptions();
//        options.error(R.drawable.no_image);
//        Glide.with(getContext())
//                .load(imgRes)
//                .apply(options)
//                .into(ivExercisePreview);
        tvExerciseDescription.setText(selectedExercise.getDescription());
    }

    private void initView(View view) {
        tvAddWorkoutToolbarTitle = getActivity().findViewById(R.id.tv_addworkout_toolbar_title);
        tvTotalExercise = view.findViewById(R.id.tv_total_exercise);
        tvTotalTime = view.findViewById(R.id.tv_total_time);
        tvExerciseName = view.findViewById(R.id.tv_exercise_name);
        pbCardio = view.findViewById(R.id.pb_cardio);
        pbStrength = view.findViewById(R.id.pb_strength);
        pbMobility = view.findViewById(R.id.pb_mobility);
        ivExercisePreview = view.findViewById(R.id.iv_exercise_preview);
        ivDelete = view.findViewById(R.id.iv_delete);
        ivSave = view.findViewById(R.id.iv_save);
        ivStart = view.findViewById(R.id.iv_start);
        tvClickToChoose = view.findViewById(R.id.tv_click_to_choose);
        edtWorkoutTitle = view.findViewById(R.id.edt_title);
        lnExercisesInfo = view.findViewById(R.id.ln_exercises_info);
        lnExerciseDetail = view.findViewById(R.id.ln_exercise_detail);
        lnWorkoutInfo = view.findViewById(R.id.ln_workout_info);
        rvPreviewSelectedExercise = view.findViewById(R.id.rv_preview_selected_exercise);
        tvExerciseDescription = view.findViewById(R.id.tv_exercise_description);
        btnAddExercise = view.findViewById(R.id.btn_add_exercise);
    }
}
