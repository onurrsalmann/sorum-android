package com.sorum.sorum.view.deneme;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sorum.sorum.R;
import com.sorum.sorum.model.Post;

import java.util.ArrayList;
import java.util.Random;

public class DenemeStartActivity extends AppCompatActivity {
    private String exam;
    private String examname;
    private String examdetay;
    private CountDownTimer countDownTimer;
    private DatabaseReference mDatabase;
    private String examPub ="";
    private String examTime ="";
    private ArrayList<Post> examQuest  = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deneme_start);
        if (android.os.Build.VERSION.SDK_INT >= 21){
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getApplication().getResources().getColor(R.color.white));
        }
        Bundle gelenVeri=getIntent().getExtras();
        examdetay = gelenVeri.getCharSequence("examdetay").toString();
        examname = gelenVeri.getCharSequence("examname").toString();
        exam = gelenVeri.getCharSequence("exam").toString();
        TextView examNameLbl = findViewById(R.id.examName);
        TextView examLevelLbl = findViewById(R.id.examLevel);
        TextView examPubLbl = findViewById(R.id.examPub);
        TextView examTimeLbl = findViewById(R.id.examTime);
        Button examStartBtn = findViewById(R.id.examStartButton);
        Button examExitBtn = findViewById(R.id.examExit);
        examNameLbl.setText(examname);
        examLevelLbl.setText(examdetay);
        Bundle bundle = new Bundle();
        Intent intent = new Intent(DenemeStartActivity.this, com.sorum.sorum.view.exam.ExamActivity.class);

        //Firebase Exam Data Read
        mDatabase = FirebaseDatabase.getInstance().getReference().child("exams").child(exam).child(examname).child(examdetay);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    Long aa= dataSnapshot.getChildrenCount();
                    int random = new Random().nextInt(aa.intValue());
                    examPub = "Bu deneme " + dataSnapshot.child(String.valueOf(random)).child("pub").getValue().toString() + " Yayıncılığa aittir.";
                    examTime = dataSnapshot.child(String.valueOf(random)).child("time").getValue().toString();
                    examPubLbl.setText(examPub);
                    examTimeLbl.setText(examTime);
                    int examDk = Integer.parseInt(examTime.substring(0,2)) * 60;
                    int examSn = Integer.parseInt(examTime.substring(3,5));
                    for(DataSnapshot data:dataSnapshot.child(String.valueOf(random)).getChildren()){
                        if(data.getKey().equals("time")==false && data.getKey().equals("pub")==false){
                            examQuest.add(new Post(
                                    data.getKey(),
                                    data.child("desp").getValue().toString(),
                                    data.child("Soru").getValue().toString(),
                                    data.child("cevapA").getValue().toString(),
                                    data.child("cevapB").getValue().toString(),
                                    data.child("cevapC").getValue().toString(),
                                    data.child("cevapD").getValue().toString(),
                                    data.child("cevapE").getValue().toString(),
                                    data.child("dogruCevap").getValue().toString()
                            ));
                        }
                    }
                    bundle.putString("examPub", dataSnapshot.child(String.valueOf(random)).child("pub").getValue().toString());
                    bundle.putString("examId", dataSnapshot.child(String.valueOf(random)).getKey());
                    bundle.putSerializable("examQuest",examQuest);
                    bundle.putInt("examTime",((examDk+examSn)*1000)+1000);

                    intent.putExtra("bundle",bundle);

                    countDownTimer = new CountDownTimer(16000,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            Spannable span = new SpannableString("Başla" + "\n(" +  millisUntilFinished/1000 + ")");
                            examStartBtn.setText(span);
                        }
                        @Override
                        public void onFinish() {
                            DenemeStartActivity.this.finish();
                            startActivity(intent);
                        }
                    }.start();

                    examStartBtn.setOnClickListener(v -> {
                        countDownTimer.cancel();
                        DenemeStartActivity.this.finish();
                        startActivity(intent);
                    });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("hata", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(postListener);
        examExitBtn.setOnClickListener(v -> {
            countDownTimer.cancel();
            onBackPressed();
        });
    }
    @Override
    public void onBackPressed() {
        countDownTimer.cancel();
        finish();
    }
}
