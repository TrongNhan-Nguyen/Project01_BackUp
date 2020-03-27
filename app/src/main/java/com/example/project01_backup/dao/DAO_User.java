package com.example.project01_backup.dao;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.example.project01_backup.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    public void insert(User user){
        dbUser.child(user.getId()).setValue(user);
    }
}
