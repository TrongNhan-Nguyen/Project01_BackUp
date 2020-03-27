package com.example.project01_backup.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.project01_backup.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_BeautifulPlaces extends Fragment {
    private View view;

    public Fragment_BeautifulPlaces() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_beautiful_places, container, false);
        initView();
        return view;
    }

    private void initView() {

    }
}
