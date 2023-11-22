package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Button openPdfButton; // Button to open the PDF

    String userEmail, userDept,userPg, userUg, userPlus, usertenth, userskills, userDob, userMob;
    String userName;

    String pdfData;

    String userId="";

    TextView t1, t2, t3, t4, t5, t6, t7, t8, t9, t10;

    int totalDrives = 0;
    int completedDrives = 0;

    private ImageView profileImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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

        Button editActionButton = findViewById(R.id.editActionButton);


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        openPdfButton = findViewById(R.id.openPdfButton); // Initialize the button by its ID

//        profileImageView = findViewById(R.id.imageView3);

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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();

                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();
                String email = profile.getEmail();
                Uri photoUrl = profile.getPhotoUrl();
//                Glide.with(this)
//                    .load(photoUrl)
//                    .into(profileImageView);

            };
        }

        if (currentUser != null) {
            userId = currentUser.getUid();

//            String profilePicUrl = currentUser.getPhotoUrl().toString();
//            Toast.makeText(this,profilePicUrl,Toast.LENGTH_LONG).show();
            // Use a library like Glide to load the image into an ImageView
//            Glide.with(this)
//                    .load(profilePicUrl)
//                    .into(profileImageView);
            // Now, you have the ID of the currently logged-in user
            // You can proceed to check this ID against the "users" collection
        }

        // Replace 'user_id' with the actual user ID you want to fetch data for
        List<String> driveInfoList = new ArrayList<>();

// Assuming you have a Button with id viewAppliedDrivesButton


        // Fetch the user details and PDF URL from Firestore
        db.collection("users").document(userId)
                .get()
                .addOnCompleteListener(userTask -> {
                    if (userTask.isSuccessful()) {
                        DocumentSnapshot userDocument = userTask.getResult();
                        if (userDocument.exists()) {
                            userEmail = userDocument.getString("email");
                            userName = userDocument.getString("name");
                            pdfData = userDocument.getString("pdfData");
                            userPg = userDocument.getString("PGdegree");

                            userDept = userDocument.getString("dept");
                            userUg = userDocument.getString("UGdegree");
                            userPlus = userDocument.getString("plustwo");
                            usertenth = userDocument.getString("tenth");
                            userskills = userDocument.getString("skills");
                            userDob = userDocument.getString("dob");
                            userMob = userDocument.getString("phone");

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
                            // You can use userEmail and userName as needed

                            List<String> driveIds = (List<String>) userDocument.get("applied");

                            if (driveIds != null && !driveIds.isEmpty()) {
                                totalDrives = driveIds.size(); // Set the total number of drives

                                // Clear the list before populating it with new data
                                driveInfoList.clear();

                                // Set up the button click listener

                                    // Iterate through each drive ID and fetch corresponding document from "drives" collection
                                    for (String driveId : driveIds) {
                                        db.collection("drives").document(driveId)
                                                .get()
                                                .addOnCompleteListener(driveTask -> {
                                                    if (driveTask.isSuccessful()) {
                                                        DocumentSnapshot driveDocument = driveTask.getResult();
                                                        if (driveDocument.exists()) {
                                                            String cname = driveDocument.getString("cname");
                                                            String cemail = driveDocument.getString("cemail");
                                                            // Do something with cname, like displaying it
                                                            // For example, assuming you have a TextView t3 for displaying cname:
                                                            // t3.setText(cname);
                                                            driveInfoList.add(cname + "\n" + cemail);

                                                            // Increment the counter for completed drives
                                                            completedDrives++;

                                                            // Check if all drives have been fetched
                                                            if (completedDrives == totalDrives) {
                                                                // All drives have been fetched, navigate to the ListActivity
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
                                                        }
                                                    } else {
                                                        // Handle errors while fetching drive document
                                                    }
                                                });
                                    }

                            }
                        }
                    } else {
                        // Handle errors while fetching user document
                    }
                });





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


        editActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the EditProfileActivity
                Intent editProfileIntent = new Intent(ProfileActivity.this, EditProfileActivity.class);

                // Pass the data to the EditProfileActivity
                editProfileIntent.putExtra("userid", userId);
                editProfileIntent.putExtra("email", userEmail);
                editProfileIntent.putExtra("name", userName);
                editProfileIntent.putExtra("pdfData", pdfData);
                editProfileIntent.putExtra("PGdegree", userPg);
                editProfileIntent.putExtra("dept", userDept);

                editProfileIntent.putExtra("UGdegree", userUg);
                editProfileIntent.putExtra("plustwo", userPlus);
                editProfileIntent.putExtra("tenth", usertenth);
                editProfileIntent.putExtra("skills", userskills);
                editProfileIntent.putExtra("dob",userDob);
                editProfileIntent.putExtra("phone",userMob);

                // Start the EditProfileActivity
                startActivity(editProfileIntent);
            }
        });



    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
