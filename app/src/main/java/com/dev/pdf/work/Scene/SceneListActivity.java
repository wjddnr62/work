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
import com.dev.pdf.work.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SceneListActivity extends AppCompatActivity {
    ArrayList<WorkListItem> item = new ArrayList<>();
    private ListView listView;
    private WorkListAdapter adapter;

    private SceneData sceneData;
    private WorkListItem workItem;

    private FirebaseDatabase database;
    private DatabaseReference ref;
    private ArrayList<String> uid = new ArrayList<>();
    private String user_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_list);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Scene");
        listView = (ListView) findViewById(R.id.work_list);
        adapter = new WorkListAdapter(item);
        listView.setAdapter(adapter);
        workItem = new WorkListItem();
        ref.orderByChild("jobDay").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                item.clear();
                uid.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    SceneData sceneData = ds.getValue(SceneData.class);
                    workItem = new WorkListItem();
                    if (sceneData.getLocation() != null) {
                        String[] location = sceneData.getLocation().split(" ");
                        if (location.length == 0) {
                            workItem.setArea(sceneData.getLocation());
                        } else {
                            workItem.setArea(location[0]);
                        }
                    } else {
                        workItem.setArea(sceneData.getLocation());
                    }
                    workItem.setLocation(sceneData.getLocation());
                    workItem.setWorkDay(sceneData.getJobDay());
                    workItem.setMoney(sceneData.getMoney());
                    uid.add(ds.getKey());
                    Log.e("dsgetley", ds.getKey());
                    item.add(workItem);
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
                Intent intent = new Intent(SceneListActivity.this, DetailInfoActivity.class);
                intent.putExtra("user_id", getIntent().getStringExtra("user_id"));
                intent.putExtra("key", uid.get(i).toString());
                intent.putExtra("detail_code", 1);
                startActivity(intent);
            }
        });

    }
}
