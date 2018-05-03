package vn.com.hieptt149.workoutmanager.home.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.adapter.HistoryListAdapter;
import vn.com.hieptt149.workoutmanager.model.ConstantValue;
import vn.com.hieptt149.workoutmanager.model.History;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private FirebaseAuth auth;
    private DatabaseReference currUserHistoryRef;
    private LineChart chartHistory;
    private RecyclerView rvHistory;
    private ArrayList<History> lstHistories;
    private HistoryListAdapter historyListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;

    public static HistoryFragment newInstance() {
        HistoryFragment historyFragment = new HistoryFragment();
        return historyFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void getWorkoutHistory() {
        currUserHistoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lstHistories.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    History history = snapshot.getValue(History.class);
                    history.setUserId(auth.getCurrentUser().getUid());
                    history.setPracticeDate(snapshot.getKey());
                    lstHistories.add(history);
                }
                historyListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void init(View view) {
        chartHistory = view.findViewById(R.id.chart_history);
        rvHistory = view.findViewById(R.id.rv_history);
        auth = FirebaseAuth.getInstance();
        lstHistories = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getContext());
        dividerItemDecoration = new DividerItemDecoration(getContext(),linearLayoutManager.getOrientation());
        historyListAdapter = new HistoryListAdapter(getContext(),lstHistories);
        rvHistory.setHasFixedSize(true);
        rvHistory.setLayoutManager(linearLayoutManager);
        rvHistory.addItemDecoration(dividerItemDecoration);
        rvHistory.setAdapter(historyListAdapter);
        if (auth.getCurrentUser() != null) {
            currUserHistoryRef = FirebaseDatabase.getInstance().getReference().child(ConstantValue.HISTORY).child(auth.getCurrentUser().getUid());
            getWorkoutHistory();
        }
    }
}
