package vn.com.hieptt149.workoutmanager.login;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.model.ConstantValue;
import vn.com.hieptt149.workoutmanager.utils.DisplayView;
import vn.com.hieptt149.workoutmanager.workoutdetails.fragment.WorkoutDetailsFragment;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private DatabaseReference userRef;
    private FirebaseAuth auth;
    private TextView tvTitle, tvForgotPassword, tvBack;
    private EditText edtEmail, edtPassword;
    private RadioGroup rdgrLoginRegister;
    private RadioButton rdbtnLogin, rdbtnRegister;
    private Button btnLoginRegister;
    private String email, password;
    private Matcher matcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        auth = FirebaseAuth.getInstance();
        rdbtnLogin.setChecked(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login_register:
                if (rdbtnLogin.isChecked()) {
                    login();
                } else if (rdbtnRegister.isChecked()) {
                    register();
                }
                break;
            case R.id.tv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.rdbtn_login:
                btnLoginRegister.setText(R.string.login);
                break;
            case R.id.rdbtn_register:
                btnLoginRegister.setText(R.string.register);
                break;
        }
    }

    private void login() {
        Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show();
    }

    private void register() {
        email = edtEmail.getText().toString();
        password = edtPassword.getText().toString();
        matcher = Pattern.compile(ConstantValue.validemail).matcher(email);
        if (email.length() == 0 || email.isEmpty()) {
            edtEmail.setError(getString(R.string.enter_email));
            return;
        } else if (!matcher.matches()) {
            edtEmail.setError(getString(R.string.not_email));
            return;
        }
        if (password.length() == 0 || password.isEmpty()) {
            edtPassword.setError(getString(R.string.enter_password));
            return;
        } else if (password.length() < 6) {
            edtPassword.setError(getString(R.string.short_password));
            return;
        }
        DisplayView.showProgressDialog(this);
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        DisplayView.dismissProgressDialog();
                        if (task.isSuccessful()) {
//                            userRef = FirebaseDatabase.getInstance().getReference().child(ConstantValue.USER);
//                            userRef.setValue(task.getResult().getUser().getUid());
                            Toast.makeText(LoginActivity.this, auth.getCurrentUser().getEmail().toString(), Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else {
                            try {
                                throw task.getException();
                            }
                            //Password có độ bảo mật yếu
                            catch (FirebaseAuthWeakPasswordException e) {
                                edtPassword.setError(getString(R.string.weak_password));
                            }
                            //Email đăng ký đã tồn tại
                            catch (FirebaseAuthUserCollisionException e) {
                                edtEmail.setError(getString(R.string.email_exist));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void initView() {
        tvTitle = findViewById(R.id.tv_title);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvBack = findViewById(R.id.tv_back);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        rdgrLoginRegister = findViewById(R.id.rdgr_login_register);
        rdbtnLogin = findViewById(R.id.rdbtn_login);
        rdbtnRegister = findViewById(R.id.rdbtn_register);
        btnLoginRegister = findViewById(R.id.btn_login_register);
        btnLoginRegister.setOnClickListener(this);
        rdgrLoginRegister.setOnCheckedChangeListener(this);
        tvBack.setOnClickListener(this);
    }
}
