package vn.com.hieptt149.workoutmanager;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import vn.com.hieptt149.workoutmanager.profile.ProfileFragment;
import vn.com.hieptt149.workoutmanager.settings.SettingsFragment;
import vn.com.hieptt149.workoutmanager.workout.WorkoutFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private LinearLayout lnAppContainer;
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lnAppContainer = findViewById(R.id.ln_app_container);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.ln_app_container,new WorkoutFragment());
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_workout:
                loadFragment(new WorkoutFragment());
                return true;
            case R.id.menu_item_profile:
                loadFragment(new ProfileFragment());
                return true;
            case R.id.menu_item_settings:
                loadFragment(new SettingsFragment());
                return true;
        }
        return false;
    }

    private void loadFragment(Fragment fragment){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ln_app_container,fragment);
        fragmentTransaction.commit();
    }
}
