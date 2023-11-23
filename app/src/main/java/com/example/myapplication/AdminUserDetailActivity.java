package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminUserDetailActivity extends AppCompatActivity {
    Button openPdfButton; // Button to open the PDF

    String userEmail, userDept,userPg, userUg, userPlus, usertenth, userSkills ,userDob, userMob;
    String userName;

    String pdfData;

    String userId="";

    TextView t1,t2,t3,t4,t5,t6,t7,t8, t9, t10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_detail);
        t1 = findViewById(R.id.textView6);
        t2 = findViewById(R.id.textView7);
        t3 = findViewById(R.id.pgmark);
        t4 = findViewById(R.id.ugmark);
        t5 = findViewById(R.id.plustwomark);
        t6 = findViewById(R.id.tenthmark);
        t7 = findViewById(R.id.textView8);
        t8 = findViewById(R.id.dob);
        t9 = findViewById(R.id.phone);
        t10 = findViewById(R.id.textemail);

        openPdfButton = findViewById(R.id.openPdfButton); // Initialize the button by its ID

        String userId = getIntent().getStringExtra("userId");

        Toast.makeText(this,userId,Toast.LENGTH_LONG).show();

        // Set an OnClickListener to open the PDF when the button is clicked
        openPdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace 'pdfUrl' with the actual URL of the PDF you want to open.
                String pdfUrl = pdfData;

                // Create an Intent to view the PDF using the device's default PDF viewer.
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(pdfUrl), "application/pdf");

                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // Handle the case where there's no app to handle the PDF file.
                    Toast.makeText(getApplicationContext(), "No PDF viewer app found.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Initialize Firebase Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();



        // Replace 'user_id' with the actual user ID you want to fetch data for


        // Fetch the user details and PDF URL from Firestore
        db.collection("users").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            userEmail = document.getString("email");
                            userName = document.getString("name");
                            userDept = document.getString("dept");
                            userPg = document.getString("PGdegree");
                            userUg = document.getString("UGdegree");
                            userPlus = document.getString("plustwo");
                            usertenth = document.getString("tenth");
                            userSkills = document.getString("skills");



                            userDob = document.getString("dob");
                            userMob = document.getString("phone");

                            t1.setText(userName);
                            t2.setText(userEmail);
                            t7.setText(userDept);
                            t3.setText(userPg);
                            t4.setText(userUg);
                            t5.setText(userPlus);
                            t6.setText(usertenth);
                            t8.setText(userDob);
                            t9.setText(userMob);
                            t10.setText(userEmail);

                            pdfData = document.getString("pdfData");

                            // You can use userEmail and userName as needed
                        }

                    }
                });

    }
}