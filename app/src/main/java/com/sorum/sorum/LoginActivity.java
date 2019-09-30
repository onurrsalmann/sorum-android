package com.sorum.sorum;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
                                        Toast.makeText(LoginActivity.this, "Giriş Yapılıyor", Toast.LENGTH_SHORT).show();
                                        String kadi = ds.child("email").getValue().toString();
                                        //Firebase Auth ile giriş yapılıyor
                                        mAuth.signInWithEmailAndPassword(kadi, pass)
                                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            LoginActivity.this.finish();
                                                            startActivity(intent);

                                                        } else {
                                                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                    }else{
                                        Toast.makeText(LoginActivity.this, "Kullanıcı Adınız Yanlış", Toast.LENGTH_SHORT).show();
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


