package vn.com.hieptt149.workoutmanager.login;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.model.ConstantValue;
import vn.com.hieptt149.workoutmanager.utils.Common;
import vn.com.hieptt149.workoutmanager.utils.DisplayView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener,
        View.OnFocusChangeListener, View.OnTouchListener {

    private DatabaseReference userRef;
    private FirebaseAuth auth;
    private TextView tvTitle, tvForgotPassword, tvBack;
    private EditText edtEmail, edtPassword;
    private RadioGroup rdgrLoginRegister;
    private RadioButton rdbtnLogin, rdbtnRegister;
    private Button btnLoginRegister;
    private String email, password;
    private Matcher matcher;

    private static boolean isPasswordShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login_register:
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
                if (Common.haveNetworkConnection(LoginActivity.this)) {
                    if (rdbtnLogin.isChecked()) {
                        login();
                    } else if (rdbtnRegister.isChecked()) {
                        register();
                    }
                } else {
                    DisplayView.dismissProgressDialog();
                    DisplayView.showToast(LoginActivity.this, getString(R.string.no_connection));
                }
                break;
            case R.id.tv_forgot_password:
                email = edtEmail.getText().toString();
                matcher = Pattern.compile(ConstantValue.validemail).matcher(email);
                if (email.length() == 0 || email.isEmpty()) {
                    edtEmail.setError(getString(R.string.enter_email));
                    return;
                } else if (!matcher.matches()) {
                    edtEmail.setError(getString(R.string.not_email));
                    return;
                }
                if (Common.haveNetworkConnection(LoginActivity.this)) {
                    sendResetPasswordInstructions();
                } else {
                    DisplayView.showToast(LoginActivity.this, getString(R.string.no_connection));
                }
                break;
            case R.id.tv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
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

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            edtPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.key, 0, R.drawable.eye, 0);
            edtPassword.setOnTouchListener(this);
        } else {
            edtPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.key, 0, 0, 0);
            edtPassword.setOnTouchListener(null);
            edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            isPasswordShow = false;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int DRAWABLE_RIGHT = 2;
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            if (motionEvent.getRawX() >= (edtPassword.getRight() - edtPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                isPasswordShow = !isPasswordShow;
                if (!isPasswordShow) {
                    edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    edtPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Đăng nhập tài khoản
     */
    private void login() {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                DisplayView.dismissProgressDialog();
                if (task.isSuccessful()) {
                    finish();
                    Toast.makeText(LoginActivity.this, getString(R.string.login_success) + " " + auth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        throw task.getException();
                    }
                    //Email không tồn tại hoặc đã bị xóa
                    catch (FirebaseAuthInvalidUserException e) {
                        edtEmail.setError(getString(R.string.email_not_exist));
                    }
                    //Nhập sai password
                    catch (FirebaseAuthInvalidCredentialsException e) {
                        edtPassword.setError(getString(R.string.wrong_password));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * Đăng ký tài khoản
     */
    private void register() {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        DisplayView.dismissProgressDialog();
                        if (task.isSuccessful()) {
                            UserInfoDialog userInfoDialog = new UserInfoDialog(LoginActivity.this);
                            userInfoDialog.setCancelable(false);
                            userInfoDialog.setCanceledOnTouchOutside(false);
                            userInfoDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    finish();
                                    Toast.makeText(LoginActivity.this, R.string.register_successfully, Toast.LENGTH_SHORT).show();
                                }
                            });
                            userInfoDialog.show();
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

    /**
     * Gửi hướng dẫn reset mật khẩu
     */
    private void sendResetPasswordInstructions() {
        DisplayView.showConfirmAlertDialog(this, "Is " + email + " your email?" + getString(R.string.send_reset_password),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, R.string.send_reset_password_success, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, R.string.send_reset_password_failed, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
    }

    private void init() {
        tvTitle = findViewById(R.id.tv_title);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvBack = findViewById(R.id.tv_back);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        rdgrLoginRegister = findViewById(R.id.rdgr_login_register);
        rdbtnLogin = findViewById(R.id.rdbtn_login);
        rdbtnRegister = findViewById(R.id.rdbtn_register);
        btnLoginRegister = findViewById(R.id.btn_login_register);
        edtPassword.setOnFocusChangeListener(this);
        btnLoginRegister.setOnClickListener(this);
        rdgrLoginRegister.setOnCheckedChangeListener(this);
        tvForgotPassword.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();
        rdbtnLogin.setChecked(true);
    }
}
