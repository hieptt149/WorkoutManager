package vn.com.hieptt149.workoutmanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.home.fragment.WorkoutFragmentIntf;
import vn.com.hieptt149.workoutmanager.model.Workout;

/**
 * Created by Administrator on 03/23/2018.
 */

public class WorkoutPreviewAdapter extends RecyclerView.Adapter<WorkoutPreviewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Workout> lstWorkouts;
    private WorkoutFragmentIntf workoutFragmentIntf;

    public WorkoutPreviewAdapter(Context context, ArrayList<Workout> lstWorkouts, WorkoutFragmentIntf workoutFragmentIntf) {
        this.context = context;
        this.lstWorkouts = lstWorkouts;
        this.workoutFragmentIntf = workoutFragmentIntf;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Workout workout = lstWorkouts.get(position);
        int imgRes = context.getResources().getIdentifier(workout.getIcon(), "drawable", context.getPackageName());
        holder.ivWorkoutIcon.setImageResource(imgRes);
        holder.tvWorkoutTitle.setText(workout.getTitle());
        holder.lnWorkoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                workoutFragmentIntf.openWorkoutDetails(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstWorkouts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout lnWorkoutItem;
        private ImageView ivWorkoutIcon;
        private TextView tvWorkoutTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            lnWorkoutItem = itemView.findViewById(R.id.ln_preview_item);
            ivWorkoutIcon = itemView.findViewById(R.id.iv_preview_item);
            tvWorkoutTitle = itemView.findViewById(R.id.tv_item_title);
        }
    }
}
