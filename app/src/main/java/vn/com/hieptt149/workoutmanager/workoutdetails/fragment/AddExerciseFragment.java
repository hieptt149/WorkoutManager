package vn.com.hieptt149.workoutmanager.workoutdetails.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.adapter.ExerciseListAdapter;
import vn.com.hieptt149.workoutmanager.utils.Common;
import vn.com.hieptt149.workoutmanager.workoutdetails.AddWorkoutActivityIntf;
import vn.com.hieptt149.workoutmanager.model.ConstantValue;
import vn.com.hieptt149.workoutmanager.model.Exercise;
import vn.com.hieptt149.workoutmanager.utils.DisplayView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddExerciseFragment extends Fragment implements AddExerciseFragmentIntf {

    private AddWorkoutActivityIntf addWorkoutActivityIntf;
    private WorkoutDetailsFragmentIntf workoutDetailsFragmentIntf;
    private TextView tvAddWorkoutToolBarTitle, tvAddworkoutDone;
    private RecyclerView rvExercise;
    private DatabaseReference exercisesRef;
    private ExerciseListAdapter exerciseListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private static ArrayList<Exercise> lstSelectedExercises;
    private ArrayList<Exercise> lstExercises;

    public AddExerciseFragment() {
    }

    public static AddExerciseFragment newInstance(Bundle bundle) {
        AddExerciseFragment addExerciseFragment = new AddExerciseFragment();
        if (bundle != null) {
            lstSelectedExercises = (ArrayList<Exercise>) bundle.getSerializable(ConstantValue.SELECTED_EXERCISE_LIST);
        }
        return addExerciseFragment;
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
        return inflater.inflate(R.layout.fragment_add_exercise, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Common.haveNetworkConnection(getContext())) {
            //Trường hợp user muốn thêm mới exercise vào workout
            if (lstSelectedExercises != null) {
                mergeSelectedExsWithDefaultExs();
            }
            //Trường hợp tạo mới workout lần đầu
            else {
                lstSelectedExercises = new ArrayList<>();
                getNewExerciseList();
            }
        } else {
            DisplayView.showToast(getContext(), getString(R.string.no_connection));
        }
    }

    @Override
    public void onExerciseItemClick(Exercise selectedExercise) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConstantValue.SELECTED_EXERCISE, selectedExercise);
        addWorkoutActivityIntf.openFragment(ExerciseDetailsFragment.newInstance(bundle), ConstantValue.EXERCISE_DETAILS);
    }

    @Override
    public void addExercise(Exercise exercise) {
        addWorkoutActivityIntf.addSelectedExercise(exercise);
    }

    @Override
    public void removeExercise(Exercise exercise) {
        addWorkoutActivityIntf.removeSelectedExercise(exercise);
    }

    /**
     * Lấy danh sách exercise từ Firebase và đổ vào recyclerview
     */
    private void getNewExerciseList() {
        DisplayView.showProgressDialog(getContext());
        exercisesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lstExercises = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    lstExercises.add(snapshot.getValue(Exercise.class));
                }
                exerciseListAdapter = new ExerciseListAdapter(getContext(), lstExercises, AddExerciseFragment.this);
                rvExercise.setAdapter(exerciseListAdapter);
                DisplayView.dismissProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                DisplayView.dismissProgressDialog();
            }
        });
    }

    /**
     * Xác định những exercise đã được người dùng chọn trong danh sách exercise lấy từ firebase
     */
    private void mergeSelectedExsWithDefaultExs() {
        DisplayView.showProgressDialog(getContext());
        exercisesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lstExercises = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    lstExercises.add(snapshot.getValue(Exercise.class));
                }
                for (int i = 0; i < lstSelectedExercises.size(); i++) {
                    for (int j = 0; j < lstExercises.size(); j++) {
                        if (lstExercises.get(j).getId() == lstSelectedExercises.get(i).getId()) {
                            lstExercises.get(j).setAdded(lstSelectedExercises.get(i).isAdded());
                        }
                    }
                }
                exerciseListAdapter = new ExerciseListAdapter(getContext(), lstExercises, AddExerciseFragment.this);
                rvExercise.setAdapter(exerciseListAdapter);
                DisplayView.dismissProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                DisplayView.dismissProgressDialog();
            }
        });
    }

    private void init(View view) {
        tvAddWorkoutToolBarTitle = getActivity().findViewById(R.id.tv_addworkout_toolbar_title);
        tvAddworkoutDone = getActivity().findViewById(R.id.tv_addworkout_back);
        rvExercise = view.findViewById(R.id.rv_exercise);
        exercisesRef = FirebaseDatabase.getInstance().getReference().child(ConstantValue.EXERCISE);
        tvAddWorkoutToolBarTitle.setText(R.string.add_exercise);
        tvAddworkoutDone.setText(R.string.done);
        linearLayoutManager = new LinearLayoutManager(getContext());
        dividerItemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
        rvExercise.setHasFixedSize(true);
        rvExercise.setLayoutManager(linearLayoutManager);
        rvExercise.addItemDecoration(dividerItemDecoration);
    }
}
