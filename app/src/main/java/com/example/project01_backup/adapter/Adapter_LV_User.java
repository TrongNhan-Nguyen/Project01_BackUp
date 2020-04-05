package com.example.project01_backup.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.project01_backup.R;
import com.example.project01_backup.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_LV_User extends BaseAdapter implements Filterable {
    private Context context;
    private List<User> userList;
    private List<User> allUser;

    public Adapter_LV_User(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
        this.allUser = new ArrayList<>(userList);
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return userList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<User> filterList = new ArrayList<>();
            if (constraint.toString().isEmpty()){
                filterList.addAll(allUser);
            }else {
                for (User user: allUser){
                    if (user.getName().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filterList.add(user);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            userList.clear();
            userList.addAll((Collection<? extends User>) results.values);
            notifyDataSetChanged();
        }
    };

    private class ViewHolder{
        CircleImageView imgAvatar;
        TextView tvName, tvEmail, tvCreated;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.raw_user,null);
            viewHolder = new ViewHolder();
            viewHolder.imgAvatar = (CircleImageView) convertView.findViewById(R.id.raw_user_imgAvatar);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.raw_user_tvName);
            viewHolder.tvEmail = (TextView) convertView.findViewById(R.id.raw_user_tvEmail);
            viewHolder.tvCreated = (TextView) convertView.findViewById(R.id.raw_user_tvCreated);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        User user = userList.get(position);
        viewHolder.tvName.setText(user.getName());
        viewHolder.tvEmail.setText(user.getEmail());
        viewHolder.tvCreated.setText(user.getStringCreated());
        try {
            Picasso.get().load(Uri.parse(user.getUriAvatar())).into(viewHolder.imgAvatar);
        }catch (Exception e){

        }
        return convertView;
    }

    public List<User> getUserList() {
        return userList;
    }
}
