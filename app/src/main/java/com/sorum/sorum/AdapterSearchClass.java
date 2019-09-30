package com.sorum.sorum;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterSearchClass extends RecyclerView.Adapter<AdapterSearchClass.MyViewHolder> {
    ArrayList<SearchUsers> list;
    public AdapterSearchClass(ArrayList<SearchUsers> list){
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.searchUserName.setText(list.get(position).getUsername());
        holder.searcExam.setText(list.get(position).getExam());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView searchUserName, searcExam;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            searchUserName = itemView.findViewById(R.id.searchUsername);
            searcExam = itemView.findViewById(R.id.searchExam);

        }
    }


}
