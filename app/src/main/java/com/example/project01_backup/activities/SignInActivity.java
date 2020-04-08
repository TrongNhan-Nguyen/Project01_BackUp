package com.example.project01_backup.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project01_backup.R;
import com.example.project01_backup.dao.DAO_User;
import com.example.project01_backup.model.FirebaseCallback;
import com.example.project01_backup.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class SignInActivity extends AppCompatActivity {
    private EditText etEmail, etPass;
    private Button btnLogIn, btnJustGo;
    private TextView tvSignUp, tvForgot;
    private FirebaseAuth mAuth;
    private String password;
    private DAO_User dao_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().setTitle("SIGN IN");
        initView();
    }

    private void initView() {
        dao_user = new DAO_User(this);
        etEmail = (EditText) findViewById(R.id.signIn_etEmail);
        etPass = (EditText) findViewById(R.id.signIn_etPass);
        btnLogIn = (Button) findViewById(R.id.signIn_btnLogin);
        btnJustGo = (Button) findViewById(R.id.signIn_btnJustGo);
        tvSignUp = (TextView) findViewById(R.id.signIn_tvSignUp);
        tvForgot = (TextView) findViewById(R.id.signIn_tvForgot);
        mAuth = FirebaseAuth.getInstance();

        btnJustGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, MainActivity.class));
            }
        });

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etEmail.getText().toString();
                String pass = etPass.getText().toString();
                if (email.isEmpty() || pass.isEmpty()){
                    toast("Vui lòng điền đầy đủ thông tin");
                }else {
                    password = pass;
                    mAuth.signInWithEmailAndPassword(email,pass)
                            .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        toast("Đăng nhập thành công");
                                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                        intent.putExtra("pass", password);
                                        intent.putExtra("email", email);
                                        startActivity(intent);
                                        finish();
                                        startActivity(intent);
                                    }else {
                                        toast("Lỗi đăng nhập");
                                    }
                                }
                            });
                }
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });

        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogForgot();
            }
        });

    }

    private void dialogForgot(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_forgot_pasword);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final EditText etEmail = (EditText) dialog.findViewById(R.id.dForgot_etEmail);
        final EditText etPass = (EditText) dialog.findViewById(R.id.dForgot_etPass);
        Button btnNext = (Button) dialog.findViewById(R.id.dForgot_btnNext);
        Button btnCancel = (Button) dialog.findViewById(R.id.dForgot_btnCancel);
        etPass.setKeyListener(null);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etEmail.getText().toString();
                if (email.isEmpty()){
                    toast("Vui lòng nhập email");
                    return;
                }
                dao_user.getAllData(new FirebaseCallback(){
                    @Override
                    public void userList(List<User> userList) {
                        for (User user : userList){
                            if (user.getEmail().equals(email)){
                                etPass.setText("Your password: " + user.getPassword());
                                return;
                            }
                        }
                        etPass.setText("Couldn't find your email");
                    }
                });


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void log (String s){
        Log.d("log",s);
    }
    private void toast (String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
