package vn.com.hieptt149.workoutmanager.home.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.home.MainActivity;
import vn.com.hieptt149.workoutmanager.home.MainActivityIntf;
import vn.com.hieptt149.workoutmanager.login.LoginActivity;
import vn.com.hieptt149.workoutmanager.model.ConstantValue;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener, SettingsBottomSheetDialogFragment.SettingsBottomSheetDialogListener {

    private MainActivity mainActivity;
    private MainActivityIntf mainActivityIntf;
    private SharedPreferences sharedPreferences;
    private Switch swSounds;
    private LinearLayout lnExercisesDuration, lnRestsDuration;
    private TextView tvExercisesDuration, tvRestsDuration, tvLogin;
    private Spinner spnThemes;
    private long exerscisesDuration, restsDuration;
    private Bundle bundle;

    public static SettingsFragment newInstance() {
        SettingsFragment settingsFragment = new SettingsFragment();
        return settingsFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivityIntf = (MainActivityIntf) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swSounds = view.findViewById(R.id.sw_sounds);
        initView(view);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        bundle = new Bundle();
        mainActivity = (MainActivity) getActivity();
        exerscisesDuration = sharedPreferences.getLong(ConstantValue.EXERCISES_DURATION, ConstantValue.DEFAULT_EXERCISES_DURATION);
        restsDuration = sharedPreferences.getLong(ConstantValue.RESTS_DURATION, ConstantValue.DEFAULT_RESTS_DURATION);
        tvExercisesDuration.setText(exerscisesDuration / 1000 + " sec");
        tvRestsDuration.setText(restsDuration / 1000 + " sec");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ln_exercises_duration:
                bundle.putLong(ConstantValue.EXERCISES_DURATION, exerscisesDuration);
                bundle.putBoolean(ConstantValue.DURATION_SETTINGS_TYPE, true);
                mainActivityIntf.showBottomSheetDialogFragment(SettingsFragment.this,
                        SettingsBottomSheetDialogFragment.newInstance(bundle), ConstantValue.EXERCISES_DURATION);
                break;
            case R.id.ln_rests_duration:
                bundle.putLong(ConstantValue.RESTS_DURATION, restsDuration);
                bundle.putBoolean(ConstantValue.DURATION_SETTINGS_TYPE, false);
                mainActivityIntf.showBottomSheetDialogFragment(SettingsFragment.this,
                        SettingsBottomSheetDialogFragment.newInstance(bundle), ConstantValue.RESTS_DURATION);
                break;
            case R.id.tv_login:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
        }
    }

    @Override
    public void onTvDoneClick(long currDuration, boolean isExercisesDuration) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isExercisesDuration) {
            exerscisesDuration = currDuration;
            tvExercisesDuration.setText(exerscisesDuration / 1000 + " sec");
            editor.putLong(ConstantValue.EXERCISES_DURATION, currDuration);
        } else {
            restsDuration = currDuration;
            tvRestsDuration.setText(restsDuration / 1000 + " sec");
            editor.putLong(ConstantValue.RESTS_DURATION, currDuration);
        }
        editor.apply();
    }

    private void initView(View view) {
        lnExercisesDuration = view.findViewById(R.id.ln_exercises_duration);
        tvExercisesDuration = view.findViewById(R.id.tv_exercises_duration);
        lnRestsDuration = view.findViewById(R.id.ln_rests_duration);
        tvRestsDuration = view.findViewById(R.id.tv_rests_duration);
        spnThemes = view.findViewById(R.id.spn_themes);
        tvLogin = view.findViewById(R.id.tv_login);
        lnExercisesDuration.setOnClickListener(this);
        lnRestsDuration.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
    }
}
