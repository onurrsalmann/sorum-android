package com.sorum.sorum;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class DenemeStartActivity extends AppCompatActivity {
    private String exam;
    private String examname;
    private String examdetay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deneme_start);

        Bundle gelenVeri=getIntent().getExtras();
        examdetay = gelenVeri.getCharSequence("examdetay").toString();
        examname = gelenVeri.getCharSequence("examname").toString();
        exam = gelenVeri.getCharSequence("exam").toString();

        Log.d("TAG", "exam: "+ exam+ " examname: "+examname+" examdetay: "+examdetay);
    }
}
