package vn.com.hieptt149.workoutmanager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import vn.com.hieptt149.workoutmanager.R;

/**
 * Created by Administrator on 04/01/2018.
 */

public class ExercisePreviewAdapter extends RecyclerView.Adapter<ExercisePreviewAdapter.ViewHolder> {



    @Override
    public ExercisePreviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ExercisePreviewAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivExercisePreview;
        private TextView tvExerciseName;

        public ViewHolder(View itemView) {
            super(itemView);
            ivExercisePreview = itemView.findViewById(R.id.iv_item_preview);
            tvExerciseName = itemView.findViewById(R.id.tv_item_title);
        }
    }
}
