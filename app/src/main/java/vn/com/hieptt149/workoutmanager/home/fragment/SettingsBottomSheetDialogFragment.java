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
import vn.com.hieptt149.workoutmanager.utils.DisplayView;
import vn.com.hieptt149.workoutmanager.utils.TimeFormatter;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private TextView tvSeekbarValue;
    private SeekBar sbDuration;
    private int seekbarProgress;
    private Fragment exerscisesDurationFragment, restsDurationFragment;
    private static long exerscisesDuration, restsDuration;

    public SettingsBottomSheetDialogFragment() {
    }

    public static SettingsBottomSheetDialogFragment newInstance(Bundle bundle) {
        SettingsBottomSheetDialogFragment settingsBottomSheetDialogFragment = new SettingsBottomSheetDialogFragment();
        if (bundle != null) {
            exerscisesDuration = bundle.getLong(ConstantValue.EXERCISES_DURATION);
            restsDuration = bundle.getLong(ConstantValue.RESTS_DURATION);
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
        tvSeekbarValue = view.findViewById(R.id.tv_seekbar_value);
        sbDuration = view.findViewById(R.id.sb_duration);
        exerscisesDurationFragment = getActivity().getSupportFragmentManager().findFragmentByTag(ConstantValue.EXERCISES_DURATION);
        restsDurationFragment = getActivity().getSupportFragmentManager().findFragmentByTag(ConstantValue.RESTS_DURATION);
        if (exerscisesDurationFragment != null) {
            tvSeekbarValue.setText(TimeFormatter.msTimeFormatter(exerscisesDuration));

//            sbDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                @Override
//                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//                }
//
//                @Override
//                public void onStartTrackingTouch(SeekBar seekBar) {
//
//                }
//
//                @Override
//                public void onStopTrackingTouch(SeekBar seekBar) {
//
//                }
//            });
        }
        if (restsDurationFragment != null) {
            tvSeekbarValue.setText(TimeFormatter.msTimeFormatter(restsDuration));
        }
    }
}
