package com.example.project01_backup.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project01_backup.R;
import com.example.project01_backup.dao.DAO_Feedback;
import com.example.project01_backup.dao.DAO_User;
import com.example.project01_backup.model.Feedback;
import com.example.project01_backup.model.FirebaseCallback;
import com.example.project01_backup.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_UserInfo extends Fragment {
    private View view;
    private Button btnEdit, btnFeedBack;
    private TextView tvName, tvEmail, tvID, tvPhone, tvBOD, tvAddress;
    private ImageView imgAvatar;
    private CircleImageView imgAvatarUser;
    private DAO_User dao_user;
    private DAO_Feedback dao_feedback;
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
        dao_feedback = new DAO_Feedback(getActivity(),this);
        spotDialog = new SpotsDialog(getActivity());
        tvName = (TextView) view.findViewById(R.id.fUserInfo_tvName);
        tvEmail = (TextView) view.findViewById(R.id.fUserInfo_tvEmail);
        tvID = (TextView) view.findViewById(R.id.fUserInfo_tvId);
        tvPhone = (TextView) view.findViewById(R.id.fUserInfo_tvPhone);
        tvBOD = (TextView) view.findViewById(R.id.fUserInfo_tvBOD);
        tvAddress = (TextView) view.findViewById(R.id.fUserInfo_tvAddress);
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
                   AuthCredential credential = EmailAuthProvider
                           .getCredential(update.getEmail(), update.getPassword());
                   currentUser.reauthenticate(credential)
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {

                               }
                           });
               }catch (Exception e){

               }
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEdit();
            }
        });
        btnFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFeedback();
            }
        });
    }

    private void dialogFeedback(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_add_feedback);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText etFeedback = (EditText) dialog.findViewById(R.id.dAddFeedback_etFeedback);
        ImageView imgPost = (ImageView) dialog.findViewById(R.id.dAddFeedback_imgPost);
        final Feedback feedback = new Feedback();
        imgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etFeedback.getText().toString().isEmpty()){
                    toast("Add your feedback");
                }else {
                    feedback.setContentFeedBack(etFeedback.getText().toString());
                    feedback.setStringPubDate(stringPubDate());
                    feedback.setLongPubDate(longPubDate());
                    feedback.setEmailUser(currentUser.getEmail());
                    feedback.setUriAvatarUser(String.valueOf(currentUser.getPhotoUrl()));
                    feedback.setIdUser(currentUser.getUid());
                    dao_feedback.inset(feedback);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private void dialogEdit() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_edit_info);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText etName, etPass, etDOB, etPhone, etAddress;
        TextView tvEmail;
        Button btnUpdate, btnClear, btnCancel;
        imgAvatarUser = (CircleImageView) dialog.findViewById(R.id.dEditInfo_imgAvatar);
        etName = (EditText) dialog.findViewById(R.id.dEditInfo_etName);
        tvEmail = (TextView) dialog.findViewById(R.id.dEditInfo_tvEmail);
        etPass = (EditText) dialog.findViewById(R.id.dEditInfo_etPass);
        etDOB = (EditText) dialog.findViewById(R.id.dEditInfo_etDOB);
        etPhone = (EditText) dialog.findViewById(R.id.dEditInfo_etPhone);
        etAddress = (EditText) dialog.findViewById(R.id.dEditInfo_etAddress);
        btnUpdate = (Button) dialog.findViewById(R.id.dEditInfo_btnUpdate);
        btnClear = (Button) dialog.findViewById(R.id.dEditInfo_btnClear);
        btnCancel = (Button) dialog.findViewById(R.id.dEditInfo_btnCancel);

        etDOB.setKeyListener(null);
        etDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker(etDOB);
            }
        });

        try {
            etName.setText(update.getName());
            tvEmail.setText(update.getEmail());
            etPass.setText(update.getPassword());
            etDOB.setText(update.getBirthDay());
            etPhone.setText(update.getPhoneNumber());
            etAddress.setText(update.getAddress());
            Picasso.get().load(Uri.parse(update.getUriAvatar())).into(imgAvatarUser);
        }catch (Exception e){

        }

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
                spotDialog.show();
                String name = etName.getText().toString();
                String bOD = etDOB.getText().toString();
                String pass = etPass.getText().toString();
                String phone = etPhone.getText().toString();
                String address = etAddress.getText().toString();
                update.setName(name);
                update.setBirthDay(bOD);
                update.setAddress(address);
                update.setPhoneNumber(phone);
                update.setPassword(pass);
                updateInfo(name, pass);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void updateInfo( final String name, final String pass){
        String uID = currentUser.getUid();
        storageReference = FirebaseStorage.getInstance()
                .getReference().child("AvatarUser/" + uID);
        imgAvatarUser.setDrawingCacheEnabled(true);
        imgAvatarUser.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imgAvatarUser.getDrawable()).getBitmap();
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
                        update.setUriAvatar(String.valueOf(uri));
                        editInfo(name,pass,uri);

                    }
                });
            }
        });
    }

    private void editInfo(final String name , final String pass, final Uri uriImage ){
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(uriImage)
                .build();
        currentUser.updateProfile(profileUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        currentUser.updatePassword(pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    toast("Your account has been successfully updated!");
                                    dao_user.insert(update);
                                    spotDialog.dismiss();
                                }else {
                                    toast("Error");
                                    spotDialog.dismiss();
                                }

                            }
                        });
                    }
                });

    }
    private void datePicker(final EditText et){
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    calendar.set(year,month,dayOfMonth);
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    et.setText(format.format(calendar.getTime()));
            }
        },year,month,day);

        dialog.show();
    }
    private void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
    private String stringPubDate(){
        String date;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        date = format.format(calendar.getTime());
        return date;
    }
    private long longPubDate(){
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 5 && data != null) {
            imgAvatarUser.setImageURI(data.getData());
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
