package com.example.project01_backup.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.project01_backup.R;
import com.example.project01_backup.activities.MainActivity;
import com.example.project01_backup.adapter.Adapter_LV_PostAdmin;
import com.example.project01_backup.dao.DAO_Post;
import com.example.project01_backup.model.FirebaseCallback;
import com.example.project01_backup.model.Post;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Censorship extends Fragment {
    private View view;
    private Adapter_LV_PostAdmin adapterPost;
    private ListView lvPost;
    private TextView tvNothing;
    private TextView tvNumber;
    private DAO_Post dao_post;

    public Fragment_Censorship() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_censorship, container, false);
        initView();
        return view;
    }

    private void initView() {
        dao_post = new DAO_Post(getActivity(),this);
        lvPost = (ListView) view.findViewById(R.id.fCensorship_lvPost);
        tvNumber = (TextView) view.findViewById(R.id.fCensorship_tvNumber);
        tvNothing = (TextView) view.findViewById(R.id.fCensorship_tvNothing);

        dao_post.getDataAdmin(new FirebaseCallback(){
            @Override
            public void postListAdmin(List<Post> postList) {
                adapterPost = new Adapter_LV_PostAdmin(getActivity(),postList);
                lvPost.setAdapter(adapterPost);
                tvNumber.setText(String.valueOf(postList.size()));
                if (postList.size()>0){
                    tvNothing.setVisibility(View.GONE);
                }else {
                    tvNothing.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
