package com.example.project01_backup.dao;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.project01_backup.model.Content;
import com.example.project01_backup.model.FirebaseCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class DAO_Content {
    private Context context;
    private Fragment fragment;
    private DatabaseReference dbAdmin, dbUser;

    public DAO_Content(Context context) {
        this.context = context;
        this.dbAdmin = FirebaseDatabase.getInstance().getReference("admin").child("contents");
        this.dbUser = FirebaseDatabase.getInstance().getReference("contents");
    }

    public DAO_Content(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        this.dbAdmin = FirebaseDatabase.getInstance().getReference("admin").child("contents");
        this.dbUser = FirebaseDatabase.getInstance().getReference("contents");

    }

    public void insertAdmin (final String idPost, final Content content, Uri uriImageView){
        final String id = dbAdmin.push().getKey();
        content.setId(id);
        if (uriImageView != null){
            Uri file = uriImageView;
            final StorageReference storage  = FirebaseStorage.getInstance().getReference("Intent/" + id);
            UploadTask uploadTask = storage.putFile(file);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            content.setUrlImage(String.valueOf(uri));
                            dbAdmin.child(idPost).child(id).setValue(content);
                        }
                    });
                }
            });
        }else {
            dbAdmin.child(idPost).child(id).setValue(content);
        }


    }
    public void insertUser (final String idPost, final Content content){
        final String id = content.getId();
        dbUser.child(idPost).child(id).setValue(content);
    }

    public void deleteAdmin(String postID){
        dbAdmin.child(postID).removeValue();
    }
    public void deleteUser(String postID){
        if (postID != null){
            dbUser.child(postID).removeValue();
        }

    }

    public void getDataAdmin (String idPost, final FirebaseCallback firebaseCallback){
        final List<Content> contentList = new ArrayList<>();
        dbAdmin.child(idPost).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contentList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Content content = ds.getValue(Content.class);
                    contentList.add(content);

                }
                firebaseCallback.contentListAdmin(contentList);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void getDataUser(String postID, final FirebaseCallback firebaseCallback){
        final List<Content> contentList = new ArrayList<>();
        dbUser.child(postID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contentList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Content content = ds.getValue(Content.class);
                    contentList.add(content);
                }
                firebaseCallback.contentListUser(contentList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void toast(String s){
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }


}


