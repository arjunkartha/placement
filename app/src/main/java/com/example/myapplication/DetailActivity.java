package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    String docId = "";

    String tenth="",plustwo="",ugdegree="",pgscore="",minmark="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Retrieve the data ID passed from the previous activity

        Intent intent = getIntent();
        String documentId = intent.getStringExtra("documentId");
        docId = documentId;


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
                                String role = document.getString("role");

                                minmark = document.getString("minmark");

                                TextView textViewRole = findViewById(R.id.textViewRoleDetail);
                                TextView textViewTitle = findViewById(R.id.textViewTitleDetail);
                                TextView textViewDate = findViewById(R.id.textViewDate);
                                TextView textViewDescription = findViewById(R.id.textViewDescriptionDetail);
                                ImageView imageView = findViewById(R.id.imageView);
                                TextView t1 = (TextView)findViewById(R.id.t1);
                                TextView t2 = (TextView)findViewById(R.id.t2);
                                TextView t3 = (TextView)findViewById(R.id.t3);
                                TextView req = (TextView) findViewById(R.id.requirements);
                                TextView markreq = (TextView) findViewById(R.id.markReq);

                                textViewRole.setText(role);
                                textViewTitle.setText("at "+title);
                                textViewDate.setText(date);
                                textViewDescription.setText(description);
                                t1.setText(salary);
                                t2.setText(time);
                                t3.setText(location);
                                req.setText(requirements);
                                markreq.setText(minmark);
                                // Load and display the image using Glide
                                if (imageUrl != null && !imageUrl.isEmpty()) {
                                    Glide.with(DetailActivity.this)
                                            .load(imageUrl)
                                            .into(imageView);
                                }

                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                if (currentUser != null) {
                                    String userId = currentUser.getUid();
                                    checkIfUserApplied(userId, docId);
                                }


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

    // Define a method to check if the user has applied for the drive
    private void checkIfUserApplied(final String userId, final String driveDocumentId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userDocumentRef = db.collection("users").document(userId);

        userDocumentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                         pgscore = document.getString("PGdegree");
                         ugdegree = document.getString("UGdegree");
                         plustwo = document.getString("plustwo");
                         tenth = document.getString("tenth");

                        // User document exists, check if "applied" array contains the driveDocumentId
                        if (document.contains("applied")) {
                            List<String> appliedList = (List<String>) document.get("applied");
                            if (appliedList != null && appliedList.contains(driveDocumentId)) {
                                // User has applied, change the button text to "Applied"
                                Button applyButton = findViewById(R.id.button); // Replace with the ID of your button
                                applyButton.setText("Applied");
                                applyButton.setEnabled(false); // Optionally disable the button
                            }
                        }
                    }
                }
            }
        });
    }
    public void applyDrive(View v) {
        int pgscoreValue = Integer.parseInt(pgscore);
        int ugdegreeValue = Integer.parseInt(ugdegree);
        int plustwoValue = Integer.parseInt(plustwo);
        int tenthValue = Integer.parseInt(tenth);
        int minmarkValue = Integer.parseInt(minmark);
        if(Integer.parseInt(pgscore) >= Integer.parseInt(minmark) && Integer.parseInt(ugdegree) >= Integer.parseInt(minmark) && Integer.parseInt(plustwo) >= Integer.parseInt(minmark) && Integer.parseInt(tenth) >= Integer.parseInt(minmark)){
            new AlertDialog.Builder(this)
                    .setTitle("Apply Confirmation")
                    .setMessage("Are you sure you want to apply for this drive?")
                    .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // User clicked the "Apply" button
                            // Add your code to apply for the drive here
                            applyForDrive();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // User clicked the "Cancel" button
                            // You can add any handling for canceling the operation here
                        }
                    })
                    .show();
        }else{
            String errorMessage = "Sorry, you are not eligible for this drive because ";

            if (pgscoreValue < minmarkValue) {
                errorMessage += "your PGScore is " + pgscoreValue + " and it should be at least " + minmarkValue + ". ";
            }

            if (ugdegreeValue < minmarkValue) {
                errorMessage += "your UG Degree score is " + ugdegreeValue + " and it should be at least " + minmarkValue + ". ";
            }

            if (plustwoValue < minmarkValue) {
                errorMessage += "your Plus Two score is " + plustwoValue + " and it should be at least " + minmarkValue + ". ";
            }

            if (tenthValue < minmarkValue) {
                errorMessage += "your Tenth score is " + tenthValue + " and it should be at least " + minmarkValue + ". ";
            }
            new AlertDialog.Builder(this)
                    .setTitle("Apply Confirmation")
                    .setMessage(errorMessage)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // User clicked the "Ok" button
                            // You can add any handling for the "Ok" button here
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // User clicked the "Cancel" button
                            // You can add any handling for canceling the operation here
                        }
                    })
                    .show();
        }

    }

    public void applyForDrive(){
//        Toast.makeText(this, docId,Toast.LENGTH_LONG ).show();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            // Reference to the specific document for the drive
            String driveDocumentId = docId; // Replace with the actual document ID
            DocumentReference driveDocumentRef = db.collection("drives").document(driveDocumentId);

            // Update the "applicants" array in the document
            driveDocumentRef.update("applicants", FieldValue.arrayUnion(userId))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // The user's ID has been added to the "applicants" array.
                            // You can perform any additional actions here if needed.
                            Toast.makeText(getApplicationContext(), "succssfully Applied", Toast.LENGTH_LONG).show();
                            DocumentReference userDocumentRef = db.collection("users").document(userId);
                            userDocumentRef.update("applied", FieldValue.arrayUnion(driveDocumentId))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // The driveDocumentId has been added to the "applied" array in the user's document.
                                    Toast.makeText(getApplicationContext(), "User Updated", Toast.LENGTH_LONG).show();
                                    Button applyButton = findViewById(R.id.button); // Replace with the ID of your button
                                    applyButton.setText("Applied");
                                    applyButton.setEnabled(false);
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("Firestore", "Error updating user's applied array: " + e.getMessage());
                                            Toast.makeText(getApplicationContext(), "Unsuccessful", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle any errors that occur during the update.
                            Log.e("Firestore", "Error updating applicants: " + e.getMessage());
                            Toast.makeText(getApplicationContext(), "unsuccssfull", Toast.LENGTH_LONG).show();

                        }
                    });
        } else {
            // Handle the case where the user is not authenticated.
            // You can prompt the user to log in or handle this situation as per your app's requirements.
        }
    }
}
