package vn.com.hieptt149.workoutmanager.home.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.mikephil.charting.data.Entry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.home.MainActivityIntf;
import vn.com.hieptt149.workoutmanager.login.LoginActivity;
import vn.com.hieptt149.workoutmanager.login.UserInfoDialog;
import vn.com.hieptt149.workoutmanager.model.ConstantValue;
import vn.com.hieptt149.workoutmanager.model.User;
import vn.com.hieptt149.workoutmanager.utils.Common;
import vn.com.hieptt149.workoutmanager.utils.DisplayView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener, SettingsBottomSheetDialogFragment.SettingsBottomSheetDialogListener {

    private DatabaseReference currUserRef;
    private FirebaseAuth auth;
    private MainActivityIntf mainActivityIntf;
    private SharedPreferences sharedPreferences;
    //    private Switch swSounds;
    private LinearLayout lnUsersSettings, lnExercisesDuration, lnRestsDuration;
    private ImageView ivUserAvatar, ivWeightChart;
    private TextView tvUsersName, tvUsersAge, tvUsersGender, tvUsersHeight, tvUsersWeight, tvChangePassword,
            tvExercisesDuration, tvRestsDuration, tvLogin, tvUpdateHeightWeight;
    //    private Spinner spnThemes;
    private long exerscisesDuration, restsDuration;
    private Bundle bundle;
    private User currUser;

    private static boolean isLogin;

    public static SettingsFragment newInstance() {
        SettingsFragment settingsFragment = new SettingsFragment();
        return settingsFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivityIntf = (MainActivityIntf) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (auth.getCurrentUser() != null) {
            currUserRef = FirebaseDatabase.getInstance().getReference().child(ConstantValue.USER)
                    .child(auth.getCurrentUser().getUid());
            getCurrUserInformation();
            isLogin = true;
            tvLogin.setText(R.string.logout);
        } else {
            isLogin = false;
            lnUsersSettings.setVisibility(View.GONE);
            tvUpdateHeightWeight.setVisibility(View.GONE);
            tvChangePassword.setVisibility(View.GONE);
            tvLogin.setText(R.string.login);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
    }

    @Override
    public void onClick(View view) {
        bundle = new Bundle();
        switch (view.getId()) {
            case R.id.iv_weight_chart:
                bundle.putSerializable(ConstantValue.CURRENT_USER, currUser);
                mainActivityIntf.showDialogFragment(SettingsFragment.this,
                        WeightHistoryDialogFragment.newInstance(bundle), ConstantValue.WEIGHT_HISTORY);
                break;
            case R.id.tv_update_height_weight:
                if (auth.getCurrentUser().getDisplayName() != null && currUser.getAge() != 0) {
                    bundle.putBoolean(ConstantValue.CHANGE_PASSWORD, false);
                    mainActivityIntf.showDialogFragment(SettingsFragment.this,
                            UpdateUserInfoDialogFragment.newInstance(bundle), ConstantValue.UPDATE_USER_INFO);
                } else {
                    UserInfoDialog userInfoDialog = new UserInfoDialog(getContext(),ConstantValue.SETTINGS);
                    userInfoDialog.show();
                }
                break;
            case R.id.tv_change_password:
                bundle.putBoolean(ConstantValue.CHANGE_PASSWORD, true);
                mainActivityIntf.showDialogFragment(SettingsFragment.this,
                        UpdateUserInfoDialogFragment.newInstance(bundle), ConstantValue.UPDATE_USER_INFO);
                break;
            case R.id.ln_exercises_duration:
                bundle.putLong(ConstantValue.EXERCISES_DURATION, exerscisesDuration);
                bundle.putBoolean(ConstantValue.DURATION_SETTINGS_TYPE, true);
                mainActivityIntf.showDialogFragment(SettingsFragment.this,
                        SettingsBottomSheetDialogFragment.newInstance(bundle), ConstantValue.EXERCISES_DURATION);
                break;
            case R.id.ln_rests_duration:
                bundle.putLong(ConstantValue.RESTS_DURATION, restsDuration);
                bundle.putBoolean(ConstantValue.DURATION_SETTINGS_TYPE, false);
                mainActivityIntf.showDialogFragment(SettingsFragment.this,
                        SettingsBottomSheetDialogFragment.newInstance(bundle), ConstantValue.RESTS_DURATION);
                break;
            case R.id.tv_login:
                if (!isLogin) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    DisplayView.showAlertDialog(getContext(), getString(R.string.confirm_logout), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            auth.signOut();
                            isLogin = false;
                            lnUsersSettings.setVisibility(View.GONE);
                            tvUpdateHeightWeight.setVisibility(View.GONE);
                            tvChangePassword.setVisibility(View.GONE);
                            tvLogin.setText(R.string.login);
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                }
                break;
        }
    }

    @Override
    public void onTvDoneClick(long currDuration, boolean isExercisesDuration) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isExercisesDuration) {
            exerscisesDuration = currDuration;
            tvExercisesDuration.setText(exerscisesDuration / 1000 + " sec");
            editor.putLong(ConstantValue.EXERCISES_DURATION, currDuration);
        } else {
            restsDuration = currDuration;
            tvRestsDuration.setText(restsDuration / 1000 + " sec");
            editor.putLong(ConstantValue.RESTS_DURATION, currDuration);
        }
        editor.apply();
    }

    /**
     * Lấy thông tin của người dùng đã đăng nhập
     */
    private void getCurrUserInformation() {
        currUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    currUser = dataSnapshot.getValue(User.class);
                    currUser.setId(dataSnapshot.getKey());
                } else {
                    currUser = new User();
                    currUser.setId(auth.getCurrentUser().getUid());
                }
                lnUsersSettings.setVisibility(View.VISIBLE);
                tvUpdateHeightWeight.setVisibility(View.VISIBLE);
                tvChangePassword.setVisibility(View.VISIBLE);
                String url = "http://picasaweb.google.com/data/entry/api/user/" + auth.getCurrentUser().getEmail() + "?alt=json";
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject object = response.getJSONObject("entry").getJSONObject("gphoto$thumbnail");
                                    String imgUrl = object.getString("$t").replace("s64", "s200");
                                    RequestOptions requestOptions = new RequestOptions();
                                    requestOptions.placeholder(R.drawable.avatar);
                                    requestOptions.circleCrop();
                                    Glide.with(getContext()).load(imgUrl).apply(requestOptions).into(ivUserAvatar);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ivUserAvatar.setImageResource(R.drawable.avatar);
                    }
                });
                Volley.newRequestQueue(getContext()).add(request);
                tvUsersName.setText(auth.getCurrentUser().getDisplayName() != null && !auth.getCurrentUser().getDisplayName().equals("") ? auth.getCurrentUser().getDisplayName() : getString(R.string.user));
                tvUsersAge.setText(currUser.getAge() != 0 ? getString(R.string.age) + ": " + currUser.getAge() : getString(R.string.unknown_age));
                if (currUser.getAge() != 0) {
                    tvUsersGender.setText(currUser.getGender() ? getString(R.string.male) : getString(R.string.female));
                } else {
                    tvUsersGender.setText(getString(R.string.unknown_gender));
                }
                tvUsersHeight.setText(currUser.getHeight() != 0 ? getString(R.string.height) + ": " + currUser.getHeight() + " cm" : getString(R.string.unknown_height));
                tvUsersWeight.setText(currUser.getWeight() != 0 ? getString(R.string.weight) + ": " + currUser.getWeight() + " kg" : getString(R.string.unknown_weight));
                DisplayView.dismissProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                DisplayView.showToast(getContext(), "Can't get your information. Please check your connection");
                DisplayView.dismissProgressDialog();
            }
        });
    }

    private void init(View view) {
        lnUsersSettings = view.findViewById(R.id.ln_users_settings);
        ivUserAvatar = view.findViewById(R.id.iv_user_avatar);
        tvUsersName = view.findViewById(R.id.tv_users_name);
        tvUsersAge = view.findViewById(R.id.tv_users_age);
        tvUsersGender = view.findViewById(R.id.tv_users_gender);
        tvUsersHeight = view.findViewById(R.id.tv_users_height);
        tvUsersWeight = view.findViewById(R.id.tv_users_weight);
        ivWeightChart = view.findViewById(R.id.iv_weight_chart);
        tvUpdateHeightWeight = view.findViewById(R.id.tv_update_height_weight);
        tvChangePassword = view.findViewById(R.id.tv_change_password);
//        swSounds = view.findViewById(R.id.sw_sounds);
        lnExercisesDuration = view.findViewById(R.id.ln_exercises_duration);
        tvExercisesDuration = view.findViewById(R.id.tv_exercises_duration);
        lnRestsDuration = view.findViewById(R.id.ln_rests_duration);
        tvRestsDuration = view.findViewById(R.id.tv_rests_duration);
//        spnThemes = view.findViewById(R.id.spn_themes);
        tvLogin = view.findViewById(R.id.tv_login);
        ivWeightChart.setOnClickListener(this);
        tvUpdateHeightWeight.setOnClickListener(this);
        tvChangePassword.setOnClickListener(this);
        lnExercisesDuration.setOnClickListener(this);
        lnRestsDuration.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        exerscisesDuration = sharedPreferences.getLong(ConstantValue.EXERCISES_DURATION, ConstantValue.DEFAULT_EXERCISES_DURATION);
        restsDuration = sharedPreferences.getLong(ConstantValue.RESTS_DURATION, ConstantValue.DEFAULT_RESTS_DURATION);
        tvExercisesDuration.setText(exerscisesDuration / 1000 + " sec");
        tvRestsDuration.setText(restsDuration / 1000 + " sec");
    }
}
