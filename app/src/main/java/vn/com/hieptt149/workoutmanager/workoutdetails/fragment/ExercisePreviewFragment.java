package vn.com.hieptt149.workoutmanager.workoutdetails.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.model.ConstantValue;
import vn.com.hieptt149.workoutmanager.utils.GifView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExercisePreviewFragment extends Fragment {

    private GifView gifvExercisePreview;
    private int gifRes;
    private boolean isPlaying;

    public ExercisePreviewFragment() {
        // Required empty public constructor
    }

    public static ExercisePreviewFragment newInstance(Bundle bundle) {
        ExercisePreviewFragment exercisePreviewFragment = new ExercisePreviewFragment();
        exercisePreviewFragment.setArguments(bundle);
        return exercisePreviewFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercise_preview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gifvExercisePreview = view.findViewById(R.id.gifv_exercise_preview);
        gifRes = getArguments().getInt(ConstantValue.GIF_RESOURCE);
        gifvExercisePreview.setGifResource(gifRes);
        gifvExercisePreview.pause();
    }

    public void playGifAnimation() {
        if (gifvExercisePreview != null) {
            gifvExercisePreview.play();
        }
    }

    public void pauseGifAnimation() {
        if (gifvExercisePreview != null) {
            gifvExercisePreview.pause();
        }
    }
}
