package vn.com.hieptt149.workoutmanager.home.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;

import java.util.ArrayList;

import vn.com.hieptt149.workoutmanager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private LineChart chartHistory;
    private RecyclerView rvHistory;

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
        initView(view);
//        ArrayList<Entry> entries = new ArrayList<>();
//        entries.add(new Entry(4f, 0));
//        entries.add(new Entry(8f, 1));
//        entries.add(new Entry(6f, 2));
//        entries.add(new Entry(12f, 3));
//        entries.add(new Entry(18f, 4));
//        entries.add(new Entry(9f, 5));
//        LineDataSet dataSet = new LineDataSet(entries,"Demo");
//        LineData lineData = new LineData(dataSet);
////        ScatterDataSet dataSet = new ScatterDataSet(entries,null);
////        ScatterData scatterData = new ScatterData(dataSet);
//        chartHistory.setData(lineData);
    }

    private void initView(View view) {
        chartHistory = view.findViewById(R.id.chart_history);
        rvHistory = view.findViewById(R.id.rv_history);
    }
}
