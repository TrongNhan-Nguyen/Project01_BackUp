package com.example.project01_backup.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.project01_backup.R;
import com.example.project01_backup.adapter.Adapter_LV_User;
import com.example.project01_backup.dao.DAO_User;
import com.example.project01_backup.model.FirebaseCallback;
import com.example.project01_backup.model.Places;
import com.example.project01_backup.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


public class Fragment_UserList extends Fragment {
    private View view;
    private ListView lvUser;
    private Adapter_LV_User adapterUser;
    private DAO_User dao_user;
    private List<User> listUser;
    private int index = -1;
    private FirebaseAuth mAuth;
    public Fragment_UserList() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_list, container, false);
        initView();
        return view;
    }

    private void initView() {
        dao_user = new DAO_User(getActivity(),this);
        mAuth = FirebaseAuth.getInstance();
        lvUser = (ListView) view.findViewById(R.id.fUserList_lvUser);
        dao_user.getData(new FirebaseCallback(){
            @Override
            public void userList(List<User> userList) {
                listUser = new ArrayList<>(userList);
                adapterUser = new Adapter_LV_User(getActivity(), userList);
                lvUser.setAdapter(adapterUser);
            }
        });
        lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                reAuth();
            }
        });
    }

    private void reAuth(){
        User user = adapterUser.getUserList().get(index);
        toast(user.getName());
        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser currentUser = mAuth.getCurrentUser();
//                            currentUser.delete();

                        } else {

                        }

                    }
                });
    }
    private void toast (String s){
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_places, menu);
        MenuItem search = menu.findItem(R.id.menu_search_places);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setQueryHint("Nhập tên user cần tìm kiếm");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterUser.getFilter().filter(newText);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}
