package vn.com.hieptt149.workoutmanager.model;

/**
 * Created by Administrator on 04/04/2018.
 */

public class ConstantValue {
    //database
    public static final String WORKOUT = "workout";
    public static final String USER = "user";
    public static final String EXERCISE = "exercise";
    public static final String HISTORY = "history";

    //tag
    public static final String ADD_EXERCISE = "add_exercise";
    public static final String ADD_WORKOUT = "add_workout";
    public static final String EXERCISE_DETAILS = "exercise_details";
    public static final String WORKOUT_DETAILS = "workout_details";
    public static final String SELECT_ICON = "select_icon";
    public static final String EXERCISES_DURATION = "exercises_duration";
    public static final String RESTS_DURATION = "rests_duration";
    public static final String START_WORKOUT = "start_workout";

    //key
    public static final String TAG = "tag";
    public static final String SELECTED_WORKOUT = "selected_workout";
    public static final String CURRENT_USER = "curr_user";
    public static final String USER_ID = "user_id";
    public static final String SELECTED_EXERCISE_LIST = "selected_exercise_list";
    public static final String SELECTED_EXERCISE = "selected_exercise";
    public static final String DURATION_SETTINGS_TYPE = "duration_type";
    public static final String WORKOUT_TITLE = "workout_title";
    public static final String TOTAL_TIME = "total_time";

    public static final String validemail = "[a-zA-Z0-9._-]{1,100}+@[a-zA-Z0-9]+\\.+[a-zA-Z0-9.]+";
    public static final int MAX_EXERSCISE_DURATION = 90000;
    public static final int MAX_RESTS_DURATION = 30000;
    public static final int DEFAULT_EXERCISES_DURATION = 30000;
    public static final int DEFAULT_RESTS_DURATION = 10000;

}
