package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;
    private List<CardItem> cardItemList;

    TextView t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t1 = findViewById(R.id.userLogged);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cardItemList = new ArrayList<>(); // Populate this list with Firebase data

        cardAdapter = new CardAdapter(cardItemList,this);
        recyclerView.setAdapter(cardAdapter);


        fetchDataFromFirestore();
        fetchUserFromFirestore();

    }

    private void fetchUserFromFirestore(){
        String loggedInUserId=null;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            loggedInUserId = currentUser.getUid();
            // Now, you have the ID of the currently logged-in user
            // You can proceed to check this ID against the "users" collection
        }


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = db.collection("users");

// Assuming you have the loggedInUserId from the previous step
        DocumentReference userDocRef = usersCollection.document(loggedInUserId);

        userDocRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // User document exists in the "users" collection
                            String userName = documentSnapshot.getString("name");
                            t1.setText(userName);
                        } else {
                            // User document does not exist, handle the case
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors in fetching user data
                    }
                });

    }

    public void signout(View v){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
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
        Toast.makeText(getApplicationContext(),String.valueOf(user.getEmail()),Toast.LENGTH_LONG).show();
        // User is authenticated, proceed with fetching data
        // Initialize Firebase Firestore
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
                    String description = document.getString("date");
                    String firstLetter = "";

                    if (title != null && !title.isEmpty()) {
                        // Check if the title is not empty
                        firstLetter = title.substring(0, 1);
                        firstLetter = firstLetter.toUpperCase();
                    }

                    CardItem cardItem = new CardItem(title, description, documentId, firstLetter);
                    cardItemList.add(cardItem);
                }

                // Notify the adapter that the data has changed
                cardAdapter.notifyDataSetChanged();
            }
        });



    }

//    private TextView textViewData;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
////        textViewData = findViewById(R.id.textViewData);
//
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        // Create a new user with a first and last name
//        // Create a reference to the Firestore collection
//        CollectionReference collectionReference = db.collection("test");
//
//// Create a snapshot listener for real-time updates
//        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
//                if (e != null) {
//                    Log.w(TAG, "Listen failed.", e);
//                    return;
//                }
//                LinearLayout linearLayout = new LinearLayout(getApplicationContext());
//
//                linearLayout.setOrientation(LinearLayout.VERTICAL);
//
//                // Clear the existing UI elements
//                linearLayout.removeAllViews();
//
//                for (QueryDocumentSnapshot document : querySnapshot) {
//                    Log.d(TAG, document.getId() + " => " + document.getData());
//                    String dataField = document.getString("first");
//
//                    // Create a TextView for displaying data
//                    TextView textView = new TextView(getApplicationContext());
//                    textView.setText("Data from Firestore: " + dataField);
//
//                    // Create an Edit Button for each data item
//                    Button editButton = new Button(getApplicationContext());
//                    editButton.setText("Edit");
//
//                    // Attach a click listener to the Edit Button
//                    editButton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            showEditDialog(document, dataField);
//                        }
//                    });
//
//                    // Add the TextView and Edit Button to the LinearLayout
//                    linearLayout.addView(textView);
//                    linearLayout.addView(editButton);
//                }
//
//                if (linearLayout.getChildCount() > 0) {
//                    // Display the data in a ScrollView
//                    ScrollView scrollView = new ScrollView(getApplicationContext());
//                    scrollView.addView(linearLayout);
//                    setContentView(scrollView);
//                } else {
//                    // No documents found
////                    textViewData.setText("No data found in Firestore.");
//                }
//            }
//        });
//
//
//    }
//
//    private void showEditDialog(QueryDocumentSnapshot document, String currentData) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        View editDialogView = getLayoutInflater().inflate(R.layout.edit_dialog_layout, null);
//
//        // Find the EditText and Save Button in the edit dialog layout
//        EditText editDataField = editDialogView.findViewById(R.id.editDataField);
//        Button saveButton = editDialogView.findViewById(R.id.saveButton);
//
//        // Set the current data to the EditText
//        editDataField.setText(currentData);
//
//        // Create the dialog
//        builder.setView(editDialogView);
//        AlertDialog editDialog = builder.create();
//
//        // Handle the save button click
//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Get the updated data from the EditText
//                String updatedData = editDataField.getText().toString();
//
//                // Update the Firestore document with the new data
//                document.getReference().update("first", updatedData)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                // Data updated successfully
//                                // You can update the UI or perform any additional actions here
//                                Toast.makeText(getApplicationContext(), "Data updated successfully", Toast.LENGTH_SHORT).show();
//
//                                // Dismiss the dialog
//                                editDialog.dismiss();
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                // Error updating data
//                                Toast.makeText(getApplicationContext(), "Error updating data", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//            }
//        });
//
//        // Show the dialog
//        editDialog.show();
//    }
}