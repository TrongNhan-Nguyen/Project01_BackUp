package com.example.project01_backup.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project01_backup.R;
import com.example.project01_backup.model.Comment;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_LV_Comment extends BaseAdapter {
    Context context;
    List<Comment> commentList;

    public Adapter_LV_Comment(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.raw_comment,null);
        TextView tvEmail = (TextView) convertView.findViewById(R.id.raw_comment_tvEmail);
        TextView tvPubDate = (TextView) convertView.findViewById(R.id.raw_comment_tvPubDate);
        TextView tvComment = (TextView) convertView.findViewById(R.id.raw_comment_tvComment);
        CircleImageView imgAvatar = (CircleImageView) convertView.findViewById(R.id.raw_comment_imgAvatar);
        Comment comment = commentList.get(position);

        tvEmail.setText(comment.getEmailUser());
        tvPubDate.setText(comment.getPubDate());
        tvComment.setText(comment.getContentComment());
        Picasso.get().load(Uri.parse(comment.getUriAvatarUser())).into(imgAvatar);


        return convertView;
    }
}
