package vn.com.hieptt149.workoutmanager.workoutdetails.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.com.hieptt149.workoutmanager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartWorkoutFragment extends Fragment {


    public StartWorkoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_workout, container, false);
    }

}
