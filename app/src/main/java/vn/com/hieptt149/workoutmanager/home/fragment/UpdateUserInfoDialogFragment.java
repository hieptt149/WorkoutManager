package vn.com.hieptt149.workoutmanager.home.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.model.ConstantValue;
import vn.com.hieptt149.workoutmanager.model.User;
import vn.com.hieptt149.workoutmanager.utils.Common;
import vn.com.hieptt149.workoutmanager.utils.DisplayView;

public class UpdateUserInfoDialogFragment extends DialogFragment implements View.OnClickListener {

    private RadioGroup rdgrGender;
    private RadioButton rdbtnMale, rdbtnFemale;
    private EditText edtName, edtAge, edtHeight, edtWeight;
    private Button btnConfirm;
    private String name;
    private int age;
    private double height, weight;
    private boolean gender;
    private User user;
    private FirebaseAuth auth;
    private UpdateUserInfoListener updateUserInfoListener;

    public UpdateUserInfoDialogFragment() {
    }

    public static UpdateUserInfoDialogFragment newInstance(Bundle bundle) {
        UpdateUserInfoDialogFragment updateUserInfoDialogFragment = new UpdateUserInfoDialogFragment();
        if (bundle != null) {
            updateUserInfoDialogFragment.setArguments(bundle);
        }
        return updateUserInfoDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_user_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        init(view);
    }

    @Override
    public void onClick(View view) {
        if (!rdbtnMale.isChecked() && !rdbtnFemale.isChecked()) {
            DisplayView.showToast(getContext(), "Please select your gender");
            return;
        }
        if (edtName.getText().toString().length() == 0 || edtName.getText().toString().isEmpty()) {
            edtName.setError(getContext().getString(R.string.enter_name));
            return;
        }
        if (edtAge.getText().toString().length() == 0 || edtAge.getText().toString().isEmpty()) {
            edtAge.setError(getContext().getString(R.string.enter_age));
            return;
        } else if (Integer.parseInt(edtAge.getText().toString().trim()) <= 0 || Integer.parseInt(edtAge.getText().toString().trim()) > 150) {
            edtAge.setError(getContext().getString(R.string.invalid_age));
            return;
        }
        if (edtHeight.getText().toString().length() == 0 || edtHeight.getText().toString().isEmpty()) {
            edtHeight.setError(getContext().getString(R.string.enter_height));
            return;
        } else if (Double.parseDouble(edtHeight.getText().toString().trim()) <= 0) {
            edtHeight.setError(getContext().getString(R.string.invalid_height));
            return;
        }
        if (edtWeight.getText().toString().length() == 0 || edtWeight.getText().toString().isEmpty()) {
            edtWeight.setError(getContext().getString(R.string.enter_weight));
            return;
        } else if (Double.parseDouble(edtWeight.getText().toString().trim()) <= 0) {
            edtWeight.setError(getContext().getString(R.string.invalid_weight));
            return;
        }
        name = edtName.getText().toString().trim();
        age = Integer.parseInt(edtAge.getText().toString().trim());
        height = Double.parseDouble(edtHeight.getText().toString().trim());
        weight = Double.parseDouble(edtWeight.getText().toString().trim());
        if (rdbtnMale.isChecked()) {
            gender = true;
        } else if (rdbtnFemale.isChecked()) {
            gender = false;
        }
        user = new User(age, gender, height, weight);
        DisplayView.showProgressDialog(getContext());
        if (Common.haveNetworkConnection(getContext())) {
            UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
            auth.getCurrentUser().updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    DisplayView.dismissProgressDialog();
                    FirebaseDatabase.getInstance().getReference().child(ConstantValue.USER).child(auth.getCurrentUser().getUid()).setValue(user);
                    Bundle bundle = new Bundle();
                    bundle.putString(ConstantValue.USERS_NAME, name);
                    bundle.putInt(ConstantValue.USERS_AGE, age);
                    bundle.putBoolean(ConstantValue.USERS_GENDER, gender);
                    bundle.putDouble(ConstantValue.USERS_HEIGHT, height);
                    bundle.putDouble(ConstantValue.USERS_WEIGHT, weight);
                    updateUserInfoListener.updateUserInfo(bundle);
                    DisplayView.dismissProgressDialog();
                    dismiss();
                }
            });
        } else {
            DisplayView.showToast(getContext(), getString(R.string.no_connection));
            DisplayView.dismissProgressDialog();
        }
    }

    private void init(View view) {
        rdgrGender = view.findViewById(R.id.rdgr_gender);
        rdbtnMale = view.findViewById(R.id.rdbtn_male);
        rdbtnFemale = view.findViewById(R.id.rdbtn_female);
        edtName = view.findViewById(R.id.edt_name);
        edtAge = view.findViewById(R.id.edt_age);
        edtHeight = view.findViewById(R.id.edt_height);
        edtWeight = view.findViewById(R.id.edt_weight);
        btnConfirm = view.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();
        updateUserInfoListener = (UpdateUserInfoListener) getTargetFragment();
    }

    public interface UpdateUserInfoListener {
        void updateUserInfo(Bundle bundle);
    }
}
