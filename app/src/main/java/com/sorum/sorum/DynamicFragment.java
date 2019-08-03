package com.sorum.sorum;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class DynamicFragment extends Fragment {
    View view;
    ArrayList<String> questions_names;
    static String exam;
    private DatabaseReference mPostReference;

    public static DynamicFragment newInstance(int val, ArrayList<String> questions_names, String exam) {
        DynamicFragment fragment = new DynamicFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", val);
        args.putStringArrayList("questions_names", questions_names);
        args.putString("exam", exam);
        fragment.setArguments(args);
        return fragment;
    }

    int val;
    TextView question_textview;
    RadioButton optionA;
    RadioButton optionB;
    RadioButton optionC;
    RadioButton optionD;
    RadioButton optionE;
    String lesson;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);
        val = getArguments().getInt("someInt", 0);
        questions_names= getArguments().getStringArrayList("questions_names");
        exam = getArguments().getString("exam");
        question_textview = view.findViewById(R.id.question_textview);
        lesson = questions_names.get(val);
        optionA = view.findViewById(R.id.optionA);
        optionB = view.findViewById(R.id.optionB);
        optionC = view.findViewById(R.id.optionC);
        optionD = view.findViewById(R.id.optionD);
        optionE = view.findViewById(R.id.optionE);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        mPostReference = db.child("questions").child(exam).child(lesson);


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String question_desp = "Soru";
                String answer_desp = "Cevap";
                String answerA = "A";
                String answerB = "B";
                String answerC = "C";
                String answerD = "D";
                String answerE = "E";
                for (DataSnapshot ds:dataSnapshot.getChildren()) {
                    question_textview.setText(ds.child(question_desp).getValue().toString());
                    optionA.setText("A) " + ds.child(answer_desp).child(answerA).getValue().toString());
                    optionB.setText("B) " + ds.child(answer_desp).child(answerB).getValue().toString());
                    optionC.setText("C) " + ds.child(answer_desp).child(answerC).getValue().toString());
                    optionD.setText("D) " + ds.child(answer_desp).child(answerD).getValue().toString());
                    optionE.setText("E) " + ds.child(answer_desp).child(answerE).getValue().toString());

                }
                mPostReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mPostReference.addValueEventListener(postListener);
        return view;
    }
}