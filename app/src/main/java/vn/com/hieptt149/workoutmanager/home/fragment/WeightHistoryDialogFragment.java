package vn.com.hieptt149.workoutmanager.home.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.github.mikephil.charting.charts.LineChart;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.model.ConstantValue;
import vn.com.hieptt149.workoutmanager.model.User;

public class WeightHistoryDialogFragment extends DialogFragment {

    private LineChart weightChart;

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
        weightChart = view.findViewById(R.id.iv_weight_chart);
        showWeightChart();
    }

    private void showWeightChart() {

    }
}
