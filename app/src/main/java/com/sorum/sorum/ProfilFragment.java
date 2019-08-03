package com.sorum.sorum;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
        tvusername.setText(username);
        tvexam.setText(exam);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getActivity().finish();
                startActivity(intent);
            }
        });
        ayarlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), AyarlarActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }
}
