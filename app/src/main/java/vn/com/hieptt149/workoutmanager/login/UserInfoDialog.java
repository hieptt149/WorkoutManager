package vn.com.hieptt149.workoutmanager.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.model.ConstantValue;
import vn.com.hieptt149.workoutmanager.model.User;
import vn.com.hieptt149.workoutmanager.utils.DisplayView;

public class UserInfoDialog extends Dialog implements View.OnClickListener {

    private FirebaseAuth auth;
    private RadioGroup rdgrGender;
    private RadioButton rdbtnMale, rdbtnFemale;
    private EditText edtName, edtAge, edtHeight, edtWeight;
    private Button btnConfirm;
    private String name;
    private int age;
    private double height, weight;
    private Activity context;
    private User user;
    private boolean gender;

    public UserInfoDialog(@NonNull Activity context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_user_info);
//        setCanceledOnTouchOutside(false);
//        setCancelable(false);
        init();
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
        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
        auth.getCurrentUser().updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                DisplayView.dismissProgressDialog();
                FirebaseDatabase.getInstance().getReference().child(ConstantValue.USER).child(auth.getCurrentUser().getUid()).setValue(user);
                dismiss();
            }
        });
    }

    private void init() {
        rdgrGender = findViewById(R.id.rdgr_gender);
        rdbtnMale = findViewById(R.id.rdbtn_male);
        rdbtnFemale = findViewById(R.id.rdbtn_female);
        edtName = findViewById(R.id.edt_name);
        edtAge = findViewById(R.id.edt_age);
        edtHeight = findViewById(R.id.edt_height);
        edtWeight = findViewById(R.id.edt_weight);
        btnConfirm = findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();
    }
}
