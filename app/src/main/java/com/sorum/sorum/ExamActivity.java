package com.sorum.sorum;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExamActivity extends AppCompatActivity {
    CountDownTimer countDownTimer;
    TextView examSoru;
    TextView examOptionA;
    TextView examOptionB;
    TextView examOptionC;
    TextView examOptionD;
    TextView examOptionE;
    TextView examDesp;
    String examOptionTrue;
    String examQuestId;
    Button examQuestNext;
    Button examQuestPass;
    ArrayList<Post> examQuest;
    RadioGroup optionBlock;
    ArrayList<Integer> examPass = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        optionBlock = findViewById(R.id.optionBlock);
        TextView lastTimeLbl = findViewById(R.id.examLastTime);
        examSoru = findViewById(R.id.examSoru);
        examDesp = findViewById(R.id.examDesp);
        examOptionA = findViewById(R.id.examOptionA);
        examOptionB = findViewById(R.id.examOptionB);
        examOptionC = findViewById(R.id.examOptionC);
        examOptionD = findViewById(R.id.examOptionD);
        examOptionE = findViewById(R.id.examOptionE);
        examQuestNext = findViewById(R.id.examQuestNext);
        examQuestPass = findViewById(R.id.examQuestPass);
        Button examExitBtn = findViewById(R.id.examLastExit);
        Bundle bundleObject = getIntent().getBundleExtra("bundle");
        examQuest = (ArrayList<Post>) bundleObject.getSerializable("examQuest");
        Integer examTime = bundleObject.getInt("examTime");

        ArrayList<String> examStringList = new ArrayList<>();
        int i=0;
        //soruyu aldık listede
        if (examQuest == null){
            throw new AssertionError();
        }else{
            questReturn(i, examQuest.size()-1);
        }

        if (android.os.Build.VERSION.SDK_INT >= 21){
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getApplication().getResources().getColor(R.color.white));
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ayrılmak istediğine emin misin?");
        builder.setMessage("Bu deneme hiç çözülmemiş sayılıcak ve sonradan kaldığınız yerden devam edemiyeceksiniz.");
        builder.setNegativeButton("KAL", (dialog, id) -> { });
        builder.setPositiveButton("AYRIL", (dialog, id) -> {
            countDownTimer.cancel();
            Intent intent = new Intent(ExamActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
        });
        examExitBtn.setOnClickListener(v -> builder.show());
        countDownTimer = new CountDownTimer(examTime,1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                int saniye = (int)millisUntilFinished/1000;
                String lastTimeDk = "00";
                String lastTimeSn;
                if(saniye >= 60){
                    int dakika = saniye/60;
                    saniye = saniye-(dakika*60);
                    if(dakika<10){ lastTimeDk = "0"+dakika; }else{ lastTimeDk = String.valueOf(dakika); }
                    if(saniye<10){ lastTimeSn = "0"+saniye; }else{ lastTimeSn = String.valueOf(saniye); }
                }else{
                    if(saniye<10){ lastTimeSn = "0"+saniye; }else{ lastTimeSn = String.valueOf(saniye); }
                }
                lastTimeLbl.setText(lastTimeDk+":"+lastTimeSn);
            }
            @Override
            public void onFinish() {

            }
        }.start();
    }

    private void questReturn(int i, int max){
        if(i<max){
            examOptionTrue = examQuest.get(i).getDogruCevap();
            examQuestId = examQuest.get(i).getSoruId();
            examSoru.setText(examQuest.get(i).getSoru());
            examOptionA.setText(examQuest.get(i).getCevapA());
            examOptionB.setText(examQuest.get(i).getCevapB());
            examOptionC.setText(examQuest.get(i).getCevapC());
            examOptionD.setText(examQuest.get(i).getCevapD());
            examOptionE.setText(examQuest.get(i).getCevapD());
            examDesp.setText(examQuest.get(i).getDesp());
            examQuestNext.setOnClickListener(v-> {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {

                    }
                }, 5000);   //5 seconds
               // Log.d("salman", ""+optionBlock.indexOfChild(findViewById(optionBlock.getCheckedRadioButtonId())));
                questReturn(i+1, max);
            });
            examQuestPass.setOnClickListener(v -> {
                examPass.add(i);
                questReturn(i+1, max);
            });
        }else if(i == max){
            if(examPass.size() == 0){
                examSoru.setText("Soru bitti");
                examQuestNext.setClickable(false);
                examQuestPass.setClickable(false);
            }else{
                passQuestReturn(0);
            }
        }
    }
    private void passQuestReturn(int a) {
        if(a<examPass.size()) {
            examOptionTrue = examQuest.get(examPass.get(a)).getDogruCevap();
            examQuestId = examQuest.get(examPass.get(a)).getSoruId();
            examSoru.setText(examQuest.get(examPass.get(a)).getSoru());
            examOptionA.setText(examQuest.get(examPass.get(a)).getCevapA());
            examOptionB.setText(examQuest.get(examPass.get(a)).getCevapB());
            examOptionC.setText(examQuest.get(examPass.get(a)).getCevapC());
            examOptionD.setText(examQuest.get(examPass.get(a)).getCevapD());
            examOptionE.setText(examQuest.get(examPass.get(a)).getCevapD());
            examDesp.setText(examQuest.get(examPass.get(a)).getDesp());
            examQuestNext.setOnClickListener(v-> {
               // Log.d("salman", ""+optionBlock.indexOfChild(findViewById(optionBlock.getCheckedRadioButtonId())));
                examPass.remove(a);
                passQuestReturn(a);
            });
            examQuestPass.setOnClickListener(v -> {
                passQuestReturn(a+1);
            });
        }
        else if(a == examPass.size()){
            if(examPass.size() == 0){
                examSoru.setText("Soru bitti");
                examDesp.setText("Soru bitti");
                examQuestNext.setClickable(false);
                examQuestPass.setClickable(false);
            }else{ passQuestReturn(0); }
        }
    }

    @Override
    public void onBackPressed() {
        countDownTimer.cancel();
        super.onBackPressed();
        Intent intent = new Intent(ExamActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
    }
}
