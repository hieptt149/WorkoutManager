package vn.com.hieptt149.workoutmanager.home.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.adapter.WorkoutPreviewAdapter;
import vn.com.hieptt149.workoutmanager.model.User;
import vn.com.hieptt149.workoutmanager.utils.ExpandableChildGridView;
import vn.com.hieptt149.workoutmanager.workoutdetails.AddWorkoutActivity;
import vn.com.hieptt149.workoutmanager.home.MainActivityIntf;
import vn.com.hieptt149.workoutmanager.model.ConstantValue;
import vn.com.hieptt149.workoutmanager.model.Workout;
import vn.com.hieptt149.workoutmanager.utils.DisplayView;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkoutFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private FirebaseAuth auth;
    private LinearLayout lnPreviewWorkoutQuickStart, lnPreviewWorkout;
    private ExpandableChildGridView gvPreviewWorkoutQuickStart, gvPreviewWorkout;
    private FloatingActionButton fabAddWorkout;
    private WorkoutPreviewAdapter workoutPreviewAdapter, quickStartAdapter;
    private ArrayList<Workout> lstUsersWorkout;
    private ArrayList<Workout> lstQuickStart;
    private DatabaseReference usersWorkoutRef;
    private MainActivityIntf mainActivityIntf;
    private DatabaseReference currUserRef;
    private DatabaseReference quickStartRef;
    private User currUser;

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
        init(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        DisplayView.showProgressDialog(getContext());
        getListQuickStartWorkout();
        if (auth.getCurrentUser() != null) {
            getListWorkout();
        } else {
            DisplayView.dismissProgressDialog();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (auth != null) {
                if (auth.getCurrentUser() == null) {
                    lstUsersWorkout.clear();
                    workoutPreviewAdapter.notifyDataSetChanged();
                    lnPreviewWorkout.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(getActivity(), AddWorkoutActivity.class);
        i.putExtra(ConstantValue.CURRENT_USER, currUser);
        i.putExtra(ConstantValue.TAG, ConstantValue.ADD_WORKOUT);
        startActivity(i);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent i = new Intent(getActivity(), AddWorkoutActivity.class);
        switch (adapterView.getId()) {
            case R.id.gv_preview_workout:
                i.putExtra(ConstantValue.CURRENT_USER, currUser);
                i.putExtra(ConstantValue.SELECTED_WORKOUT, lstUsersWorkout.get(position));
                i.putExtra(ConstantValue.TAG, ConstantValue.WORKOUT_DETAILS);
                startActivity(i);
                break;
            case R.id.gv_preview_workout_quick_start:
                i.putExtra(ConstantValue.CURRENT_USER, currUser);
                i.putExtra(ConstantValue.SELECTED_WORKOUT, lstQuickStart.get(position));
                i.putExtra(ConstantValue.TAG, ConstantValue.WORKOUT_DETAILS);
                startActivity(i);
                break;
        }
    }

    private void getCurrUserInfo() {
        currUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    currUser = dataSnapshot.getValue(User.class);
                    currUser.setId(dataSnapshot.getKey());
                    DisplayView.dismissProgressDialog();
                } else {
                    currUser = new User();
                    currUser.setId(auth.getCurrentUser().getUid());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                DisplayView.dismissProgressDialog();
            }
        });
    }

    private void getListWorkout() {
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
                lnPreviewWorkout.setVisibility(View.VISIBLE);
                DisplayView.dismissProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                DisplayView.showToast(getContext(), "Can't get your workouts. Please check your connection");
                DisplayView.dismissProgressDialog();
            }
        });
    }


    private void getListQuickStartWorkout() {
        quickStartRef = FirebaseDatabase.getInstance().getReference().child(ConstantValue.WORKOUT).child(ConstantValue.QUICK_START);
        quickStartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lstQuickStart.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Workout usersWorkout = snapshot.getValue(Workout.class);
                    usersWorkout.setId(snapshot.getKey());
                    usersWorkout.setUserId(auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "");
                    lstQuickStart.add(usersWorkout);
                }
                quickStartAdapter.notifyDataSetChanged();
                lnPreviewWorkoutQuickStart.setVisibility(View.VISIBLE);
                DisplayView.dismissProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                DisplayView.showToast(getContext(), "Can't get quick start workouts from server. Please check your connection");
                DisplayView.dismissProgressDialog();
            }
        });
    }

    private void init(View view) {
        fabAddWorkout = view.findViewById(R.id.fab_addworkout);
        lnPreviewWorkout = view.findViewById(R.id.ln_preview_workout);
        lnPreviewWorkoutQuickStart = view.findViewById(R.id.ln_preview_workout_quick_start);
        gvPreviewWorkout = view.findViewById(R.id.gv_preview_workout);
        gvPreviewWorkoutQuickStart = view.findViewById(R.id.gv_preview_workout_quick_start);
        fabAddWorkout.setOnClickListener(this);
        gvPreviewWorkoutQuickStart.setOnItemClickListener(this);
        gvPreviewWorkout.setOnItemClickListener(this);
        auth = FirebaseAuth.getInstance();
        lstUsersWorkout = new ArrayList<>();
        lstQuickStart = new ArrayList<>();
        quickStartAdapter = new WorkoutPreviewAdapter(getContext(), lstQuickStart);
        workoutPreviewAdapter = new WorkoutPreviewAdapter(getContext(), lstUsersWorkout);
        gvPreviewWorkout.setAdapter(workoutPreviewAdapter);
        gvPreviewWorkoutQuickStart.setAdapter(quickStartAdapter);
        if (auth.getCurrentUser() != null) {
            currUserRef = FirebaseDatabase.getInstance().getReference().child(ConstantValue.USER).child(auth.getCurrentUser().getUid());
            getCurrUserInfo();
        }
    }
}
