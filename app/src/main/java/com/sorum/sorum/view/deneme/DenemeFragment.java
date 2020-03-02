package com.sorum.sorum.view.deneme;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sorum.sorum.controller.ExamRecyclerViewAdapter;
import com.sorum.sorum.R;
import com.sorum.sorum.model.SQliteHelper;

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
