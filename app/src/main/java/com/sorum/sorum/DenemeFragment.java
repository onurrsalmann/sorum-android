package com.sorum.sorum;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class DenemeFragment extends Fragment {

    ArrayList<exam> tarifList;
    FirebaseDatabase database;
    private ListView listView;
    private DatabaseReference mPostReference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_deneme, container, false);

        tarifList=new ArrayList<exam>();

        listView = (ListView) v.findViewById(R.id.listView);
        database = FirebaseDatabase.getInstance();
        final DatabaseReference dbRef=database.getReference("exams");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();
        mPostReference = database.getReference("users").child(uid).child("exam");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomAdapter adapter =new CustomAdapter(getActivity(),tarifList);
                Log.d("TAG", "tiklandi: "+adapter.getItem(position) );
            }
        });


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    String exam = dataSnapshot.getValue().toString();
                    dbRef.child(exam).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            tarifList.clear();
                            for (DataSnapshot ds:dataSnapshot.getChildren()){
                                String isim=ds.getKey();
                                tarifList.add(new exam(isim));
                            }
                            CustomAdapter adapter =new CustomAdapter(getActivity(),tarifList);
                            listView.setAdapter(adapter);
                            dbRef.removeEventListener(this);}
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else{
                    Intent ntent =new Intent(getContext(), RegisterTwoActivity.class);///İntent ouşturup 2. activity'e gideceğini belirledik.
                    ntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    ntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    getActivity().finish();
                    startActivity(ntent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mPostReference.addValueEventListener(postListener);

        return v;
    }

}
