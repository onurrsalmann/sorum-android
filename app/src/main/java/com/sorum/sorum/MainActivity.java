package com.sorum.sorum;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mPostReference;
    private ArrayAdapter<String> question_names;
    private Boolean question_names_selected = false;
    private String question_name;
    private String exam ;
    private String name;
    private String username;
    Context context = this;
    SQliteHelper sqlitedb = new SQliteHelper(context);
    LinearLayout ustBar;
    BottomNavigationView bottomnav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT >= 21){
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getApplication().getResources().getColor(R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        sqlitedb.onUpgrade(sqlitedb.getWritableDatabase(),1,2);

        question_names = sqlitedb.getLesson(MainActivity.this);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dbrun = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference dbRef = db.child("questions");
        ustBar = (LinearLayout) findViewById(R.id.ustbar);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


        if (currentUser == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            MainActivity.this.finish();
            startActivity(intent);
        }else{
            String uid = currentUser.getUid();
            mPostReference = db.child("users").child(uid).child("exam");
            ValueEventListener veriler = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String dbname =dataSnapshot.child(uid).child("name").getValue().toString();
                    String dbusername =dataSnapshot.child(uid).child("username").getValue().toString();
                    String dbexam =dataSnapshot.child(uid).child("exam").getValue().toString();
                    name = dbname;
                    username = dbusername;
                    exam = dbexam;
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            };
            dbrun.addValueEventListener(veriler);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dbrun = FirebaseDatabase.getInstance().getReference("users");
        String uid = currentUser.getUid();

        if(question_names_selected.equals(false)){
            AlertDialog.Builder uyari = new AlertDialog.Builder(MainActivity.this);
            TextView title = new TextView(MainActivity.this);
            title.setText("Çözmek İstediğiniz Dersi Seçin");
            title.setPadding(10, 30, 10, 15);
            title.setGravity(Gravity.CENTER);
            title.setTextColor(Color.BLACK);
            title.setTextSize(20);
            uyari.setCancelable(false);
            uyari.setCustomTitle(title);
            uyari.setAdapter(question_names, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ValueEventListener veriler = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            question_name = question_names.getItem(which);
                            question_names_selected=true;
                            String dbname =dataSnapshot.child(uid).child("name").getValue().toString();
                            String dbusername =dataSnapshot.child(uid).child("username").getValue().toString();
                            String dbexam =dataSnapshot.child(uid).child("exam").getValue().toString();
                            name = dbname;
                            username = dbusername;
                            exam = dbexam;

                            ArrayList<String> underlying = new ArrayList<String>();
                            for (int i = 0; i < question_names.getCount(); i++)
                                underlying .add(question_names.getItem(i));
                            Fragment home = new HomeFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("name", name);
                            bundle.putInt("question_name_which", which);
                            bundle.putString("exam", exam);
                            bundle.putString("username", username);
                            bundle.putString("question_name", question_name);
                            bundle.putStringArrayList("question_names", underlying);
                            home.setArguments(bundle);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    home).commit();
                            dialog.dismiss();
                            dbrun.removeEventListener(this);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                    };
                    dbrun.addValueEventListener(veriler);
                }
            });
            AlertDialog dialog = uyari.create();
            if(isNetworkAvailable() == true){
                dialog.show();
            }else{
                exam = "exam";
                name = "name";
                username = "username";
            }
        }

        bottomnav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomnav.setOnNavigationItemSelectedListener(navListener);

        ImageButton Istatistik = (ImageButton)  this.findViewById(R.id.istatistik);
        Istatistik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IstatistikActivity.class);
                startActivity(intent);
            }
        });
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    ArrayList<String> underlying = new ArrayList<String>();
                    for (int i = 0; i < question_names.getCount(); i++)
                        underlying .add(question_names.getItem(i));
                    Fragment selectedFragment = null;
                    Bundle bundle = new Bundle();
                    bundle.putString("name", sqlitedb.getUser("name"));
                    bundle.putString("exam", sqlitedb.getUser("exam"));
                    bundle.putString("username", sqlitedb.getUser("username"));
                    bundle.putString("question_name", question_name);
                    bundle.putStringArrayList("question_names", underlying);

                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_search:
                            selectedFragment = new SearchFragment();
                            break;
                        case R.id.nav_deneme:
                            selectedFragment = new DenemeFragment();
                            break;
                        case R.id.nav_profil:
                            selectedFragment = new ProfilFragment();
                            break;
                    }
                    selectedFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };
    public void ustBarHide() {
        if (ustBar.getVisibility() == View.VISIBLE) {
            ustBar.setVisibility(View.GONE);
            bottomnav.setVisibility(View.GONE);
        }else if (ustBar.getVisibility() == View.GONE) {
            ustBar.setVisibility(View.VISIBLE);
            bottomnav.setVisibility(View.VISIBLE);
        }
    }
}
