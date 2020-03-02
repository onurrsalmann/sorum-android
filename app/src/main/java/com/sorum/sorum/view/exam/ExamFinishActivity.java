package com.sorum.sorum.view.exam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sorum.sorum.R;
import com.sorum.sorum.model.ExamAnswer;

import java.util.ArrayList;

public class ExamFinishActivity extends AppCompatActivity {
    private ArrayList<ExamAnswer> falseAnswerList;
    private String examPub;
    private String examId;
    private String finishTime;
    private int examQuestSize;
    private int progressStatus = 0;
    private Handler handler = new Handler();


    private TextView pubTakipText;
    private TextView examFinishTime;
    private TextView examTrueSize;
    private TextView examFalseSize;
    private TextView yuzdeText;
    private TextView yuzdeTwoText;
    private TextView examSize;
    private Button examExit;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_finish);
        if (android.os.Build.VERSION.SDK_INT >= 21){
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getApplication().getResources().getColor(R.color.white));

            Bundle bundleObject = getIntent().getBundleExtra("bundle");
            falseAnswerList = (ArrayList<ExamAnswer>) bundleObject.getSerializable("falseAnswerList");
            examId = bundleObject.getString("examId");
            examPub = bundleObject.getString("examPub");
            finishTime = bundleObject.getString("finishTime");
            examQuestSize = bundleObject.getInt("examQuestSize");

            pubTakipText = findViewById(R.id.pubTakipText);
            examFinishTime = findViewById(R.id.examFinishTime);
            examTrueSize = findViewById(R.id.examTrueSize);
            examFalseSize = findViewById(R.id.examFalseSize);
            examSize = findViewById(R.id.examSize);
            examExit = findViewById(R.id.examExit);
            progressBar = findViewById(R.id.progressBar);
            yuzdeText = findViewById(R.id.yuzdeText);
            yuzdeTwoText = findViewById(R.id.yuzdeTwoText);

            examExit.setOnClickListener(v -> { onBackPressed(); });

            pubTakipText.setText(examPub);
            examFinishTime.setText(finishTime);
            examTrueSize.setText(String.valueOf(examQuestSize-falseAnswerList.size()));
            examFalseSize.setText(String.valueOf(falseAnswerList.size()));
            examSize.setText(String.valueOf(examQuestSize));

            progressBarStart();
        }
    }
    private void progressBarStart(){
        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 50) {
                    progressStatus += 1;
                    // Update the progress bar and display the
                    //current value in the text view
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                            yuzdeText.setText("%"+progressStatus);
                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
