package com.dev.pdf.work.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.pdf.work.Data.EmData;
import com.dev.pdf.work.Data.IdData;
import com.dev.pdf.work.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class EmSign2Activity extends AppCompatActivity {

    private TextView magazine, tidal_Ball, technician;
    View.OnClickListener job_kine_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            magazine.setSelected(false);
            tidal_Ball.setSelected(false);
            technician.setSelected(false);

            switch (view.getId()) {
                case R.id.magazine:
                    magazine.setSelected(true);
                    break;
                case R.id.tidal_Ball:
                    tidal_Ball.setSelected(true);
                    break;
                case R.id.technician:
                    technician.setSelected(true);
                    break;
            }
        }
    };
    private String career = "1년", proficiency = "상", jobKind = "잡부";
    private Spinner careerSpinner, proficiencySpinner;
    private EditText myInfoEdit;
    private ArrayList carrerArray;
    private ArrayList proficiencyArray;
    private FirebaseDatabase database;
    private DatabaseReference ref, ref_id;
    private String id, pw, name, ssn, location, locationDetail,
            phoneNum, tel, bankNum, bank;
    private EmData emData;
    private IdData idData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2_employee);

        init();
    }

    private void init() {
        idData = new IdData();
        id = getIntent().getStringExtra("id");
        idData.setId(id);
        idData.setUserKind(0);//근로자는 0
        pw = getIntent().getStringExtra("pw");
        name = getIntent().getStringExtra("name");
        ssn = getIntent().getStringExtra("ssn");
        location = getIntent().getStringExtra("location");
        locationDetail = getIntent().getStringExtra("locationDetail");
        phoneNum = getIntent().getStringExtra("phoneNum");
        tel = getIntent().getStringExtra("tel");
        bankNum = getIntent().getStringExtra("bankNum");
        bank = getIntent().getStringExtra("bank");


        emData = new EmData();
        emData.setId(id);
        emData.setPw(pw);
        emData.setName(name);
        emData.setSsn(ssn);
        emData.setLocation(location);
        emData.setLocationDetail(locationDetail);
        emData.setPhoneNum(phoneNum);
        emData.setTel(tel);
        emData.setBank(bank);
        emData.setBankNum(bankNum);

        carrerArray = new ArrayList(Arrays.asList(getResources().getStringArray(R.array.career)));
        proficiencyArray = new ArrayList(Arrays.asList(getResources().getStringArray(R.array.proficiency)));

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("emUser");
        ref_id = database.getReference("userId");

        magazine = (TextView) findViewById(R.id.magazine);
        tidal_Ball = (TextView) findViewById(R.id.tidal_Ball);
        technician = (TextView) findViewById(R.id.technician);

        magazine.setSelected(true);
        tidal_Ball.setSelected(false);
        technician.setSelected(false);

        magazine.setOnClickListener(job_kine_click);
        tidal_Ball.setOnClickListener(job_kine_click);
        technician.setOnClickListener(job_kine_click);

        myInfoEdit = (EditText) findViewById(R.id.my_info);
        careerSpinner = (Spinner) findViewById(R.id.career_spinner);
        proficiencySpinner = (Spinner) findViewById(R.id.proficiency_spinner);

        careerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                career = carrerArray.get(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        proficiencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                proficiency = proficiencyArray.get(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void backBtn(View view) {
        finish();
    }

    public void signOk(View view) {
        emData.setJobKind(jobKind);
        emData.setCareer(career);
        emData.setProficiency(proficiency);
        emData.setMyInfo(myInfoEdit.getText().toString());

        ref.push().setValue(emData);
        ref_id.push().setValue(idData);
        Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
        Intent intentLogin = new Intent(EmSign2Activity.this, LoginActivity.class);
        intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentLogin.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intentLogin);
        finish();
    }

}
