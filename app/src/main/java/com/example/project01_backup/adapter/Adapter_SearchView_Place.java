package com.example.project01_backup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project01_backup.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Adapter_SearchView_Place extends BaseAdapter implements Filterable {
    private Context context;
    private List<String> placeList, allPlaces;

    public Adapter_SearchView_Place(Context context, List<String> placeList) {
        this.context = context;
        this.placeList = placeList;
        this.allPlaces = new ArrayList<>(placeList);
    }


    @Override
    public int getCount() {
        return placeList.size();
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
        convertView = inflater.inflate(R.layout.raw_places, null);
        TextView tvName = convertView.findViewById(R.id.raw_places_tvName);
        final String placeName = placeList.get(position);
        tvName.setText(placeName);

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<String> filterList = new ArrayList<>();
            if (constraint.toString().isEmpty()){
                filterList.addAll(allPlaces);
            }else {
                for (String name: allPlaces){
                    if (name.toLowerCase().contains(constraint.toString().toLowerCase())){
                        filterList.add(name);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            placeList.clear();
            placeList.addAll((Collection<? extends String>) results.values);
            notifyDataSetChanged();
        }
    };
}
