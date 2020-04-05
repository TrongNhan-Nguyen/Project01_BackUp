package com.example.project01_backup.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project01_backup.R;
import com.example.project01_backup.activities.MainActivity;
import com.example.project01_backup.model.Content;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_LV_Content_Edit extends BaseAdapter {
    private Context context;
    private List<Content> contentList;

    public Adapter_LV_Content_Edit(Context context, List<Content> contentList) {
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
        convertView = inflater.inflate(R.layout.raw_content_edit, null);
        EditText tvDescription = (EditText) convertView.findViewById(R.id.raw_content_edit_etDescription);
        ImageView imgContent = (ImageView) convertView.findViewById(R.id.raw_content_edit_imgContent);
        ImageView imageDelete = (ImageView) convertView.findViewById(R.id.raw_content_edit_imgDelete);

        final Content content = contentList.get(position);
        imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentList.remove(content);
                notifyDataSetChanged();
            }
        });

        if (content.getUrlImage() != null){
            Picasso.get().load(Uri.parse(content.getUrlImage())).into(imgContent);
        }else {
            imgContent.setImageURI(content.getUriImage());
        }

        return convertView;
    }

}
