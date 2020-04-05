package com.example.project01_backup.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_UserInfo extends Fragment {
    private View view;
    private Button btnDelete, btnEdit, btnFeedBack;
    private TextView tvName, tvEmail, tvID, tvPhone, tvBOD, tvAddress;
    private ImageView imgAvatar;
    private CircleImageView imgAvatarUser;
    private DAO_User dao_user;
    private String idUser;
    private User update;
    private FirebaseUser currentUser;
    private Dialog spotDialog;
    private StorageReference storageReference;


    public Fragment_UserInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_infor, container, false);
        initView();
        return view;
    }

    private void initView() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        dao_user = new DAO_User(getActivity(), this);
        spotDialog = new SpotsDialog(getActivity());
        tvName = (TextView) view.findViewById(R.id.fUserInfo_tvName);
        tvEmail = (TextView) view.findViewById(R.id.fUserInfo_tvEmail);
        tvID = (TextView) view.findViewById(R.id.fUserInfo_tvId);
        tvPhone = (TextView) view.findViewById(R.id.fUserInfo_tvPhone);
        tvBOD = (TextView) view.findViewById(R.id.fUserInfo_tvBOD);
        tvAddress = (TextView) view.findViewById(R.id.fUserInfo_tvAddress);
        btnDelete = (Button) view.findViewById(R.id.fUserInfo_btnDelete);
        btnEdit = (Button) view.findViewById(R.id.fUserInfo_btnEdit);
        btnFeedBack = (Button) view.findViewById(R.id.fUserInfo_btnFeedBack);
        imgAvatar = (ImageView) view.findViewById(R.id.fUserInfo_imgAvatar);

        dao_user.getUserInfo(currentUser.getUid(), new FirebaseCallback() {
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
                dialog.setMessage("Tài khoản sau khi xóa sẽ mất toàn quyền truy cập thông tin cá nhân" +
                        " cũng như các tính năng khác!!");
                dialog.setNegativeButton("XÓA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                dialog.setPositiveButton("HỦY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });
    }

    private void dialogEdit() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_edit_info);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText etName, etEmail, etPass, etDOB, etPhone, etAddress;
        Button btnUpdate, btnClear, btnCancel;
        imgAvatarUser = (CircleImageView) dialog.findViewById(R.id.dEditInfo_imgAvatar);
        etName = (EditText) dialog.findViewById(R.id.dEditInfo_etName);
        etEmail = (EditText) dialog.findViewById(R.id.dEditInfo_etEmail);
        etPass = (EditText) dialog.findViewById(R.id.dEditInfo_etPass);
        etDOB = (EditText) dialog.findViewById(R.id.dEditInfo_etDOB);
        etPhone = (EditText) dialog.findViewById(R.id.dEditInfo_etPhone);
        etAddress = (EditText) dialog.findViewById(R.id.dEditInfo_etAddress);
        btnUpdate = (Button) dialog.findViewById(R.id.dEditInfo_btnUpdate);
        btnClear = (Button) dialog.findViewById(R.id.dEditInfo_btnClear);
        btnCancel = (Button) dialog.findViewById(R.id.dEditInfo_btnCancel);

        etName.setText(update.getName());
        etEmail.setText(update.getEmail());
        etPass.setText(update.getPassword());
        etDOB.setText(update.getBirthDay());
        etPhone.setText(update.getPhoneNumber());
        etAddress.setText(update.getAddress());
        Picasso.get().load(Uri.parse(update.getUriAvatar())).into(imgAvatarUser);

        imgAvatarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select picture"), 5);
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etName.setText("");
                etEmail.setText("");
                etPass.setText("");
                etDOB.setText("");
                etPhone.setText("");
                etAddress.setText("");
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String bOD = etDOB.getText().toString();
                String email = etEmail.getText().toString();
                String pass = etPass.getText().toString();
                String phone = etPhone.getText().toString();
                String address = etAddress.getText().toString();

                update.setName(name);
                update.setBirthDay(bOD);
                update.setAddress(address);
                update.setPhoneNumber(phone);
                update.setEmail(email);
                update.setPassword(pass);

                dialog.dismiss();
            }
        });

        dialog.show();
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
