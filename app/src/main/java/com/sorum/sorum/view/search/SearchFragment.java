package com.sorum.sorum.view.search;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sorum.sorum.R;
import com.sorum.sorum.controller.AdapterSearchClass;
import com.sorum.sorum.model.SearchUsers;

import java.util.ArrayList;


public class SearchFragment extends Fragment {
    private String exam;
    private String username;
    private String name;
    DatabaseReference ref;
    ArrayList<SearchUsers> list;
    RecyclerView recyclerView;
    SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        Bundle bundle = getArguments();
        name = bundle.getString("name");
        exam = bundle.getString("exam");
        username = bundle.getString("username");

        ref = FirebaseDatabase.getInstance().getReference().child("users");
        recyclerView = v.findViewById(R.id.result_list);
        searchView = v.findViewById(R.id.searchView);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(ref != null){
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        list = new ArrayList<>();
                        for(DataSnapshot ds: dataSnapshot.getChildren()){
                            list.add(ds.getValue(SearchUsers.class));
                        }
                        AdapterSearchClass adapterSearchClass = new AdapterSearchClass(list);
                        recyclerView.setAdapter(adapterSearchClass);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), databaseError.getMessage() , Toast.LENGTH_SHORT).show();
                }
            });
        }
        if(searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }
    }
    private void search(String string){
        ArrayList<SearchUsers> myList = new ArrayList<>();
        for(SearchUsers object : list){
            if(object.getUsername().toLowerCase().contains(string.toLowerCase())){
                myList.add(object);
            }
        }
        AdapterSearchClass adapterSearchClass = new AdapterSearchClass(myList);
        recyclerView.setAdapter(adapterSearchClass);
    }
}
