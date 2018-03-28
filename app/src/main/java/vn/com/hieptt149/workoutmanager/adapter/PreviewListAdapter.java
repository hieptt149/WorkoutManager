package vn.com.hieptt149.workoutmanager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import vn.com.hieptt149.workoutmanager.R;

/**
 * Created by Administrator on 03/23/2018.
 */

public class PreviewListAdapter extends RecyclerView.Adapter<PreviewListAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivItemPreview;
        private TextView tvItemTitle;
        public ViewHolder(View itemView) {
            super(itemView);
            ivItemPreview = itemView.findViewById(R.id.iv_item_preview);
            tvItemTitle = itemView.findViewById(R.id.tv_item_title);
        }
    }
}
