package com.sorum.sorum;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class IstatisticFragmentTwo extends Fragment {
    private String selectone;
    private String select;
    private TextView selectLabel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_istatictic_two, container, false);
        Bundle bundle = getArguments();
        assert bundle != null;
        select = bundle.getString("select");
        selectone = bundle.getString("selectone");
        selectLabel = v.findViewById(R.id.selectTwoLabel);
        selectLabel.setText(selectone);

        return v;
    }

}
