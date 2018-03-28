package vn.com.hieptt149.workoutmanager.addworkout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import vn.com.hieptt149.workoutmanager.R;

public class AddWorkoutActivity extends AppCompatActivity implements View.OnClickListener,OpenDetailsFragmentCallback {

    private TextView tvAddWorkoutBack;
    private LinearLayout lnAddWorkoutContainer;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);
        tvAddWorkoutBack = findViewById(R.id.tv_addworkout_back);
        lnAddWorkoutContainer = findViewById(R.id.ln_addworkout_container);
        tvAddWorkoutBack.setOnClickListener(this);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.ln_addworkout_container, WorkoutDetailsFragment.newInstance());
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_addworkout_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void openFragment(Fragment fragment, String tag) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ln_addworkout_container,fragment);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0){
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}