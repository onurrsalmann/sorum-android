package com.sorum.sorum.view.deneme;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sorum.sorum.R;
import com.sorum.sorum.controller.ExamRecyclerTwoViewAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class DenemeTwoFragment extends Fragment {
    private ArrayList<String> denemetwo_baslik;
    private String examname;
    private String exam;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_deneme_two, container, false);
        TextView errorNet = v.findViewById(R.id.errorNetExam);
        Bundle bundle = getArguments();
        assert bundle != null;
        examname = bundle.getString("examname");
        exam = bundle.getString("exam");
        denemetwo_baslik =new ArrayList<>();
        TextView deneme_two_baslik = v.findViewById(R.id.deneme_two_baslik);
        deneme_two_baslik.setText(examname);

        if(isNetworkAvailable()){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference dbRef= database.getReference("exams").child(exam);
            dbRef.child(examname).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    denemetwo_baslik.clear();
                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                        String isim=ds.getKey();
                        denemetwo_baslik.add(isim);
                    }
                    initRecyclerView(v);
                    dbRef.removeEventListener(this);}
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{
            errorNet.setVisibility(View.VISIBLE);
        }
        return v;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void initRecyclerView(View v){
        RecyclerView recyclerView = v.findViewById(R.id.recyclerListTwo);
        ExamRecyclerTwoViewAdapter adapter = new ExamRecyclerTwoViewAdapter(denemetwo_baslik,exam,examname);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
