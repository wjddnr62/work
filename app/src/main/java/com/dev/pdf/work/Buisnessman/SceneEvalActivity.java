package com.dev.pdf.work.Buisnessman;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.dev.pdf.work.Data.SceneData;
import com.dev.pdf.work.Data.SceneEval_Data;
import com.dev.pdf.work.Data.Worklist_Data;
import com.dev.pdf.work.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SceneEvalActivity extends AppCompatActivity {

    TextView location, title;
    ListView listview;
    SceneEvalAdapter sceneEvalAdapter;
    String title_data, location_data;
    private FirebaseDatabase database;
    private DatabaseReference scene_ref, worklist_ref, sceneEval_ref;
    ArrayList<String> user_id = new ArrayList<>();
    ArrayList<String> user_name = new ArrayList<>();
    ArrayList<String> user_address = new ArrayList<>();
    ArrayList<String> user_tel = new ArrayList<>();
    ArrayList<String> user_status = new ArrayList<>();
    ArrayList<String> user_key = new ArrayList<>();
    String job_kind;
    int eval_status = 0;
    int snapshot_count = 0;
    int worklist_stauts = 0;
    int code = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sceneevalactivity);

        database = FirebaseDatabase.getInstance();
        scene_ref = database.getReference("Scene");
        worklist_ref = database.getReference("worklist");
        sceneEval_ref = database.getReference("SceneEval");

        title_data = getIntent().getStringExtra("title");
        location_data = getIntent().getStringExtra("location");

        sceneEvalAdapter = new SceneEvalAdapter();

        listview = findViewById(R.id.listview);
        location = findViewById(R.id.location);
        title = findViewById(R.id.title);

        title.setText(title_data);
        location.setText(location_data);

        listview.setAdapter(sceneEvalAdapter);

        scene_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    SceneData sceneData = snapshot.getValue(SceneData.class);
                    if (title_data == null){

                    }else{
                        if (title_data.equals(sceneData.getTitle()) && location_data.equals(sceneData.getLocation())){
                            job_kind = sceneData.getJobKind();

                        }
                    }

                }
                worklist_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        user_id.clear();
                        user_name.clear();
                        user_tel.clear();
                        user_address.clear();
                        user_status.clear();
                        user_key.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Worklist_Data worklist_data = snapshot.getValue(Worklist_Data.class);
                            if (title_data.equals(worklist_data.getTitle()) && location_data.equals(worklist_data.getLocation())){
                                user_id.add(worklist_data.getId());
                                user_name.add(worklist_data.getName());
                                user_tel.add(worklist_data.getUser_tel());
                                user_address.add(worklist_data.getUser_address());
                                user_status.add(String.valueOf(worklist_data.getStatus()));
                                user_key.add(snapshot.getKey());
                            }
                        }
                        sceneEval_ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                sceneEvalAdapter = new SceneEvalAdapter();
                                listview.setAdapter(sceneEvalAdapter);
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    SceneEval_Data sceneEval_data = snapshot.getValue(SceneEval_Data.class);
                                    snapshot_count = (int) snapshot.getChildrenCount();
                                    Log.e("eval2", String.valueOf(eval_status));
                                    eval_status = 1;
                                    Log.e("wwwww ", title_data + ", " + sceneEval_data.getTitle() + ", " + location_data + ", " + sceneEval_data.getLocation());
                                    if (title_data.equals(sceneEval_data.getTitle()) && location_data.equals(sceneEval_data.getLocation())){
                                        code = 1;
                                        for (int i = 0; i < user_id.size(); i++){
                                            eval_status = 1;
                                            Log.e("qqqqq ", user_id.get(i) + ", " + sceneEval_data.getUser_id() + ", " + user_status.get(i));
                                            if (user_id.get(i).equals(sceneEval_data.getUser_id()) && user_status.get(i).equals("2")){
                                                sceneEvalAdapter.addItem(user_name.get(i), job_kind, sceneEval_data.getRating() + "점", user_tel.get(i), user_address.get(i), title_data, location_data, user_id.get(i), user_key.get(i));
                                                eval_status = 1;
                                                break;
                                            }else if (user_id.get(i).equals(sceneEval_data.getUser_id()) && user_status.get(i).equals("1")){
                                                worklist_ref.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                                eval_status = 1;
                                                sceneEvalAdapter.addItem(user_name.get(i), job_kind, "작업완료처리", user_tel.get(i), user_address.get(i), title_data, location_data, user_id.get(i), user_key.get(i));
                                                Log.e("작업완료처리", ".");
                                            }
                                        }
                                    }
                                }
                                if (code != 1){
                                    for (int i = 0; i < user_name.size(); i++){
                                        if (user_status.get(i).equals("2")){
                                            Log.e("user_name", user_name.get(i) + "," + user_id.get(i) + "," + job_kind);
                                            sceneEvalAdapter.addItem(user_name.get(i), job_kind, "평가하기", user_tel.get(i), user_address.get(i), title_data, location_data, user_id.get(i), user_key.get(i));
                                        } else if (user_status.get(i).equals("1")){
                                            Log.e("user_name", user_name.get(i) + "," + user_id.get(i) + "," + job_kind);
                                            sceneEvalAdapter.addItem(user_name.get(i), job_kind, "작업완료처리", user_tel.get(i), user_address.get(i), title_data, location_data, user_id.get(i), user_key.get(i));
                                        }

                                    }
                                }
                                sceneEvalAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        Log.e("eval1", String.valueOf(eval_status));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
