package com.example.project01_backup.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project01_backup.model.FirebaseCallback;
import com.example.project01_backup.model.Places;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DAO_Places {
    private Context context;
    private Fragment fragment;
    private DatabaseReference dbPlaces;

    public DAO_Places(Context context) {
        this.context = context;
        this.dbPlaces = FirebaseDatabase.getInstance().getReference("places");
    }

    public DAO_Places(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        this.dbPlaces = FirebaseDatabase.getInstance().getReference("places");
    }

    public void insert(Places places){
        String id = dbPlaces.push().getKey();
        places.setId(id);
        dbPlaces.child(id).setValue(places, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

            }
        });
    }
    public void getData (final FirebaseCallback firebaseCallback){
        final List<Places> placesList = new ArrayList<>();
        dbPlaces.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                placesList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Places places = ds.getValue(Places.class);
                    placesList.add(places);
                }
                firebaseCallback.placesList(placesList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void log(String s){
        Log.d("log",s);
    }

    private void toast(String s){
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

}
