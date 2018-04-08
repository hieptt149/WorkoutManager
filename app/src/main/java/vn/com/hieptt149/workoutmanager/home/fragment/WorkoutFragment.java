package vn.com.hieptt149.workoutmanager.home.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.adapter.WorkoutPreviewAdapter;
import vn.com.hieptt149.workoutmanager.addworkout.AddWorkoutActivity;
import vn.com.hieptt149.workoutmanager.home.MainActivity;
import vn.com.hieptt149.workoutmanager.home.MainActivityIntf;
import vn.com.hieptt149.workoutmanager.model.ConstantValue;
import vn.com.hieptt149.workoutmanager.model.User;
import vn.com.hieptt149.workoutmanager.model.Workout;
import vn.com.hieptt149.workoutmanager.utils.DisplayView;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkoutFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener{

    private GridView gvPreviewWorkout;
    private FloatingActionButton fabAddWorkout;
    private WorkoutPreviewAdapter workoutPreviewAdapter;
    private ArrayList<Workout> lstUsersWorkout;
    private DatabaseReference usersWorkoutRef;
    private MainActivity mainActivity;
    private String userId;

    public WorkoutFragment() {
    }

    public static WorkoutFragment newInstance() {
        WorkoutFragment workoutFragment = new WorkoutFragment();
        return workoutFragment;
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
        mainActivity = (MainActivity) getActivity();
        lstUsersWorkout = new ArrayList<>();
        workoutPreviewAdapter = new WorkoutPreviewAdapter(getContext(),lstUsersWorkout);
        gvPreviewWorkout.setAdapter(workoutPreviewAdapter);
        mainActivity.setMainActivityIntf(new MainActivityIntf() {
            @Override
            public void sendCurrUserInfo(final User currUser) {
                userId = currUser.getId();
                usersWorkoutRef = FirebaseDatabase.getInstance().getReference().child("workout").child(currUser.getId());
                usersWorkoutRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        lstUsersWorkout.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Workout usersWorkout = snapshot.getValue(Workout.class);
                            usersWorkout.setId(snapshot.getKey());
                            usersWorkout.setUserId(currUser.getId());
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
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(getActivity(), AddWorkoutActivity.class);
        i.putExtra("userid",userId);
        i.putExtra("tag", ConstantValue.ADD_WORKOUT);
        startActivity(i);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent i = new Intent(getActivity(),AddWorkoutActivity.class);
        i.putExtra("workout",lstUsersWorkout.get(position));
        i.putExtra("tag",ConstantValue.WORKOUT_DETAILS);
        startActivity(i);
    }

    private void initView(View view) {
        fabAddWorkout = view.findViewById(R.id.fab_addworkout);
        gvPreviewWorkout = view.findViewById(R.id.gv_preview_workout);
        fabAddWorkout.setOnClickListener(this);
        gvPreviewWorkout.setOnItemClickListener(this);
    }
}
