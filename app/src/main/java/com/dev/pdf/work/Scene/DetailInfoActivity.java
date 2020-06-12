package com.dev.pdf.work.Scene;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dev.pdf.work.Data.EmData;
import com.dev.pdf.work.Data.SceneData;
import com.dev.pdf.work.Data.Worklist_Data;
import com.dev.pdf.work.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class DetailInfoActivity extends AppCompatActivity {

    private TextView location, jobDay, jobKind, recruitmentContents, duringWork, title;
    private String key = "";
    private FirebaseDatabase database;
    private DatabaseReference ref, emuser_ref, worklist_ref, job_eval_ref;
    private ImageView prev, imgs, next;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private int imgCount = 0;
    private String[] imgString;
    private int nowImg = 0;
    private ProgressBar loading;
    private String user_id;
    private int work_status = 0;
    private int detail_code = 0;
    private LinearLayout regi_form, deadline_form;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);
        init();
        user_id = getIntent().getStringExtra("user_id");
        key = getIntent().getStringExtra("key");
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Scene");
        emuser_ref = database.getReference("emUser");
        job_eval_ref = database.getReference("Job_evaluation");
        worklist_ref = database.getReference("worklist");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        detail_code = getIntent().getIntExtra("detail_code", 0);
        if (detail_code == 0) {
            regi_form.setVisibility(View.GONE);
            deadline_form.setVisibility(View.VISIBLE);
        } else if (detail_code == 1) {
            regi_form.setVisibility(View.VISIBLE);
            deadline_form.setVisibility(View.GONE);
        }

        getData();

    }

    private void getImages(String ref) {
        if (storageReference != null) {
            Log.e("reftest ", ref);
            if (ref != null && !ref.isEmpty()) {
                Log.e("testtest ", storageReference.child(ref).getName());
                if (!storageReference.child(ref).getName().isEmpty()) {
                    StorageReference imgRef = storageReference.child(ref);

                    imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(DetailInfoActivity.this).load(uri).into(imgs);
                        }
                    });
                }


                if (nowImg == 0) {
                    prev.setVisibility(View.INVISIBLE);
                    if (imgCount > 1) next.setVisibility(View.VISIBLE);
                } else if (nowImg == imgCount - 1) {
                    next.setVisibility(View.INVISIBLE);
                    prev.setVisibility(View.VISIBLE);
                } else {
                    prev.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);
                }
            }
        }


    }

    private void init() {
        location = (TextView) findViewById(R.id.location);
        jobDay = (TextView) findViewById(R.id.job_day);
        jobKind = (TextView) findViewById(R.id.job_kind);
        recruitmentContents = (TextView) findViewById(R.id.recruitment_contents);
        duringWork = (TextView) findViewById(R.id.during_work);
        title = (TextView) findViewById(R.id.title);
        prev = (ImageView) findViewById(R.id.prev);
        imgs = (ImageView) findViewById(R.id.imgs);
        next = (ImageView) findViewById(R.id.next);
        regi_form = findViewById(R.id.regi_form);
        deadline_form = findViewById(R.id.deadline_form);
        if (imgCount == 0) {
            prev.setVisibility(View.INVISIBLE);
            next.setVisibility(View.INVISIBLE);
        }
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nowImg--;
                getImages(imgString[nowImg]);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nowImg++;
                getImages(imgString[nowImg]);
            }
        });
    }

    private void getData() {
        ref.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SceneData sceneData = dataSnapshot.getValue(SceneData.class);
                location.setText(sceneData.getLocation());
                jobDay.setText(sceneData.getJobDay());
                jobKind.setText(sceneData.getJobKind());
                recruitmentContents.append("\n\n" + sceneData.getRecruitmentContents());
                duringWork.setText(sceneData.getDuringWork());
                title.setText(sceneData.getTitle());
                String pic = sceneData.getPictures();
                imgString = pic.split(",");
                Log.e("imgString ", imgString[0]);

                for (int i = 0; i < imgString.length; i++) {
                    Log.e("imageList ", imgString[i]);
                }

                if (imgString.length > 1) {
                    imgCount = imgString.length;
                    prev.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);
                } else if (imgString.length != 0) {
                    getImages(imgString[0]);
                    nowImg = 0;
                    prev.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void deadline(View view) {
        ref.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SceneData sceneData = dataSnapshot.getValue(SceneData.class);
                Log.e("sceneStatus ", String.valueOf(sceneData.getStatus()));
                if (sceneData.getStatus() == 0) {
                    Map<String, Object> taskmap = new HashMap<>();
                    taskmap.put(key+"/status", 1);
                    ref.updateChildren(taskmap);
                    Toast.makeText(DetailInfoActivity.this, "마감되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailInfoActivity.this, "이미 마감된 작업입니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("test", databaseError.getDetails());
            }
        });
    }

    public void regi(View view) {
        ref.orderByChild("jobDay").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    SceneData sceneData = ds.getValue(SceneData.class);
                    if (sceneData.getTitle().equals(title.getText().toString()) && sceneData.getLocation().equals(location.getText().toString())) {
                        if (sceneData.getStatus() != 1) {
                            worklist_ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Log.e("data", String.valueOf(dataSnapshot.getChildrenCount()));

                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        Worklist_Data worklist_data = snapshot.getValue(Worklist_Data.class);
                                        if (title.getText().toString().equals(worklist_data.getTitle()) && location.getText().toString().equals(worklist_data.getLocation()) && user_id.equals(worklist_data.getId())) {
                                            work_status = 1;
                                        }
                                    }

                                    if (work_status == 0) {
                                        emuser_ref.orderByChild("id").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    final EmData emData = snapshot.getValue(EmData.class);
                                                    if (user_id.equals(emData.getId())) {
                                                        Worklist_Data worklist_data2 = new Worklist_Data();
                                                        worklist_data2.setTitle(title.getText().toString());
                                                        worklist_data2.setLocation(location.getText().toString());
                                                        worklist_data2.setId(emData.getId());
                                                        worklist_data2.setStatus(0);
                                                        worklist_data2.setJob_eval_status(0);
                                                        worklist_data2.setName(emData.getName());
                                                        worklist_data2.setDuring_work(duringWork.getText().toString());
                                                        worklist_data2.setUser_address(emData.getLocation() + " " + emData.getLocationDetail());
                                                        worklist_data2.setUser_tel(emData.getPhoneNum());
                                                        worklist_data2.setJob_kind(jobKind.getText().toString());
                                                        worklist_data2.setCareer(emData.getCareer());
                                                        worklist_ref.push().setValue(worklist_data2);
                                                        Toast.makeText(DetailInfoActivity.this, "신청되었습니다.", Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.e("test", databaseError.getDetails());
                                }
                            });

                            if (work_status == 1) {
                                Toast.makeText(DetailInfoActivity.this, "이미 신청완료된 작업입니다.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(DetailInfoActivity.this, "이미 마감된 작업입니다.", Toast.LENGTH_SHORT).show();
                        }
//                        Log.e("sceneStatus ", String.valueOf(sceneData.getStatus()) + ", " + title.getText().toString() + ", " + sceneData.getTitle() + ", " + sceneData.getLocation());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
