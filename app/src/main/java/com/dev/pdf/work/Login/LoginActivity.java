package com.dev.pdf.work.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dev.pdf.work.BackPressCloseHandler;
import com.dev.pdf.work.Buisnessman.BuisnessMainActivity;
import com.dev.pdf.work.Data.BuData;
import com.dev.pdf.work.Data.EmData;
import com.dev.pdf.work.Data.IdData;
import com.dev.pdf.work.Data.UserData;
import com.dev.pdf.work.MainActivity;
import com.dev.pdf.work.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    boolean checkId = false;
    UserData userData;
    private Button sign_em;
    private Button sign_bu;
    private Button login_btn;
    private EditText idEdit, pwEdit;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private DatabaseReference ref_id;
    private IdData idData;
    private ProgressBar loading;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        backPressCloseHandler = new BackPressCloseHandler(this);

        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = pref.edit();

        sign_em = (Button) findViewById(R.id.sign_em);//근로자 회원가입
        sign_bu = (Button) findViewById(R.id.sign_bu);//사업자 회원가입
        login_btn = (Button) findViewById(R.id.login_btn);//로그인 버튼

        sign_em.setOnClickListener(this);
        sign_bu.setOnClickListener(this);
        login_btn.setOnClickListener(this);

        idEdit = (EditText) findViewById(R.id.login_id);
        pwEdit = (EditText) findViewById(R.id.login_pw);
        database = FirebaseDatabase.getInstance();
        ref_id = database.getReference("userId");
        ref = database.getReference();
        userData = UserData.getInstance();
        loading = (ProgressBar) findViewById(R.id.loading);
        loading.setVisibility(View.GONE);

        if (!pref.getString("user_id", "").isEmpty()|| !pref.getString("user_id", "").equals("")){
            idEdit.setText(pref.getString("user_id", ""));
            pwEdit.setText(pref.getString("user_pw", ""));
            login_btn.performClick();
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        Intent intent = null;
        switch (viewId) {
            case R.id.sign_bu:
                intent = new Intent(LoginActivity.this, Sign1Activity.class);
                intent.putExtra("division", 1);
                break;
            case R.id.sign_em:
                intent = new Intent(LoginActivity.this, Sign1Activity.class);
                intent.putExtra("division", 0);
                break;
            case R.id.login_btn:
                if (checkNull()) {
                    checkId();
                } else {
                    Toast.makeText(this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }

    private void checkId() {
        idData = new IdData();

        ref_id.orderByChild("id").equalTo(idEdit.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("id", idEdit.getText().toString());
                if (dataSnapshot.getChildrenCount() == 0)
                    checkId = false;
                else {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        idData = postSnapshot.getValue(IdData.class);
                        checkId = true;
                    }
                }
                checkLogin();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkLogin() {
        if (checkId) {
            if (idData.getUserKind() == 0) {
                loading.setVisibility(View.VISIBLE);
                // 근로자
                ref.child("emUser").orderByChild("id").equalTo(idEdit.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("count", String.valueOf(dataSnapshot.getChildrenCount()));
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            EmData emData = postSnapshot.getValue(EmData.class);
                            Log.d("pwpw", emData.getId());
                            if (pwEdit.getText().toString().equals(emData.getPw())) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                userData.setId(idEdit.getText().toString());
                                editor.putString("user_id", idEdit.getText().toString());
                                editor.putString("user_pw", pwEdit.getText().toString());
                                editor.commit();
                                intent.putExtra("user_id", idEdit.getText().toString());
                                startActivity(intent);
                                loading.setVisibility(View.GONE);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        loading.setVisibility(View.GONE);
                    }
                });
            } else
                if (idData.getUserKind() == 1) {
                loading.setVisibility(View.VISIBLE);
                // 사업자
                ref.child("buUser").orderByChild("id").equalTo(idEdit.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            BuData buData = postSnapshot.getValue(BuData.class);
                            if (buData.getPw().equals(pwEdit.getText().toString())) {
                                Intent intent = new Intent(LoginActivity.this, BuisnessMainActivity.class);
                                userData.setId(idEdit.getText().toString());
                                intent.putExtra("id", idEdit.getText().toString());
                                startActivity(intent);
                                loading.setVisibility(View.GONE);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        loading.setVisibility(View.GONE);
                    }
                });
            }

        } else Toast.makeText(this, "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT).show();
    }

    private boolean checkNull() {
        if (idEdit.getText().toString().isEmpty() || pwEdit.getText().toString().isEmpty())
            return false;
        else return true;
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}
