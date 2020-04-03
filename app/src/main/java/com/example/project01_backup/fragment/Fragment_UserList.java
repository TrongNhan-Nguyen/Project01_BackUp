package com.example.project01_backup.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project01_backup.R;


public class Fragment_UserList extends Fragment {
    private View view;
    public Fragment_UserList() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.raw_user, container, false);
        initView();
        return view;
    }

    private void initView() {

    }
}
