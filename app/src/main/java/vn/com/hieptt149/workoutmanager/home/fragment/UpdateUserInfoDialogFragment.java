package vn.com.hieptt149.workoutmanager.home.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.model.ConstantValue;

public class UpdateUserInfoDialogFragment extends DialogFragment implements View.OnTouchListener,View.OnFocusChangeListener{

    private TextView tvUpdateTitle;
    private EditText edtOld, edtNew;
    private Button btnConfirm;
    private static boolean isChangePassword, isOldPasswordShow = false, isNewPasswordShow = false;

    public UpdateUserInfoDialogFragment() {
    }

    public static UpdateUserInfoDialogFragment newInstance(Bundle bundle) {
        UpdateUserInfoDialogFragment updateUserInfoDialogFragment = new UpdateUserInfoDialogFragment();
        if (bundle != null) {
            updateUserInfoDialogFragment.setArguments(bundle);
            isChangePassword = bundle.getBoolean(ConstantValue.CHANGE_PASSWORD);
        }
        return updateUserInfoDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_update_user_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        init(view);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.edt_old:
                if (hasFocus) {
                    edtOld.setCompoundDrawablesWithIntrinsicBounds(R.drawable.key, 0, R.drawable.eye, 0);
                    edtOld.setOnTouchListener(this);
                } else {
                    edtOld.setCompoundDrawablesWithIntrinsicBounds(R.drawable.key, 0, 0, 0);
                    edtOld.setOnTouchListener(null);
                    edtOld.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isOldPasswordShow = false;
                }
                break;
            case R.id.edt_new:
                if (hasFocus) {
                    edtNew.setCompoundDrawablesWithIntrinsicBounds(R.drawable.key, 0, R.drawable.eye, 0);
                    edtNew.setOnTouchListener(this);
                } else {
                    edtNew.setCompoundDrawablesWithIntrinsicBounds(R.drawable.key, 0, 0, 0);
                    edtNew.setOnTouchListener(null);
                    edtNew.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isNewPasswordShow = false;
                }
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int DRAWABLE_RIGHT = 2;
        switch (view.getId()) {
            case R.id.edt_old:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (motionEvent.getRawX() >= (edtOld.getRight() - edtOld.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        isOldPasswordShow = !isOldPasswordShow;
                        if (!isOldPasswordShow) {
                            edtOld.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        } else {
                            edtOld.setInputType(InputType.TYPE_CLASS_TEXT);
                        }
                        return true;
                    }
                }
                break;
            case R.id.edt_new:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (motionEvent.getRawX() >= (edtNew.getRight() - edtNew.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        isNewPasswordShow = !isNewPasswordShow;
                        if (!isNewPasswordShow) {
                            edtNew.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        } else {
                            edtNew.setInputType(InputType.TYPE_CLASS_TEXT);
                        }
                        return true;
                    }
                }
                break;
        }
        return false;
    }

    private void init(View view) {
        tvUpdateTitle = view.findViewById(R.id.tv_update_title);
        edtOld = view.findViewById(R.id.edt_old);
        edtNew = view.findViewById(R.id.edt_new);
        btnConfirm = view.findViewById(R.id.btn_confirm);
        if (isChangePassword) {
            tvUpdateTitle.setText(R.string.change_password);
            edtOld.setCompoundDrawablesWithIntrinsicBounds(R.drawable.key, 0, 0, 0);
            edtNew.setCompoundDrawablesWithIntrinsicBounds(R.drawable.key, 0, 0, 0);
            edtOld.setHint(R.string.old_password);
            edtNew.setHint(R.string.new_password);
            edtOld.setOnFocusChangeListener(this);
            edtNew.setOnFocusChangeListener(this);
        } else {
            tvUpdateTitle.setText(R.string.update_height_weight);
            edtOld.setInputType(InputType.TYPE_CLASS_NUMBER);
            edtNew.setInputType(InputType.TYPE_CLASS_NUMBER);
            edtOld.setCompoundDrawablesWithIntrinsicBounds(R.drawable.height, 0, 0, 0);
            edtNew.setCompoundDrawablesWithIntrinsicBounds(R.drawable.scale, 0, 0, 0);
            edtOld.setHint(R.string.height);
            edtNew.setHint(R.string.weight);
        }
    }
}
