package com.example.project01_backup.dao;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.project01_backup.model.FirebaseCallback;
import com.example.project01_backup.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DAO_User {
    Context context;
    Fragment fragment;
    DatabaseReference dbUser;

    public DAO_User(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        this.dbUser = FirebaseDatabase.getInstance().getReference("users");
    }

    public DAO_User(Context context) {
        this.context = context;
        this.dbUser = FirebaseDatabase.getInstance().getReference("users");
    }

    public void insert(User user) {
        dbUser.child(user.getId()).setValue(user);
    }

    public void getUserInfo(String idUser, final FirebaseCallback firebaseCallback){

        dbUser.child(idUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   User user = dataSnapshot.getValue(User.class);
                   firebaseCallback.getUser(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void delete(String idUser){
        dbUser.child(idUser).removeValue();
    }

    public void getData(final FirebaseCallback firebaseCallback) {
        final List<User> userList = new ArrayList<>();
        dbUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    if (user.getType().equalsIgnoreCase("User")){
                        userList.add(user);
                    }
                }
                firebaseCallback.userList(userList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void getAllData(final FirebaseCallback firebaseCallback) {
        final List<User> userList = new ArrayList<>();
        dbUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    userList.add(user);
                }
                firebaseCallback.userList(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
