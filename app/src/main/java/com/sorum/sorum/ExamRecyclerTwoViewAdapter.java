package com.sorum.sorum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

public class ExamRecyclerTwoViewAdapter extends RecyclerView.Adapter<ExamRecyclerTwoViewAdapter.ViewHolder> {
    ArrayList<String> list;
    String exam;
    String examname;

    public ExamRecyclerTwoViewAdapter(ArrayList<String> list, String exam, String examname) {
        this.list = list;
        this.exam = exam;
        this.examname = examname;
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
                CharSequence examdetay =list.get(position);//Yazı dizisi oluşturup kullanıcının yazdığı yazıyı buraya attık.
                Intent intent=new Intent((MainActivity)holder.context,DenemeStartActivity.class);///İntent ouşturup 2. activity'e gideceğini belirledik.
                intent.putExtra("examdetay",examdetay);
                intent.putExtra("exam", exam);
                intent.putExtra("examname", examname);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                (holder.context).startActivity(intent);
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
        Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            examListitem = itemView.findViewById(R.id.examListitem);
            examLayout = itemView.findViewById(R.id.examLayout);
            context = itemView.getContext();
        }
    }
}
