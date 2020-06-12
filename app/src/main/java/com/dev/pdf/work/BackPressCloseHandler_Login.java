package com.dev.pdf.work;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.dev.pdf.work.Login.LoginActivity;

import static android.content.Context.MODE_PRIVATE;

public class BackPressCloseHandler_Login {

    private long backKeyPressedTime = 0;
    private Toast toast;
    private Activity activity;

    public BackPressCloseHandler_Login(Activity context) {
        this.activity = context;
    }
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis(); showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            Intent intent = new Intent(activity, LoginActivity.class);
            SharedPreferences pref = activity.getSharedPreferences("pref", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
            activity.startActivity(intent);
            activity.finish(); toast.cancel();
        }
    }
    public void showGuide() {
        toast = Toast.makeText(activity, "버튼을 한번 더 누르시면 로그아웃됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }


}
