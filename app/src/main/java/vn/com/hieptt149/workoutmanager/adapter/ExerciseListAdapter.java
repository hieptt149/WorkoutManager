package vn.com.hieptt149.workoutmanager.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.addworkout.fragment.AddExerciseFragment;
import vn.com.hieptt149.workoutmanager.addworkout.fragment.AddExerciseFragmentIntf;
import vn.com.hieptt149.workoutmanager.model.Exercise;

/**
 * Created by Administrator on 03/28/2018.
 */

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Exercise> lstExercises;
    private AddExerciseFragmentIntf addExerciseFragmentIntf;

    public ExerciseListAdapter(Context context, ArrayList<Exercise> lstExercises, AddExerciseFragmentIntf addExerciseFragmentIntf) {
        this.context = context;
        this.lstExercises = lstExercises;
        this.addExerciseFragmentIntf = addExerciseFragmentIntf;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Exercise exercise = lstExercises.get(position);
        holder.tvExerciseName.setText(exercise.getName());
        holder.tvExerciseName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExerciseFragmentIntf.onExerciseItemClick(exercise);
            }
        });
        if (exercise.isAdded() == true) {
            holder.ivSelectExercise.setImageLevel(1);
        } else {
            holder.ivSelectExercise.setImageLevel(0);
        }
        RequestOptions options = new RequestOptions();
        options.error(R.drawable.no_connection);
        Glide.with(context)
                .asBitmap()
                .load(exercise.getUrl())
                .apply(options)
                .into(holder.ivExerciseIcon);
        holder.ivSelectExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (exercise.isAdded() == true) {
                    addExerciseFragmentIntf.removeExercise(exercise);
                    exercise.setAdded(false);
                } else {
                    exercise.setAdded(true);
                    addExerciseFragmentIntf.addExercise(exercise);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstExercises.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivExerciseIcon, ivSelectExercise;
        private TextView tvExerciseName;

        public ViewHolder(View itemView) {
            super(itemView);
            ivExerciseIcon = itemView.findViewById(R.id.iv_exercise_icon);
            ivSelectExercise = itemView.findViewById(R.id.iv_select_exercise);
            tvExerciseName = itemView.findViewById(R.id.tv_exercise_name);
        }
    }
}
