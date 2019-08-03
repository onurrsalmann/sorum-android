package com.sorum.sorum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DenemeTwoFragment extends Fragment {
    private ArrayList<String> denemetwo_baslik;
    FirebaseDatabase database;
    private ListView listViewtwo;
    private DatabaseReference mPostReference;
    private String examname;
    private String exam;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_deneme_two, container, false);
        Bundle bundle = getArguments();
        examname = bundle.getString("examname");
        exam = bundle.getString("exam");
        denemetwo_baslik =new ArrayList<String>();
        database = FirebaseDatabase.getInstance();
        final DatabaseReference dbRef=database.getReference("exams").child(exam);

        TextView deneme_two_baslik = v.findViewById(R.id.deneme_two_baslik);
        listViewtwo = (ListView) v.findViewById(R.id.listViewtwo);
        deneme_two_baslik.setText(examname);

        listViewtwo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("examname",denemetwo_baslik.get(position));

                CharSequence examdetay =denemetwo_baslik.get(position);//Yazı dizisi oluşturup kullanıcının yazdığı yazıyı buraya attık.
                Intent intent=new Intent(getContext(),DenemeStartActivity.class);///İntent ouşturup 2. activity'e gideceğini belirledik.
                intent.putExtra("examdetay",examdetay);
                intent.putExtra("exam", exam);
                intent.putExtra("examname", examname);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getActivity().finish();
                startActivity(intent);
            }
        });
        dbRef.child(examname).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                denemetwo_baslik.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    String isim=ds.getKey();
                    denemetwo_baslik.add(isim);
                }
                Log.d("TAG", "salan: "+denemetwo_baslik);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.listview_text, denemetwo_baslik);
                listViewtwo.setAdapter(adapter);
                dbRef.removeEventListener(this);}
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return v;
    }
}
