package vn.com.hieptt149.workoutmanager.home.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.model.ConstantValue;
import vn.com.hieptt149.workoutmanager.model.History;
import vn.com.hieptt149.workoutmanager.model.User;

public class WeightHistoryDialogFragment extends DialogFragment implements OnChartValueSelectedListener {

    private DatabaseReference currUserHistoryRef;
    private LineChart chartWeight;
    private ArrayList<Entry> lstWeightChartEntries;
    private LineDataSet lineDataSet;
    private LineData lineData;

    private static User currUser;

    public WeightHistoryDialogFragment() {
        // Required empty public constructor
    }

    public static WeightHistoryDialogFragment newInstance(Bundle bundle) {
        WeightHistoryDialogFragment weightHistoryDialogFragment = new WeightHistoryDialogFragment();
        if (bundle != null) {
            weightHistoryDialogFragment.setArguments(bundle);
            currUser = (User) bundle.getSerializable(ConstantValue.CURRENT_USER);
        }
        return weightHistoryDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_fragment_weight_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        chartWeight = view.findViewById(R.id.chart_weight);
        lstWeightChartEntries = new ArrayList<>();
        showWeightChart();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    private void showWeightChart() {
        currUserHistoryRef = FirebaseDatabase.getInstance().getReference().child(ConstantValue.HISTORY)
                .child(currUser.getId());
        currUserHistoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (isAdded()) {
                    lstWeightChartEntries.clear();
                    chartWeight.clear();
                    if (dataSnapshot.hasChildren()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            History history = snapshot.getValue(History.class);
                            history.setUserId(currUser.getId());
                            history.setPracticeDate(snapshot.getKey());
                            lstWeightChartEntries.add(new Entry(Float.parseFloat(history.getPracticeDate()), (float) history.getCurrWeight()));
                        }
                        setupWeightChartData();
                    } else {
                        lineDataSet.notifyDataSetChanged();
                        lineData.notifyDataChanged();
                        chartWeight.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setupWeightChartData() {
        if (lstWeightChartEntries != null) {
            lineDataSet = new LineDataSet(lstWeightChartEntries, "Weight");
            lineDataSet.setColor(Color.BLACK);
            lineDataSet.setHighlightEnabled(true);
            lineDataSet.setDrawHighlightIndicators(false);
            lineDataSet.setDrawValues(true);
            lineDataSet.setCircleRadius(5f);
            lineDataSet.setCircleColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

            lineData = new LineData(lineDataSet);

            chartWeight.setData(lineData);
            chartWeight.invalidate();
            chartWeight.getDescription().setEnabled(false);
            chartWeight.getLegend().setEnabled(false);
            chartWeight.getAxisRight().setEnabled(false);

            YAxis yAxis = chartWeight.getAxisLeft();
            yAxis.setDrawGridLines(true);
            yAxis.setTextColor(Color.GRAY);
            yAxis.setAxisMinimum(0.0f);
            yAxis.setAxisLineColor(Color.BLACK);

            XAxis xAxis = chartWeight.getXAxis();
            xAxis.setDrawGridLines(false);
            xAxis.setAvoidFirstLastClipping(true);
            xAxis.setEnabled(false);
            xAxis.setAxisLineColor(Color.BLACK);

            chartWeight.animateX(1500, Easing.EasingOption.EaseInBack);
            chartWeight.fitScreen();
            chartWeight.setPinchZoom(false);
            chartWeight.setDoubleTapToZoomEnabled(false);
            chartWeight.setOnChartValueSelectedListener(this);
        }
    }
}
