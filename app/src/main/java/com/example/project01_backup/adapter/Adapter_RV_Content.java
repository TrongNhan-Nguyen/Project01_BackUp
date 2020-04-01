package com.example.project01_backup.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project01_backup.R;
import com.example.project01_backup.model.Content;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_RV_Content extends RecyclerView.Adapter<Adapter_RV_Content.ViewHolder> {
    Context context;
    List<Content> contentList;

    public Adapter_RV_Content(Context context, List<Content> contentList) {
        this.context = context;
        this.contentList = contentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.raw_content, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Content content = contentList.get(position);
        holder.tvDescription.setText(content.getDescription());
        Picasso.get().load(Uri.parse(content.getUrlImage())).into(holder.imgContent);
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvDescription;
        ImageView imgContent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = (TextView) itemView.findViewById(R.id.raw_content_tvDescription);
            imgContent = (ImageView) itemView.findViewById(R.id.raw_content_imgContent);
        }
    }
}
