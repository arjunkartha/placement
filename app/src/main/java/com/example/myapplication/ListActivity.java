package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Retrieve the list data from the intent
        ArrayList<String> driveInfoList = getIntent().getStringArrayListExtra("driveInfoList");

        // Set up the ListView with the adapter
        ListView listView = findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, driveInfoList);
        listView.setAdapter(adapter);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                // Load the Home fragment or activity
                loadFragment(new HomeFragment());
                return true;
                // } else if (itemId == R.id.navigation_dashboard) {
                // Load the Dashboard fragment or activity
                // loadFragment(new DashboardFragment());
                // return true;
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