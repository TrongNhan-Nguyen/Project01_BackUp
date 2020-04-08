package com.example.project01_backup.dao;

import android.content.Context;
import android.media.midi.MidiDevice;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project01_backup.model.Feedback;
import com.example.project01_backup.model.FirebaseCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DAO_Feedback {
    Context context;
    Fragment fragment;
    DatabaseReference dbFeedback;

    public DAO_Feedback(Context context) {
        this.context = context;
        this.dbFeedback = FirebaseDatabase.getInstance().getReference("feedback");
    }

    public DAO_Feedback(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        this.dbFeedback = FirebaseDatabase.getInstance().getReference("feedback");
    }

    public void inset(Feedback feedback){
        String id = dbFeedback.push().getKey();
        feedback.setIdFeedBack(id);
        dbFeedback.child(id).setValue(feedback, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null){
                    toast("Thank you for your feedback");
                }
            }
        });
    }

    public void getData(final FirebaseCallback firebaseCallback){
        final List<Feedback> feedbackList = new ArrayList<>();
        dbFeedback.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                feedbackList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Feedback feedback = ds.getValue(Feedback.class);
                    feedbackList.add(feedback);
                }
                firebaseCallback.feedbackList(feedbackList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void delete(String idFeedBack){
        if (idFeedBack != null){
            dbFeedback.child(idFeedBack).removeValue();
        }
    }

    private void toast(String s){
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
}
