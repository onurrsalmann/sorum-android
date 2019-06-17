package com.sorum.sorum;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class exam {
    private String examcatname;
    private ArrayList<String> test = new ArrayList<>();


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public exam(){

    }

    public exam(String examcatname){
        this.examcatname = examcatname;
    }
    public void examcat(){


    }
    public ArrayList<String> getTest(){
        return this.test;
    }


}
