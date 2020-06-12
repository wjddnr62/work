package com.dev.pdf.work.Login;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.pdf.work.Buisnessman.SceneRegiActivity;
import com.dev.pdf.work.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Sign1Activity extends AppCompatActivity {

    private int division = 0;
    private TextView skt, kt, lg, address;
    private EditText idEdit, pwEdit, pwCheckEdit, nameEdit, ssn1Edit,
            ssn2Edit, loationDetailEdit, phoneNumEdit, bankNumEdit, bankEdit;
    private TextView overlapCheck, location;
    private boolean overlapCheckState = false;
    private String tel = "SKT";
    private LinearLayout ssnForm;
    View.OnClickListener tel_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            skt.setSelected(false);
            lg.setSelected(false);
            kt.setSelected(false);

            switch (view.getId()) {
                case R.id.tel_skt:
                    skt.setSelected(true);
                    tel = "SKT";
                    break;
                case R.id.tel_lg:
                    lg.setSelected(true);
                    tel = "LGU+";
                    break;
                case R.id.tel_kt:
                    kt.setSelected(true);
                    tel = "KT";
                    break;
            }
        }
    };
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        division = getIntent().getIntExtra("division", 0);

        init();
        init_webView();
    }

//    public void init_webView() {
//        // WebView 설정
//        webView = (WebView) findViewById(R.id.webview);
//        // JavaScript 허용
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        webView.getSettings().setSupportMultipleWindows(true);
//        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        webView.setWebViewClient(new WebViewClient());
//        webView.setWebChromeClient(new WebChromeClient(){
//            @Override
//            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
//                WebView newWeb = new WebView(Sign1Activity.this);
//                newWeb.getSettings().setJavaScriptEnabled(true);
//                final Dialog dialog = new Dialog(Sign1Activity.this);
//                dialog.setContentView(newWeb);
//
//                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
//                params.width = WindowManager.LayoutParams.MATCH_PARENT;
//                params.height = WindowManager.LayoutParams.MATCH_PARENT;
//                dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
//
//                dialog.show();
//                newWeb.setWebChromeClient(new WebChromeClient(){
//                    @Override
//                    public void onCloseWindow(WebView window) {
////                                webView.setV
//                        dialog.dismiss();
//                    }
//                });
//
//                ((WebView.WebViewTransport) resultMsg.obj).setWebView(newWeb);
//                resultMsg.sendToTarget();
//
//                return true;
//            }
//        });
//        webView.loadUrl("https://work-39653.firebaseapp.com/");
//    }

    public void init_webView() {
        // WebView 설정
        webView = (WebView) findViewById(R.id.webview);
        // JavaScript 허용
        webView.getSettings().setJavaScriptEnabled(true);
        // JavaScript의 window.open 허용
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        // 두 번째 파라미터는 사용될 php에도 동일하게 사용해야함
        webView.addJavascriptInterface(new AndroidBridge(), "workApp");
        // web client 를 chrome 으로 설정
        webView.setWebChromeClient(new WebChromeClient());
        // webview url load
//        webView.loadUrl("http://108.160.128.68/daumapi.php");
//        webView.loadUrl("file:///android_asset/w/daumapi.html");
    }

    private void init() {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("userId");

        skt = (TextView) findViewById(R.id.tel_skt);
        lg = (TextView) findViewById(R.id.tel_lg);
        kt = (TextView) findViewById(R.id.tel_kt);
        address = (TextView) findViewById(R.id.address);

        ssnForm = (LinearLayout) findViewById(R.id.ssnForm);

        if (division == 1) {
            ssnForm.setVisibility(View.GONE);
        }

        skt.setSelected(true);
        lg.setSelected(false);
        kt.setSelected(false);

        skt.setOnClickListener(tel_click);
        lg.setOnClickListener(tel_click);
        kt.setOnClickListener(tel_click);


        idEdit = (EditText) findViewById(R.id.id_edit);
        pwEdit = (EditText) findViewById(R.id.pw_edit);
        pwCheckEdit = (EditText) findViewById(R.id.pw_check_edit);
        nameEdit = (EditText) findViewById(R.id.name_edit);
        ssn1Edit = (EditText) findViewById(R.id.social_security_number_edit1);
        ssn2Edit = (EditText) findViewById(R.id.social_security_number_edit2);
        loationDetailEdit = (EditText) findViewById(R.id.location_detail);
        phoneNumEdit = (EditText) findViewById(R.id.phone_number_edit);
        bankNumEdit = (EditText) findViewById(R.id.bank_num_edit);
        bankEdit = (EditText) findViewById(R.id.bank);

        overlapCheck = (TextView) findViewById(R.id.overlap_check);
        location = (TextView) findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView = (WebView) findViewById(R.id.webview);
                // JavaScript 허용
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                webView.getSettings().setSupportMultipleWindows(true);
                webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                webView.setWebViewClient(new WebViewClient());
                webView.setWebChromeClient(new WebChromeClient(){
                    @Override
                    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                        WebView newWeb = new WebView(Sign1Activity.this);
                        newWeb.getSettings().setJavaScriptEnabled(true);
                        final Dialog dialog = new Dialog(Sign1Activity.this);
                        dialog.setContentView(newWeb);

                        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                        params.width = WindowManager.LayoutParams.MATCH_PARENT;
                        params.height = WindowManager.LayoutParams.MATCH_PARENT;
                        dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

                        dialog.show();
                        newWeb.setWebChromeClient(new WebChromeClient(){
                            @Override
                            public void onCloseWindow(WebView window) {
//                                webView.setV
                                dialog.dismiss();
                            }
                        });

                        ((WebView.WebViewTransport) resultMsg.obj).setWebView(newWeb);
                        resultMsg.sendToTarget();

                        return true;
                    }
                });
                webView.loadUrl("https://work-39653.firebaseapp.com/");
            }
        });

        overlapCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idEdit.getText().toString().isEmpty()) {
                    Toast.makeText(Sign1Activity.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    checkOverlapId(idEdit.getText().toString());
                }
            }
        });

    }

    public void nextBtn(View view) {
        Intent intent = null;

        if (division == 0) intent = new Intent(Sign1Activity.this, EmSign2Activity.class);// 근로자
        else intent = new Intent(Sign1Activity.this, BuSign2Activity.class);//사업자

        if (checkNull()) {
            if (overlapCheckState) {
                intent.putExtra("id", idEdit.getText().toString());
                intent.putExtra("pw", pwEdit.getText().toString());
                intent.putExtra("name", nameEdit.getText().toString());
                intent.putExtra("ssn", ssn1Edit.getText().toString() + "-" + ssn2Edit.getText().toString());
                intent.putExtra("location", location.getText().toString());
                intent.putExtra("locationDetail", loationDetailEdit.getText().toString());
                intent.putExtra("phoneNum", phoneNumEdit.getText().toString());
                intent.putExtra("tel", tel);
                intent.putExtra("bankNum", bankNumEdit.getText().toString());
                intent.putExtra("bank", bankEdit.getText().toString());
                startActivity(intent);
            } else Toast.makeText(this, "아이디 중복확인해 주세요.", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
    }

    private boolean checkNull() {
//         ||
//        bankNumEdit.getText().toString().isEmpty() ||
//                bankEdit.getText().toString().isEmpty()
        if (division == 1) {
            if (
                    idEdit.getText().toString().isEmpty() ||
                            pwEdit.getText().toString().isEmpty() ||
                            pwCheckEdit.getText().toString().isEmpty() ||
                            nameEdit.getText().toString().isEmpty() ||
                            loationDetailEdit.getText().toString().isEmpty() ||
                            phoneNumEdit.getText().toString().isEmpty()) {
                return false;
            } else {
                return true;
            }
        } else {
            if (
                    idEdit.getText().toString().isEmpty() ||
                            pwEdit.getText().toString().isEmpty() ||
                            pwCheckEdit.getText().toString().isEmpty() ||
                            nameEdit.getText().toString().isEmpty() ||
                            ssn1Edit.getText().toString().isEmpty() ||
                            ssn2Edit.getText().toString().isEmpty() ||
                            loationDetailEdit.getText().toString().isEmpty() ||
                            phoneNumEdit.getText().toString().isEmpty()) {
                return false;
            } else {
                return true;
            }
        }

    }

    private void checkOverlapId(String id) {
        ref.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    overlapCheckState = true;
                    Toast.makeText(Sign1Activity.this, "사용 가능한 아이디 입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    overlapCheckState = false;
                    Toast.makeText(Sign1Activity.this, "이미 사용중인 아이디 입니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    // WebView를 초기화 하지않으면 재사용할 수 없음
                    address.setText(arg2.toString() + " " + arg3.toString());
                    webView.setVisibility(View.GONE);
                    init_webView();
                }
            });
        }
    }
}
