package com.sorum.sorum;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterTwoActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private static final String TAG = "MainActivity";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<String> sinav = new ArrayList<String>();
    private Spinner hazirlan;
    private String dgtarih;
    private ArrayAdapter<String> dataAdapterForIller;
    private String secilenSinav = "Lütfen Sınav Seçiniz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);

        auth = FirebaseAuth.getInstance();

        Button ilerle = (Button) this.findViewById(R.id.ilerle);
        EditText ad = (EditText) this.findViewById(R.id.ad);
        DatePicker pickerDate = (DatePicker)findViewById(R.id.dgtarih);

        Calendar today = Calendar.getInstance();

        pickerDate.init(
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener(){

                    @Override
                    public void onDateChanged(DatePicker view,
                                              int year, int monthOfYear,int dayOfMonth) {
                        monthOfYear +=1;
                        dgtarih = dayOfMonth+"-"+monthOfYear+"-"+year;

                    }});


        sinav.add("Lütfen Sınav Seçiniz");
        db.collection("deneme")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                sinav.add(document.getId());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


        hazirlan = (Spinner) findViewById(R.id.hazirlan);
        dataAdapterForIller = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sinav);
        dataAdapterForIller.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hazirlan.setAdapter(dataAdapterForIller)  ;
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
        ilerle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kadi = ad.getText().toString();
                if (TextUtils.isEmpty(kadi)) {
                    Toast.makeText(getApplicationContext(), "Lütfen kullanıcı adınızı giriniz", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, Object> user = new HashMap<>();
                user.put("name", kadi);
                user.put("exam", secilenSinav);
                user.put("dateofbirth", dgtarih);

                db.collection("users")
                        .document(auth.getCurrentUser().getUid())
                        .update(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void thisVoid ) {
                                Intent intent = new Intent(RegisterTwoActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                RegisterTwoActivity.this.finish();
                                startActivity(intent);
                            }

                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("sorum", "Hata ", e);
                            }
                        });



            }
        });

    }
}
