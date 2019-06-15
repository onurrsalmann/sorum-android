package com.sorum.sorum;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class DenemeFragment extends Fragment {
    private ArrayAdapter adapter;
    private String exam;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_deneme, container, false);

        ListView list = (ListView) v.findViewById(R.id.test_list);
        final ArrayList<String> test = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        String uid = currentUser.getUid();



        final DocumentReference docRef = db.collection("users").document(uid);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    exam = snapshot.getString("exam");
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });


        CollectionReference ref = db.collection("deneme");
        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot document : task.getResult()) {
                    if(document.get(exam) != null){
                        List<String> firelist = (List<String>) document.get(exam);
                        for (String item : firelist) {
                            test.add(item);
                        }
                        adapter = new ArrayAdapter(getActivity(), R.layout.test_list_item, test);
                        list.setAdapter(adapter);

                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                               /* CharSequence gönderilenYazi=test.get(i);//Yazı dizisi oluşturup kullanıcının yazdığı yazıyı buraya attık.
                                Intent ıntent=new Intent(getActivity(),ikinciEkran.class);///İntent ouşturup 2. activity'e gideceğini belirledik.
                                ıntent.putExtra("examHead",gönderilenYazi);//Gönderilecek veriyi ve bir anahtar belirledik.
                                startActivity(ıntent);;
                                Bundle gelenVeri=getIntent().getExtras();
                                tv.setText(gelenVeri.getCharSequence("anahtar").toString());*/
                            }
                        });
                    }
                }
            }
        });

        return v;
    }
}
