package vn.com.hieptt149.workoutmanager.home.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.model.ConstantValue;
import vn.com.hieptt149.workoutmanager.model.User;
import vn.com.hieptt149.workoutmanager.utils.DisplayView;

public class UpdateUserInfoDialogFragment extends DialogFragment implements View.OnTouchListener, View.OnFocusChangeListener, View.OnClickListener {

    private TextView tvUpdateTitle;
    private EditText edtOld, edtNew, edtAge;
    private Button btnConfirm;
    private FirebaseAuth auth;
    private DatabaseReference currUserRef;
    private AuthCredential credential;
    private String oldPassword, newPassword;
    private double newHeight, newWeight;
    private int newAge;

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

    @Override
    public void onClick(View view) {
        if (isChangePassword) {
            oldPassword = edtOld.getText().toString();
            newPassword = edtNew.getText().toString();
            if (oldPassword.length() == 0 || oldPassword.isEmpty()) {
                edtOld.setError(getString(R.string.enter_password));
                return;
            } else if (oldPassword.length() < 6) {
                edtOld.setError(getString(R.string.short_password));
                return;
            }
            if (newPassword.length() == 0 || newPassword.isEmpty()) {
                edtNew.setError(getString(R.string.enter_password));
                return;
            } else if (newPassword.length() < 6) {
                edtNew.setError(getString(R.string.short_password));
                return;
            }
            DisplayView.showProgressDialog(getContext());
            credential = EmailAuthProvider.getCredential(auth.getCurrentUser().getEmail(), oldPassword);
            auth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        auth.getCurrentUser().updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    DisplayView.showToast(getContext(), getString(R.string.password_updated));
                                } else {
                                    DisplayView.showToast(getContext(), getString(R.string.update_password_failed));
                                }
                                dismiss();
                            }
                        });
                    } else {
                        try {
                            throw task.getException();
                        }
                        //Nhập sai password
                        catch (FirebaseAuthInvalidCredentialsException e) {
                            edtOld.setError(getString(R.string.wrong_password));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    DisplayView.dismissProgressDialog();
                }
            });
        } else {
            if (edtAge.getText().toString().length() == 0 || edtAge.getText().toString().isEmpty()) {
                edtAge.setError(getContext().getString(R.string.enter_age));
                return;
            }
            if (edtOld.getText().toString().length() == 0 || edtOld.getText().toString().isEmpty()) {
                edtOld.setError(getContext().getString(R.string.enter_height));
                return;
            }
            if (edtNew.getText().toString().length() == 0 || edtNew.getText().toString().isEmpty()) {
                edtNew.setError(getContext().getString(R.string.enter_weight));
                return;
            }
            newHeight = Double.parseDouble(edtOld.getText().toString());
            newWeight = Double.parseDouble(edtNew.getText().toString());
            newAge = Integer.parseInt(edtAge.getText().toString());
            DisplayView.showProgressDialog(getContext());
            currUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User userInfo = dataSnapshot.getValue(User.class);
                    currUserRef.setValue(new User(newAge, userInfo.getGender(), newHeight, newWeight));
                    dismiss();
                    DisplayView.dismissProgressDialog();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    DisplayView.showToast(getContext(), getString(R.string.update_info_failed));
                    DisplayView.dismissProgressDialog();
                }
            });
        }
    }

    private void init(View view) {
        tvUpdateTitle = view.findViewById(R.id.tv_update_title);
        edtAge = view.findViewById(R.id.edt_age);
        edtOld = view.findViewById(R.id.edt_old);
        edtNew = view.findViewById(R.id.edt_new);
        btnConfirm = view.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);
        if (isChangePassword) {
            tvUpdateTitle.setText(R.string.change_password);
            edtOld.setCompoundDrawablesWithIntrinsicBounds(R.drawable.key, 0, 0, 0);
            edtNew.setCompoundDrawablesWithIntrinsicBounds(R.drawable.key, 0, 0, 0);
            edtOld.setHint(R.string.old_password);
            edtNew.setHint(R.string.new_password);
            edtOld.setOnFocusChangeListener(this);
            edtNew.setOnFocusChangeListener(this);
        } else {
            tvUpdateTitle.setText(R.string.update_info);
            edtAge.setVisibility(View.VISIBLE);
            edtOld.setInputType(InputType.TYPE_CLASS_NUMBER);
            edtNew.setInputType(InputType.TYPE_CLASS_NUMBER);
            edtOld.setCompoundDrawablesWithIntrinsicBounds(R.drawable.height, 0, 0, 0);
            edtNew.setCompoundDrawablesWithIntrinsicBounds(R.drawable.scale, 0, 0, 0);
            edtOld.setHint(R.string.height);
            edtNew.setHint(R.string.weight);
        }
        auth = FirebaseAuth.getInstance();
        currUserRef = FirebaseDatabase.getInstance().getReference().child(ConstantValue.USER).child(auth.getCurrentUser().getUid());
    }
}
