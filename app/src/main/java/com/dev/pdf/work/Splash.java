package com.dev.pdf.work;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.dev.pdf.work.Login.LoginActivity;

public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.clear();
//        editor.commit();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Splash.this, LoginActivity.class));
                finish();
            }
        }, 2000);
    }
}
