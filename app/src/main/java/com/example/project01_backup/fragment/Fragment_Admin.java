package com.example.project01_backup.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project01_backup.R;
import com.example.project01_backup.activities.LoginActivity;
import com.example.project01_backup.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class Fragment_Admin extends Fragment {
    private View view;
    private CardView cvCensorship, cvUser, cvFeedback, cvMain, cvLogout, cvExit;
    private CircleImageView imgAvatar;
    private TextView tvName;
    private FirebaseUser currentUser;
    private String name, email, urlAvatar, pass;

    public Fragment_Admin() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_admin, container, false);
        initView();
        replaceFragment(new Fragment_UserList());
        return view;
    }
    private void initView() {
        Intent intent = getActivity().getIntent();
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        cvCensorship = (CardView) view.findViewById(R.id.fAdmin_cvCensor);
        cvUser = (CardView) view.findViewById(R.id.fAdmin_cvUser);
        cvFeedback = (CardView) view.findViewById(R.id.fAdmin_cvFeedback);
        cvMain = (CardView) view.findViewById(R.id.fAdmin_cvMain);
        cvLogout = (CardView) view.findViewById(R.id.fAdmin_cvLogout);
        cvExit = (CardView) view.findViewById(R.id.fAdmin_cvExit);

        tvName = (TextView) view.findViewById(R.id.fAdmin_tvName);
        imgAvatar = (CircleImageView) view.findViewById(R.id.fAdmin_imgAvatar);
        if (intent != null){
            name = intent.getStringExtra("name");
            urlAvatar = intent.getStringExtra("avatar");
            email = intent.getStringExtra("email");
            pass = intent.getStringExtra("pass");
            tvName.setText(name);
            try {

            }catch (Exception e){
                Picasso.get().load(Uri.parse(urlAvatar)).into(imgAvatar);
            }
        }


        cvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        cvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
//                finish();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reAuth();
//                getActivity().finish();
//                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        cvCensorship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new Fragment_Censorship());
            }
        });

        cvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new Fragment_UserList());
            }
        });

        cvFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("Phản ánh người dùng");
            }
        });
    }
    private void replaceFragment(Fragment fragment){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.admin_FrameLayout, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void reAuth(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email,"123456")
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        getActivity().finish();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    }
                });
    }

    private void toast(String s){
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
}
