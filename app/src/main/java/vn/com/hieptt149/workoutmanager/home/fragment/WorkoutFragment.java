package vn.com.hieptt149.workoutmanager.home.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.adapter.WorkoutPreviewAdapter;
import vn.com.hieptt149.workoutmanager.workoutdetails.AddWorkoutActivity;
import vn.com.hieptt149.workoutmanager.home.MainActivity;
import vn.com.hieptt149.workoutmanager.home.MainActivityIntf;
import vn.com.hieptt149.workoutmanager.model.ConstantValue;
import vn.com.hieptt149.workoutmanager.model.User;
import vn.com.hieptt149.workoutmanager.model.Workout;
import vn.com.hieptt149.workoutmanager.utils.DisplayView;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkoutFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private FirebaseAuth auth;
    private GridView gvPreviewWorkout;
    private FloatingActionButton fabAddWorkout;
    private WorkoutPreviewAdapter workoutPreviewAdapter;
    private ArrayList<Workout> lstUsersWorkout;
    private DatabaseReference usersWorkoutRef;
    private MainActivityIntf mainActivityIntf;

    public WorkoutFragment() {
    }

    public static WorkoutFragment newInstance() {
        WorkoutFragment workoutFragment = new WorkoutFragment();
        return workoutFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivityIntf = (MainActivityIntf) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initVar();
    }

    @Override
    public void onResume() {
        super.onResume();
        DisplayView.showProgressDialog(getContext());
        if (auth.getCurrentUser() != null) {
            usersWorkoutRef = FirebaseDatabase.getInstance().getReference().child(ConstantValue.WORKOUT).child(auth.getCurrentUser().getUid());
            usersWorkoutRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    lstUsersWorkout.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Workout usersWorkout = snapshot.getValue(Workout.class);
                        usersWorkout.setId(snapshot.getKey());
                        usersWorkout.setUserId(auth.getCurrentUser().getUid());
                        lstUsersWorkout.add(usersWorkout);
                    }
                    workoutPreviewAdapter.notifyDataSetChanged();
                    DisplayView.dismissProgressDialog();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    DisplayView.dismissProgressDialog();
                }
            });
        } else {
            DisplayView.dismissProgressDialog();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (auth != null){
                if (auth.getCurrentUser() == null && lstUsersWorkout.size() != 0) {
                    lstUsersWorkout.clear();
                    workoutPreviewAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(getActivity(), AddWorkoutActivity.class);
        i.putExtra(ConstantValue.TAG, ConstantValue.ADD_WORKOUT);
        startActivity(i);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent i = new Intent(getActivity(), AddWorkoutActivity.class);
        i.putExtra(ConstantValue.SELECTED_WORKOUT, lstUsersWorkout.get(position));
        i.putExtra(ConstantValue.TAG, ConstantValue.WORKOUT_DETAILS);
        startActivity(i);
    }

    private void initView(View view) {
        fabAddWorkout = view.findViewById(R.id.fab_addworkout);
        gvPreviewWorkout = view.findViewById(R.id.gv_preview_workout);
        fabAddWorkout.setOnClickListener(this);
        gvPreviewWorkout.setOnItemClickListener(this);
    }

    private void initVar() {
        auth = FirebaseAuth.getInstance();
        lstUsersWorkout = new ArrayList<>();
        workoutPreviewAdapter = new WorkoutPreviewAdapter(getContext(), lstUsersWorkout);
        gvPreviewWorkout.setAdapter(workoutPreviewAdapter);
    }
}
