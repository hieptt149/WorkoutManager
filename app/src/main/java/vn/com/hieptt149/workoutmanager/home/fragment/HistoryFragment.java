package vn.com.hieptt149.workoutmanager.home.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
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
public class HistoryFragment extends Fragment implements OnChartValueSelectedListener {

    private FirebaseAuth auth;
    private DatabaseReference currUserRef;
    private DatabaseReference currUserHistoryRef;
    private LineChart chartHistory;
    private RecyclerView rvHistory;
    private TextView tvCaloriesBurnADay;
    private ArrayList<History> lstHistories;
    private List<Entry> lstHistoryChartEntries;
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
            if (auth != null) {
                if (auth.getCurrentUser() == null && (lstHistoryChartEntries.size() != 0 || lstHistories.size() != 0)) {
                    lstHistories.clear();
                    lstHistoryChartEntries.clear();
                    chartHistory.clear();
                    historyListAdapter.notifyDataSetChanged();
                    lineDataSet.notifyDataSetChanged();
                    lineData.notifyDataChanged();
                    chartHistory.notifyDataSetChanged();
                    tvCaloriesBurnADay.setText("");
                }
            }
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        rvHistory.scrollToPosition(lineDataSet.getEntryIndex(e));
        historyListAdapter.setItemHighlight(lineDataSet.getEntryIndex(e));
        historyListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected() {

    }

    private void setupHistoryChartData() {
        if (lstHistoryChartEntries != null) {
            lineDataSet = new LineDataSet(lstHistoryChartEntries, "Workout history");
            lineDataSet.setColor(Color.BLACK);
            lineDataSet.setDrawValues(false);
            lineDataSet.setHighlightEnabled(true);
            lineDataSet.setDrawHighlightIndicators(false);
            lineDataSet.setCircleRadius(6f);
            lineDataSet.setCircleColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

            lineData = new LineData(lineDataSet);

            chartHistory.setData(lineData);
            chartHistory.invalidate();
            chartHistory.getDescription().setEnabled(false);
            chartHistory.getLegend().setEnabled(false);
            chartHistory.getAxisRight().setEnabled(false);

            YAxis yAxis = chartHistory.getAxisLeft();
            yAxis.setDrawGridLines(true);
            yAxis.setTextColor(Color.GRAY);
            yAxis.setAxisMinimum(0.0f);
            yAxis.setAxisLineColor(Color.BLACK);

            XAxis xAxis = chartHistory.getXAxis();
            xAxis.setDrawGridLines(false);
            xAxis.setAvoidFirstLastClipping(true);
            xAxis.setEnabled(false);
            xAxis.setAxisLineColor(Color.BLACK);

            LimitLine ll = new LimitLine((float) caloriesBurnADay, "cal/day");
            ll.setLineWidth(2f);
            ll.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
            ll.setTextSize(12f);
            yAxis.addLimitLine(ll);
            yAxis.setDrawLimitLinesBehindData(true);

            chartHistory.animateX(1500, Easing.EasingOption.EaseInBack);
            chartHistory.fitScreen();
            chartHistory.setPinchZoom(false);
            chartHistory.setDoubleTapToZoomEnabled(false);
            chartHistory.setOnChartValueSelectedListener(this);
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
//                setupChartAxes();
                getWorkoutHistory();
                DisplayView.dismissProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                DisplayView.showToast(getContext(), "Can't get your history. Please check your connection");
                DisplayView.dismissProgressDialog();
            }
        });
    }

    private void getWorkoutHistory() {
        currUserHistoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                lineData = chartHistory.getData();
                if (isAdded()) {
                    lstHistories.clear();
                    lstHistoryChartEntries.clear();
                    chartHistory.clear();
                    if (dataSnapshot.hasChildren()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            History history = snapshot.getValue(History.class);
                            history.setUserId(auth.getCurrentUser().getUid());
                            history.setPracticeDate(snapshot.getKey());
                            lstHistories.add(history);
                            if (history.getCaloriesBurn() > caloriesBurnADay) {
                                lstHistoryChartEntries.add(new Entry(Float.parseFloat(history.getPracticeDate()), (float) history.getCaloriesBurn(), getResources().getDrawable(R.drawable.star)));
                            } else {
                                lstHistoryChartEntries.add(new Entry(Float.parseFloat(history.getPracticeDate()), (float) history.getCaloriesBurn()));
                            }
                        }
                        historyListAdapter.notifyDataSetChanged();
                        setupHistoryChartData();
                    } else {
                        historyListAdapter.notifyDataSetChanged();
                        lineDataSet.notifyDataSetChanged();
                        lineData.notifyDataChanged();
                        chartHistory.notifyDataSetChanged();
                        tvCaloriesBurnADay.setText("");
                    }
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
        lstHistoryChartEntries = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getContext());
        dividerItemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
        historyListAdapter = new HistoryListAdapter(getContext(), lstHistories);
        rvHistory.setHasFixedSize(true);
        rvHistory.setLayoutManager(linearLayoutManager);
        rvHistory.addItemDecoration(dividerItemDecoration);
        rvHistory.setAdapter(historyListAdapter);
        chartHistory.setDrawGridBackground(false);
        lineDataSet = new LineDataSet(lstHistoryChartEntries, "Workout history");
        lineData = new LineData(lineDataSet);
    }
}