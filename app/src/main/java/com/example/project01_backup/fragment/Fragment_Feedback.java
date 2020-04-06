package com.example.project01_backup.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.project01_backup.R;
import com.example.project01_backup.adapter.Adapter_LV_Feedback;
import com.example.project01_backup.adapter.Adapter_LV_PostUser;
import com.example.project01_backup.dao.DAO_Feedback;
import com.example.project01_backup.model.Feedback;
import com.example.project01_backup.model.FirebaseCallback;

import java.util.List;

public class Fragment_Feedback extends Fragment {
    private View view;
    private ListView listView;
    private Adapter_LV_Feedback adapter;
    private DAO_Feedback dao_feedback;


    public Fragment_Feedback() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_feedback, container, false);
        dao_feedback = new DAO_Feedback(getActivity(), this);
        listView = (ListView) view.findViewById(R.id.fFeedback_lvFeedback);
        dao_feedback.getData(new FirebaseCallback(){
            @Override
            public void feedbackList(List<Feedback> feedbackList) {
                adapter = new Adapter_LV_Feedback(getActivity(),feedbackList);
                listView.setAdapter(adapter);
            }
        });
        return view;
    }
}
