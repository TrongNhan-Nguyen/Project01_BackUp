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
import com.example.project01_backup.model.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_LV_Post extends BaseAdapter {
    Context context;
    List<Post> postList;

    public Adapter_LV_Post(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @Override
    public int getCount() {
        return postList.size();
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
        convertView = inflater.inflate(R.layout.raw_post,null);
        TextView tvPubDate = (TextView) convertView.findViewById(R.id.raw_post_tvPubDate);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.raw_post_tvTitle);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.raw_post_tvDescription);
        ImageView imgPost = (ImageView) convertView.findViewById(R.id.raw_post_imgPost);
        ImageView imgAvatar = (ImageView) convertView.findViewById(R.id.raw_post_imgAvatarUser);
        Post post = postList.get(position);

        tvPubDate.setText(post.getPubDate());
        tvTitle.setText(post.getTittle());
        tvDescription.setText(post.getDescription());
        Picasso.get().load(Uri.parse(post.getUrlAvatarUser())).into(imgAvatar);
        Picasso.get().load(Uri.parse(post.getUrlImage())).into(imgPost);


        return convertView;
    }
}
