package vn.com.hieptt149.workoutmanager.home;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.home.fragment.ProfileFragment;
import vn.com.hieptt149.workoutmanager.home.fragment.SettingsFragment;
import vn.com.hieptt149.workoutmanager.model.User;
import vn.com.hieptt149.workoutmanager.utils.CustomViewPager;
import vn.com.hieptt149.workoutmanager.home.fragment.WorkoutFragment;
import vn.com.hieptt149.workoutmanager.utils.DisplayView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private CustomViewPager vpAppContainer;
    private MyPagerAdapter myPagerAdapter;
    private TextView tvAppToolbarTitle;
    private SharedPreferences sharedPreferences;
    private DatabaseReference usersRef;
    private MainActivityIntf mainActivityIntf;
    //Demo user
    private int userId = -1;
    private User currUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        usersRef = FirebaseDatabase.getInstance().getReference().child("user");
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        //set số page được off screen
        vpAppContainer.setOffscreenPageLimit(2);
        vpAppContainer.setAdapter(myPagerAdapter);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(0);
        tvAppToolbarTitle.setText(R.string.workout);
        //Lấy id của user đã đăng nhập lần trước
//        if (sharedPreferences.contains("userid")){
//            userId = sharedPreferences.getInt("userid",-1);
//        }
        //Demo
        //Trong trường hợp không có phiên đăng nhập nào lần trước
//        if (userId == -1){
//
//        } else {
//
//        }
        userId = 9;
        getUserInformation();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_workout:
                tvAppToolbarTitle.setText(R.string.workout);
                vpAppContainer.setCurrentItem(0);
                return true;
            case R.id.menu_item_profile:
                tvAppToolbarTitle.setText(R.string.profile);
                vpAppContainer.setCurrentItem(1);
                return true;
            case R.id.menu_item_settings:
                tvAppToolbarTitle.setText(R.string.settings);
                vpAppContainer.setCurrentItem(2);
                return true;
        }
        return false;
    }

    private void initView() {
        bottomNavigationView = findViewById(R.id.bottom_nav);
        vpAppContainer = findViewById(R.id.vp_app_container);
        tvAppToolbarTitle = findViewById(R.id.tv_app_toolbar_title);
    }

    /**
     *
     */
    private void getUserInformation() {
        DisplayView.showProgressDialog(this);
        DatabaseReference userRef = usersRef.child(Integer.toString(userId));
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currUser = dataSnapshot.getValue(User.class);
                currUser.setId(userId);
                mainActivityIntf.sendCurrUserInfo(currUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                DisplayView.dismissProgressDialog();
            }
        });
    }

    public void setMainActivityIntf(MainActivityIntf mainActivityIntf){
        this.mainActivityIntf = mainActivityIntf;
    }

    private static class MyPagerAdapter extends FragmentPagerAdapter {

        private static int NUM_ITEMS = 3;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return WorkoutFragment.newInstance();
                case 1:
                    return ProfileFragment.newInstance();
                case 2:
                    return SettingsFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
    }

}
