package com.example.project01_backup.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project01_backup.R;
import com.example.project01_backup.dao.DAO_User;
import com.example.project01_backup.model.FirebaseCallback;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_UserInfoAdmin extends Fragment {
    private View view;
    private Button btnDelete;
    private TextView tvName, tvEmail, tvID, tvPhone, tvBOD, tvAddress;
    private ImageView imgAvatar;
    private CircleImageView imgAvatarUser;
    private DAO_User dao_user;
    private String idUser;
    private User update;
    private FirebaseUser currentUser;
    private Dialog spotDialog;
    private StorageReference storageReference;


    public Fragment_UserInfoAdmin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_infor_admin, container, false);
        initView();
        return view;
    }

    private void initView() {
        idUser = Fragment_Tab_UserInfoAdmin.user.getId();
        dao_user = new DAO_User(getActivity(), this);
        spotDialog = new SpotsDialog(getActivity());
        tvName = (TextView) view.findViewById(R.id.fUserInfoAdmin_tvName);
        tvEmail = (TextView) view.findViewById(R.id.fUserInfoAdmin_tvEmail);
        tvID = (TextView) view.findViewById(R.id.fUserInfoAdmin_tvId);
        tvPhone = (TextView) view.findViewById(R.id.fUserInfoAdmin_tvPhone);
        tvBOD = (TextView) view.findViewById(R.id.fUserInfoAdmin_tvBOD);
        tvAddress = (TextView) view.findViewById(R.id.fUserInfoAdmin_tvAddress);
        btnDelete = (Button) view.findViewById(R.id.fUserInfoAdmin_btnDelete);
        imgAvatar = (ImageView) view.findViewById(R.id.fUserInfoAdmin_imgAvatar);

        dao_user.getUserInfo(idUser, new FirebaseCallback() {
            @Override
            public void getUser(User user) {
               try {
                   tvName.setText(user.getName());
                   tvEmail.setText(user.getEmail());
                   tvID.setText(user.getId());
                   tvPhone.setText(user.getPhoneNumber());
                   tvBOD.setText(user.getBirthDay());
                   tvAddress.setText(user.getAddress());
                   Picasso.get().load(Uri.parse(user.getUriAvatar())).into(imgAvatar);
                   update = user;
               }catch (Exception e){

               }
            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setMessage("Delete account!");
                dialog.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        spotDialog.show();
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        mAuth.signInWithEmailAndPassword(update.getEmail(), update.getPassword())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                        currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                dao_user.delete(update.getId());
                                                getActivity()
                                                        .getSupportFragmentManager()
                                                        .beginTransaction()
                                                        .replace(R.id.admin_FrameLayout, new Fragment_UserList())
                                                        .addToBackStack(null)
                                                        .commit();
                                                spotDialog.dismiss();
                                                toast("Account was deleted");
                                            }
                                        });
                                    }


                                });
                    }
                });
                dialog.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });
    }







    private void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 5 && data != null) {
            imgAvatarUser.setImageURI(data.getData());
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
