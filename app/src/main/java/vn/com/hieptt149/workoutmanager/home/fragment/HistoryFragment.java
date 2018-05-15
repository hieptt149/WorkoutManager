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
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.adapter.HistoryListAdapter;
import vn.com.hieptt149.workoutmanager.model.ConstantValue;
import vn.com.hieptt149.workoutmanager.model.History;
import vn.com.hieptt149.workoutmanager.model.User;
import vn.com.hieptt149.workoutmanager.utils.DisplayView;
import vn.com.hieptt149.workoutmanager.utils.Common;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private FirebaseAuth auth;
    private DatabaseReference currUserRef;
    private DatabaseReference currUserHistoryRef;
    private LineChart chartHistory;
    private RecyclerView rvHistory;
    private TextView tvCaloriesBurnADay;
    private ArrayList<History> lstHistories;
    private List<Entry> lstChartEntries;
    private HistoryListAdapter historyListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private LineDataSet lineDataSet;
    private LineData lineData;
    private double caloriesBurnADay;
    private User currUser;
    private NumberFormat nf = new DecimalFormat("#.##");

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
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (auth.getCurrentUser() == null && (lstChartEntries != null || lstHistories != null)) {
                lstHistories.clear();
                lstChartEntries.clear();
                chartHistory.clear();
                historyListAdapter.notifyDataSetChanged();
                lineDataSet.notifyDataSetChanged();
                lineData.notifyDataChanged();
                chartHistory.notifyDataSetChanged();
                tvCaloriesBurnADay.setText("");
            }
        }
    }

    private void setupChartAxes() {
        XAxis xAxis = chartHistory.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setEnabled(false);

        YAxis leftYAxis = chartHistory.getAxisLeft();
        leftYAxis.setDrawGridLines(true);
        leftYAxis.setAxisMinimum(0f);

        YAxis rightYAxis = chartHistory.getAxisRight();
        rightYAxis.setEnabled(false);

        LimitLine ll = new LimitLine((float) caloriesBurnADay, "cal/day");
        ll.setLineWidth(3f);
        ll.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll.setTextSize(12f);
        leftYAxis.removeAllLimitLines();
        leftYAxis.addLimitLine(ll);
        leftYAxis.setDrawLimitLinesBehindData(true);
    }

    private void setupChartData() {
        if (lstChartEntries != null) {
//            lineDataSet = new LineDataSet(lstChartEntries, "Workout history");
//            lineData = new LineData(lineDataSet);
            lineDataSet.setLineWidth(2f);
            lineDataSet.setCircleRadius(8f);
            lineDataSet.setValueTextSize(12f);
            // To show values of each point
            lineDataSet.setDrawValues(true);
            lineDataSet.notifyDataSetChanged();
            lineData.notifyDataChanged();
            chartHistory.setData(lineData);
            chartHistory.invalidate();
        }
    }

    private void getCurrUserInfo() {
        currUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currUser = dataSnapshot.getValue(User.class);
                currUser.setId(dataSnapshot.getKey());
                caloriesBurnADay = Common.calculateCaloriesBurnADay(currUser.getGender(), currUser.getAge(), currUser.getHeight(), currUser.getWeight());
                tvCaloriesBurnADay.setText("Calories need to burn a day: " + nf.format(caloriesBurnADay));
                setupChartAxes();
                getWorkoutHistory();
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
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        History history = snapshot.getValue(History.class);
                        history.setUserId(auth.getCurrentUser().getUid());
                        history.setPracticeDate(snapshot.getKey());
//                    lineData.addEntry(new Entry(Float.parseFloat(history.getPracticeDate()), (float) history.getCaloriesBurn()),0);
                        lstHistories.add(history);
                        lstChartEntries.add(new Entry(Float.parseFloat(history.getPracticeDate()), (float) history.getCaloriesBurn()));
                    }
                    historyListAdapter.notifyDataSetChanged();
                    setupChartData();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void init(View view) {
        chartHistory = view.findViewById(R.id.chart_history);
        rvHistory = view.findViewById(R.id.rv_history);
        tvCaloriesBurnADay = view.findViewById(R.id.tv_calories_burn_a_day);
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
        chartHistory.setDrawGridBackground(false);
        lineDataSet = new LineDataSet(lstChartEntries, "Workout history");
        lineData = new LineData(lineDataSet);
    }
}