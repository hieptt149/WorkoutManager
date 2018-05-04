package vn.com.hieptt149.workoutmanager.home.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.adapter.HistoryListAdapter;
import vn.com.hieptt149.workoutmanager.model.ConstantValue;
import vn.com.hieptt149.workoutmanager.model.History;
import vn.com.hieptt149.workoutmanager.model.User;
import vn.com.hieptt149.workoutmanager.utils.DisplayView;
import vn.com.hieptt149.workoutmanager.utils.Formula;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private FirebaseAuth auth;
    private DatabaseReference currUserRef;
    private DatabaseReference currUserHistoryRef;
    private LineChart chartHistory;
    private RecyclerView rvHistory;
    private ArrayList<History> lstHistories;
    private List<Entry> lstChartEntries;
    private HistoryListAdapter historyListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private LineDataSet lineDataSet;
    private LineData lineData;
    private double caloriesBurnADay;
    private User currUser;

    public static HistoryFragment newInstance() {
        HistoryFragment historyFragment = new HistoryFragment();
        return historyFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (auth.getCurrentUser() != null) {
            currUserRef = FirebaseDatabase.getInstance().getReference().child(ConstantValue.USER)
                    .child(auth.getCurrentUser().getUid());
            currUserHistoryRef = FirebaseDatabase.getInstance().getReference().child(ConstantValue.HISTORY)
                    .child(auth.getCurrentUser().getUid());
            getCurrUserInfo();
//            setupChartAxes();
//            setupChartData();
            getWorkoutHistory();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

        }
    }

//    private void setupChartAxes() {
//        XAxis xAxis = chartHistory.getXAxis();
//        xAxis.setDrawGridLines(false);
//        xAxis.setAvoidFirstLastClipping(true);
//        xAxis.setEnabled(true);
//
//        YAxis leftYAxis = chartHistory.getAxisLeft();
//        leftYAxis.setDrawGridLines(true);
//
//        YAxis rightYAxis = chartHistory.getAxisRight();
//        rightYAxis.setEnabled(false);
//
//        LimitLine ll = new LimitLine((float) caloriesBurnADay, "cal/day");
//        ll.setLineWidth(2f);
//        ll.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
//        ll.setTextSize(10f);
//        leftYAxis.removeAllLimitLines();
//        leftYAxis.addLimitLine(ll);
//        leftYAxis.setDrawLimitLinesBehindData(true);
//    }

    private void setupChartData() {
//        lineDataSet = new LineDataSet(null,"Workout history");
//        lineData = new LineData();
//        lineDataSet.setLineWidth(2f);
//        lineDataSet.setCircleRadius(4f);
//        lineDataSet.setValueTextSize(10f);
//        // To show values of each point
//        lineDataSet.setDrawValues(true);
//        chartHistory.setData(lineData);
//        chartHistory.invalidate();
    }

    private void getCurrUserInfo() {
        currUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currUser = dataSnapshot.getValue(User.class);
                currUser.setId(dataSnapshot.getKey());
                caloriesBurnADay = Formula.calculateCaloriesBurnADay(currUser.getGender(), currUser.getAge(), currUser.getHeight(), currUser.getWeight());
                DisplayView.dismissProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                DisplayView.dismissProgressDialog();
            }
        });
    }

    private void getWorkoutHistory() {
        currUserHistoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                lineData = chartHistory.getData();
                lstChartEntries.clear();
                lstHistories.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    History history = snapshot.getValue(History.class);
                    history.setUserId(auth.getCurrentUser().getUid());
                    history.setPracticeDate(snapshot.getKey());
//                    lineData.addEntry(new Entry(Float.parseFloat(history.getPracticeDate()), (float) history.getCaloriesBurn()),0);
                    lstHistories.add(history);
                    lstChartEntries.add(new Entry(Float.parseFloat(history.getPracticeDate()), (float) history.getCaloriesBurn()));
                }
                historyListAdapter.notifyDataSetChanged();
                lineDataSet = new LineDataSet(lstChartEntries,"Workout history");
                lineData = new LineData(lineDataSet);
                chartHistory.setData(lineData);
                chartHistory.invalidate();
//                lineData.notifyDataChanged();
//                chartHistory.notifyDataSetChanged();
//                chartHistory.invalidate();
                DisplayView.dismissProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                DisplayView.dismissProgressDialog();
            }
        });
    }

    private void init(View view) {
        chartHistory = view.findViewById(R.id.chart_history);
        rvHistory = view.findViewById(R.id.rv_history);
        auth = FirebaseAuth.getInstance();
        lstHistories = new ArrayList<>();
        lstChartEntries = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getContext());
        dividerItemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
        historyListAdapter = new HistoryListAdapter(getContext(), lstHistories);
        rvHistory.setHasFixedSize(true);
        rvHistory.setLayoutManager(linearLayoutManager);
        rvHistory.addItemDecoration(dividerItemDecoration);
        rvHistory.setAdapter(historyListAdapter);
    }
}