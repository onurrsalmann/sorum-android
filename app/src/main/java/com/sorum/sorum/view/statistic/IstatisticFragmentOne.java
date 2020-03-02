package com.sorum.sorum.view.statistic;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sorum.sorum.view.statistic.IstatisticFragmentTwo;
import com.sorum.sorum.R;
import com.sorum.sorum.model.SQliteHelper;


public class IstatisticFragmentOne extends Fragment {
    private ListView sorularListview;
    private ListView denemelerListview;
    private ArrayAdapter<String> dbSorular;
    private ArrayAdapter<String> dbDenemeler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_istatictic_one, container, false);
        Context context =  getContext();
        SQliteHelper sqlitedb = new SQliteHelper(context);

        sqlitedb.onUpgrade(sqlitedb.getWritableDatabase(),1,2);

        dbSorular = sqlitedb.getLesson(getActivity(), R.layout.istatistic_question_select);
        dbDenemeler = sqlitedb.istatisticGetExam(getActivity(), R.layout.istatistic_question_select);

        sorularListview = (ListView)v.findViewById(R.id.istatistikSorularListview);
        sorularListview.setAdapter(dbSorular);
        sorularListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("select","lesson");
                bundle.putString("selectone",dbSorular.getItem(position));

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                IstatisticFragmentTwo istatisticFragmentTwo = new IstatisticFragmentTwo();
                istatisticFragmentTwo.setArguments(bundle);
                fragmentTransaction.replace(R.id.istatisticFragment, istatisticFragmentTwo);
                fragmentTransaction.addToBackStack(istatisticFragmentTwo.getClass().getName());
                fragmentTransaction.commit();
                Log.d("sorular", dbSorular.getItem(position));
            }
        });
        denemelerListview= (ListView)v.findViewById(R.id.istatistikDenemelerListview);
        denemelerListview.setAdapter(dbDenemeler);
        denemelerListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("select","exam");
                bundle.putString("selectone",dbDenemeler.getItem(position));
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                IstatisticFragmentTwo istatisticFragmentTwo = new IstatisticFragmentTwo();
                istatisticFragmentTwo.setArguments(bundle);
                fragmentTransaction.replace(R.id.istatisticFragment, istatisticFragmentTwo);
                fragmentTransaction.commit();
                Log.d("sorular", dbSorular.getItem(position));
            }
        });
        return v;
    }

}
