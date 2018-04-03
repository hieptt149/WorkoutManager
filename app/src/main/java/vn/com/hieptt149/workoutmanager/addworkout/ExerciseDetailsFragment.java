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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.model.Exercise;

/**
 * Created by Administrator on 03/28/2018.
 */

public class ExerciseDetailsFragment extends Fragment {

    private AddWorkoutActivityIntf addWorkoutActivityIntf;
    private TextView tvAddWorkoutToolbarTitle, tvTotalExercise, tvTotalTime, tvClickToChoose, tvExerciseDescription;
    private ProgressBar pbCardio, pbStrength, pbMobility;
    private ImageView ivExercisePreview;
    private EditText edtWorkoutTitle;
    private LinearLayout lnExercisesInfo;
    private RecyclerView rvPreviewSelectedExercise;
    private ScrollView svDescriptionContainer;
    private RelativeLayout rlBtnAddExerciseContainer;
    private Button btnAddExercise;
    private static Exercise selectedExercise;

    public static ExerciseDetailsFragment newInstance(Bundle bundle) {
        ExerciseDetailsFragment exerciseDetailsFragment = new ExerciseDetailsFragment();
        if (bundle != null){
            selectedExercise = (Exercise) bundle.getSerializable("selectedexercise");
        } else {
            selectedExercise = null;
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
        lnExercisesInfo.setVisibility(View.GONE);
        svDescriptionContainer.setVisibility(View.VISIBLE);
        edtWorkoutTitle.setVisibility(View.GONE);
        rlBtnAddExerciseContainer.setVisibility(View.GONE);
        tvAddWorkoutToolbarTitle.setText(selectedExercise.getName());
        pbCardio.setProgress(selectedExercise.getCadioRate());
        pbStrength.setProgress(selectedExercise.getStrengthRate());
        pbMobility.setProgress(selectedExercise.getMobilityRate());
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.no_image)
                .error(R.drawable.no_connection);
        Glide.with(getContext())
                .load(selectedExercise.getUrl())
                .apply(options)
                .into(ivExercisePreview);
        tvExerciseDescription.setText(selectedExercise.getDescription());
    }

    private void initView(View view) {
        tvAddWorkoutToolbarTitle = getActivity().findViewById(R.id.tv_addworkout_toolbar_title);
        tvTotalExercise = view.findViewById(R.id.tv_total_exercise);
        tvTotalTime = view.findViewById(R.id.tv_total_time);
        pbCardio = view.findViewById(R.id.pb_cardio);
        pbStrength = view.findViewById(R.id.pb_strength);
        pbMobility = view.findViewById(R.id.pb_mobility);
        ivExercisePreview = view.findViewById(R.id.iv_choose_icon);
        tvClickToChoose = view.findViewById(R.id.tv_click_to_choose);
        edtWorkoutTitle = view.findViewById(R.id.edt_title);
        lnExercisesInfo = view.findViewById(R.id.ln_exercises_info);
        rvPreviewSelectedExercise = view.findViewById(R.id.rv_preview_selected_exercise);
        svDescriptionContainer = view.findViewById(R.id.sv_description_container);
        tvExerciseDescription = view.findViewById(R.id.tv_exercise_description);
        rlBtnAddExerciseContainer = view.findViewById(R.id.rl_btn_add_exercise_container);
        btnAddExercise = view.findViewById(R.id.btn_add_exercise);
    }
}
