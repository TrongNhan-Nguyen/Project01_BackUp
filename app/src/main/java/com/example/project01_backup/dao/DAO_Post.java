package com.example.project01_backup.dao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.project01_backup.model.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class DAO_Post {
    private Context context;
    private Fragment fragment;
    private DatabaseReference dbPost;
    private String node;
    private StorageReference storagePost;

    public DAO_Post(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        this.dbPost = FirebaseDatabase.getInstance().getReference("restaurant").child("Ca Mau");
    }

    public void insert(final Post post, ImageView imageView){
        Calendar calendar = Calendar.getInstance();

        final String id = dbPost.push().getKey();
        final StorageReference storageReference = FirebaseStorage.getInstance()
                .getReference("Post/" + id);
        post.setId(id);
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bAOS);
        byte[] data = bAOS.toByteArray();
        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        post.setUrlImage(String.valueOf(uri));
                        dbPost.child(id).setValue(post);
                        toast("Thêm thành công");
                    }
                });
            }
        });
    }

    private void toast(String s){
        Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
    }
}
