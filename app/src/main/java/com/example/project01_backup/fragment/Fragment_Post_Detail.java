package com.example.project01_backup.fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project01_backup.R;
import com.example.project01_backup.adapter.Adapter_LV_Comment;
import com.example.project01_backup.adapter.Adapter_LV_Content;
import com.example.project01_backup.adapter.Adapter_LV_Post;
import com.example.project01_backup.dao.DAO_Comment;
import com.example.project01_backup.dao.DAO_Content;
import com.example.project01_backup.model.Comment;
import com.example.project01_backup.model.Content;
import com.example.project01_backup.model.FirebaseCallback;
import com.example.project01_backup.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Post_Detail extends Fragment {

    private View view;
    private ImageView imgAvatar, imgPost;
    private TextView tvTitle, tvPubDate, tvDescription, tvAddress, tvEmail;
    private EditText etComment;
    private ListView lvComment, lvContent;
    private FirebaseUser currentUser;
    private Button btnPost;
    private Post post;
    private Adapter_LV_Comment adapterComment;
    private Adapter_LV_Content adapterContent;
    private DAO_Comment dao_comment;
    private DAO_Content dao_content;

    public Fragment_Post_Detail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_detail, container, false);
        initView();
        return view;
    }

    private void initView() {
        dao_comment = new DAO_Comment(getActivity(),this);
        dao_content = new DAO_Content(getActivity(),this);
        Bundle bundle = getArguments();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        post = (Post) bundle.getSerializable(Adapter_LV_Post.POST);
        tvEmail = (TextView) view.findViewById(R.id.fDetail_tvEmail);
        tvPubDate = (TextView) view.findViewById(R.id.fDetail_tvPubDate);
        tvTitle = (TextView) view.findViewById(R.id.fDetail_tvTitle);
        tvAddress = (TextView) view.findViewById(R.id.fDetail_tvAddress);
        tvDescription = (TextView) view.findViewById(R.id.fDetail_tvDescription);
        imgAvatar = (ImageView) view.findViewById(R.id.fDetail_imgAvatarUser);
        imgPost = (ImageView) view.findViewById(R.id.fDetail_imgPost);
        etComment = (EditText) view.findViewById(R.id.fDetail_etAddComment);
        btnPost = (Button) view.findViewById(R.id.fDetail_btnPost);
        lvComment = (ListView) view.findViewById(R.id.fDetail_lvComment);
        lvContent = (ListView) view.findViewById(R.id.fDetail_lvContent);

        dao_comment.getData(post.getId(),new FirebaseCallback(){
            @Override
            public void commentList(List<Comment> commentList) {
                adapterComment = new Adapter_LV_Comment(getActivity(),commentList);
                lvComment.setAdapter(adapterComment);
            }
        });

        dao_content.getData(post.getId(), new FirebaseCallback(){
            @Override
            public void contentList(List<Content> contentList) {
                adapterContent = new Adapter_LV_Content(getActivity(),contentList);
                lvContent.setAdapter(adapterContent);
            }
        });

        String email = post.getUser();
        String pubDate = post.getPubDate();
        String title = post.getTittle();
        String address = post.getAddress();
        String description = post.getDescription();
        String uriAvatar = post.getUrlAvatarUser();
        String uriPost = post.getUrlImage();

        tvEmail.setText(email);
        tvPubDate.setText(pubDate);
        tvTitle.setText(title);
        tvAddress.setText(address);
        tvDescription.setText(description);
        Picasso.get().load(Uri.parse(uriAvatar)).into(imgAvatar);
        Picasso.get().load(Uri.parse(uriPost)).into(imgPost);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();
            }
        });


    }
    private void postComment(){
        String idPost = post.getId();
        Comment comment = new Comment();
        comment.setContentComment(etComment.getText().toString());
        comment.setIdUser(currentUser.getUid());
        comment.setEmailUser(currentUser.getEmail());
        comment.setPubDate(stringPubDate());
        comment.setLongPubDate(longPubDate());
        comment.setUriAvatarUser(String.valueOf(currentUser.getPhotoUrl()));
        dao_comment.insert(idPost,comment);
    }
    private String stringPubDate(){
        String pubDate;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        pubDate = format.format(calendar.getTime());
        return pubDate;
    }
    private long longPubDate(){
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }
    private void toast (String s){
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    private void log (String s){
        Log.d("log", s);
    }
}
