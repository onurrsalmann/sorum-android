package com.sorum.sorum;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DenemeFragment extends Fragment {

    private String exam;
    private ArrayList<String> deneme_baslik;
    FirebaseDatabase database;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_deneme, container, false);
        deneme_baslik =new ArrayList<String>();
        Bundle bundle = getArguments();
        exam = bundle.getString("exam");
        listView = (ListView) v.findViewById(R.id.listView);
        database = FirebaseDatabase.getInstance();
        final DatabaseReference dbRef=database.getReference("exams");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle bundle = new Bundle();
                bundle.putString("examname",deneme_baslik.get(position));
                bundle.putString("exam", exam);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                DenemeTwoFragment denemeTwoFragment = new DenemeTwoFragment() ;
                denemeTwoFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.fragment_container, denemeTwoFragment);
                fragmentTransaction.commit();
            }
        });
        dbRef.child(exam).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                deneme_baslik.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    String isim=ds.getKey();
                    deneme_baslik.add(isim);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.listview_text, deneme_baslik);
                listView.setAdapter(adapter);
                dbRef.removeEventListener(this);}
                @Override
                public void onCancelled(DatabaseError databaseError) { }
        });
        return v;
    }
}
