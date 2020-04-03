package com.example.project01_backup.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project01_backup.R;
import com.example.project01_backup.fragment.Fragment_Accommodations;
import com.example.project01_backup.fragment.Fragment_AddPost;
import com.example.project01_backup.fragment.Fragment_BeautifulPlaces;
import com.example.project01_backup.fragment.Fragment_Blog;
import com.example.project01_backup.fragment.Fragment_Post_Detail;
import com.example.project01_backup.fragment.Fragment_Restaurant;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private FirebaseUser firebaseUser;
    private String password;
    public static final String POINT_TO_NODE = "point to node";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent getPass = getIntent();
        if (getPass != null){
            password = getPass.getStringExtra("pass");
        }
        initView();
    }

    private void initView() {
        
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        drawerLayout = (DrawerLayout) findViewById(R.id.main_Drawer);
        navigationView = (NavigationView) findViewById(R.id.main_Navigation);
        toggle = new ActionBarDrawerToggle(this,drawerLayout, R.string.open, R.string.close);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        replaceFragment(new Fragment_Restaurant());
        showInfo();
        hideAdmin();
    }

    private void showInfo(){
        if (firebaseUser != null){
            View headerView = navigationView.getHeaderView(0);
            CircleImageView imgAvatar = (CircleImageView) headerView.findViewById(R.id.header_imgAvatar);
            TextView tvDisplayName = (TextView) headerView.findViewById(R.id.header_tvDisplayName);
            TextView tvEmail = (TextView) headerView.findViewById(R.id.header_tvEmail);
            Uri uriAvatar = firebaseUser.getPhotoUrl();
            Picasso.get().load(uriAvatar).into(imgAvatar);
            tvDisplayName.setText(firebaseUser.getDisplayName());
            tvEmail.setText(firebaseUser.getEmail());
        }
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
    private void hideAdmin(){
        if (firebaseUser == null) {
            Menu drawerMenu = navigationView.getMenu();
            drawerMenu.findItem(R.id.menu_drawer_Admin).setVisible(false);
        }
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
                replaceFragment(new Fragment_Accommodations());
                break;
            case R.id.menu_drawer_CheckIn:
                replaceFragment(new Fragment_BeautifulPlaces());
                break;
            case R.id.menu_drawer_Blog:
                replaceFragment(new Fragment_Blog());
                break;
            case R.id.menu_drawer_Admin:
                finish();
                Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                intent.putExtra("name", firebaseUser.getDisplayName());
                intent.putExtra("avatar", String.valueOf(firebaseUser.getPhotoUrl()));
                intent.putExtra("email", firebaseUser.getEmail());
                intent.putExtra("pass", password);
                startActivity(intent);
                break;
            case R.id.menu_drawer_Information:
                break;
            case R.id.menu_drawer_LogOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
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
