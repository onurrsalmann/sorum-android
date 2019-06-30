package com.sorum.sorum;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import static android.support.constraint.Constraints.TAG;

public class ProfilFragment extends Fragment {
    private Button logout;
    private Button ayarlar;
    private TextView name;
    private TextView username;
    private TextView examm;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profil, container, false);

        logout = v.findViewById(R.id.logout);
        ayarlar = v.findViewById(R.id.settings);
        name = v.findViewById(R.id.profile_name);
        username = v.findViewById(R.id.profile_username);
        examm= v.findViewById(R.id.profile_exam);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        String uid = currentUser.getUid();
        ValueEventListener veriler = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String namee =dataSnapshot.child("users").child(uid).child("name").getValue().toString();
                String user =dataSnapshot.child("users").child(uid).child("username").getValue().toString();
                String exam =dataSnapshot.child("users").child(uid).child("exam").getValue().toString();
                name.setText(namee);
                username.setText(user);
                examm.setText(exam);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        db.addValueEventListener(veriler);

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
