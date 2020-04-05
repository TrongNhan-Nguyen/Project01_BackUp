package com.example.project01_backup.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.project01_backup.R;
import com.example.project01_backup.adapter.Adapter_Tab_User;
import com.example.project01_backup.adapter.Adapter_Tab_UserAdmin;
import com.example.project01_backup.model.User;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Tab_UserInfo extends Fragment {
    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    static User user;

    public Fragment_Tab_UserInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        if (bundle != null){
            user = (User) bundle.getSerializable("user");
        }
        view = inflater.inflate(R.layout.fragment_tab_user, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.fTabUser_tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.fTabUser_viewPager);
        viewPager.setAdapter(new Adapter_Tab_User(getChildFragmentManager(),1));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.post_list);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_people_black_24dp);
        return view;
    }

}
