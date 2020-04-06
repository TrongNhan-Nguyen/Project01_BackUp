package com.example.project01_backup.dao;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.project01_backup.model.Comment;
import com.example.project01_backup.model.FirebaseCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DAO_Comment {
    Context context;
    Fragment fragment;
    DatabaseReference dbComment;
    public DAO_Comment(Context context) {
        this.context = context;
        this.dbComment = FirebaseDatabase.getInstance().getReference("comments");
    }

    public DAO_Comment(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        this.dbComment = FirebaseDatabase.getInstance().getReference("comments");
    }

    public void insert(String idPost,Comment comment){
        String idComment = dbComment.push().getKey();
        comment.setIdComment(idComment);
        comment.setIdPost(idPost);
        dbComment.child(idPost).child(idComment).setValue(comment);
    }
    public void deleteByIdPost(String idPost){
        if (idPost != null){
            dbComment.child(idPost).removeValue();
        }
    }

    public void update(Comment comment){
        dbComment.child(comment.getIdPost())
                .child(comment.getIdComment())
                .setValue(comment);
    }
    public void deleteByIdComment(Comment comment){
        dbComment.child(comment.getIdPost()).child(comment.getIdComment()).removeValue();
    }


    public void getData(String idPost, final FirebaseCallback firebaseCallback){
        final List<Comment> commentList = new ArrayList<>();
        dbComment.child(idPost).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Comment comment = ds.getValue(Comment.class);
                    commentList.add(comment);
                }
                Collections.sort(commentList, new Comparator<Comment>() {
                    @Override
                    public int compare(Comment o1, Comment o2) {
                        if (o1.getLongPubDate() > o2.getLongPubDate()){
                            return -1;
                        }else if (o1.getLongPubDate() < o2.getLongPubDate()){
                            return 1;
                        }else {
                            return 0;
                        }
                    }
                });
                firebaseCallback.commentList(commentList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
