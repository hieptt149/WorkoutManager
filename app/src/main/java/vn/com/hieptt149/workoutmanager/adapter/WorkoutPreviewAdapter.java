package vn.com.hieptt149.workoutmanager.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
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

public class WorkoutPreviewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Workout> lstWorkouts;

    public WorkoutPreviewAdapter(Context context, ArrayList<Workout> lstWorkouts) {
        this.context = context;
        this.lstWorkouts = lstWorkouts;
    }

    @Override
    public int getCount() {
        return lstWorkouts.size();
    }

    @Override
    public Object getItem(int i) {
        return lstWorkouts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_preview, null);
            view.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, (int) (150 * Resources.getSystem().getDisplayMetrics().density)));
            viewHolder.ivWorkoutIcon = view.findViewById(R.id.iv_preview_item);
            viewHolder.tvWorkoutTitle = view.findViewById(R.id.tv_item_title);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Workout workout = lstWorkouts.get(i);
        int imgRes = context.getResources().getIdentifier(workout.getIcon(), "drawable", context.getPackageName());
        viewHolder.ivWorkoutIcon.setImageResource(imgRes);
        viewHolder.tvWorkoutTitle.setText(workout.getTitle());
        return view;
    }

    public class ViewHolder {
        private ImageView ivWorkoutIcon;
        private TextView tvWorkoutTitle;
    }
}
