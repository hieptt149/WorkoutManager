package vn.com.hieptt149.workoutmanager.addworkout;


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
import vn.com.hieptt149.workoutmanager.model.Exercise;
import vn.com.hieptt149.workoutmanager.utils.DisplayView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddExerciseFragment extends Fragment {

    private TextView tvAddWorkoutToolBarTitle;
    private RecyclerView rvExercise;
    private DatabaseReference exercisesRef;
    private ExerciseListAdapter exerciseListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private ArrayList<Exercise> lstExercises;

    public AddExerciseFragment() {
    }

    public static AddExerciseFragment newInstance() {
        AddExerciseFragment addExerciseFragment = new AddExerciseFragment();
        return addExerciseFragment;
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
        initView(view);
        DisplayView.showProgressDialog(getContext());
        exercisesRef = FirebaseDatabase.getInstance().getReference().child("exercise");
        tvAddWorkoutToolBarTitle.setText(R.string.add_exercise);
//        exerciseListAdapter = new ExerciseListAdapter(lstExercises);
        linearLayoutManager = new LinearLayoutManager(getContext());
        dividerItemDecoration = new DividerItemDecoration(getContext(),linearLayoutManager.getOrientation());
        rvExercise.setHasFixedSize(true);
        rvExercise.setLayoutManager(linearLayoutManager);
        rvExercise.addItemDecoration(dividerItemDecoration);
        getNewExerciseList();
//        rvExercise.setAdapter(exerciseListAdapter);
    }

    private void initView(View view) {
        tvAddWorkoutToolBarTitle = getActivity().findViewById(R.id.tv_addworkout_toolbar_title);
        rvExercise = view.findViewById(R.id.rv_exercise);
    }

    private void getNewExerciseList() {
        exercisesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lstExercises = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    lstExercises.add(snapshot.getValue(Exercise.class));
                }
                exerciseListAdapter = new ExerciseListAdapter(lstExercises);
                rvExercise.setAdapter(exerciseListAdapter);
                DisplayView.dismissProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                DisplayView.dismissProgressDialog();
            }
        });
    }
}
