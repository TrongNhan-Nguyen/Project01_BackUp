package com.example.project01_backup.dao;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.project01_backup.model.Content;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class DAO_Content {
    Context context;
    Fragment fragment;
    DatabaseReference dbContent;

    public DAO_Content(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        this.dbContent = FirebaseDatabase.getInstance().getReference("contents");
    }

    public void insert (final String idPost, final Content content, Uri uriImageView){
        final String id = dbContent.push().getKey();
        content.setId(id);
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
                        dbContent.child(idPost).child(id).setValue(content);
                        toast("Thêm thành công");
                    }
                });
            }
        });

    }
    private void toast(String s){
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }


}


