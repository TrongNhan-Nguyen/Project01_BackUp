package com.example.project01_backup.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.project01_backup.R;
import com.example.project01_backup.dao.DAO_User;
import com.example.project01_backup.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity {
    private EditText etDisplayName, etEmail, etPassword;
    private ImageView imgChangeAvatar;
    private CircleImageView imgAvatar;
    private Button btnResister;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private DAO_User dao_user;
    private AlertDialog dialog;
    private User insert;
    private static final int PICK_IMAGE_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("REGISTRATION");
        initView();
    }

    private void initView() {
        dao_user = new DAO_User(this);
        insert = new User();
        mAuth = FirebaseAuth.getInstance();
        etDisplayName = (EditText) findViewById(R.id.register_etDisplayName);
        etEmail = (EditText) findViewById(R.id.register_etEmail);
        etPassword = (EditText) findViewById(R.id.register_etPassword);
        imgAvatar = (CircleImageView) findViewById(R.id.register_imgAvatar);
        imgChangeAvatar = (ImageView) findViewById(R.id.register_imgChangeAvatar);
        btnResister = (Button) findViewById(R.id.register_btnRegister);

        dialog = new SpotsDialog(this);

        imgChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select picture"),PICK_IMAGE_CODE);
            }
        });

        btnResister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String displayName = etDisplayName.getText().toString();
                String email = etEmail.getText().toString();
                String pass = etPassword.getText().toString();
                if (displayName.isEmpty() || email.isEmpty() || pass.isEmpty()){
                    toast("Vui lòng diền đầy đủ thông tin");
                }else {
                    dialog.show();
                    createUser(displayName,email,pass);
                }


            }
        });

    }
    private void createUser(final String name, String mail, String pass){
        insert.setPassword(pass);
        mAuth.createUserWithEmailAndPassword(mail,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            String uID = mAuth.getCurrentUser().getUid();
                            storageReference = FirebaseStorage.getInstance()
                                    .getReference().child("AvatarUser/" + uID);
                            imgAvatar.setDrawingCacheEnabled(true);
                            imgAvatar.buildDrawingCache();
                            Bitmap bitmap = ((BitmapDrawable) imgAvatar.getDrawable()).getBitmap();
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
                                            updateUser(name,uri,mAuth.getCurrentUser());
                                        }
                                    });
                                }
                            });


                        }else {
                            toast("Lỗi");
                            dialog.dismiss();
                        }
                    }
                });

    }

    private void updateUser(final String name, final Uri uriImage, final FirebaseUser currentUser ){
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(uriImage)
                .build();

        currentUser.updateProfile(profileUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            insert.setName(name);
                            insert.setUriAvatar(String.valueOf(uriImage));
                            insert.setEmail(currentUser.getEmail());
                            insert.setId(currentUser.getUid());
                            insert.setType("User");
                            insert.setStringCreated(stringCreated());
                            insert.setLongCreated(longCreated());
                            dao_user.insert(insert);
                            dialog.dismiss();
                            toast("Register complete");
                            finish();
                            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                        }
                    }
                });

    }

    private void log(String s){
        Log.d("log",s);
    }
    private void toast(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
    private String stringCreated(){
        String created;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
       created = format.format(calendar.getTime());
        return created;
    }

    private long longCreated(){
        long created;
        Calendar calendar = Calendar.getInstance();
        created = calendar.getTimeInMillis();
        return  created;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_CODE && data != null){
            imgAvatar.setImageURI(data.getData());
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
