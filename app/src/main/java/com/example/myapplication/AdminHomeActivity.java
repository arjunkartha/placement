package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdminCardAdapter cardAdapter;
    private List<CardItem> cardItemList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        recyclerView = findViewById(R.id.recylcerViewAdmin);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cardItemList = new ArrayList<>(); // Populate this list with Firebase data

        cardAdapter = new AdminCardAdapter(cardItemList, this);
        recyclerView.setAdapter(cardAdapter);

        fetchDataFromFirestore();



    }


    public void addDrive(View v){
        Intent addIntent = new Intent(AdminHomeActivity.this, DriveAddActivity.class);
        startActivity(addIntent);
    }


    public void signout(View v){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(AdminHomeActivity.this,LoginActivity.class);
        startActivity(intent);
    }


    private void fetchDataFromFirestore() {
        // Check if the user is authenticated
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // User is not logged in, do not fetch data
            // You can show a login screen or take appropriate action here
            return;
        }
//        Toast.makeText(getApplicationContext(), String.valueOf(user.getEmail()), Toast.LENGTH_LONG).show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the Firestore collection where your data is stored
        CollectionReference collectionRef = db.collection("drives");

        // Add a snapshot listener to listen for real-time changes
        collectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    // Handle errors here
                    Log.e("Firestore", "Error: " + e.getMessage());
                    return;
                }

                cardItemList.clear(); // Clear the list to avoid duplicates

                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    // Parse the data from Firestore and create CardItem objects
                    String documentId = document.getId();
                    String title = document.getString("cname");

                    String firstLetter = "";
                    String imageUrl = document.getString("image");


                    String date = document.getString("date");
                    String location = document.getString("location");
                    String jtime = document.getString("jtime");
                    String sal = document.getString("salary");

                    if (title != null && !title.isEmpty()) {
                        // Check if the title is not empty
                        firstLetter = title.substring(0, 1); // Get the first character
                        firstLetter = firstLetter.toUpperCase(); // Convert to uppercase
                    }


                    CardItem cardItem = new CardItem(title, date, firstLetter,location,jtime,sal, documentId);
                    cardItemList.add(cardItem);
                }

                // Notify the adapter that the data has changed
                cardAdapter.notifyDataSetChanged();
            }
        });
    }
}