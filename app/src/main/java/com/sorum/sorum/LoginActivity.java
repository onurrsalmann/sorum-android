package com.sorum.sorum;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "Sorum";
    private FirebaseAuth mAuth;
    Context context = this;
    SQliteHelper sqlitedb = new SQliteHelper(context);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (android.os.Build.VERSION.SDK_INT >= 21){
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getApplication().getResources().getColor(R.color.white));
        }

        sqlitedb.onUpgrade(sqlitedb.getWritableDatabase(),1,2);
        mAuth = FirebaseAuth.getInstance();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = rootRef.child("users");
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }else{
            final EditText txtUserName = (EditText) this.findViewById(R.id.username);
            final EditText txtUserPass = (EditText) this.findViewById(R.id.password);
            Button loginButton = (Button) this.findViewById(R.id.login);
            Button registerButton = (Button) this.findViewById(R.id.register);
            Button passChange = (Button) this.findViewById(R.id.sifredegis);


            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                }
            });
            passChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, PassChangeActivity.class));
                }
            });

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //ProgressDialog
                    ProgressDialog progressDialog = ProgressDialog.show(context, "",
                            "Giriş Yapılıyor. Lütfen bekleyin...", true);

                    String kadi = txtUserName.getText().toString();
                    String pass = txtUserPass.getText().toString();

                    if (TextUtils.isEmpty(kadi)) {
                        Toast.makeText(getApplicationContext(), "Lütfen kullanıcı adınızı giriniz giriniz", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (TextUtils.isEmpty(pass)) {
                        Toast.makeText(getApplicationContext(), "Lütfen parolanızı giriniz", Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        ValueEventListener eventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String searchUserName = kadi;
                                Boolean found = false;
                                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                    // Firebase users tablosunda kullanıcı adını arıyor
                                    String userName = ds.child("username").getValue(String.class);
                                    found = userName.equals(searchUserName);
                                    if(found == true){
                                       // alertShow.show();
                                        progressDialog.show();

                                       // Toast.makeText(LoginActivity.this, "Giriş Yapılıyor", Toast.LENGTH_SHORT).show();
                                        String kadi = ds.child("email").getValue().toString();
                                        String name = ds.child("name").getValue().toString();
                                        String secilensinav = ds.child("exam").getValue().toString();
                                        rootRef.child("exams").child(secilensinav).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot dk:dataSnapshot.getChildren()){
                                                    String examName = dk.getKey();
                                                    sqlitedb.addExam(examName);
                                                }
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) { }
                                        });
                                        rootRef.child("questions").child(secilensinav).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot ds:dataSnapshot.getChildren()){
                                                    String dersName=ds.getKey();
                                                    sqlitedb.addLesson(dersName);
                                                }
                                                rootRef.child("questions").child(secilensinav).removeEventListener(this);}
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) { }
                                        });
                                        //Firebase Auth ile giriş yapılıyor
                                        mAuth.signInWithEmailAndPassword(kadi, pass)
                                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            sqlitedb.addUser(userName, name ,secilensinav);
                                                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            LoginActivity.this.finish();
                                                            startActivity(intent);

                                                        } else {
                                                            //alertShow.dismiss();
                                                            progressDialog.dismiss();
                                                            String uyarimesaj;
                                                            switch (task.getException().getMessage()){
                                                                case "The password is invalid or the user does not have a password.":
                                                                    uyarimesaj = "Şifreniz veya kullanıcı adınız hatalı. Lütfen tekrar deneyin";
                                                                    break;
                                                                case "We have blocked all requests from this device due to unusual activity. Try again later. [ Too many unsuccessful login attempts.  Please include reCaptcha verification or try again later ]":
                                                                    uyarimesaj = "Hata oluştu. Lütfen tekrar giriş yapmayı deneyin.";
                                                                    break;
                                                                default:
                                                                    uyarimesaj = task.getException().getMessage();
                                                                    break;
                                                            }
                                                            Toast.makeText(getApplicationContext(), uyarimesaj, Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                    }else{
                                        Toast.makeText(LoginActivity.this, "Kullanıcı adınız yanlış", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        };
                        usersRef.addListenerForSingleValueEvent(eventListener);
                    }
                }
            });
        }
    }
}


