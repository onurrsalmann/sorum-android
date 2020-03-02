package com.sorum.sorum.controller;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.sorum.sorum.view.deneme.DenemeTwoFragment;
import com.sorum.sorum.MainActivity;
import com.sorum.sorum.R;

import java.util.ArrayList;

public class ExamRecyclerViewAdapter extends RecyclerView.Adapter<ExamRecyclerViewAdapter.ViewHolder> {
    ArrayList<String> list;
    String exam;

    public ExamRecyclerViewAdapter(ArrayList<String> list, String exam) {
        this.list = list;
        this.exam = exam;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_listitem,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("salman", "onBindViewHolder: called");

        holder.examListitem.setText(list.get(position));

        holder.examLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Log.d("salman", "tiklandi"+list.get(position));
                Bundle bundle = new Bundle();
                bundle.putString("examname",list.get(position));
                bundle.putString("exam", exam);

                FragmentManager fragmentManager = ((MainActivity)holder.activity).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                DenemeTwoFragment denemeTwoFragment = new DenemeTwoFragment() ;
                denemeTwoFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container, denemeTwoFragment);
                fragmentTransaction.addToBackStack(denemeTwoFragment.getClass().getName());
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView examListitem;
        RelativeLayout examLayout;
        Context activity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            examListitem = itemView.findViewById(R.id.examListitem);
            examLayout = itemView.findViewById(R.id.examLayout);
            activity = itemView.getContext();
        }
    }
}
