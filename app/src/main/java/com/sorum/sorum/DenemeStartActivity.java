package com.sorum.sorum;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class DenemeStartActivity extends AppCompatActivity {
    private String exam;
    private String examname;
    private String examdetay;

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

        Log.d("TAG", "exam: "+ exam+ " examname: "+examname+" examdetay: "+examdetay);
    }
}
