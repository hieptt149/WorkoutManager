package vn.com.hieptt149.workoutmanager.login;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vn.com.hieptt149.workoutmanager.R;
import vn.com.hieptt149.workoutmanager.model.ConstantValue;
import vn.com.hieptt149.workoutmanager.utils.DisplayView;

/**
 * A simple {@link Fragment} subclass.
 */
//public class RegisterFragment extends Fragment implements View.OnClickListener {
//
//    private FirebaseAuth firebaseAuth;
//    private TextView tvTitle, tvForgotPassword, tvBack;
//    private EditText edtEmail, edtPassword;
//    private Button btnRegister, btnCreateNew;
//    private String email, password;
//    private Matcher matcher;
//
//    public RegisterFragment() {
//        // Required empty public constructor
//    }
//
//    public static RegisterFragment newInstance() {
//        RegisterFragment registerFragment = new RegisterFragment();
//        return registerFragment;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_login, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        initView(view);
//        firebaseAuth = FirebaseAuth.getInstance();
//        btnCreateNew.setVisibility(View.GONE);
//        tvForgotPassword.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.btn_positive:
//                registerAccount();
//                break;
//            case R.id.tv_back:
//                getActivity().onBackPressed();
//                break;
//        }
//    }
//
//    private void registerAccount() {
//        email = edtEmail.getText().toString();
//        password = edtPassword.getText().toString();
//        matcher = Pattern.compile(ConstantValue.validemail).matcher(email);
//        if (email.length() == 0 || email.isEmpty()) {
//            edtEmail.setError(getString(R.string.enter_email));
//            return;
//        } else if (!matcher.matches()) {
//            edtEmail.setError(getString(R.string.not_email));
//            return;
//        }
//        if (password.length() == 0 || password.isEmpty()) {
//            edtPassword.setError(getString(R.string.enter_password));
//            return;
//        } else if (password.length() < 6) {
//            edtPassword.setError(getString(R.string.short_password));
//            return;
//        }
//        DisplayView.showProgressDialog(getContext());
//        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(),
//                new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        DisplayView.dismissProgressDialog();
//                        if (task.isSuccessful()) {
//                            Toast.makeText(getContext(), R.string.register_successfully, Toast.LENGTH_SHORT).show();
//                            getActivity().onBackPressed();
//                        } else {
//                            try {
//                                throw task.getException();
//                            }
//                            //Password có độ bảo mật yếu
//                            catch (FirebaseAuthWeakPasswordException e){
//                                edtPassword.setError(getString(R.string.weak_password));
//                            }
//                            //Email đăng ký đã tồn tại
//                            catch (FirebaseAuthUserCollisionException e){
//                                edtEmail.setError(getString(R.string.email_exist));
//                            }
//                            catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                });
//    }
//
//    private void initView(View view) {
//        tvTitle = view.findViewById(R.id.tv_title);
//        edtEmail = view.findViewById(R.id.edt_above);
//        edtPassword = view.findViewById(R.id.edt_below);
//        btnRegister = view.findViewById(R.id.btn_positive);
//        btnCreateNew = view.findViewById(R.id.btn_create_new);
//        tvForgotPassword = view.findViewById(R.id.tv_forgot_password);
//        tvBack = view.findViewById(R.id.tv_back);
//        btnRegister.setOnClickListener(this);
//        tvBack.setOnClickListener(this);
//    }
//}
