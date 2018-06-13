package vn.com.hieptt149.workoutmanager.home.fragment;


import android.app.TimePickerDialog;
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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.home.MainActivityIntf;
import vn.com.hieptt149.workoutmanager.login.LoginActivity;
import vn.com.hieptt149.workoutmanager.model.ConstantValue;
import vn.com.hieptt149.workoutmanager.model.User;
import vn.com.hieptt149.workoutmanager.utils.AlarmReceiver;
import vn.com.hieptt149.workoutmanager.utils.Common;
import vn.com.hieptt149.workoutmanager.utils.DisplayView;
import vn.com.hieptt149.workoutmanager.utils.NotificationScheduler;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener,
        SettingsBottomSheetDialogFragment.SettingsBottomSheetDialogListener, UpdateUserInfoDialogFragment.UpdateUserInfoListener, CompoundButton.OnCheckedChangeListener {

    private DatabaseReference currUserRef;
    private FirebaseAuth auth;
    private MainActivityIntf mainActivityIntf;
    private SharedPreferences sharedPreferences;
    private LinearLayout lnUsersSettings, lnExercisesDuration, lnRestsDuration, lnReminder, lnReminderTime;
    private ImageView ivUserAvatar, ivWeightChart;
    private Switch swReminder;
    private TextView tvUsersName, tvUsersAge, tvUsersGender, tvUsersHeight, tvUsersWeight, tvChangePassword,
            tvExercisesDuration, tvRestsDuration, tvLogin, tvUpdateHeightWeight, tvReminderTime;
    private long exerscisesDuration, restsDuration;
    private Bundle bundle;
    private User currUser;
    private boolean isReminderEnabled;
    private String timeRemind;
    private SharedPreferences.Editor editor;
    private TimePickerDialog timePickerDialog;
    private SimpleDateFormat hhmmformat = new SimpleDateFormat("hh:mm a");

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
                mainActivityIntf.showDialogFragment(SettingsFragment.this,
                        UpdateUserInfoDialogFragment.newInstance(bundle), ConstantValue.UPDATE_USER_INFO);
                break;
            case R.id.tv_change_password:
                mainActivityIntf.showDialogFragment(SettingsFragment.this,
                        ChangePasswordDialogFragment.newInstance(bundle), ConstantValue.UPDATE_USER_INFO);
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
                    DisplayView.showConfirmAlertDialog(getContext(), getString(R.string.confirm_logout), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            auth.signOut();
                            isLogin = false;
                            tvUsersName.setText("");
                            tvUsersAge.setText("");
                            tvUsersGender.setText("");
                            tvUsersHeight.setText("");
                            tvUsersWeight.setText("");
                            ivUserAvatar.setImageResource(R.drawable.avatar);
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
            case R.id.ln_reminder_time:
                Calendar defaultTime = Calendar.getInstance();
                try {
                    defaultTime.setTime(hhmmformat.parse(timeRemind));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeRemind = hhmmformat.format(new Date(0, 0, 0, selectedHour, selectedMinute));
                        tvReminderTime.setText(timeRemind);
                        editor = sharedPreferences.edit();
                        editor.putString(ConstantValue.TIME_REMIND, timeRemind);
                        editor.apply();
                        NotificationScheduler.setReminder(getContext(), AlarmReceiver.class, selectedHour, selectedMinute);
                    }
                }, defaultTime.get(Calendar.HOUR_OF_DAY), defaultTime.get(Calendar.MINUTE), false);
                timePickerDialog.show();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        isReminderEnabled = isChecked;
        if (isChecked) {
            lnReminderTime.setVisibility(View.VISIBLE);
            Calendar currentScheduler = Calendar.getInstance();
            try {
                currentScheduler.setTime(hhmmformat.parse(timeRemind));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            NotificationScheduler.setReminder(getContext(), AlarmReceiver.class, currentScheduler.get(Calendar.HOUR_OF_DAY), currentScheduler.get(Calendar.MINUTE));
        } else {
            lnReminderTime.setVisibility(View.GONE);
            NotificationScheduler.cancelReminder(getContext(), AlarmReceiver.class);
        }
        editor = sharedPreferences.edit();
        editor.putBoolean(ConstantValue.REMINDER_STATUS, isReminderEnabled);
        editor.apply();

    }

    @Override
    public void onTvDoneClick(long currDuration, boolean isExercisesDuration) {

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

    @Override
    public void updateUserInfo(Bundle bundle) {
        if (bundle != null) {
            tvUsersName.setText(bundle.getString(ConstantValue.USERS_NAME));
            tvUsersAge.setText(getString(R.string.age) + ": " + bundle.getInt(ConstantValue.USERS_AGE));
            tvUsersGender.setText(bundle.getBoolean(ConstantValue.USERS_GENDER) ? getString(R.string.male) : getString(R.string.female));
            tvUsersHeight.setText(getString(R.string.height) + ": " + bundle.getDouble(ConstantValue.USERS_HEIGHT) + " cm");
            tvUsersWeight.setText(getString(R.string.weight) + ": " + bundle.getDouble(ConstantValue.USERS_WEIGHT) + " kg");
        }
    }

    /**
     * Lấy thông tin của người dùng đã đăng nhập
     */
    private void getCurrUserInformation() {
        if (Common.haveNetworkConnection(getContext())) {
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
                    tvUsersName.setText(auth.getCurrentUser().getDisplayName() != null && !auth.getCurrentUser().getDisplayName().equals("") ?
                            auth.getCurrentUser().getDisplayName() : getString(R.string.user));
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
        } else {
            DisplayView.showToast(getContext(), getString(R.string.no_connection));
            DisplayView.dismissProgressDialog();
        }
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
        lnExercisesDuration = view.findViewById(R.id.ln_exercises_duration);
        tvExercisesDuration = view.findViewById(R.id.tv_exercises_duration);
        lnReminder = view.findViewById(R.id.ln_reminder);
        lnReminderTime = view.findViewById(R.id.ln_reminder_time);
        lnRestsDuration = view.findViewById(R.id.ln_rests_duration);
        tvRestsDuration = view.findViewById(R.id.tv_rests_duration);
        tvReminderTime = view.findViewById(R.id.tv_reminder_time);
        swReminder = view.findViewById(R.id.sw_reminder);
        tvLogin = view.findViewById(R.id.tv_login);
        ivWeightChart.setOnClickListener(this);
        tvUpdateHeightWeight.setOnClickListener(this);
        tvChangePassword.setOnClickListener(this);
        lnExercisesDuration.setOnClickListener(this);
        lnRestsDuration.setOnClickListener(this);
        lnReminderTime.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        swReminder.setOnCheckedChangeListener(this);
        auth = FirebaseAuth.getInstance();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        exerscisesDuration = sharedPreferences.getLong(ConstantValue.EXERCISES_DURATION, ConstantValue.DEFAULT_EXERCISES_DURATION);
        restsDuration = sharedPreferences.getLong(ConstantValue.RESTS_DURATION, ConstantValue.DEFAULT_RESTS_DURATION);
        isReminderEnabled = sharedPreferences.getBoolean(ConstantValue.REMINDER_STATUS, false);
        timeRemind = sharedPreferences.getString(ConstantValue.TIME_REMIND, "08:00");
        lnReminderTime.setVisibility(isReminderEnabled ? View.VISIBLE : View.GONE);
        tvExercisesDuration.setText(exerscisesDuration / 1000 + " sec");
        tvRestsDuration.setText(restsDuration / 1000 + " sec");
        swReminder.setChecked(isReminderEnabled);
        tvReminderTime.setText(timeRemind);
    }
}
