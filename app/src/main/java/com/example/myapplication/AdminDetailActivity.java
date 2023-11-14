package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class AdminDetailActivity extends AppCompatActivity {
    String docId = "";
    private FirebaseFirestore db;
    Button openMenuButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_detail);




        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Retrieve the data ID passed from the previous activity
        Intent intent = getIntent();
        String documentId = intent.getStringExtra("documentId");
        docId = documentId;

        openMenuButton = findViewById(R.id.openMenuButton);


        openMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a PopupMenu
                PopupMenu popupMenu = new PopupMenu(AdminDetailActivity.this, view);

                // Inflate the menu resource
                popupMenu.getMenuInflater().inflate(R.menu.menu_admin_detail, popupMenu.getMenu());

                // Set an item click listener for the popup menu
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.action_delete_drive) {
                            // Handle the delete drive action here
                            // You can display a confirmation dialog and then delete the drive.
                            // For example, you can show a popup confirmation dialog.

                            // Create a popup dialog to confirm the deletion

                            AlertDialog.Builder builder = new AlertDialog.Builder(AdminDetailActivity.this);
                            builder.setTitle("Confirm Delete");
                            builder.setMessage("Are you sure you want to delete this drive?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Perform the deletion logic here
                                    // You can use Firestore to delete the drive document.
                                    // Make sure to replace 'docId' with the actual document ID.
                                    db.collection("drives").document(docId)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Drive deleted successfully
                                                    // You can also navigate back to the previous activity or perform any other actions.
                                                    Intent goBack = new Intent(AdminDetailActivity.this, AdminHomeActivity.class);
                                                    startActivity(goBack);
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Handle the deletion failure
                                                    // You can show an error message or take appropriate action.
                                                }
                                            });
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.create().show();

                            return true;
                        } else {
                            return false;
                        }
                    }
                });

                // Show the popup menu
                popupMenu.show();
            }
        });


        // Reference to Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the specific document in Firestore
        db.collection("drives") // Replace with your Firestore collection name
                .document(documentId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Data exists in Firestore, retrieve and display it
                                String title = document.getString("cname");
                                String date = document.getString("date");
                                String description = document.getString("cdesc");
                                String imageUrl = document.getString("image"); // Assuming "image" is the field for image links
                                String salary = document.getString("salary");
                                String time = document.getString("jtime");
                                String location = document.getString("location");
                                String requirements = document.getString("req");

                                TextView textViewTitle = findViewById(R.id.textViewTitleDetail);
                                TextView textViewDate = findViewById(R.id.textViewDate);
                                TextView textViewDescription = findViewById(R.id.textViewDescriptionDetail);
                                ImageView imageView = findViewById(R.id.imageView);
                                TextView t1 = (TextView)findViewById(R.id.t1);
                                TextView t2 = (TextView)findViewById(R.id.t2);
                                TextView t3 = (TextView)findViewById(R.id.t3);
                                TextView req = (TextView) findViewById(R.id.requirements);
                                textViewTitle.setText(title);
                                textViewDate.setText(date);
                                textViewDescription.setText(description);
                                t1.setText(salary);
                                t2.setText(time);
                                t3.setText(location);
                                req.setText(requirements);

                                // Load and display the image using Glide
                                if (imageUrl != null && !imageUrl.isEmpty()) {
                                    Glide.with(AdminDetailActivity.this)
                                            .load(imageUrl)
                                            .into(imageView);
                                }

                                // Retrieve and display the list of applicants
                                List<String> applicants = (List<String>) document.get("applicants");
                                // ...
                                // ...
                                // ...

                                if (applicants != null && !applicants.isEmpty()) {
                                    TableLayout applicantsTable = findViewById(R.id.applicantsTable);
                                    int rowNumber = 1;

                                    for (String applicantId : applicants) {
                                        final int currentRow = rowNumber; // Create a final variable for the current row number

                                        DocumentReference userRef = db.collection("users").document(applicantId);
                                        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot userDocumentSnapshot) {
                                                if (userDocumentSnapshot.exists()) {
                                                    String userName = userDocumentSnapshot.getString("name");
                                                    String userEmail = userDocumentSnapshot.getString("email");
                                                    String userRole = userDocumentSnapshot.getString("role");

//                                                    Toast.makeText(AdminDetailActivity.this,userName,Toast.LENGTH_LONG).show();
//                                                    Toast.makeText(AdminDetailActivity.this,String.valueOf(userEmail),Toast.LENGTH_LONG).show();


                                                    if (userName != null && userEmail != null) {
                                                        TableRow row = new TableRow(AdminDetailActivity.this);

                                                        // Set layout parameters to add space between columns
                                                        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                                                        params.setMargins(10, 0, 10, 0); // Adjust margins as needed

                                                        TextView numberTextView = new TextView(AdminDetailActivity.this);
                                                        numberTextView.setText(String.valueOf(currentRow));
                                                        numberTextView.setLayoutParams(params);
                                                        numberTextView.setTextSize(18);

                                                        TextView nameTextView = new TextView(AdminDetailActivity.this);
                                                        nameTextView.setText(userName);
                                                        nameTextView.setLayoutParams(params);

                                                        TextView emailTextView = new TextView(AdminDetailActivity.this);
                                                        emailTextView.setText(userEmail);
                                                        emailTextView.setLayoutParams(params);

                                                        // Add the views to the row
                                                        row.addView(numberTextView);
                                                        row.addView(nameTextView);
                                                        row.addView(emailTextView);

                                                        // Add the row to the table
                                                        applicantsTable.addView(row);
                                                        row.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                // When the row is clicked, launch the UserDetailActivity
                                                                Intent userDetailIntent = new Intent(AdminDetailActivity.this, UserDetailActivity.class);
                                                                // Pass user details to the UserDetailActivity
                                                                userDetailIntent.putExtra("userName", userName);
                                                                userDetailIntent.putExtra("userEmail", userEmail);
                                                                userDetailIntent.putExtra("userRole", userRole);
                                                                userDetailIntent.putExtra("userId",applicantId);
                                                                // Start the UserDetailActivity
                                                                startActivity(userDetailIntent);
                                                            }
                                                        });
                                                    }

                                                }
                                            }
                                        });

                                        // Increment the row number for the next iteration
                                        rowNumber++;
                                    }
                                }

// ...

                            } else {
                                // Document doesn't exist
                                // Handle this case, e.g., show an error message
                            }
                        } else {
                            // Handle failures, e.g., show an error message
                        }
                    }
                });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin_detail, menu);
        return true;
    }


}
