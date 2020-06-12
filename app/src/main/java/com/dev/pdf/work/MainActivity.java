package com.dev.pdf.work;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.pdf.work.Scene.JobLog_ListActivity;
import com.dev.pdf.work.Scene.SceneListActivity;

public class MainActivity extends AppCompatActivity {

    private LinearLayout imgBulid, imgDemolition;
    private TextView my_job_log;
    private BackPressCloseHandler_Login backPressCloseHandler;

    View.OnClickListener imgClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = null;
            intent = new Intent(MainActivity.this, SceneListActivity.class);
            intent.putExtra("user_id", getIntent().getStringExtra("user_id"));
            switch (view.getId()) {
                case R.id.btn_build:
//                    imgBulid.setImageResource(R.drawable.btn_bulid_click);
                    break;
                case R.id.btn_demolition:
//                    imgDemolition.setImageResource(R.drawable.btn_demolition_click);
                    break;
            }
            if (intent != null) {
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backPressCloseHandler = new BackPressCloseHandler_Login(this);

        init();

        my_job_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JobLog_ListActivity.class);
                intent.putExtra("user_id", getIntent().getStringExtra("user_id"));
                startActivity(intent);
            }
        });
    }

    private void init() {
        imgBulid = (LinearLayout) findViewById(R.id.btn_build);
        imgDemolition = (LinearLayout) findViewById(R.id.btn_demolition);
        my_job_log = findViewById(R.id.my_job_log);

        imgBulid.setOnClickListener(imgClick);
        imgDemolition.setOnClickListener(imgClick);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        imgBulid.setImageResource(R.drawable.btn_bulid_default);
//        imgDemolition.setImageResource(R.drawable.btn_demolition_default);
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}
