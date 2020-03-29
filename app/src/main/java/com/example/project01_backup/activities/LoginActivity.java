package com.example.project01_backup.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project01_backup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPass;
    private Button btnLogIn, btnJustGo;
    private TextView tvSignUp;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        etEmail = (EditText) findViewById(R.id.login_etEmail);
        etPass = (EditText) findViewById(R.id.login_etPass);
        btnLogIn = (Button) findViewById(R.id.login_btnLogin);
        btnJustGo = (Button) findViewById(R.id.login_btnJustGo);
        tvSignUp = (TextView) findViewById(R.id.login_tvSignUp);
        mAuth = FirebaseAuth.getInstance();

        btnJustGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String pass = etPass.getText().toString();
                if (email.isEmpty() || pass.isEmpty()){
                    toast("Vui lòng điền đầy đủ thông tin");
                }else {
                    mAuth.signInWithEmailAndPassword(email,pass)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        toast("Đăng nhập thành công");
                                        finish();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
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
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }

    private void log (String s){
        Log.d("log",s);
    }
    private void toast (String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
