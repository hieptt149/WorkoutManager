package vn.com.hieptt149.workoutmanager.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.model.Workout;

/**
 * Created by Administrator on 03/23/2018.
 */

public class WorkoutPreviewAdapter extends RecyclerView.Adapter<WorkoutPreviewAdapter.ViewHolder> {

    private ArrayList<Workout> lstWorkouts;

    public WorkoutPreviewAdapter(ArrayList<Workout> lstWorkouts) {
        this.lstWorkouts = lstWorkouts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }
    @Override
    public int getItemCount() {
        return lstWorkouts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivWorkoutIcon;
        private TextView tvWorkoutTitle;
        public ViewHolder(View itemView) {
            super(itemView);
            ivWorkoutIcon = itemView.findViewById(R.id.iv_item_preview);
            tvWorkoutTitle = itemView.findViewById(R.id.tv_item_title);
        }
    }
}
