package com.example.project01_backup.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.project01_backup.R;
import com.example.project01_backup.activities.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Accommodations extends Fragment {
    private View view;
    private FloatingActionButton fabAdd;
    private TextView tvTitle;

    public Fragment_Accommodations() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_accommodations, container, false);
        initView();
        return view;
    }

    private void initView() {
        tvTitle = (TextView) view.findViewById(R.id.fAccommodation_tvTitle);
        fabAdd = (FloatingActionButton) view.findViewById(R.id.fAccommodation_fabAddPost);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_AddPost addPost = new Fragment_AddPost();
                Bundle bundle = new Bundle();
                bundle.putString(MainActivity.POINT_TO_NODE,"accommodations");
                addPost.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_FrameLayout,addPost)
                        .commit();
            }
        });

    }
}
