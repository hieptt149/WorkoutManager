package vn.com.hieptt149.workoutmanager.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

/**
 * Created by Administrator on 03/28/2018.
 */

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<Exercise> lstExercises = new ArrayList<>();
    private ProgressDialog progressDialog;
//    private RequestOptions requestOptions;

    public ExerciseListAdapter(Activity activity, DatabaseReference exerciseRef) {
        this.activity = activity;
        progressDialog = new ProgressDialog(activity);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        exerciseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lstExercises.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    lstExercises.add(snapshot.getValue(Exercise.class));
                }
                notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Exercise exercise = lstExercises.get(position);
        holder.tvExerciseName.setText(exercise.getName());
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
