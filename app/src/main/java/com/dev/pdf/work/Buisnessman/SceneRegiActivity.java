package com.dev.pdf.work.Buisnessman;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.pdf.work.R;

public class SceneRegiActivity extends AppCompatActivity {

    private EditText titleEdit, recruitmentContentsEdit, moneyEdit;
    private TextView locationText, startDayText, endDayText, jobDayText, magazinText, tidal_ballText, technicianText, realLocationText;

    private String title, location, duringWork, jobDay, jobKind = "잡부", recruitmentContents, money;

    private AlertDialog startDayDialog, endDayDialog, wantDayDialog;
    private AlertDialog.Builder builder1, builder2, builder3;
    private WebView webView;
    private View.OnClickListener jobClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int ids = view.getId();
            magazinText.setSelected(false);
            tidal_ballText.setSelected(false);
            technicianText.setSelected(false);
            switch (ids) {
                case R.id.magazine:
                    magazinText.setSelected(true);
                    jobKind = "잡부";
                    break;
                case R.id.tidal_ball:
                    tidal_ballText.setSelected(true);
                    jobKind = "조력공";
                    break;
                case R.id.technician:
                    technicianText.setSelected(true);
                    jobKind = "기술공";
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_enrollment_2);
        init();
        init_webView();
    }

    private void init() {
        titleEdit = (EditText) findViewById(R.id.title);
        recruitmentContentsEdit = (EditText) findViewById(R.id.recruitment_contents);
        moneyEdit = (EditText) findViewById(R.id.money);

        locationText = (TextView) findViewById(R.id.location);
        startDayText = (TextView) findViewById(R.id.start_day);
        endDayText = (TextView) findViewById(R.id.end_day);
        jobDayText = (TextView) findViewById(R.id.job_day);
        magazinText = (TextView) findViewById(R.id.magazine);
        tidal_ballText = (TextView) findViewById(R.id.tidal_ball);
        technicianText = (TextView) findViewById(R.id.technician);
        realLocationText = (TextView) findViewById(R.id.real_location);
        magazinText.setSelected(true);
        locationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                webView.getSettings().setSupportMultipleWindows(true);
                webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                webView.setWebViewClient(new WebViewClient());
                webView.setWebChromeClient(new WebChromeClient(){
                    @Override
                    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                        WebView newWeb = new WebView(SceneRegiActivity.this);
                        newWeb.getSettings().setJavaScriptEnabled(true);
                        final Dialog dialog = new Dialog(SceneRegiActivity.this);
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
//                webView.setVisibility(View.VISIBLE);
            }
        });

        magazinText.setOnClickListener(jobClick);
        tidal_ballText.setOnClickListener(jobClick);
        technicianText.setOnClickListener(jobClick);

        setDialog1();
        setDialog2();
        setDialog3();
        startDayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDayDialog.show();
            }
        });
        endDayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endDayDialog.show();
            }
        });
        jobDayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wantDayDialog.show();
            }
        });

    }

    private void setDialog1() {
        builder1 = new AlertDialog.Builder(SceneRegiActivity.this);
        View v = getLayoutInflater().inflate(R.layout.datepicker_dialog, null);
        Button okBtn = v.findViewById(R.id.ok_btn);
        Button cancelBtn = v.findViewById(R.id.can_btn);
        final DatePicker datePicker = v.findViewById(R.id.date_picker);
        builder1.setView(v);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = datePicker.getYear();
                int month = datePicker.getMonth() + 1;
                int day = datePicker.getDayOfMonth();
                startDayText.setText(year + "-" + month + "-" + day);
                startDayDialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDayDialog.dismiss();
            }
        });
        startDayDialog = builder1.create();

    }

    private void setDialog2() {
        builder2 = new AlertDialog.Builder(SceneRegiActivity.this);
        View v = getLayoutInflater().inflate(R.layout.datepicker_dialog, null);
        Button okBtn = v.findViewById(R.id.ok_btn);
        Button cancelBtn = v.findViewById(R.id.can_btn);
        final DatePicker datePicker = v.findViewById(R.id.date_picker);
        builder2.setView(v);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = datePicker.getYear();
                int month = datePicker.getMonth() + 1;
                int day = datePicker.getDayOfMonth();
                endDayText.setText(year + "-" + month + "-" + day);
                endDayDialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endDayDialog.dismiss();
            }
        });
        endDayDialog = builder2.create();

    }

    private void setDialog3() {
        builder3 = new AlertDialog.Builder(SceneRegiActivity.this);
        View v = getLayoutInflater().inflate(R.layout.datepicker_dialog, null);
        Button okBtn = v.findViewById(R.id.ok_btn);
        Button cancelBtn = v.findViewById(R.id.can_btn);
        final DatePicker datePicker = v.findViewById(R.id.date_picker);
        builder3.setView(v);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = datePicker.getYear();
                int month = datePicker.getMonth() + 1;
                int day = datePicker.getDayOfMonth();
                jobDayText.setText(year + "-" + month + "-" + day);
                wantDayDialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDayDialog.dismiss();
            }
        });
        wantDayDialog = builder3.create();

    }

    public void nextBtn(View view) {
        if (checkNull()) {
            Intent intent = new Intent(SceneRegiActivity.this, SceneRegiPicturesActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("location", location);
            intent.putExtra("duringWork", duringWork);
            intent.putExtra("jobDay", jobDay);
            intent.putExtra("jobKind", jobKind);
            intent.putExtra("recruitmentContents", recruitmentContents);
            intent.putExtra("money", money);
            intent.putExtra("status", 0);
            startActivity(intent);
        } else {
            Toast.makeText(this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }

    }

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

    private boolean checkNull() {
        if (titleEdit.getText().toString().isEmpty() || realLocationText.getText().toString().isEmpty() ||
                startDayText.getText().toString().contains("날짜") || endDayText.getText().toString().contains("날짜") ||
                jobDayText.getText().toString().contains("날짜") || recruitmentContentsEdit.getText().toString().isEmpty() ||
                moneyEdit.getText().toString().isEmpty()) {

            return false;
        } else {
            title = titleEdit.getText().toString();
            location = realLocationText.getText().toString();
            duringWork = startDayText.getText().toString() + " ~ " + endDayText.getText().toString();
            jobDay = jobDayText.getText().toString();
            recruitmentContents = recruitmentContentsEdit.getText().toString();
            money = moneyEdit.getText().toString();
            return true;
        }
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    // 우편번, 시구, 대로주소
//                    String resultadd = "("+arg1.toString()+")"+" "+arg2.toString()+" "+arg3.toString();
//                    Intent intent = new Intent(getApplicationContext(), DetailInfoActivity.class);
//                    intent.putExtra("place", resultadd);
//                    startActivity(intent);
//                    finish();
                    // WebView를 초기화 하지않으면 재사용할 수 없음
                    realLocationText.setText(arg2.toString() + " " + arg3.toString());
                    webView.setVisibility(View.GONE);
                    init_webView();
                }
            });
        }
    }
}
