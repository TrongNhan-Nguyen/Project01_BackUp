package com.example.project01_backup.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project01_backup.R;
import com.example.project01_backup.adapter.Adapter_LV_PostUser;
import com.example.project01_backup.dao.DAO_Comment;
import com.example.project01_backup.dao.DAO_Content;
import com.example.project01_backup.dao.DAO_Post;
import com.example.project01_backup.model.FirebaseCallback;
import com.example.project01_backup.model.Post;
import com.example.project01_backup.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_PostListAdmin extends Fragment {
    private View view;
    private ListView lvPost;
    private TextView tvNothing;
    private Adapter_LV_PostUser adapterPost;
    private DAO_Post dao_post;
    private List<Post> listPost;
    private DAO_Content dao_content;
    private DAO_Comment dao_comment;

    public Fragment_PostListAdmin() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_post_list_admin, container, false);
        dao_post = new DAO_Post(getActivity(), this);
        dao_content = new DAO_Content(getActivity(),this);
        dao_comment = new DAO_Comment(getActivity(),this);
        User user = Fragment_Tab_UserInfoAdmin.user;
        lvPost = (ListView) view.findViewById(R.id.fPostListAdmin_lvPost);
        tvNothing = (TextView) view.findViewById(R.id.fPostListAdmin_tvNothing);
        dao_post.getDataByUser(user.getEmail(), new FirebaseCallback(){
            @Override
            public void postListUser(List<Post> postList) {
                listPost = new ArrayList<>(postList);
                adapterPost = new Adapter_LV_PostUser(getActivity(), listPost);
                lvPost.setAdapter(adapterPost);
                if (postList.size()>0){
                    tvNothing.setVisibility(View.GONE);
                }else {
                    tvNothing.setVisibility(View.VISIBLE);
                }
            }
        });

        lvPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment_Post_Detail fragment_post_detail = new Fragment_Post_Detail();
                Bundle bundle = new Bundle();
                Post post = listPost.get(position);
                bundle.putSerializable("post", post);
                fragment_post_detail.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.admin_FrameLayout, fragment_post_detail)
                        .addToBackStack(null)
                        .commit();
            }
        });
        lvPost.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setMessage("Delete an article");
                dialog.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Post post = listPost.get(position);
                        dao_post.deleteUser(post.getCategory(),post.getPlace(),post.getIdPost());
                        dao_content.deleteUser(post.getIdPost());
                        dao_comment.deleteByIdPost(post.getIdPost());
                    }
                });
                dialog.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
                return true;
            }
        });
        return view;
    }

    private void toast(String s){
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
}
