package vn.com.hieptt149.workoutmanager.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.adapter.HomeScreenPagerAdapter;
import vn.com.hieptt149.workoutmanager.model.ConstantValue;
import vn.com.hieptt149.workoutmanager.utils.CustomViewPager;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, MainActivityIntf {

    private BottomNavigationView bottomNavigationView;
    private CustomViewPager vpAppContainer;
    private HomeScreenPagerAdapter homeScreenPagerAdapter;
    private TextView tvAppToolbarTitle;
    private SharedPreferences sharedPreferences;
    private DatabaseReference usersRef;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_workout:
                tvAppToolbarTitle.setText(R.string.workout);
                vpAppContainer.setCurrentItem(0);
                return true;
            case R.id.menu_item_history:
                tvAppToolbarTitle.setText(R.string.history);
                vpAppContainer.setCurrentItem(1);
                return true;
            case R.id.menu_item_settings:
                tvAppToolbarTitle.setText(R.string.settings);
                vpAppContainer.setCurrentItem(2);
                return true;
        }
        return false;
    }

    @Override
    public void showDialogFragment(Fragment targetFragment, DialogFragment dialogFragment, String tag) {
        dialogFragment.setTargetFragment(targetFragment, 1);
        dialogFragment.show(fragmentManager, tag);
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

    private void init() {
        bottomNavigationView = findViewById(R.id.bottom_nav);
        vpAppContainer = findViewById(R.id.vp_app_container);
        tvAppToolbarTitle = findViewById(R.id.tv_app_toolbar_title);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        usersRef = FirebaseDatabase.getInstance().getReference().child(ConstantValue.USER);
        fragmentManager = getSupportFragmentManager();
        homeScreenPagerAdapter = new HomeScreenPagerAdapter(getSupportFragmentManager());
        //set số page được off screen
        vpAppContainer.setOffscreenPageLimit(2);
        vpAppContainer.setAdapter(homeScreenPagerAdapter);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(0);
        tvAppToolbarTitle.setText(R.string.workout);
    }
}
