package com.sorum.sorum;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SearchFragment extends Fragment {
    private String exam;
    private String username;
    private String name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        Bundle bundle = getArguments();
        name = bundle.getString("name");
        exam = bundle.getString("exam");
        username = bundle.getString("username");

        Button b = v.findViewById(R.id.addl);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(b.getText().equals("Takip Et")){
                    b.setText("Takip Ediliyor");
                }else {
                    b.setText("Takip Et");
                }
            }
        });




        return v;

    }

}
