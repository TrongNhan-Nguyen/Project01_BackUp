package com.example.project01_backup.adapter;

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
    private DAO_Comment dao_comment;
    private int index = -1;
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
        TextView tvEmail = (TextView) convertView.findViewById(R.id.raw_feedback_tvEmail);
        TextView tvPubDate = (TextView) convertView.findViewById(R.id.raw_feedback_tvPubDate);
        TextView tvComment = (TextView) convertView.findViewById(R.id.raw_feedback_tvComment);
        CircleImageView imgAvatar = (CircleImageView) convertView.findViewById(R.id.raw_feedback_imgAvatar);
        Feedback feedback = feedbackList.get(position);
        tvEmail.setText(feedback.getEmailUser());
        tvPubDate.setText(feedback.getStringPubDate());
        tvComment.setText(feedback.getContentFeedBack());
        Picasso.get().load(Uri.parse(feedback.getUriAvatarUser())).into(imgAvatar);
        return convertView;
    }




}
