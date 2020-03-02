package com.sorum.sorum.view.home;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sorum.sorum.MainActivity;
import com.sorum.sorum.R;
import com.sorum.sorum.model.Post;

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
    private LinearLayout trueAnswer;
    private LinearLayout falseAnswer;
    private RelativeLayout showQuest;
    private RadioGroup optionBlock;
    private RadioButton optionA;
    RadioButton optionB;
    RadioButton optionC;
    RadioButton optionD;
    RadioButton optionE;
    String lesson;
    String level;
    String trueOption;
    Button questionNext;
    private ArrayList<Post> mPost;
    public int a = 0;
    int i =0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);
        val = getArguments().getInt("someInt", 0);
        questions_names= getArguments().getStringArrayList("questions_names");
        optionBlock = view.findViewById(R.id.optionBlock);
        trueAnswer = view.findViewById(R.id.trueAnswer);
        falseAnswer = view.findViewById(R.id.falseAnswer);
        showQuest = view.findViewById(R.id.showQuest);
        exam = getArguments().getString("exam");
        question_textview = view.findViewById(R.id.question_textview);

        lesson = questions_names.get(val);
        level = "Basit";
        questionNext = view.findViewById(R.id.questionNext);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                Handler handler = new Handler();
                Runnable runn = new Runnable() {
                    @Override
                    public void run() {
                        i=0;
                    }
                };
                if(i==1){
                    handler.postDelayed(runn, 400);
                }else if(i==2){
                    gizle();
                    i = 0;
                }
            }
        });
        optionA = view.findViewById(R.id.optionA);
        optionB = view.findViewById(R.id.optionB);
        optionC = view.findViewById(R.id.optionC);
        optionD = view.findViewById(R.id.optionD);
        optionE = view.findViewById(R.id.optionE);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        mPostReference = db.child("questions").child(exam).child(lesson);
        mPost = new ArrayList<>();




        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String question_desp = "Soru";
                String answer_desp = "Cevap";

                for (DataSnapshot ds:dataSnapshot.getChildren()) {
                    for(DataSnapshot asa:ds.child(level).getChildren()) {
                        Log.d("salaman", "+"+asa.child("desp").getValue()+asa.child(question_desp).getValue());
                        mPost.add(new Post(
                                asa.getKey(),
                                asa.child("desp").getValue().toString(),
                                asa.child(question_desp).getValue().toString(),
                                asa.child(answer_desp).child("A").getValue().toString(),
                                asa.child(answer_desp).child("B").getValue().toString(),
                                asa.child(answer_desp).child("C").getValue().toString(),
                                asa.child(answer_desp).child("D").getValue().toString(),
                                asa.child(answer_desp).child("E").getValue().toString(),
                                asa.child("DogruCevap").getValue().toString()
                        )); } }
                mPostReference.removeEventListener(this);
                if (mPost.size()>0 && a< mPost.size()){ showExam(a); }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mPostReference.addValueEventListener(postListener);
        return view;
    }
    public void showExam(int c){
        if (mPost.size()>0 && c< mPost.size()){
            question_textview.setText(mPost.get(a).getSoru());
            optionA.setText("A) "+mPost.get(a).getCevapA());
            optionB.setText("B) "+mPost.get(a).getCevapB());
            optionC.setText("C) "+mPost.get(a).getCevapC());
            optionD.setText("D) "+mPost.get(a).getCevapD());
            optionE.setText("E) "+mPost.get(a).getCevapE());
            trueOption = ""+mPost.get(a).getDogruCevap();
            questionNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("salman","tıklandı");
                    showQuest.setVisibility(View.INVISIBLE);
                    String checkOption;
                    switch (optionBlock.indexOfChild(view.findViewById(optionBlock.getCheckedRadioButtonId()))) {
                        case 1 :
                            checkOption = "A";
                            break;
                        case 2 :
                            checkOption = "B";
                            break;
                        case 3 :
                            checkOption = "C";
                            break;
                        case 4 :
                            checkOption = "D";
                            break;
                        case 5 :
                            checkOption = "E";
                            break;
                        default :
                            checkOption = "PAS";
                            break;
                    }
                    if(trueOption.equals(checkOption)){
                        trueAnswer.setVisibility(View.VISIBLE);
                        showQuest.setVisibility(View.INVISIBLE);
                        new CountDownTimer(500, 1000) {
                            public void onTick(long millisUntilFinished) {
                            }
                            public void onFinish() {
                                trueAnswer.setVisibility(View.INVISIBLE);
                                showQuest.setVisibility(View.VISIBLE);
                            }
                        }.start();
                        a += 1;
                        showExam(a);
                        optionA.setChecked(false);
                        optionB.setChecked(false);
                        optionC.setChecked(false);
                        optionD.setChecked(false);
                        optionE.setChecked(false);
                    }else {
                        falseAnswer.setVisibility(View.VISIBLE);
                        showQuest.setVisibility(View.INVISIBLE);
                        new CountDownTimer(500, 1000) {
                            public void onTick(long millisUntilFinished) {
                            }
                            public void onFinish() {
                                falseAnswer.setVisibility(View.INVISIBLE);
                                showQuest.setVisibility(View.VISIBLE);
                            }
                        }.start();
                        a += 1;
                        showExam(a);
                        optionA.setChecked(false);
                        optionB.setChecked(false);
                        optionC.setChecked(false);
                        optionD.setChecked(false);
                        optionE.setChecked(false);
                    }
                }
            });
        }else{
            question_textview.setText("Soru Bitti");
        }
    }
    public void gizle(){
        ((MainActivity)getActivity()).ustBarHide();
    }
}