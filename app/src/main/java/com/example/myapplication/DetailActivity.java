package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class DetailActivity extends AppCompatActivity {
    String docId = "";
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
                                    Glide.with(DetailActivity.this)
                                            .load(imageUrl)
                                            .into(imageView);
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

    public void applyDrive(View v){
        Toast.makeText(this, docId,Toast.LENGTH_LONG ).show();
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
                            Toast.makeText(getApplicationContext(), "succssfully added", Toast.LENGTH_LONG).show();
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
