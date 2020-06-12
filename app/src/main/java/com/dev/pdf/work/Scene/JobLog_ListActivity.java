package com.dev.pdf.work.Scene;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dev.pdf.work.Data.SceneData;
import com.dev.pdf.work.Data.Worklist_Data;
import com.dev.pdf.work.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class JobLog_ListActivity extends AppCompatActivity {
    ArrayList<JobLog_ListItem> item = new ArrayList<>();
    private ListView listView;
    private JobLog_ListAdapter adapter;

    private Worklist_Data worklist_data;
    private JobLog_ListItem workItem;

    private FirebaseDatabase database;
    private DatabaseReference ref, job_eval, scene_ref;
    private ArrayList<String> uid = new ArrayList<>();
    private String user_id;
    private String key;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joblog_list);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("worklist");
        job_eval = database.getReference("Job_evaluation");
        scene_ref = database.getReference("Scene");
        listView = (ListView) findViewById(R.id.work_list);
        adapter = new JobLog_ListAdapter(item);
        listView.setAdapter(adapter);
        workItem = new JobLog_ListItem();
        ref.orderByChild("jobDay").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                item.clear();
                uid.clear();
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                    final Worklist_Data worklist_data = ds.getValue(Worklist_Data.class);

                    if (worklist_data.getId().equals(getIntent().getStringExtra("user_id"))) {
                        workItem = new JobLog_ListItem();
                        if (worklist_data.getLocation() != null) {
                            String[] location = worklist_data.getLocation().split(" ");
                            if (location.length == 0) {
                                workItem.setArea(worklist_data.getLocation());
                            } else {
                                workItem.setArea(location[0]);
                            }
                        } else {
                            workItem.setArea(worklist_data.getLocation());
                        }
                        workItem.setLocation(worklist_data.getLocation());
                        if (worklist_data.getStatus() == 0) {
                            workItem.setStatus("접수 완료");
                        } else if (worklist_data.getStatus() == 1) {
                            workItem.setStatus("접수 승인");
                        } else if (worklist_data.getStatus() == 2) {
                            workItem.setStatus("작업 완료");
                        }
                        if (worklist_data.getJob_eval_status() == 0) {
                            workItem.setEvaluation("평가필요");
                        } else if (worklist_data.getJob_eval_status() == 1) {
                            workItem.setEvaluation("평가완료");
                        }

                        workItem.setTitle(worklist_data.getTitle());
                        workItem.setDate(worklist_data.getDuring_work());
                        workItem.setUser_id(getIntent().getStringExtra("user_id"));


                        scene_ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    SceneData sceneData = snapshot.getValue(SceneData.class);
                                    Log.e("scene", sceneData.getTitle() + "," + sceneData.getLocation());
                                    if (workItem.getTitle().equals(sceneData.getTitle()) && workItem.getLocation().equals(sceneData.getLocation())) {
                                        key = snapshot.getKey();
                                        break;
                                    }
                                }
                                uid.add(key);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        item.add(workItem);
                    }


                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(JobLog_ListActivity.this, DetailInfoActivity.class);
                intent.putExtra("user_id", getIntent().getStringExtra("user_id"));
                intent.putExtra("key", uid.get(i).toString());
                intent.putExtra("detail_code", 1);
                startActivity(intent);
            }
        });

    }
}
