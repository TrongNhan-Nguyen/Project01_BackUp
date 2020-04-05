package com.example.project01_backup.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.project01_backup.fragment.Fragment_PostList;
import com.example.project01_backup.fragment.Fragment_PostListAdmin;
import com.example.project01_backup.fragment.Fragment_UserInfo;
import com.example.project01_backup.fragment.Fragment_UserInfoAdmin;

public class Adapter_Tab_User extends FragmentStatePagerAdapter {
    public Adapter_Tab_User(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Fragment_PostList();
            case 1:
                return new Fragment_UserInfo();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
