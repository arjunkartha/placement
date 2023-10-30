package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        bottomNavigationView = findViewById(R.id.bottom_navigation);


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                // Load the Home fragment or activity
                loadFragment(new HomeFragment());
                return true;
//            } else if (itemId == R.id.navigation_dashboard) {
//                // Load the Dashboard fragment or activity
//                loadFragment(new DashboardFragment());
//                return true;
            } else if (itemId == R.id.profile) {
                // Load the Notifications fragment or activity
                loadFragment(new ProfileFragment());
                return true;
            }

            return false;
        });
    }
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}