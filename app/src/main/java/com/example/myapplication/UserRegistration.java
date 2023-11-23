package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class UserRegistration extends AppCompatActivity {
    EditText emailEditText, passwordEditText, fullNameEditText, departmentText,tenthEditText,skillsEditText, degreeEditText, plusTwoEditText, pgdegreeEditText, phoneEditText,dobEditText, collegeEditText;
    Button reg, choosePdfButton;
    Uri pdfUri;
    private FirebaseAuth mAuth;
    private static final int REQUEST_PDF_PICK = 1;


    String email="" ;
    String password="" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.pass);
        fullNameEditText = findViewById(R.id.fname);
        departmentText = findViewById(R.id.dept);
        degreeEditText = findViewById(R.id.degreeEditText);
        pgdegreeEditText = findViewById(R.id.pgDegreeEditText);
        plusTwoEditText = findViewById(R.id.plusTwoEditText);
        tenthEditText = findViewById(R.id.tenthEditText);
        skillsEditText = findViewById(R.id.skillsEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        dobEditText = findViewById(R.id.dob);
        collegeEditText = findViewById(R.id.college);

        reg = findViewById(R.id.registerButton);
        choosePdfButton = findViewById(R.id.choosePdfButton);

        mAuth = FirebaseAuth.getInstance();

        choosePdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");

        startActivityForResult(intent, REQUEST_PDF_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PDF_PICK && resultCode == RESULT_OK && data != null) {
            pdfUri = data.getData();
            Toast.makeText(getApplicationContext(), "PDF selected successfully", Toast.LENGTH_LONG).show();
        }
    }

    public void registerNewUser() {
       email = emailEditText.getText().toString();
       password = passwordEditText.getText().toString();
        // ... (your existing code)

        // Email and password validation
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // Email is empty or not valid
            Toast.makeText(getApplicationContext(), "Please enter a valid email address", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            // Password is empty or less than 6 characters
            Toast.makeText(getApplicationContext(), "Please enter a password with at least 6 characters", Toast.LENGTH_LONG).show();
            return;
        }
         email = emailEditText.getText().toString();
         password = passwordEditText.getText().toString();
        String fullName = fullNameEditText.getText().toString();
        String department = departmentText.getText().toString();
        String degree = degreeEditText.getText().toString();
        String pgdegree = pgdegreeEditText.getText().toString();
        String plustwo = plusTwoEditText.getText().toString();
        String tenth = tenthEditText.getText().toString();
        String skills = skillsEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String dob =dobEditText.getText().toString();
        String college = collegeEditText.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference userRef = db.collection("users").document(user.getUid());

                            Map<String, Object> userData = new HashMap<>();
                            userData.put("name", fullName);
                            userData.put("role", "student");
                            userData.put("email", email);
                            userData.put("dept",department);
                            userData.put("UGdegree",degree);
                            userData.put("PGdegree",pgdegree);
                            userData.put("plustwo",plustwo);
                            userData.put("tenth",tenth);
                            userData.put("skills",skills);
                            userData.put("phone",phone);
                            userData.put("dob",dob);
                            userData.put("college",college);
                            if (pdfUri != null) {
                                // Store the PDF file in Firebase Storage
                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                StorageReference storageRef = storage.getReference();
                                StorageReference pdfRef = storageRef.child("pdfs/" + user.getUid() + ".pdf");

                                pdfRef.putFile(pdfUri)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                // Get the download URL for the PDF file
                                                pdfRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri downloadUri) {
                                                        // Store the PDF download URL in Firestore
                                                        userData.put("pdfData", downloadUri.toString());

                                                        // Save the user data with the PDF reference to Firestore
                                                        userRef.set(userData)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_LONG).show();
                                                                        Intent intent = new Intent(UserRegistration.this, LoginActivity.class);
                                                                        startActivity(intent);
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(getApplicationContext(), "Firestore error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                                    }
                                                                });
                                                    }
                                                });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), "Storage error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                            } else {
                                // Save the user data without a PDF reference
                                userRef.set(userData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(UserRegistration.this, LoginActivity.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), "Firestore error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Authentication error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void gotosignin(View v){
        Intent intent = new Intent(UserRegistration.this, LoginActivity.class);
        finish();
        startActivity(intent);
    }
}



