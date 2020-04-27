package com.example.project01_backup.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project01_backup.R;
import com.example.project01_backup.dao.DAO_Comment;
import com.example.project01_backup.model.Comment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_LV_Comment extends BaseAdapter {
    private Context context;
    private List<Comment> commentList;
    private DAO_Comment dao_comment;
    private int index = -1;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.raw_comment,null);
        dao_comment = new DAO_Comment(context);
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        TextView tvEmail = (TextView) convertView.findViewById(R.id.raw_comment_tvEmail);
        TextView tvPubDate = (TextView) convertView.findViewById(R.id.raw_comment_tvPubDate);
        TextView tvComment = (TextView) convertView.findViewById(R.id.raw_comment_tvComment);
        CircleImageView imgAvatar = (CircleImageView) convertView.findViewById(R.id.raw_comment_imgAvatar);
        final ImageView imgMore = (ImageView) convertView.findViewById(R.id.raw_comment_imgMore);
        final Comment comment = commentList.get(position);
        tvEmail.setText(comment.getDisplayName());
        tvPubDate.setText(comment.getPubDate());
        tvComment.setText(comment.getContentComment());
        Picasso.get().load(Uri.parse(comment.getUriAvatarUser())).into(imgAvatar);

        imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = position;
                if (currentUser!=null){
                    if (currentUser.getEmail().equalsIgnoreCase("nhan@gmail.com")||
                        currentUser.getEmail().equalsIgnoreCase("ngan@gmail.com")||
                        currentUser.getEmail().equalsIgnoreCase("lam@gmail.com")||
                        currentUser.getEmail().equalsIgnoreCase("hao@gmail.com")){
                        popupMenuAdmin(imgMore);
                    }else if (currentUser.getEmail().equalsIgnoreCase(comment.getEmailUser())){
                        popupMenuUser(imgMore);
                    }
                }
            }
        });


        return convertView;
    }

    private void popupMenuAdmin(ImageView imageView){
        final PopupMenu popupMenu = new PopupMenu(context, imageView);
        popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());
        popupMenu.getMenu().findItem(R.id.menu_popup_edit).setVisible(false);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_popup_delete:
                        deleteComment();
                        break;
                    case R.id.menu_popup_cancel:
                        break;
                }
                return false;
            }
        });
        popupMenu.show();

    }
    private void popupMenuUser(ImageView imageView){
        final PopupMenu popupMenu = new PopupMenu(context, imageView);
        popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_popup_edit:
                        editComment();
                        break;
                    case R.id.menu_popup_delete:
                        deleteComment();
                        break;
                    case R.id.menu_popup_cancel:
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }
    private void deleteComment(){
        Comment comment = commentList.get(index);
        dao_comment.deleteByIdComment(comment);
    }
    private void editComment(){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_edit_comment);
        final EditText etComment = (EditText) dialog.findViewById(R.id.dEditComment_etComment);
        ImageView imgPost = (ImageView) dialog.findViewById(R.id.dEditComment_imgPost);
        final Comment comment = commentList.get(index);
        etComment.setText(comment.getContentComment());
        imgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etComment.getText().toString().isEmpty()){
                    Toast.makeText(context, "Vui lòng nhập nội dung comment", Toast.LENGTH_SHORT).show();
                }else {
                    comment.setContentComment(etComment.getText().toString());
                    comment.setPubDate(stringPubDate());
                    comment.setLongPubDate(longPubDate());
                    dao_comment.update(comment);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }
    private String stringPubDate(){
        String date;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        date = format.format(calendar.getTime());
        return date;
    }
    private long longPubDate(){
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }
}
