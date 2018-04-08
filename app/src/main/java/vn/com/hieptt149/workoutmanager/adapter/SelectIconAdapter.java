package vn.com.hieptt149.workoutmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import vn.com.hieptt149.workoutmanager.R;

/**
 * Created by Administrator on 04/08/2018.
 */

public class SelectIconAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> lstIconTags;

    public SelectIconAdapter(Context context, ArrayList<String> lstIconTags) {
        this.context = context;
        this.lstIconTags = lstIconTags;
    }

    @Override
    public int getCount() {
        return lstIconTags.size();
    }

    @Override
    public Object getItem(int i) {
        return lstIconTags.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_icon,null);
            viewHolder.ivIcon = view.findViewById(R.id.iv_icon);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        String iconTag = lstIconTags.get(i);
        int imgRes = context.getResources().getIdentifier(iconTag,"drawable",context.getPackageName());
        viewHolder.ivIcon.setImageResource(imgRes);
        return view;
    }

    public class ViewHolder{
        public ImageView ivIcon;
    }
}
