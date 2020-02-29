package com.sorum.sorum;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AyarlarActivity extends AppCompatActivity {
    private List<String> sinav = new ArrayList<String>();
    private Spinner hazirlann;
    private Button vazgec;
    private Button kaydet;
    private Button userdelete;
    private Button istatistiksifirla;
    private EditText username;
    private EditText name;
    private ArrayAdapter<String> dataaAdapterForIller;
    private String secilenSinav = "Lütfen Sınav Seçiniz";
    private FirebaseAuth auth;
    private String namename;
    private Boolean sasa = true;
    private String userExam = "";
    private String dbname;
    private String dbusername;
    private String dbexam;
    Context context = this;
    FirebaseAuth firebaseAuth;
    SQliteHelper sqlitedb = new SQliteHelper(context);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayarlar);
        if (android.os.Build.VERSION.SDK_INT >= 21){
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getApplication().getResources().getColor(R.color.white));
        }


        sqlitedb.onUpgrade(sqlitedb.getWritableDatabase(),1,2);
        auth = FirebaseAuth.getInstance();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference animalsRef = rootRef.child("users");
        String uid = currentUser.getUid();
        firebaseAuth = FirebaseAuth.getInstance();

        hazirlann = (Spinner) findViewById(R.id.hazirlann);
        vazgec = (Button) findViewById(R.id.vazgec);
        kaydet = (Button) findViewById(R.id.kaydet);
        istatistiksifirla = (Button) findViewById(R.id.istatistiksifirla);
        userdelete = (Button) findViewById(R.id.userdelete);
        name = (EditText) findViewById(R.id.namechange);
        username = (EditText) findViewById(R.id.usernamechange);
        Context aa = this;
        ImageButton Istatistik = (ImageButton)  this.findViewById(R.id.istatistik);
        final FirebaseUser user  = firebaseAuth.getCurrentUser();

        userdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user!=null){
                    sasa = false;
                    AlertDialog.Builder uyari = new AlertDialog.Builder(AyarlarActivity.this);
                    uyari.setTitle("Emin misin?");
                    uyari.setMessage("Bu işlem hesabınızı ve tüm bilgilerinizi silecektir. Geri dönüşü olamaz. Emin misiniz ?");
                    uyari.setPositiveButton("Sil", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Log.d("TAG", "BURDA789");
                                                Toast.makeText(getApplicationContext(),"Hesabınız silindi.",Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(AyarlarActivity.this, MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                AyarlarActivity.this.finish();
                                                startActivity(intent);
                                                animalsRef.child(uid).removeValue();
                                            }
                                            if(task.isSuccessful()){
                                                Toast.makeText(getApplicationContext(),"Hesabınız silinemedi.",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    });

                    uyari.setNegativeButton("Vazgeç", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog = uyari.create();
                    alertDialog.show();
                }
            }
        });


        if(sasa == true) {
            db.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("TAG", "BURDA00");
                    dbname = dataSnapshot.child("name").getValue().toString();
                    dbusername = dataSnapshot.child("username").getValue().toString();
                    dbexam = dataSnapshot.child("exam").getValue().toString();
                    namename = dbusername;
                    name.setText(dbname);
                    username.setText(dbusername);
                    secilenSinav = dbexam;
                    sinav.add(dbexam);
                    db.child("exams").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds:dataSnapshot.getChildren()){
                                if(!dbexam.equals(ds.getKey())){
                                    sinav.add(ds.getKey());
                                }
                            }
                            dataaAdapterForIller = new ArrayAdapter<String>(aa, android.R.layout.simple_spinner_item, sinav);
                            dataaAdapterForIller.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            hazirlann.setAdapter(dataaAdapterForIller);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    db.removeEventListener(this);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        hazirlann.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                secilenSinav = parent.getSelectedItem().toString();
                Log.d("TAG", "sala: "+parent.getSelectedItem().toString());
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Istatistik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AyarlarActivity.this, IstatistikActivity.class);
                startActivity(intent);
            }
        });
        vazgec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { onBackPressed(); }
        });


        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_string = name.getText().toString();
                String username_string = username.getText().toString();
                if(username_string.equals(namename) == false){
                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String search = username_string;
                            Boolean found;
                            Boolean found_sonuc = false;
                            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                String movieName = ds.child("username").getValue(String.class);
                                found = movieName.equals(search);
                                if(found.equals(true)){
                                    found_sonuc = true;
                                    Log.d("TAG", "burdamm: "+found+" sasa:"+movieName+" sasasa: "+search);
                                    Toast.makeText(AyarlarActivity.this, "Bu kullanıcı adı kullanılamaz", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            if(found_sonuc.equals(false)){
                                Log.d("Tag", secilenSinav);
                                Map<String, Object> userr = new HashMap<>();
                                userr.put("name", name_string);
                                userr.put("username", username_string);
                                userr.put("exam", secilenSinav);


                                db.child("users").child(auth.getCurrentUser().getUid()).updateChildren(userr)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                ProgressDialog progressDialog = ProgressDialog.show(context, "",
                                                        "Kaydediliyor. Lütfen bekleyin...", true);
                                                progressDialog.show();
                                                sqlitedb.addUser(name_string,username_string,secilenSinav);
                                                Intent myIntent = new Intent(AyarlarActivity.this, MainActivity.class);
                                                finish();
                                                AyarlarActivity.this.startActivity(myIntent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("TAG", "Error writing document", e);
                                                Log.w("TAG", "Yazdırılamadı", e);
                                            }
                                        });
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    };
                    animalsRef.addListenerForSingleValueEvent(eventListener);
                }else{
                    Log.d("Tag", secilenSinav);
                    Map<String, Object> userr = new HashMap<>();
                    userr.put("name", name_string);
                    userr.put("username", username_string);
                    userr.put("exam", secilenSinav);


                    db.child("users").child(auth.getCurrentUser().getUid()).updateChildren(userr)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    ProgressDialog progressDialog = ProgressDialog.show(context, "",
                                            "Kaydediliyor. Lütfen bekleyin...", true);
                                    progressDialog.show();
                                    sqlitedb.addUser(name_string,username_string,secilenSinav);
                                    Intent myIntent = new Intent(AyarlarActivity.this, MainActivity.class);
                                    finish();
                                    AyarlarActivity.this.startActivity(myIntent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("TAG", "Error writing document", e);
                                    Log.w("TAG", "Yazdırılamadı", e);
                                }
                            });
                }
            }
        });
    }
}
