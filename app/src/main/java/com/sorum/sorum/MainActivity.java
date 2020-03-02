package com.sorum.sorum;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sorum.sorum.model.SQliteHelper;
import com.sorum.sorum.view.login.LoginActivity;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class MainActivity extends AppCompatActivity {
    protected ArrayAdapter<String> question_names;
    Context context = this;
    Deque<Integer> mStack = new ArrayDeque<>();
    boolean isBackPressed  = false;
    Fragment HomeFragment = new com.sorum.sorum.view.home.HomeFragment();
    Fragment SearchFragment = new com.sorum.sorum.view.search.SearchFragment();
    Fragment DenemeFragment = new com.sorum.sorum.view.deneme.DenemeFragment();
    Fragment ProfilFragment = new com.sorum.sorum.view.profil.ProfilFragment();
    FragmentManager mFragmentManager = getSupportFragmentManager();
    SQliteHelper sqlitedb = new SQliteHelper(context);
    LinearLayout ustBar;
    BottomNavigationView bottomnav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 21){
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getApplication().getResources().getColor(R.color.white));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            MainActivity.this.finish();
            startActivity(intent);
        }else{
            sqlitedb.onUpgrade(sqlitedb.getWritableDatabase(),1,2);
            question_names = sqlitedb.getLesson(MainActivity.this, R.layout.question_select);
            ustBar = findViewById(R.id.ustbar);
            bottomnav = findViewById(R.id.bottom_navigation);
            ImageButton Istatistik = this.findViewById(R.id.istatistik);
            ArrayList<String> underlying = new ArrayList<>();

            for (int i = 0; i < question_names.getCount(); i++)
                underlying .add(question_names.getItem(i));

            Log.d("Salman", sqlitedb.getUser("name"));
            Bundle bundle = new Bundle();
            bundle.putString("name", sqlitedb.getUser("name"));
            bundle.putString("exam", sqlitedb.getUser("exam"));
            bundle.putString("username", sqlitedb.getUser("username"));
            bundle.putStringArrayList("question_names", underlying);
            setBottomNavigationView(bundle);

            Istatistik.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, com.sorum.sorum.view.statistic.IstatistikActivity.class);
                startActivity(intent);
            });
        }


        /*if(question_names_selected.equals(false)){
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
                            /*String dbname =dataSnapshot.child(uid).child("name").getValue().toString();
                            String dbusername =dataSnapshot.child(uid).child("username").getValue().toString();
                            String dbexam =dataSnapshot.child(uid).child("exam").getValue().toString();
                            name = dbname;
                            username = dbusername;
                            exam = dbexam;*/

                           /* ArrayList<String> underlying = new ArrayList<String>();
                            for (int i = 0; i < question_names.getCount(); i++)
                                underlying .add(question_names.getItem(i));
                            Fragment home = new HomeFragment();
                            Bundle bundle = new Bundle();*/
                           /* bundle.putString("name", name);
                            bundle.putInt("question_name_which", which);
                            bundle.putString("exam", exam);
                            bundle.putString("username", username);*/
                           /* bundle.putString("question_name", question_name);
                            //bundle.putStringArrayList("question_names", underlying);
                            home.setArguments(bundle);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    home).commit();
                            dialog.dismiss();
                            dbrun.removeEventListener(this);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                    };

                }
            });
            AlertDialog dialog = uyari.create();*/
    }
    private void setBottomNavigationView(Bundle bundle) {
        bottomnav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    if(!isBackPressed) {
                        pushFragmentIntoStack(R.id.nav_home);
                    }
                    isBackPressed = false;
                    setFragment(HomeFragment, "HOME_FRAGMENT", bundle);
                    return true;
                case R.id.nav_search:
                    if(!isBackPressed) {
                        pushFragmentIntoStack(R.id.nav_search);
                    }
                    isBackPressed = false;
                    setFragment(SearchFragment, "SEARCH_FRAGMENT", bundle);
                    return true;
                case R.id.nav_deneme:
                    if(!isBackPressed) {
                        pushFragmentIntoStack(R.id.nav_deneme);
                    }
                    isBackPressed = false;
                    setFragment(DenemeFragment, "DENEME_FRAGMENT", bundle);
                    return true;
                case R.id.nav_profil:
                    if(!isBackPressed) {
                        pushFragmentIntoStack(R.id.nav_profil);
                    }
                    isBackPressed = false;
                    setFragment(ProfilFragment, "PROFIL_FRAGMENT", bundle);
                    return true;

                default:
                    return false;
            }
        });
        bottomnav.setOnNavigationItemReselectedListener(item -> {
        });
        bottomnav.setSelectedItemId(R.id.nav_home);
        HomeFragment.setArguments(bundle);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, HomeFragment, "HOME_FRAGMENT");
        transaction.commit();
        pushFragmentIntoStack(R.id.nav_home);
    }
    private void pushFragmentIntoStack(int id) {
        if(mStack.size() < 3) {
            mStack.push(id);
        } else {
            mStack.removeLast();
            mStack.push(id);
        }
    }
    private void setFragment(Fragment fragment, String tag, Bundle bundle) {
        fragment.setArguments(bundle);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, tag);
        transaction.commit();
    }
    public void ustBarHide() {
        if (ustBar.getVisibility() == View.VISIBLE) {
            ustBar.setVisibility(View.GONE);
            bottomnav.setVisibility(View.GONE);
        }else if (ustBar.getVisibility() == View.GONE) {
            ustBar.setVisibility(View.VISIBLE);
            bottomnav.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onBackPressed() {
        if(bottomnav.getSelectedItemId () != R.id.nav_home) {
            bottomnav.setSelectedItemId(R.id.nav_home);
        } else {
            super.onBackPressed();
        }
    }
}