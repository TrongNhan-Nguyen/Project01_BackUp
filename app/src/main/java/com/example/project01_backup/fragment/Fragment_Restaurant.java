package com.example.project01_backup.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project01_backup.R;
import com.example.project01_backup.activities.MainActivity;
import com.example.project01_backup.adapter.Adapter_LV_PostUser;
import com.example.project01_backup.dao.DAO_Places;
import com.example.project01_backup.dao.DAO_Post;
import com.example.project01_backup.model.FirebaseCallback;
import com.example.project01_backup.model.Places;
import com.example.project01_backup.model.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Restaurant extends Fragment {
    private View view;
    private DAO_Places dao_places;
    private DAO_Post dao_post;
    private List<String> placeNames;
    private TextView tvTitle, tvNothing;
    private ListView listView;
    private Adapter_LV_PostUser adapterPost;
    private FirebaseUser user;
    private FloatingActionButton fbaAdd;
    private List<Post> listPost;
    private String categoryNode;


    public Fragment_Restaurant() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_restaurant, container, false);
        initView();
        return view;
    }

    private void initView() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        dao_post = new DAO_Post(getActivity(), this);
        dao_places = new DAO_Places(getActivity(), this);
        tvTitle = (TextView) view.findViewById(R.id.fRestaurant_tvTitle);
        tvNothing = (TextView) view.findViewById(R.id.fRestaurant_tvNothing);
        fbaAdd = (FloatingActionButton) view.findViewById(R.id.fRestaurant_fabAddPost);
        listView = (ListView) view.findViewById(R.id.fRestaurant_lvPost);
        categoryNode = "Restaurants";
        dao_post.getDataUser(categoryNode, new FirebaseCallback(){
            @Override
            public void postListUser(List<Post> postList) {
               refreshLV(postList);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment_Post_Detail fragment_post_detail = new Fragment_Post_Detail();
                Bundle bundle = new Bundle();
                Post post = listPost.get(position);
                bundle.putSerializable("post", post);
                fragment_post_detail.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_FrameLayout, fragment_post_detail)
                        .addToBackStack(null)
                        .commit();
            }
        });


        if (user == null) {
            fbaAdd.setVisibility(View.GONE);
        }

        fbaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_AddPost addPost = new Fragment_AddPost();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_FrameLayout, addPost)
                        .commit();
            }
        });

        placeNames = new ArrayList<>();


    }

    private void log(String s) {
        Log.d("log", s);
    }

    private void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
    private void refreshLV(List<Post> postList){
        listPost = new ArrayList<>(postList);
        adapterPost = new Adapter_LV_PostUser(getActivity(),listPost);
        listView.setAdapter(adapterPost);
        if (postList.size()>0){
            tvNothing.setVisibility(View.GONE);
        }else {
            tvNothing.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_places,menu);
        MenuItem search = menu.findItem(R.id.menu_search_places);
        final SearchView searchView = (SearchView) search.getActionView();
        searchView.setQueryHint("Vui lòng nhập tên địa điểm");
        final SearchView.SearchAutoComplete autoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        autoComplete.setBackgroundColor(Color.WHITE);
        autoComplete.setTextColor(Color.BLACK);
        autoComplete.setDropDownBackgroundResource(android.R.color.holo_blue_light);
        autoComplete.setThreshold(1);
        dao_places.getData(new FirebaseCallback(){
            @Override
            public void placesList(final List<Places> placesList) {
                placeNames.clear();
                for (Places places : placesList){
                    placeNames.add(places.getName());
                    ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, placeNames);
                    autoComplete.setAdapter(newsAdapter);
                }
                autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                        String placeNode =(String)adapterView.getItemAtPosition(itemIndex);
                        autoComplete.setText(placeNode);
                        tvTitle.setText(placeNode);
                        dao_post.getDataByPlace(categoryNode,placeNode, new FirebaseCallback(){
                            @Override
                            public void postListPlace(List<Post> postList) {
                                refreshLV(postList);
                            }
                        });

                    }
                });

            }
        });
        final MenuItem refresh = menu.findItem(R.id.menu_search_refresh);
        refresh.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                dao_post.getDataUser(categoryNode, new FirebaseCallback(){
                    @Override
                    public void postListUser(List<Post> postList) {
                        refreshLV(postList);
                        tvTitle.setText(categoryNode);
                    }
                });
                return false;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);
    }
}
