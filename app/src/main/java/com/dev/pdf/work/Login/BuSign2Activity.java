package com.dev.pdf.work.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.dev.pdf.work.Data.BuData;
import com.dev.pdf.work.Data.IdData;
import com.dev.pdf.work.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BuSign2Activity extends AppCompatActivity {

    private String id, pw, name, buisnessNum, location, locationDetail, buisnessKind = "개인사업", phoneNum, buisnessInfo;
    private FirebaseDatabase database;
    private DatabaseReference ref, ref_id;
    private BuData buData;
    private IdData idData;
    private RadioButton personal, corporation;
    private EditText buisnessNum1, buisnessNum2, buisnessNum3;
    private EditText buisnessManName, buisnessName, buisnessLocation,
            myBuisnessInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2_buisnessman);
        init();
    }

    private void init() {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("buUser");
        ref_id = database.getReference("userId");
        buData = new BuData();
        idData = new IdData();
        id = getIntent().getStringExtra("id");
        idData.setId(id);
        idData.setUserKind(1);//사업자는 1
        pw = getIntent().getStringExtra("pw");
        location = getIntent().getStringExtra("location");
        locationDetail = getIntent().getStringExtra("locationDetail");
        phoneNum = getIntent().getStringExtra("phoneNum");

        personal = (RadioButton) findViewById(R.id.personal);
        corporation = (RadioButton) findViewById(R.id.corporation);

        buisnessNum1 = (EditText) findViewById(R.id.buisness_num1);
        buisnessNum2 = (EditText) findViewById(R.id.buisness_num2);
        buisnessNum3 = (EditText) findViewById(R.id.buisness_num3);

        buisnessManName = (EditText) findViewById(R.id.buisness_man_name);
        buisnessName = (EditText) findViewById(R.id.buisness_name);
        buisnessLocation = (EditText) findViewById(R.id.buisness_location);
        myBuisnessInfo = (EditText) findViewById(R.id.buisness_info);
    }

    private boolean checkNull() {
        if (buisnessNum1.getText().toString().isEmpty() ||
                buisnessNum2.getText().toString().isEmpty() ||
                buisnessNum3.getText().toString().isEmpty() ||
                buisnessManName.getText().toString().isEmpty() ||
                buisnessName.getText().toString().isEmpty() ||
                buisnessLocation.getText().toString().isEmpty()) {
            return false;
        } else {
            name = buisnessName.getText().toString();
            buisnessNum = buisnessNum1.getText().toString() + "" + buisnessNum2.getText().toString() + "" + buisnessNum3.getText().toString();
            buisnessInfo = myBuisnessInfo.getText().toString();
            location = buisnessLocation.getText().toString();
            locationDetail = "_상세 주소";
            return true;
        }
    }

    public void backBtn(View view) {
        finish();
    }

    public void signOk(View view) {
        if (checkNull()) {
            buData.setId(id);
            buData.setPw(pw);
            buData.setLocation(location);
            buData.setLocationDetail(locationDetail);
            buData.setName(name);
            buData.setPhoneNum(phoneNum);
            buData.setBuisnessInfo(buisnessInfo);
            buData.setBuisnessNum(buisnessNum);
            if (personal.isChecked()) buisnessKind = "개인사업";
            else if (corporation.isChecked()) buisnessKind = "법인사업";
            buData.setBuisnessKind(buisnessKind);
            ref.push().setValue(buData);
            ref_id.push().setValue(idData);
            Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            Intent intentLogin = new Intent(BuSign2Activity.this, LoginActivity.class);
            intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intentLogin.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intentLogin);
            finish();
        } else {
            Toast.makeText(this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }

    }

}
