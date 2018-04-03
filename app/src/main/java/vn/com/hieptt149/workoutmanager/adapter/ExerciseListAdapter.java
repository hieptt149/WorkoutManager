package vn.com.hieptt149.workoutmanager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.addworkout.fragment.AddExerciseFragmentIntf;
import vn.com.hieptt149.workoutmanager.model.Exercise;

/**
 * Created by Administrator on 03/28/2018.
 */

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ViewHolder> {

    private ArrayList<Exercise> lstExercises;
    private AddExerciseFragmentIntf addExerciseFragmentIntf;

    public ExerciseListAdapter(ArrayList<Exercise> lstExercises,AddExerciseFragmentIntf addExerciseFragmentIntf) {
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
        if (exercise.isAdded() == true){
            holder.ivSelectExercise.setImageLevel(1);
        } else {
            holder.ivSelectExercise.setImageLevel(0);
        }
        holder.ivSelectExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return lstExercises.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivSelectExercise;
        private TextView tvExerciseName;

        public ViewHolder(View itemView) {
            super(itemView);
            ivSelectExercise = itemView.findViewById(R.id.iv_select_exercise);
            tvExerciseName = itemView.findViewById(R.id.tv_exercise_name);
        }
    }
}
