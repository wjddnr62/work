package com.dev.pdf.work.Buisnessman;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.dev.pdf.work.Data.EmData;
import com.dev.pdf.work.Data.SceneData;
import com.dev.pdf.work.Data.Worklist_Data;
import com.dev.pdf.work.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Scene_Apply extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference ref, worklist_ref;
    private EmData emData;
    private ListView listView_apply;
    private Scene_Apply_Adapter scene_apply_adapter;
    private ArrayList<String> location;
    private ArrayList<String> title;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scene_apply);

        location = new ArrayList<>();
        title = new ArrayList<>();

        listView_apply = findViewById(R.id.listview_apply);
        scene_apply_adapter = new Scene_Apply_Adapter();

        listView_apply.setAdapter(scene_apply_adapter);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Scene");
        worklist_ref = database.getReference("worklist");

        ref.orderByChild("id").equalTo(getIntent().getStringExtra("id")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                location.clear();
                title.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    SceneData sceneData = ds.getValue(SceneData.class);
                    location.add(sceneData.getLocation());
                    title.add(sceneData.getTitle());
                }
                scene_apply_adapter.notifyDataSetChanged();
                worklist_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        scene_apply_adapter.clearitem();
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            Worklist_Data worklistData = ds.getValue(Worklist_Data.class);
                            for (int i = 0; i < location.size(); i++){
                                if (worklistData.getLocation().equals(location.get(i)) && worklistData.getTitle().equals(title.get(i)) && worklistData.getStatus() == 0){
                                    scene_apply_adapter.addItem(worklistData.getName(), worklistData.getUser_tel(), worklistData.getJob_kind(), worklistData.getCareer(), "승인", ds.getKey());

                                }
                            }
                            scene_apply_adapter.notifyDataSetChanged();
                        }
                        scene_apply_adapter.notifyDataSetChanged();
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
