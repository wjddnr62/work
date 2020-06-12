package com.dev.pdf.work.Buisnessman;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dev.pdf.work.BackPressCloseHandler;
import com.dev.pdf.work.BackPressCloseHandler_Login;
import com.dev.pdf.work.Data.SceneData;
import com.dev.pdf.work.Data.UserData;
import com.dev.pdf.work.R;
import com.dev.pdf.work.Scene.DetailInfoActivity;
import com.dev.pdf.work.Scene.JobLog_ListActivity;
import com.dev.pdf.work.Scene.WorkListAdapter;
import com.dev.pdf.work.Scene.WorkListItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BuisnessMainActivity extends AppCompatActivity {
    ArrayList<WorkListItem> item = new ArrayList<>();
    private ListView listView;
    private WorkListAdapter adapter;
    private FloatingActionButton fab;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private UserData userData;
    private SceneData sceneData;
    private WorkListItem workItem;
    private ArrayList<String> uid = new ArrayList<>();
    private BackPressCloseHandler_Login backPressCloseHandler;
    private TextView apply_check;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_list_enrollment);
        apply_check = findViewById(R.id.apply_check);
        apply_check.setVisibility(View.VISIBLE);

        apply_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuisnessMainActivity.this, Scene_Apply.class);
                intent.putExtra("id", getIntent().getStringExtra("id"));
                startActivity(intent);
            }
        });

        backPressCloseHandler = new BackPressCloseHandler_Login(this);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Scene");
        userData = UserData.getInstance();
        fab = (FloatingActionButton) findViewById(R.id.fab);

        listView = (ListView) findViewById(R.id.work_list);
        adapter = new WorkListAdapter(item);
        listView.setAdapter(adapter);
        workItem = new WorkListItem();
        ref.orderByChild("id").equalTo(userData.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                item.clear();
                uid.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    SceneData sceneData = ds.getValue(SceneData.class);
                    uid.add(ds.getKey());
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
                    workItem.setTitle(sceneData.getTitle());
                    workItem.setLocation(sceneData.getLocation());
                    workItem.setWorkDay(sceneData.getJobDay());
                    workItem.setMoney("평가하기");

                    item.add(workItem);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BuisnessMainActivity.this, SceneRegiActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(BuisnessMainActivity.this, DetailInfoActivity.class);
                intent.putExtra("user_id", getIntent().getStringExtra("user_id"));
                intent.putExtra("key", uid.get(i).toString());
                intent.putExtra("detail_code", 0);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}
