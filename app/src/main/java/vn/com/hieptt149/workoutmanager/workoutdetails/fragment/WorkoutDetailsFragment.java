package vn.com.hieptt149.workoutmanager.workoutdetails.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
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
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.adapter.ExercisePreviewAdapter;
import vn.com.hieptt149.workoutmanager.model.ConstantValue;
import vn.com.hieptt149.workoutmanager.model.Exercise;
import vn.com.hieptt149.workoutmanager.model.User;
import vn.com.hieptt149.workoutmanager.model.Workout;
import vn.com.hieptt149.workoutmanager.utils.DisplayView;
import vn.com.hieptt149.workoutmanager.utils.Common;
import vn.com.hieptt149.workoutmanager.utils.GifView;
import vn.com.hieptt149.workoutmanager.workoutdetails.AddWorkoutActivityIntf;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkoutDetailsFragment extends Fragment implements View.OnClickListener, WorkoutDetailsFragmentIntf,
        SelectIconDialogFragment.SelectIconDialogListener {

    private FirebaseAuth auth;
    private AddWorkoutActivityIntf addWorkoutActivityIntf;
    private DatabaseReference currUsersWorkoutRef;
    private SharedPreferences sharedPreferences;
    private TextView tvAddWorkoutToolbarTitle, tvExerciseName, tvTotalExercise, tvTotalTime, tvClickToChoose, tvExerciseDescription;
    private ProgressBar pbCardio, pbStrength, pbMobility;
    private ImageView ivChooseWorkoutIcon, ivStart, ivSave, ivDelete;
    private GifView ivExercisePreview;
    private EditText edtWorkoutTitle;
    private LinearLayout lnExercisesInfo, lnWorkoutInfo, lnExerciseDetail;
    private RecyclerView rvPreviewSelectedExercise;
    private Button btnAddExercise;
    private ExercisePreviewAdapter exercisePreviewAdapter;
    private ArrayList<Exercise> lstSelectedExercise = new ArrayList<>();
    private long practiceTime, restTime, totalTime;
    private int totalExercise, cadioRate = 0, strengthRate = 0, mobilityRate = 0;
    private boolean isFirstTime = true;
    private Bundle fragmentBundle;

    private static User currUser;
    private static Workout usersWorkoutDetails;
    private static String tag;
    private static String imgTag;
    private static boolean isQuickStart;

    public static WorkoutDetailsFragment newInstance(Bundle bundle) {
        WorkoutDetailsFragment workoutDetailsFragment = new WorkoutDetailsFragment();
        if (bundle != null) {
            workoutDetailsFragment.setArguments(bundle);
            currUser = (User) bundle.getSerializable(ConstantValue.CURRENT_USER);
            usersWorkoutDetails = (Workout) bundle.getSerializable(ConstantValue.SELECTED_WORKOUT);
            tag = bundle.getString(ConstantValue.TAG);
            isQuickStart = bundle.getBoolean(ConstantValue.WORKOUT_TYPE);
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
        init(view);
        //Trường hợp user xem chi tiết workout
        if (tag.equals(ConstantValue.WORKOUT_DETAILS)) {
            tvAddWorkoutToolbarTitle.setText(usersWorkoutDetails.getTitle());
            if (!isQuickStart) {
                ivDelete.setVisibility(View.VISIBLE);
            }
            //Nếu người dùng mới mở màn hình chi tiết lần đầu
            if (isFirstTime) {
                edtWorkoutTitle.setText(usersWorkoutDetails.getTitle());
                imgTag = usersWorkoutDetails.getIcon();
                lstSelectedExercise.addAll(usersWorkoutDetails.getLstUsersExercises());
            }
            if (lstSelectedExercise.size() != 0) {
                ivStart.setVisibility(View.VISIBLE);
                if (!isQuickStart) {
                    ivSave.setVisibility(View.VISIBLE);
                }
            }
            showUsersWorkoutDetails();
        }
        //Trường hợp tạo mới workout
        else if (tag.equals(ConstantValue.ADD_WORKOUT)) {
            tvAddWorkoutToolbarTitle.setText(R.string.add_workout);
            //Nếu người dùng đã thêm exercise
            if (!isFirstTime) {
                if (lstSelectedExercise.size() != 0) {
                    ivStart.setVisibility(View.VISIBLE);
                    if (auth.getCurrentUser() != null) {
                        ivSave.setVisibility(View.VISIBLE);
                    } else {
                        ivSave.setVisibility(View.GONE);
                    }
                }
                showUsersWorkoutDetails();
            } else {
                imgTag = null;
            }
        }
        rvPreviewSelectedExercise.setHasFixedSize(true);
        rvPreviewSelectedExercise.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        exercisePreviewAdapter = new ExercisePreviewAdapter(getContext(), lstSelectedExercise, this);
        rvPreviewSelectedExercise.setAdapter(exercisePreviewAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_exercise:
                addExercise();
                break;
            case R.id.iv_choose_icon:
                addWorkoutActivityIntf.showDialogFragment(WorkoutDetailsFragment.this,
                        SelectIconDialogFragment.newInstance(), ConstantValue.SELECT_ICON);
                break;
            case R.id.iv_start:
                startWorkout();
                break;
            case R.id.iv_save:
                saveWorkout();
                break;
            case R.id.iv_delete:
                deleteWorkout();
                break;
        }
    }

    @Override
    public void onExerciseItemClick(Exercise selectedExercise) {
        fragmentBundle = new Bundle();
        fragmentBundle.putSerializable(ConstantValue.SELECTED_EXERCISE, selectedExercise);
        addWorkoutActivityIntf.openFragment(ExerciseDetailsFragment.newInstance(fragmentBundle), ConstantValue.EXERCISE_DETAILS);
        isFirstTime = false;
    }

    @Override
    public void onIconItemClick(String tag) {
        int imgRes = getResources().getIdentifier(tag, "drawable", getContext().getPackageName());
        imgTag = tag;
        ivChooseWorkoutIcon.setTag(imgTag);
        ivChooseWorkoutIcon.setImageResource(imgRes);
    }

    /**
     * Mở màn hình thêm bài tập vào workout
     */
    private void addExercise() {
        isFirstTime = false;
        fragmentBundle = new Bundle();
        fragmentBundle.putSerializable(ConstantValue.SELECTED_EXERCISE_LIST, lstSelectedExercise);
        addWorkoutActivityIntf.openFragment(AddExerciseFragment.newInstance(fragmentBundle), ConstantValue.ADD_EXERCISE);
    }

    /**
     * Bắt đầu tập với bài workout
     */
    private void startWorkout() {
        isFirstTime = false;
        fragmentBundle = new Bundle();
        fragmentBundle.putSerializable(ConstantValue.CURRENT_USER, currUser);
        fragmentBundle.putString(ConstantValue.WORKOUT_TITLE, edtWorkoutTitle.getText().toString());
        fragmentBundle.putSerializable(ConstantValue.SELECTED_EXERCISE_LIST, lstSelectedExercise);
        fragmentBundle.putLong(ConstantValue.EXERCISES_DURATION, practiceTime);
        fragmentBundle.putLong(ConstantValue.RESTS_DURATION, restTime);
        addWorkoutActivityIntf.openFragment(StartWorkoutFragment.newInstance(fragmentBundle), ConstantValue.START_WORKOUT);
    }

    /**
     * Lưu thông tin workout vào db
     */
    private void saveWorkout() {
        if (edtWorkoutTitle.getText().toString().length() == 0 || edtWorkoutTitle.getText().toString().isEmpty()) {
            edtWorkoutTitle.setError(getString(R.string.enter_workout_title));
            return;
        }
        DisplayView.showAlertDialog(getContext(), getString(R.string.confirm_save),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        Workout newWorkout = new Workout(edtWorkoutTitle.getText().toString(),
                                (String) ivChooseWorkoutIcon.getTag(), lstSelectedExercise, cadioRate, strengthRate, mobilityRate);
                        if (Common.haveNetworkConnection(getContext())) {
                            DisplayView.showProgressDialog(getContext());
                            if (tag.equals(ConstantValue.ADD_WORKOUT)) {
                                if (currUser != null) {
                                    currUsersWorkoutRef = FirebaseDatabase.getInstance().getReference()
                                            .child(ConstantValue.WORKOUT).child(currUser.getId());
                                } else {
                                    if (auth.getCurrentUser() != null) {
                                        currUsersWorkoutRef = FirebaseDatabase.getInstance().getReference()
                                                .child(ConstantValue.WORKOUT).child(auth.getCurrentUser().getUid());
                                    }
                                }
                                currUsersWorkoutRef.push().setValue(newWorkout).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        DisplayView.dismissProgressDialog();
                                        if (!task.isComplete()) {
                                            DisplayView.showToast(getContext(), "Can't save your workout. Please check your connection and try again");
                                        } else {
                                            dialogInterface.dismiss();
                                            getActivity().onBackPressed();
                                        }
                                    }
                                });
                            } else if (tag.equals(ConstantValue.WORKOUT_DETAILS)) {
                                currUsersWorkoutRef = FirebaseDatabase.getInstance().getReference().child(ConstantValue.WORKOUT).
                                        child(usersWorkoutDetails.getUserId()).child(usersWorkoutDetails.getId());
                                currUsersWorkoutRef.setValue(newWorkout).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        DisplayView.dismissProgressDialog();
                                        if (!task.isComplete()) {
                                            DisplayView.showToast(getContext(), "Can't save your change. Please check your connection and try again");
                                        } else {
                                            dialogInterface.dismiss();
                                            getActivity().onBackPressed();
                                        }
                                    }
                                });
                            }
                        } else {
                            DisplayView.showToast(getContext(), getString(R.string.no_connection));
                        }
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
    }

    /**
     * Xoá workout trong db
     */
    private void deleteWorkout() {
        DisplayView.showConfirmAlertDialog(getContext(), getString(R.string.confirm_delete),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        currUsersWorkoutRef = FirebaseDatabase.getInstance().getReference().child(ConstantValue.WORKOUT).
                                child(usersWorkoutDetails.getUserId()).child(usersWorkoutDetails.getId());
                        currUsersWorkoutRef.removeValue();
                        dialogInterface.dismiss();
                        getActivity().onBackPressed();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
    }

    /**
     * Hiển thị chi tiết workout
     */
    private void showUsersWorkoutDetails() {
        totalExercise = 0;
        totalTime = 0;
        cadioRate = 0;
        strengthRate = 0;
        mobilityRate = 0;
        for (int i = 0; i < lstSelectedExercise.size(); i++) {
            totalTime += i == 0 ? practiceTime : practiceTime + restTime;
            cadioRate += lstSelectedExercise.get(i).getCardioRate();
            strengthRate += lstSelectedExercise.get(i).getStrengthRate();
            mobilityRate += lstSelectedExercise.get(i).getMobilityRate();
        }
        if (lstSelectedExercise.size() != 0) {
            totalExercise = lstSelectedExercise.size();
            cadioRate = cadioRate / (lstSelectedExercise.size());
            strengthRate = strengthRate / (lstSelectedExercise.size());
            mobilityRate = mobilityRate / (lstSelectedExercise.size());
        }
        pbCardio.post(new Runnable() {
            @Override
            public void run() {
                pbCardio.setProgress(cadioRate);
                pbStrength.setProgress(strengthRate);
                pbMobility.setProgress(mobilityRate);
            }
        });
        if (totalExercise > 1) {
            tvTotalExercise.setText(totalExercise + " exercises");
        } else {
            tvTotalExercise.setText(totalExercise + " exercise");
        }
        tvTotalTime.setText(Common.msTimeFormatter(totalTime));
        if (imgTag != null) {
            int imgRes = getResources().getIdentifier(imgTag, "drawable", getContext().getPackageName());
            ivChooseWorkoutIcon.setTag(imgTag);
            ivChooseWorkoutIcon.setImageResource(imgRes);
        }
    }

    /**
     * Thêm exercise vào list exercise
     *
     * @param exercise: exercise cần thêm
     */
    public void addSelectedExercise(Exercise exercise) {
        lstSelectedExercise.add(exercise);
        isFirstTime = false;
    }

    /**
     * Xóa exercise trong list exercise
     *
     * @param exercise: exercise muốn xóa
     */
    public void removeSelectedExercise(Exercise exercise) {
        int tmpId = -1;
        for (int i = 0; i < lstSelectedExercise.size(); i++) {
            if (lstSelectedExercise.get(i).getId() == exercise.getId()) {
                tmpId = i;
            }
        }
        lstSelectedExercise.remove(tmpId);
        isFirstTime = false;
    }

    private void init(View view) {
        tvAddWorkoutToolbarTitle = getActivity().findViewById(R.id.tv_addworkout_toolbar_title);
        tvTotalExercise = view.findViewById(R.id.tv_total_exercise);
        tvTotalTime = view.findViewById(R.id.tv_total_time);
        tvExerciseName = view.findViewById(R.id.tv_exercise_name);
        pbCardio = view.findViewById(R.id.pb_cardio);
        pbStrength = view.findViewById(R.id.pb_strength);
        pbMobility = view.findViewById(R.id.pb_mobility);
        ivChooseWorkoutIcon = view.findViewById(R.id.iv_choose_icon);
        ivExercisePreview = view.findViewById(R.id.iv_exercise_preview);
        ivDelete = view.findViewById(R.id.iv_delete);
        ivSave = view.findViewById(R.id.iv_save);
        ivStart = view.findViewById(R.id.iv_start);
        tvClickToChoose = view.findViewById(R.id.tv_click_to_choose);
        edtWorkoutTitle = view.findViewById(R.id.edt_title);
        lnExercisesInfo = view.findViewById(R.id.ln_exercises_info);
        lnWorkoutInfo = view.findViewById(R.id.ln_workout_info);
        lnExerciseDetail = view.findViewById(R.id.ln_exercise_detail);
        rvPreviewSelectedExercise = view.findViewById(R.id.rv_preview_selected_exercise);
        tvExerciseDescription = view.findViewById(R.id.tv_exercise_description);
        btnAddExercise = view.findViewById(R.id.btn_add_exercise);
        btnAddExercise.setOnClickListener(this);
        ivChooseWorkoutIcon.setOnClickListener(this);
        ivStart.setOnClickListener(this);
        ivSave.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        lnExerciseDetail.setVisibility(View.GONE);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        practiceTime = sharedPreferences.getLong(ConstantValue.EXERCISES_DURATION, ConstantValue.DEFAULT_EXERCISES_DURATION);
        restTime = sharedPreferences.getLong(ConstantValue.RESTS_DURATION, ConstantValue.DEFAULT_RESTS_DURATION);
        auth = FirebaseAuth.getInstance();
    }
}
