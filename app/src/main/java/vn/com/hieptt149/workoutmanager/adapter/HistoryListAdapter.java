package vn.com.hieptt149.workoutmanager.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.model.History;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<History> lstHistories;
    private NumberFormat nf = new DecimalFormat("#.##");
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private int itemHighlight = -1;

    public HistoryListAdapter(Context context, ArrayList<History> lstHistories) {
        this.lstHistories = lstHistories;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        History history = lstHistories.get(position);
        holder.tvPracticeDate.setText(sdf.format(Long.parseLong(history.getPracticeDate())));
        holder.tvWorkoutTimes.setText(context.getString(R.string.workout_times) + " " + history.getWorkoutTimes());
        holder.tvCaloriesBurn.setText(context.getString(R.string.total_calories_burned) + " " + nf.format(history.getCaloriesBurn()) + " cal");
        holder.tvHeight.setText(context.getString(R.string.height) + ": " + history.getCurrHeight() + " cm");
        holder.tvWeight.setText(context.getString(R.string.weight) + ": " + history.getCurrWeight() + " kg");
        if (itemHighlight == position){
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.itemFocused));
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return lstHistories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvPracticeDate, tvWorkoutTimes, tvCaloriesBurn, tvHeight, tvWeight;

        public ViewHolder(View itemView) {
            super(itemView);
            tvPracticeDate = itemView.findViewById(R.id.tv_practice_date);
            tvWorkoutTimes = itemView.findViewById(R.id.tv_workout_times);
            tvCaloriesBurn = itemView.findViewById(R.id.tv_calories_burn);
            tvHeight = itemView.findViewById(R.id.tv_height);
            tvWeight = itemView.findViewById(R.id.tv_weight);
        }
    }

    public int getItemHighlight() {
        return itemHighlight;
    }

    public void setItemHighlight(int itemHighlight) {
        this.itemHighlight = itemHighlight;
    }
}
