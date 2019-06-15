package com.sorum.sorum;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class AyarlarActivity extends AppCompatActivity {

    private List<String> sinav = new ArrayList<String>();
    private Spinner hazirlan;
    private ArrayAdapter<String> dataAdapterForIller;
    private String secilenSinav = "Lütfen Sınav Seçiniz";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayarlar);

        sinav.add("Lütfen Sınav Seçiniz");
        sinav.add("1inci");
        hazirlan = (Spinner) findViewById(R.id.hazirlan);
        dataAdapterForIller = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sinav);
        dataAdapterForIller.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hazirlan.setAdapter(dataAdapterForIller);
        hazirlan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if(parent.getSelectedItem().toString() != "Lütfen Sınav Seçiniz"){
                    secilenSinav = parent.getSelectedItem().toString();

                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ImageButton Istatistik = (ImageButton)  this.findViewById(R.id.istatistik);
        Istatistik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AyarlarActivity.this, IstatistikActivity.class);
                startActivity(intent);
            }
        });
    }
}
