package com.sorum.sorum;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
public class CustomAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    ArrayList<exam> tarifList;
    public CustomAdapter(Activity activity, ArrayList<exam> tarifList){
        layoutInflater= (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.tarifList=tarifList;}
    @Override
    public int getCount() {
        return tarifList.size();
    }
    @Override
    public Object getItem(int position) {
        return tarifList.get(getCount()-position-1);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        exam tarif = (exam) getItem(position);
        View satir = layoutInflater.inflate(R.layout.exams_satir, null);
        TextView isim = (TextView) satir.findViewById(R.id.textViewIsim);
        isim.setText(tarif.getIsim());
        return satir;
    }}