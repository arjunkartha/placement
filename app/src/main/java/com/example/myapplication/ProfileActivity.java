package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class ProfileActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Button openPdfButton; // Button to open the PDF

    String userEmail;
    String userName;

    String pdfData;

    String userId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        openPdfButton = findViewById(R.id.openPdfButton); // Initialize the button by its ID

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

        if (currentUser != null) {
            userId = currentUser.getUid();
            // Now, you have the ID of the currently logged-in user
            // You can proceed to check this ID against the "users" collection
        }

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
                            pdfData = document.getString("pdfData");
                            // You can use userEmail and userName as needed
                        }

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




    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
