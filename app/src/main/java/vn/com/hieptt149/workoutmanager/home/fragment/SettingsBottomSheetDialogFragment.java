package vn.com.hieptt149.workoutmanager.home.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.model.ConstantValue;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsBottomSheetDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private TextView tvSeekbarValue, tvDone;
    private SeekBar sbDuration;
    private SettingsBottomSheetDialogListener settingsBottomSheetDialogListener;

    private static long exerscisesDuration, restsDuration, newExercisesDuration,newRestsDuration;
    private static boolean isExercisesDuration;

    public SettingsBottomSheetDialogFragment() {
    }

    public static SettingsBottomSheetDialogFragment newInstance(Bundle bundle) {
        SettingsBottomSheetDialogFragment settingsBottomSheetDialogFragment = new SettingsBottomSheetDialogFragment();
        if (bundle != null) {
            exerscisesDuration = bundle.getLong(ConstantValue.EXERCISES_DURATION);
            restsDuration = bundle.getLong(ConstantValue.RESTS_DURATION);
            isExercisesDuration = bundle.getBoolean(ConstantValue.DURATION_SETTINGS_TYPE);
        }
        return settingsBottomSheetDialogFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_fragment_settings_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initVar();
        if (isExercisesDuration) {
            setupView(exerscisesDuration, 15000, 15, 5000.0);
        } else {
            setupView(restsDuration, 5000, 25, 1000.0);
        }
    }

    @Override
    public void onClick(View view) {
        if (isExercisesDuration){
            settingsBottomSheetDialogListener.onTvDoneClick(newExercisesDuration, isExercisesDuration);
        } else {
            settingsBottomSheetDialogListener.onTvDoneClick(newRestsDuration, isExercisesDuration);
        }
        dismiss();
    }

    private void setupView(long currDuration, final long minDuration, int seekbarMaxValue, final double valuePerDrag) {
        double currProgress = (currDuration - minDuration) / valuePerDrag;
        tvSeekbarValue.setText(currDuration / 1000 + " sec");
        sbDuration.setMax(seekbarMaxValue);
        sbDuration.setProgress((int) currProgress);
        sbDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSeekbarValue.setText((int) (((progress * valuePerDrag) + minDuration) / 1000) + " sec");
                if (isExercisesDuration) {
                    newExercisesDuration = (long) ((progress * valuePerDrag) + minDuration);
                } else {
                    newRestsDuration = (long) ((progress * valuePerDrag) + minDuration);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public interface SettingsBottomSheetDialogListener {
        void onTvDoneClick(long newDuration, boolean isExercisesDuration);
    }

    private void initView(View view) {
        tvSeekbarValue = view.findViewById(R.id.tv_seekbar_value);
        tvDone = view.findViewById(R.id.tv_done);
        sbDuration = view.findViewById(R.id.sb_duration);
        tvDone.setOnClickListener(this);
    }

    private void initVar() {
        settingsBottomSheetDialogListener = (SettingsBottomSheetDialogListener) getTargetFragment();
        newExercisesDuration = exerscisesDuration;
        newRestsDuration = restsDuration;
    }
}
