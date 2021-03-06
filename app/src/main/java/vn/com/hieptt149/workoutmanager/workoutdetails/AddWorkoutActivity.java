package vn.com.hieptt149.workoutmanager.workoutdetails;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.model.ConstantValue;
import vn.com.hieptt149.workoutmanager.model.Exercise;
import vn.com.hieptt149.workoutmanager.workoutdetails.fragment.WorkoutDetailsFragment;

public class AddWorkoutActivity extends AppCompatActivity implements View.OnClickListener, AddWorkoutActivityIntf {

    private TextView tvAddWorkoutBack;
    private LinearLayout lnAddWorkoutContainer;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);
        initView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_addworkout_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void openFragment(Fragment fragment, String tag) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ln_addworkout_container, fragment);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    @Override
    public void showDialogFragment(Fragment targetFragment, DialogFragment dialogFragment, String tag) {
        dialogFragment.setTargetFragment(targetFragment, 1);
        dialogFragment.show(fragmentManager, tag);
    }

    @Override
    public void addSelectedExercise(Exercise exercise) {
        WorkoutDetailsFragment workoutDetailsFragment = (WorkoutDetailsFragment) fragmentManager.findFragmentByTag(ConstantValue.WORKOUT_DETAILS);
        workoutDetailsFragment.addSelectedExercise(exercise);
    }

    @Override
    public void removeSelectedExercise(Exercise exercise) {
        WorkoutDetailsFragment workoutDetailsFragment = (WorkoutDetailsFragment) fragmentManager.findFragmentByTag(ConstantValue.WORKOUT_DETAILS);
        workoutDetailsFragment.removeSelectedExercise(exercise);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)ev.getRawX(), (int)ev.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void initView() {
        tvAddWorkoutBack = findViewById(R.id.tv_addworkout_back);
        lnAddWorkoutContainer = findViewById(R.id.ln_addworkout_container);
        tvAddWorkoutBack.setOnClickListener(this);
        bundle = getIntent().getExtras();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.ln_addworkout_container, WorkoutDetailsFragment.newInstance(bundle), ConstantValue.WORKOUT_DETAILS);
        fragmentTransaction.commit();
    }
}
