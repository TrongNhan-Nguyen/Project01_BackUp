package com.example.project01_backup.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.project01_backup.R;
import com.example.project01_backup.fragment.Fragment_Censorship;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminActivity extends AppCompatActivity {
    private CardView cvCensorship, cvUser, cvFeedback, cvMain, cvLogout, cvExit;
    private ScrollView container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        replaceFragment(new Fragment_Censorship());
    }

    private void initView() {
        cvCensorship = (CardView) findViewById(R.id.admin_cvCensor);
        cvUser = (CardView) findViewById(R.id.admin_cvUser);
        cvFeedback = (CardView) findViewById(R.id.admin_cvFeedback);
        cvMain = (CardView) findViewById(R.id.admin_cvMain);
        cvLogout = (CardView) findViewById(R.id.admin_cvLogout);
        cvExit = (CardView) findViewById(R.id.admin_cvExit);
        container = (ScrollView) findViewById(R.id.admin_layoutContainer);

        cvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(AdminActivity.this, LoginActivity.class));
            }
        });

        cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(AdminActivity.this, MainActivity.class));
            }
        });

        cvCensorship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new Fragment_Censorship());
            }
        });

        cvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("Quản lý người dùng");
            }
        });

        cvFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("Phản ánh người dùng");
            }
        });


    }


    private void replaceFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.admin_FrameLayout, fragment)
                .commit();
        container.setVisibility(View.GONE);
    }

    private void log (String s){
        Log.d("log",s);
    }
    private void toast (String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            startActivity(getIntent());
        }
        return super.onOptionsItemSelected(item);
    }
}
