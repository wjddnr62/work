package com.dev.pdf.work.Buisnessman;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.pdf.work.Data.SceneEval_Data;
import com.dev.pdf.work.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SceneRegi2Activity extends AppCompatActivity {

    TextView user_name, user_tel, user_address, etc_text;
    Spinner spinner1, spinner2;
    ArrayList<String> data = new ArrayList<>();
    ArrayList<String> data2 = new ArrayList<>();
    Spinner_Adapter adapter;
    Spinner_Adapter2 adapter2;
    Button finish;
    int job_eval = 0, rating = 6;
    private FirebaseDatabase database;
    private DatabaseReference sceneEval_ref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sceneregi2activity);

        database = FirebaseDatabase.getInstance();
        sceneEval_ref = database.getReference("SceneEval");

        user_name = findViewById(R.id.user_name);
        user_tel = findViewById(R.id.user_tel);
        user_address = findViewById(R.id.user_address);
        etc_text = findViewById(R.id.etc_text);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        finish = findViewById(R.id.finish);

        user_name.setText(getIntent().getStringExtra("name"));
        user_tel.setText(getIntent().getStringExtra("tel"));
        user_address.setText(getIntent().getStringExtra("address"));

        data.add("작업내용 우수");
        data.add("작업내용 양호");
        data.add("작업내용 보통");
        data.add("작업내용 불성실");
        data.add("작업내용 매우 불성실");

        data2.add("0");
        data2.add("1");
        data2.add("2");
        data2.add("3");
        data2.add("4");
        data2.add("5");

        adapter = new Spinner_Adapter(this, data);
        adapter2 = new Spinner_Adapter2(this, data2);

        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter2);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (job_eval == 0 || rating == 6){
                    Toast.makeText(SceneRegi2Activity.this, "1번 항목 또는 2번 항목 중 선택 되지 않은 항목이 있습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    SceneEval_Data sceneEval_data = new SceneEval_Data();
                    if (etc_text.getText().toString() == null || etc_text.getText().toString().equals("")){
                        sceneEval_data.setEtc("");
                    }else{
                        sceneEval_data.setEtc(etc_text.getText().toString());
                    }
                    sceneEval_data.setJob_sincerity(job_eval);
                    sceneEval_data.setUser_location(user_address.getText().toString());
                    sceneEval_data.setName(user_name.getText().toString());
                    sceneEval_data.setRating(String.valueOf(rating));
                    sceneEval_data.setTitle(getIntent().getStringExtra("title"));
                    sceneEval_data.setLocation(getIntent().getStringExtra("location"));
                    sceneEval_data.setTel(user_tel.getText().toString());
                    sceneEval_data.setUser_id(getIntent().getStringExtra("user_id"));
                    sceneEval_ref.push().setValue(sceneEval_data);
                    Toast.makeText(SceneRegi2Activity.this, "평가되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("test2", String.valueOf(spinner1.getItemAtPosition(position)));
                if (spinner1.getItemAtPosition(position).equals("작업내용 우수")) {
                    job_eval = 5;
                } else if (spinner1.getItemAtPosition(position).equals("작업내용 양호")) {
                    job_eval = 4;
                } else if (spinner1.getItemAtPosition(position).equals("작업내용 보통")) {
                    job_eval = 3;
                } else if (spinner1.getItemAtPosition(position).equals("작업내용 불성실")) {
                    job_eval = 2;
                } else if (spinner1.getItemAtPosition(position).equals("작업내용 매우 불성실")) {
                    job_eval = 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("test", String.valueOf(spinner2.getItemAtPosition(position)));
                if (spinner2.getItemAtPosition(position).equals("0")) {
                    rating = 0;
                } else if (spinner2.getItemAtPosition(position).equals("1")) {
                    rating = 1;
                } else if (spinner2.getItemAtPosition(position).equals("2")) {
                    rating = 2;
                } else if (spinner2.getItemAtPosition(position).equals("3")) {
                    rating = 3;
                } else if (spinner2.getItemAtPosition(position).equals("4")) {
                    rating = 4;
                } else if (spinner2.getItemAtPosition(position).equals("5")) {
                    rating = 5;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
}
