package vn.com.hieptt149.workoutmanager.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ExercisePreviewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> lstExercisePreviewFragments;

    public ExercisePreviewPagerAdapter(FragmentManager fm,ArrayList<Fragment> lstExercisePreviewFragments) {
        super(fm);
        this.lstExercisePreviewFragments = lstExercisePreviewFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return lstExercisePreviewFragments.get(position);
    }

    @Override
    public int getCount() {
        return lstExercisePreviewFragments.size();
    }
}
