package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class UserRegistration extends AppCompatActivity {
    EditText emailEditText, passwordEditText, fullNameEditText;
    Button reg;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.pass);
        fullNameEditText = findViewById(R.id.fname);
        reg = findViewById(R.id.registerButton);
        // Initialize Firebase Authentication

        mAuth = FirebaseAuth.getInstance();

        // Register the user with email and password

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });


    }


        public void registerNewUser (){
                String email = emailEditText.getText().toString(); // Retrieve email
                String password = passwordEditText.getText().toString(); // Retrieve password
                String fullName = fullNameEditText.getText().toString(); // Retrieve full name




            Toast.makeText(getApplicationContext(),email,Toast.LENGTH_LONG).show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Registration success, get the user object
                                FirebaseUser user = mAuth.getCurrentUser();

                                // Create a user document in Firestore
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                DocumentReference userRef = db.collection("users").document(user.getUid());

                                // Add user details to Firestore
                                Map<String, Object> userData = new HashMap<>();
                                userData.put("name", fullName);
                                // Add other details as needed

                                userRef.set(userData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(UserRegistration.this,LoginActivity.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), "Firestore error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                            } else {
                                Toast.makeText(getApplicationContext(), "Authentication error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                                Toast.makeText(getApplicationContext(), "not working", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

