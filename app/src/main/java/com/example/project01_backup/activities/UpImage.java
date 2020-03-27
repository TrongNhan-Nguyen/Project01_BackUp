package com.example.project01_backup.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.project01_backup.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import dmax.dialog.SpotsDialog;

public class UpImage extends AppCompatActivity {
    private ImageView img;
    private Button btn;
    private AlertDialog dialog;
    private StorageReference storageRef;
    private static final int PICK_IMAGE_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_image);

        initView();
    }

    private void initView() {
        storageRef = FirebaseStorage.getInstance().getReference("upload2");
        log(storageRef.getPath());

        img = (ImageView) findViewById(R.id.imageView);
        btn = (Button) findViewById(R.id.btn_TakePhoto);
        dialog = new SpotsDialog(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select picture"),PICK_IMAGE_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_CODE && data!=null){
            dialog.show();
            UploadTask uploadTask = storageRef.putFile(data.getData());
            Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        Toast.makeText(UpImage.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                    return storageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        String url = task.getResult().toString();
                        Log.d("link",url);
                        Picasso.get().load(url).into(img);
                        dialog.dismiss();
                    }
                }
            });

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    private void log(String s){
        Log.d("log",s);
    }
}
