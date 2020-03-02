package com.sorum.sorum.view.login;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sorum.sorum.R;
import com.sorum.sorum.view.login.RegisterTwoActivity;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (android.os.Build.VERSION.SDK_INT >= 21){
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getApplication().getResources().getColor(R.color.white));
        }

        auth=FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button login = (Button) this.findViewById(R.id.login);
        final EditText txtkadi = (EditText) this.findViewById(R.id.username);
        final EditText txtpass = (EditText) this.findViewById(R.id.password);
        final EditText txtrepass = (EditText) this.findViewById(R.id.repassword);
        final EditText txtemail = (EditText) this.findViewById(R.id.email);
        Button register = (Button) this.findViewById(R.id.register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kadi = txtkadi.getText().toString();
                String pass = txtpass.getText().toString();
                String repass = txtrepass.getText().toString();
                String email = txtemail.getText().toString();

                if (TextUtils.isEmpty(kadi)) {
                    Toast.makeText(getApplicationContext(), "Lütfen kullanıcı adınızı giriniz", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(getApplicationContext(), "Lütfen parolanızı giriniz", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(repass)) {
                    Toast.makeText(getApplicationContext(), "Lütfen parolanızı tekrar giriniz", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Lütfen e-posta adresini giriniz", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!pass.equals(repass)){
                    Toast.makeText(getApplicationContext(), "Girilen şifreler uyuşmamaktadır.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pass.length()<6){
                    Toast.makeText(getApplicationContext(),"Parola en az 6 haneli olmalıdır",Toast.LENGTH_SHORT).show();
                    return;
                }


                auth.createUserWithEmailAndPassword(email,pass)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Yetkilendirme Hatası",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("username", kadi);
                                    user.put("email", email);
                                    user.put("follow", "null");

                                    mDatabase.child("users").child(auth.getCurrentUser().getUid()).setValue(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Intent ntent =new Intent(RegisterActivity.this, RegisterTwoActivity.class);///İntent ouşturup 2. activity'e gideceğini belirledik.
                                                    ntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                                    ntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    ntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    RegisterActivity.this.finish();
                                                    startActivity(ntent);
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
        });
            }
}