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
import android.widget.TextView;
import android.widget.Toast;

import com.example.project01_backup.R;
import com.example.project01_backup.dao.DAO_Places;
import com.example.project01_backup.fragment.Fragment_Accommodations;
import com.example.project01_backup.fragment.Fragment_BeautifulPlaces;
import com.example.project01_backup.fragment.Fragment_JourneyDiary;
import com.example.project01_backup.fragment.Fragment_Restaurant;
import com.example.project01_backup.fragment.Fragment_Tab_UserInfo;
import com.example.project01_backup.fragment.Fragment_UserInfo;
import com.example.project01_backup.model.Places;
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
    private FirebaseUser currentUser;
    private String password;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent getPass = getIntent();
        if (getPass != null) {
            password = getPass.getStringExtra("pass");
            email = getPass.getStringExtra("email");
        }
        initView();
    }

    private void initView() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser!=null){
            Toast.makeText(this, "Hello " + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
        }
        drawerLayout = (DrawerLayout) findViewById(R.id.main_Drawer);
        navigationView = (NavigationView) findViewById(R.id.main_Navigation);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        replaceFragment(new Fragment_Restaurant());
        showInfo();
        hideAdmin();
    }

    private void showInfo() {
        if (currentUser != null) {
            View headerView = navigationView.getHeaderView(0);
            CircleImageView imgAvatar = (CircleImageView) headerView.findViewById(R.id.header_imgAvatar);
            TextView tvDisplayName = (TextView) headerView.findViewById(R.id.header_tvDisplayName);
            TextView tvEmail = (TextView) headerView.findViewById(R.id.header_tvEmail);
            Uri uriAvatar = currentUser.getPhotoUrl();
            Picasso.get().load(uriAvatar).into(imgAvatar);
            tvDisplayName.setText(currentUser.getDisplayName());
            tvEmail.setText(currentUser.getEmail());
        }
    }
    private void insertPlaces(){
        DAO_Places dao_places = new DAO_Places(this);
        String[] placeList = {"Da Lat","Sapa","Dong Nai","Can Tho", "Vung Tau","Quy Nhon","Da Nang",
                            "Ca Mau", "TP HCM", "Ha Noi","Long An"};
        for (int i =0; i<placeList.length; i++){
            Places places = new Places();
            places.setName(placeList[i]);
            dao_places.insert(places);
        }
    }
    private void runtimePermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(MainActivity.this, "All granted", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();
    }

    private void hideAdmin() {
        Menu drawerMenu = navigationView.getMenu();
        drawerMenu.findItem(R.id.menu_drawer_Admin).setVisible(false);
        drawerMenu.findItem(R.id.menu_drawer_Information).setVisible(false);
        String[] adminList = {"nhan@gmail.com","ngan@gmail.com", "hao@gmail.com", "lam@gmail.com"};
        if (currentUser != null){
            drawerMenu.findItem(R.id.menu_drawer_Information).setVisible(true);
            for (String admin : adminList){
                if (currentUser.getEmail().equalsIgnoreCase(admin)){
                    drawerMenu.findItem(R.id.menu_drawer_Admin).setVisible(true);
                    break;

                }
            }
        }
        if (currentUser == null){
            drawerMenu.findItem(R.id.menu_drawer_SignOut).setTitle("Sign in");
        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
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
                replaceFragment(new Fragment_JourneyDiary());
                break;
            case R.id.menu_drawer_Admin:
                Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                intent.putExtra("name", currentUser.getDisplayName());
                intent.putExtra("avatar", String.valueOf(currentUser.getPhotoUrl()));
                intent.putExtra("email", email);
                intent.putExtra("pass", password);
                startActivity(intent);
                break;
            case R.id.menu_drawer_Information:
                replaceFragment(new Fragment_Tab_UserInfo());
                break;
            case R.id.menu_drawer_SignOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                break;
            case R.id.menu_drawer_Exit:
                FirebaseAuth.getInstance().signOut();
                finishAffinity();
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().
                replace(R.id.main_FrameLayout, fragment)
                .addToBackStack(null)
                .commit();
    }


}
