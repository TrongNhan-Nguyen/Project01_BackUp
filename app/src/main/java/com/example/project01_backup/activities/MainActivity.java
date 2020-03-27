package com.example.project01_backup.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.project01_backup.R;
import com.example.project01_backup.dao.DAO_Places;
import com.example.project01_backup.fragment.Fragment_Accomodations;
import com.example.project01_backup.fragment.Fragment_BeautifulPlaces;
import com.example.project01_backup.fragment.Fragment_Blog;
import com.example.project01_backup.fragment.Fragment_Restaurant;
import com.example.project01_backup.model.Places;
import com.google.android.material.navigation.NavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    DAO_Places dao_places;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
    }

    private void initView() {
//        dao_places = new DAO_Places(this);
//        dao_places.insert(new Places("Da Lat"));
//        dao_places.insert(new Places("Ca Mau"));
//        dao_places.insert(new Places("Ha Long"));
//        dao_places.insert(new Places("Can Tho"));
        drawerLayout = (DrawerLayout) findViewById(R.id.main_Drawer);
        navigationView = (NavigationView) findViewById(R.id.main_Navigation);
        toggle = new ActionBarDrawerToggle(this,drawerLayout, R.string.open, R.string.close);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        replaceFragment(new Fragment_Restaurant());
    }

    private void runtimePermission(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()){
                            Toast.makeText(MainActivity.this, "All granted", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_drawer_Restaurant:
                replaceFragment(new Fragment_Restaurant());
                break;
            case R.id.menu_drawer_Accommodations:
                replaceFragment(new Fragment_Accomodations());
                break;
            case R.id.menu_drawer_CheckIn:
                replaceFragment(new Fragment_BeautifulPlaces());
                break;
            case R.id.menu_drawer_Blog:
                replaceFragment(new Fragment_Blog());
                break;
            case R.id.menu_drawer_Admin:
                break;
            case R.id.menu_drawer_Information:
                break;
            case R.id.menu_drawer_LogOut:
                break;
            case R.id.menu_drawer_About:
                break;
            case R.id.menu_drawer_Exit:
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void replaceFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.main_FrameLayout,fragment).commit();
    }
}
