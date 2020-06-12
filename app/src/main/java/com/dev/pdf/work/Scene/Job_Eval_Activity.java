package com.dev.pdf.work.Scene;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.pdf.work.Buisnessman.BuisnessMainActivity;
import com.dev.pdf.work.Buisnessman.SceneRegiPicturesActivity;
import com.dev.pdf.work.Data.Job_Eval_Data;
import com.dev.pdf.work.Data.SceneData;
import com.dev.pdf.work.Data.UserData;
import com.dev.pdf.work.Data.Worklist_Data;
import com.dev.pdf.work.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Job_Eval_Activity extends AppCompatActivity implements View.OnClickListener {

    final int REQUEST_TAKE_ALBUM = 1;
    final int ImageViewCount = 3;
    final int[] senceImgIds = {R.id.scene_img1, R.id.scene_img2, R.id.scene_img3};
    final int[] cancelImgIds = {R.id.cancel_img1, R.id.cancel_img2, R.id.cancel_img3};
    ArrayList<Bitmap> bitmaps;
    ArrayList imgStrings;
    int stateCount = 0;
    int tmp = 0;
    private ImageView[] senceImg;
    private ImageView[] cancelImg;
    private boolean[] imgState = {false, false, false};
    private String title, location, duringWork, jobDay, jobKind, recruitmentContents, money;
    private FirebaseDatabase database;
    private DatabaseReference ref, worklist_ref;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private UserData userData;
    private ProgressBar loading;
    private EditText etc_text;
    private RadioGroup radioGroup1, radioGroup2;
    private int mas_status;
    private int stas_status;
    private Button eval_btn;
    private TextView title_text, location_text, date_text;
    private String user_id;
    private String key;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_eval_acitivty);

        etc_text = findViewById(R.id.etc_text);
        radioGroup1 = findViewById(R.id.radiogroup1);
        radioGroup2 = findViewById(R.id.radiogroup2);
        eval_btn = findViewById(R.id.eval_btn);

        title_text = findViewById(R.id.title);
        location_text = findViewById(R.id.location);
        date_text = findViewById(R.id.date);

        title_text.setText(getIntent().getStringExtra("title"));
        location_text.setText(getIntent().getStringExtra("location"));
        date_text.setText(getIntent().getStringExtra("date"));
        user_id = getIntent().getStringExtra("user_id");

        eval_btn.setOnClickListener(this);

        loading = (ProgressBar) findViewById(R.id.loading);
        loading.setVisibility(View.GONE);
        userData = UserData.getInstance();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Job_Eval");
        worklist_ref = database.getReference("worklist");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        senceImg = new ImageView[ImageViewCount];
        cancelImg = new ImageView[ImageViewCount];

        for (int i = 0; i < ImageViewCount; i++) {
            senceImg[i] = new ImageView(this);
            senceImg[i] = (ImageView) findViewById(senceImgIds[i]);
            cancelImg[i] = new ImageView(this);
            cancelImg[i] = (ImageView) findViewById(cancelImgIds[i]);

            cancelImg[i].setOnClickListener(this);
        }

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.mas_top) {
//                    Toast.makeText(Job_Eval_Activity.this, "상", Toast.LENGTH_SHORT).show();
                    mas_status = 3;
                } else if (checkedId == R.id.mas_middle) {
//                    Toast.makeText(Job_Eval_Activity.this, "중", Toast.LENGTH_SHORT).show();
                    mas_status = 2;
                } else if (checkedId == R.id.mas_bottem) {
//                    Toast.makeText(Job_Eval_Activity.this, "하", Toast.LENGTH_SHORT).show();
                    mas_status = 1;
                }
            }
        });

        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.stas_good) {
//                    Toast.makeText(Job_Eval_Activity.this, "양호", Toast.LENGTH_SHORT).show();
                    stas_status = 2;
                } else if (checkedId == R.id.stas_bad) {
//                    Toast.makeText(Job_Eval_Activity.this, "불량", Toast.LENGTH_SHORT).show();
                    stas_status = 1;
                }
            }
        });
    }

    private void getAlbum() {
        imgStrings = new ArrayList();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("image/*");
                //intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, REQUEST_TAKE_ALBUM);
            } catch (Exception e) {
                Log.e("error", e.toString());
            }
        } else {
            Log.e("kitkat under", "..");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_TAKE_ALBUM:
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList imageList = new ArrayList();
                    //멀티 선택을 지원하지 않는 기기에서는 getData()로 접근해야 함
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                        Log.d("tt",data.getClipData());
                        if (data.getClipData() == null) {
                            //멀티선택에서 하나만 선택했을 경우 도 여기로 듩어옴
                            imageList.add(getRealPathFromURI(data.getData()));
                            imgStrings.add(getImageNameToUri(data.getData()));
                            setImg(imageList);
                        } else {
                            ClipData clipData = data.getClipData();
                            Log.d("count", String.valueOf(clipData.getItemCount()));
                            if (clipData.getItemCount() > 3) {
                                Toast.makeText(this, "사진은 3개까지 선택가능 합니다.", Toast.LENGTH_SHORT).show();
                                return;
                            } else if (clipData.getItemCount() >= 1 && clipData.getItemCount() <= 3) {
                                for (int i = 0; i < clipData.getItemCount(); i++) {
                                    String dataStr = getRealPathFromURI(clipData.getItemAt(i).getUri());
                                    imageList.add(dataStr);
                                    imgStrings.add(getImageNameToUri(clipData.getItemAt(i).getUri()));
                                }
                                setImg(imageList);
                            }
                        }

                    } else {
                        //멀티선택 지원하지않는 기기
                        Log.e("tes", ".");
                    }
                } else {
                    Toast.makeText(this, "사진 선택을 취소하였습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void setImg(ArrayList imgList) {
        bitmaps = new ArrayList<>();
        for (int i = 0; i < imgState.length; i++) {
            imgState[i] = false;
        }
        for (int i = 0; i < imgList.size(); i++) {
            String imagePath = imgList.get(i).toString();
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
            imgState[i] = true;
            bitmaps.add(bitmap);

        }
        for (int i = 0; i < imgState.length; i++) {
            if (imgState[i]) {
                senceImg[i].setImageBitmap(bitmaps.get(i));
                cancelImg[i].setVisibility(View.VISIBLE);
            } else {
                senceImg[i].setImageResource(R.drawable.icon_image_default);
                cancelImg[i].setVisibility(View.GONE);
            }

        }

    }

    private String getRealPathFromURI(Uri contentUri) {
        int column_index = 0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }

        return cursor.getString(column_index);
    }

    public void getPicturesBtn(View view) {
        getAlbum();
    }

    public String getImageNameToUri(Uri data) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);
        return imgName;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.cancel_img1:
                senceImg[0].setImageResource(R.drawable.icon_image_default);
                cancelImg[0].setVisibility(View.GONE);
                break;
            case R.id.cancel_img2:
                senceImg[1].setImageResource(R.drawable.icon_image_default);
                cancelImg[1].setVisibility(View.GONE);
                break;
            case R.id.cancel_img3:
                senceImg[2].setImageResource(R.drawable.icon_image_default);
                cancelImg[2].setVisibility(View.GONE);
                break;
            case R.id.eval_btn:
                if (mas_status == 0 || stas_status == 0){
                    Toast.makeText(this, "1번 또는 2번 항목은 필수로 선택해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    loading.setVisibility(View.VISIBLE);
                    final ArrayList imgDownList = new ArrayList();
                    stateCount = 0;
                    tmp = 0;
                    for (boolean state : imgState) {
                        if (state) stateCount++;
                    }
                    if (stateCount == 0) {
                        sendSceneData(imgDownList);
                    } else {
                        for (int i = 0; i < imgState.length; i++) {
                            final int pos = i;
                            if (imgState[i]) {
                                final String name = imgStrings.get(i).toString();
                                Bitmap bitmap = bitmaps.get(i);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] data = baos.toByteArray();
                                UploadTask uploadTask = storageReference.child("images/" + userData.getId() + "/" + title + "/" + name).putBytes(data);
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        tmp += 1;
                                        if (tmp == stateCount) {
                                            loading.setVisibility(View.GONE);
                                        }
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        tmp += 1;
                                        imgDownList.add("images/" + userData.getId() + "/" + title + "/" + name);
                                        if (tmp == stateCount) {
                                            sendSceneData(imgDownList);
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
                break;
        }
    }

    public void sendSceneData(ArrayList imgList) {
        Job_Eval_Data job_eval_data = new Job_Eval_Data();
        String imgs = "";
        if (imgList.size() != 0) {
            imgs = imgList.get(0).toString();
        }
        for (int i = 1; i < imgList.size(); i++) {
            imgs += "," + imgList.get(i).toString();
        }
        job_eval_data.setTitle(title_text.getText().toString());
        job_eval_data.setDate(date_text.getText().toString());
        if (!etc_text.getText().toString().isEmpty()){
            job_eval_data.setEtc(etc_text.getText().toString());
        }else{
            job_eval_data.setEtc("");
        }

        job_eval_data.setLocation(location_text.getText().toString());
        job_eval_data.setMas(mas_status);
        job_eval_data.setStas(stas_status);
        if(!imgList.isEmpty()){
            job_eval_data.setPicture(String.valueOf(imgList.get(0)));
        }else{
            job_eval_data.setPicture("");
        }

        job_eval_data.setUser_id(user_id);
        ref.push().setValue(job_eval_data);

        worklist_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Worklist_Data worklist_data = snapshot.getValue(Worklist_Data.class);
                    if (title_text.getText().toString().equals(worklist_data.getTitle()) && location_text.getText().toString().equals(worklist_data.getLocation())){
                        key = snapshot.getKey();
                    }
                }
                Map<String, Object> taskmap = new HashMap<>();
                Log.e("key", key);
                taskmap.put(key+"/job_eval_status", 1);
                worklist_ref.updateChildren(taskmap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        loading.setVisibility(View.GONE);
        Toast.makeText(Job_Eval_Activity.this, "등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }
}
