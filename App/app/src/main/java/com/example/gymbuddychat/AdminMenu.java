package com.example.gymbuddychat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // Correct import for Toolbar
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AdminMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    Toolbar toolbar;
    private ImageView btnAttendance, btnDashboard, btnAddMember, btnViewMembers, btnDisplayHealthReport;
    Button logout;

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
//        TextView titleTextView = toolbar.findViewById(R.id.toolbar2); // Assuming the title TextView has an id of "toolbar_title"
//        titleTextView.setTextColor(getResources().getColor(R.color.white));


        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setBackground(null);

        // Programmatically select the "Attendance" item
        bottomNavigationView.setSelectedItemId(R.id.bottom_attendance); // Change to bottom_attendance

        fragmentManager = getSupportFragmentManager(); // Initialize fragmentManager

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.bottom_addMembers) {
                    replaceFragment(new AddMembers_Fragment());
                } else if (itemId == R.id.bottom_attendance) {
                    replaceFragment(new AttendanceFragment());

                }  else if (itemId == R.id.bottom_report) {
                    replaceFragment(new Display_Health_Fragment());
                } else if (itemId == R.id.bottom_Members) {
                    replaceFragment(new Display_Members());
                }
                return true; // Return true to indicate that the item is selected
            }
        });

        // Replace the initial fragment with AttendanceFragment
        replaceFragment(new AttendanceFragment());
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();

        if (itemId == R.id.Drawer_exit) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(AdminMenu.this, Login1.class);
            startActivity(intent);
            finish();
            return true; // Return true to indicate that the item is selected
        }

        return false;
    }

    // Method to replace fragment
    private void replaceFragment(Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }
}
