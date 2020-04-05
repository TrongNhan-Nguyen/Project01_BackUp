package com.example.project01_backup.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.project01_backup.R;
import com.example.project01_backup.adapter.Adapter_LV_PostUser;
import com.example.project01_backup.dao.DAO_Post;
import com.example.project01_backup.model.FirebaseCallback;
import com.example.project01_backup.model.Post;
import com.example.project01_backup.model.User;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_PostListAdmin extends Fragment {
    private View view;
    private ListView lvPost;
    private Adapter_LV_PostUser adapterPost;
    private DAO_Post dao_post;

    public Fragment_PostListAdmin() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_post_list_admin, container, false);
        dao_post = new DAO_Post(getActivity(), this);
        User user = Fragment_Tab_UserInfoAdmin.user;
        lvPost = (ListView) view.findViewById(R.id.fPostListAdmin_lvPost);
        dao_post.getDataByUser(user.getEmail(), new FirebaseCallback(){
            @Override
            public void postListUser(List<Post> postList) {
                adapterPost = new Adapter_LV_PostUser(getActivity(), postList);
                lvPost.setAdapter(adapterPost);
            }
        });
        return view;
    }

    private void toast(String s){
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
}
