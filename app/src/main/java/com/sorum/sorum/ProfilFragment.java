package com.sorum.sorum;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ProfilFragment extends Fragment {
    private Button logout;
    private Button ayarlar;
    private TextView tvname;
    private TextView tvusername;
    private TextView tvexam;
    private String exam;
    private String username;
    private String name;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profil, container, false);
        SQliteHelper sqlitedb = new SQliteHelper(getActivity());


        logout = v.findViewById(R.id.logout);
        ayarlar = v.findViewById(R.id.settings);
        tvname = v.findViewById(R.id.profile_name);
        tvusername = v.findViewById(R.id.profile_username);
        tvexam= v.findViewById(R.id.profile_exam);
        Bundle bundle = getArguments();
        name = bundle.getString("name");
        exam = bundle.getString("exam");
        username = bundle.getString("username");
        tvname.setText(name);
        tvusername.setText("@"+username);
        tvexam.setText(exam);
        sqlitedb.onUpgrade(sqlitedb.getWritableDatabase(),1,2);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Çıkış yapmak istediğine emin misin?");
        builder.setNegativeButton("Vazgeç", (dialog, id) -> { });
        builder.setPositiveButton("Çıkış Yap", (dialog, id) -> {
            FirebaseAuth.getInstance().signOut();
            sqlitedb.resetTables();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getActivity().finish();
            startActivity(intent);
        });

        logout.setOnClickListener(v1 -> builder.show());
        ayarlar.setOnClickListener(v12 -> {
            Intent intent = new Intent(getActivity(), AyarlarActivity.class);
            startActivity(intent);
        });
        return v;
    }
}
