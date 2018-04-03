package vn.com.hieptt149.workoutmanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.model.Exercise;

/**
 * Created by Administrator on 04/01/2018.
 */

public class ExercisePreviewAdapter extends RecyclerView.Adapter<ExercisePreviewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Exercise> lstExercises;

    public ExercisePreviewAdapter(Context context, ArrayList<Exercise> lstExercises) {
        this.context = context;
        this.lstExercises = lstExercises;
    }

    @Override
    public ExercisePreviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExercisePreviewAdapter.ViewHolder holder, int position) {
        Exercise exercise = lstExercises.get(position);
        holder.lnExerciseItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.no_image)
                .error(R.drawable.no_connection);
        Glide.with(context)
                .load(exercise.getUrl())
                .apply(options)
                .into(holder.ivExercisePreview);
        holder.tvExerciseName.setText(exercise.getName());
    }

    @Override
    public int getItemCount() {
        return lstExercises.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout lnExerciseItem;
        private ImageView ivExercisePreview;
        private TextView tvExerciseName;

        public ViewHolder(View itemView) {
            super(itemView);
            lnExerciseItem = itemView.findViewById(R.id.ln_preview_item);
            ivExercisePreview = itemView.findViewById(R.id.iv_preview_item);
            tvExerciseName = itemView.findViewById(R.id.tv_item_title);
        }
    }
}
