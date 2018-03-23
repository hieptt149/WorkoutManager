package vn.com.hieptt149.workoutmanager.addworkout;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vn.com.hieptt149.workoutmanager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddExerciseFragment extends Fragment {

    private TextView tvAddWorkoutToolBarTitle;
    private RecyclerView rvExercise;

    public static AddExerciseFragment newInstance() {
        AddExerciseFragment addExerciseFragment = new AddExerciseFragment();
        return addExerciseFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_exercise, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvAddWorkoutToolBarTitle = getActivity().findViewById(R.id.tv_addworkout_toolbar_title);
        rvExercise = view.findViewById(R.id.rv_exercise);
        tvAddWorkoutToolBarTitle.setText(R.string.add_exercise);
    }
}
