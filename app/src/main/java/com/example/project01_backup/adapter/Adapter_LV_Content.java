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
import com.example.project01_backup.model.Content;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_LV_Content extends BaseAdapter {
    private Context context;
    private List<Content> contentList;

    public Adapter_LV_Content(Context context, List<Content> contentList) {
        this.context = context;
        this.contentList = contentList;
    }

    @Override
    public int getCount() {
        return contentList.size();
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
        convertView = inflater.inflate(R.layout.raw_content, null);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.raw_content_tvDescription);
        ImageView imgContent = (ImageView) convertView.findViewById(R.id.raw_content_imgContent);

        Content content = contentList.get(position);
        tvDescription.setText(content.getDescription());
        if (content.getUrlImage() != null){
            Picasso.get().load(Uri.parse(content.getUrlImage())).into(imgContent);
        }else {
            imgContent.setImageURI(content.getUriImage());
        }

        return convertView;
    }
}
