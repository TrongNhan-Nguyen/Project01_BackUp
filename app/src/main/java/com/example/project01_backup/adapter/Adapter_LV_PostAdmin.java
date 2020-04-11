package com.example.project01_backup.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project01_backup.R;
import com.example.project01_backup.dao.DAO_Content;
import com.example.project01_backup.dao.DAO_Post;
import com.example.project01_backup.model.Content;
import com.example.project01_backup.model.FirebaseCallback;
import com.example.project01_backup.model.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_LV_PostAdmin extends BaseAdapter {
    private Context context;
    private List<Post> postList;
    private int index = -1;
    private DAO_Content dao_content;
    private DAO_Post dao_post;


    public Adapter_LV_PostAdmin(Context context, List<Post> postList) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.raw_post,null);
        TextView tvPubDate = (TextView) convertView.findViewById(R.id.raw_post_tvPubDate);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.raw_post_tvTitle);
        TextView tvUser = (TextView) convertView.findViewById(R.id.raw_post_tvUser);
        TextView tvAddress = (TextView) convertView.findViewById(R.id.raw_post_tvAddress);
        ImageView imgPost = (ImageView) convertView.findViewById(R.id.raw_post_imgPost);
        CircleImageView imgAvatar = (CircleImageView) convertView.findViewById(R.id.raw_post_imgAvatarUser);
        final Post post = postList.get(position);
        dao_content = new DAO_Content(context);
        dao_post = new DAO_Post(context);

        tvUser.setText(post.getEmailUser());
        tvPubDate.setText(post.getPubDate());
        tvTitle.setText(post.getTittle());
        tvAddress.setText(post.getAddress());
        try {
            Picasso.get().load(Uri.parse(post.getUrlAvatarUser())).into(imgAvatar);
            Picasso.get().load(Uri.parse(post.getUrlImage())).into(imgPost);
        }catch (Exception e){

        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = position;
                dialogCensorship();

            }
        });

        return convertView;
    }

    private void dialogCensorship(){
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Material_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_censorship);
        final Post post = postList.get(index);
        TextView tvDone = (TextView) dialog.findViewById(R.id.dCensorship_tvDone);
        TextView tvEmail = (TextView) dialog.findViewById(R.id.dCensorship_tvDisplayName);
        TextView tvPubDate = (TextView) dialog.findViewById(R.id.dCensorship_tvPubDate);
        TextView tvTitle = (TextView) dialog.findViewById(R.id.dCensorship_tvTitle);
        TextView tvAddress = (TextView) dialog.findViewById(R.id.dCensorship_tvAddress);
        TextView tvDescription = (TextView) dialog.findViewById(R.id.dCensorship_tvDescription);
        final ImageView imgCensorship = (ImageView) dialog.findViewById(R.id.dCensorship_imgCensorship);
        ImageView imgDelete = (ImageView) dialog.findViewById(R.id.dCensorship_imgDelete);
        CircleImageView imgAvatar = (CircleImageView) dialog.findViewById(R.id.dCensorship_imgAvatarUser);
        final ImageView imgPost = (ImageView) dialog.findViewById(R.id.dCensorship_imgPost);
        final ListView lvContent = (ListView) dialog.findViewById(R.id.dCensorship_lvContent);
        final List<Content> listContent = new ArrayList<>();

        tvEmail.setText(post.getDisplayName());
        tvPubDate.setText(post.getPubDate());
        tvTitle.setText(post.getTittle());
        tvAddress.setText(post.getAddress());
        tvDescription.setText(post.getDescription());
        Picasso.get().load(Uri.parse(post.getUrlImage())).into(imgPost);
        Picasso.get().load(Uri.parse(post.getUrlAvatarUser())).into(imgAvatar);

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        imgCensorship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogCensorship = new AlertDialog.Builder(context);
                dialogCensorship.setMessage("Allow users to access this article!");
                dialogCensorship.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {
                        dao_post.insertUser(post,imgPost);
                        for (Content content : listContent){
                            dao_content.insertUser(post.getIdPost(),content);
                        }
                        dao_post.deleteAdmin(post.getCategory(), post.getPlace(), post.getIdPost());
                        dao_content.deleteAdmin(post.getIdPost());
                        dialog.dismiss();


                    }
                });
                dialogCensorship.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {

                    }
                });
                dialogCensorship.show();
            }
        });

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogDelete = new AlertDialog.Builder(context);
                dialogDelete.setMessage("Delete an article?");
                dialogDelete.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {
                        dao_post.deleteAdmin(post.getCategory(), post.getPlace(), post.getIdPost());
                        dao_content.deleteAdmin(post.getIdPost());
                        dialog.dismiss();
                    }
                });

                dialogDelete.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialogDelete.show();

            }
        });

        dao_content.getDataAdmin(post.getIdPost(),new FirebaseCallback(){
            @Override
            public void contentListAdmin(List<Content> contentList) {
                for (Content content : contentList){
                    listContent.add(content);
                }
                Adapter_LV_Content adapter = new Adapter_LV_Content(context,contentList);
                lvContent.setAdapter(adapter);

            }
        });


        dialog.show();
    }



    private void toast (String s){
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
}
