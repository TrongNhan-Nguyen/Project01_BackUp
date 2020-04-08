package com.example.project01_backup.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.project01_backup.dao.DAO_Feedback;
import com.example.project01_backup.model.Comment;
import com.example.project01_backup.model.Feedback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_LV_Feedback extends BaseAdapter {
    private Context context;
    private List<Feedback> feedbackList;
    private DAO_Feedback dao_feedback;

    public Adapter_LV_Feedback(Context context, List<Feedback> feedbackList) {
        this.context = context;
        this.feedbackList = feedbackList;
    }

    @Override
    public int getCount() {
        return feedbackList.size();
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
        convertView = inflater.inflate(R.layout.raw_feedback,null);
        dao_feedback = new DAO_Feedback(context);
        TextView tvEmail = (TextView) convertView.findViewById(R.id.raw_feedback_tvEmail);
        TextView tvPubDate = (TextView) convertView.findViewById(R.id.raw_feedback_tvPubDate);
        TextView tvComment = (TextView) convertView.findViewById(R.id.raw_feedback_tvComment);
        CircleImageView imgAvatar = (CircleImageView) convertView.findViewById(R.id.raw_feedback_imgAvatar);
        final Feedback feedback = feedbackList.get(position);
        tvEmail.setText(feedback.getEmailUser());
        tvPubDate.setText(feedback.getStringPubDate());
        tvComment.setText(feedback.getContentFeedBack());
        Picasso.get().load(Uri.parse(feedback.getUriAvatarUser())).into(imgAvatar);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Delete feedback?");
                dialog.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dao_feedback.delete(feedback.getIdFeedBack());
                    }
                });
                dialog.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });
        return convertView;
    }




}
