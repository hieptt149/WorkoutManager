package vn.com.hieptt149.workoutmanager.workoutdetails.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vn.com.hieptt149.workoutmanager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartWorkoutFragment extends Fragment {

    private TextView tvAddWorkoutToolbarTitle;

    public StartWorkoutFragment() {
        // Required empty public constructor
    }

    public static StartWorkoutFragment newInstance(Bundle bundle) {
        StartWorkoutFragment startWorkoutFragment = new StartWorkoutFragment();
        return startWorkoutFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_workout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        tvAddWorkoutToolbarTitle = getActivity().findViewById(R.id.tv_addworkout_toolbar_title);
    }
}
