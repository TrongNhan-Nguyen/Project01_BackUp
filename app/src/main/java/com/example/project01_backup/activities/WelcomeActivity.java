package com.example.project01_backup.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.project01_backup.R;

public class WelcomeActivity extends AppCompatActivity {
    ProgressBar progressBar;
    CountDownTimer count;
    int current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
//        progressBar = (ProgressBar) findViewById(R.id.welcome_Progressbar);
//        progressBar.setProgress(0);
//        progressBar.setMax(40);
        count = new CountDownTimer(3000,500) {
            @Override
            public void onTick(long millisUntilFinished) {
//                current = progressBar.getProgress();
//                progressBar.setProgress(current + 5);
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(WelcomeActivity.this, SignInActivity.class);
                finish();
                startActivity(intent);
                Animatoo.animateFade(WelcomeActivity.this);
            }
        };
        count.start();
    }
}
