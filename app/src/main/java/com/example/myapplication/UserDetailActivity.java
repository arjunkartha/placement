package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class UserDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        // Retrieve user details from the intent
        String userName = getIntent().getStringExtra("userName");
        String userEmail = getIntent().getStringExtra("userEmail");
        String userRole = getIntent().getStringExtra("userRole");


        // Find TextViews or other UI elements in the layout
        TextView userNameTextView = findViewById(R.id.userNameTextView);
        TextView userEmailTextView = findViewById(R.id.userEmailTextView);
        TextView userRoleTextView = findViewById(R.id.userRoleTextView);


        // Set the text in the UI elements
        userNameTextView.setText(userName);
        userEmailTextView.setText(userEmail);
        userRoleTextView.setText(userRole);

    }
}
