package com.sorum.sorum;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_deneme, container, false);
        Bundle bundle = getArguments();
        Context context =  getContext();
        SQliteHelper sqlitedb = new SQliteHelper(context);
        exam = bundle.getString("exam");
        sqlitedb.onUpgrade(sqlitedb.getWritableDatabase(),1,2);
        deneme_baslik = sqlitedb.getExam();
        initRecyclerView(v);
        return v;
    }
    private void initRecyclerView(View v){
        RecyclerView recyclerView = v.findViewById(R.id.recyclerList);
        ExamRecyclerViewAdapter adapter = new ExamRecyclerViewAdapter(deneme_baslik,exam);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
