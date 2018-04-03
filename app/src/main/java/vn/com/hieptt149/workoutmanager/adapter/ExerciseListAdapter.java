package vn.com.hieptt149.workoutmanager.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.model.Exercise;
import vn.com.hieptt149.workoutmanager.utils.DisplayView;

/**
 * Created by Administrator on 03/28/2018.
 */

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ViewHolder> {

    private ArrayList<Exercise> lstExercises;

    public ExerciseListAdapter(ArrayList<Exercise> lstExercises) {
        this.lstExercises = lstExercises;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Exercise exercise = lstExercises.get(position);
        holder.lnItemExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.tvExerciseName.setText(exercise.getName());
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

        private LinearLayout lnItemExercise;
        private ImageView ivSelectExercise;
        private TextView tvExerciseName;

        public ViewHolder(View itemView) {
            super(itemView);
            lnItemExercise = itemView.findViewById(R.id.ln_item_exercise);
            ivSelectExercise = itemView.findViewById(R.id.iv_select_exercise);
            tvExerciseName = itemView.findViewById(R.id.tv_exercise_name);
        }
    }
}
