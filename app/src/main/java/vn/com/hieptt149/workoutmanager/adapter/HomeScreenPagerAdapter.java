package vn.com.hieptt149.workoutmanager.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import vn.com.hieptt149.workoutmanager.home.fragment.HistoryFragment;
import vn.com.hieptt149.workoutmanager.home.fragment.SettingsFragment;
import vn.com.hieptt149.workoutmanager.home.fragment.WorkoutFragment;

public class HomeScreenPagerAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 3;

    public HomeScreenPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return WorkoutFragment.newInstance();
            case 1:
                return HistoryFragment.newInstance();
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
