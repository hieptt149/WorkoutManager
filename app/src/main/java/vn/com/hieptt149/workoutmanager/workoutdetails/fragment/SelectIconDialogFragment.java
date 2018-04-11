package vn.com.hieptt149.workoutmanager.workoutdetails.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.adapter.SelectIconAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectIconDialogFragment extends DialogFragment {

    private GridView gvIcon;
    private SelectIconAdapter selectIconAdapter;
    private ArrayList<String> lstIconTags;
    private SelectIconDialogListener selectIconDialogListener;

    public SelectIconDialogFragment() {
    }

    public static SelectIconDialogFragment newInstance() {
        SelectIconDialogFragment selectIconDialogFragment = new SelectIconDialogFragment();
        return selectIconDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_select_icon, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gvIcon = view.findViewById(R.id.gv_icon);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        selectIconDialogListener = (SelectIconDialogListener) getTargetFragment();
        createIconList();
        selectIconAdapter = new SelectIconAdapter(getContext(),lstIconTags);
        gvIcon.setAdapter(selectIconAdapter);
        gvIcon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                selectIconDialogListener.onIconItemClick(lstIconTags.get(position));
                dismiss();
            }
        });
    }

    public void createIconList(){
        lstIconTags = new ArrayList<>();
        lstIconTags.add("bench");
        lstIconTags.add("cardiogram");
        lstIconTags.add("dumbbell");
        lstIconTags.add("fast_food");
        lstIconTags.add("juice");
        lstIconTags.add("measuring_tape");
        lstIconTags.add("music_player");
        lstIconTags.add("notepad");
        lstIconTags.add("proteins");
        lstIconTags.add("punching_bag");
        lstIconTags.add("stationary_bike");
        lstIconTags.add("stopclock");
        lstIconTags.add("water_bottle");
        lstIconTags.add("weight");
    }

    public interface SelectIconDialogListener{
        void onIconItemClick(String tag);
    }
}
