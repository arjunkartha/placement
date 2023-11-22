package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProfileActivity extends AppCompatActivity {

    Uri pdfUri;

    private static final int REQUEST_PDF_PICK = 1;

    String userId = "";

    EditText nameEditText;
    EditText pgEditText;
    EditText ugEditText;
    EditText plusEditText;
    EditText tenthEditText;
    EditText dept;
    EditText skillEditText, phoneEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        nameEditText = findViewById(R.id.fname);
        pgEditText = findViewById(R.id.pgDegreeEditText);
        ugEditText = findViewById(R.id.degreeEditText);
        plusEditText = findViewById(R.id.plusTwoEditText);
        tenthEditText = findViewById(R.id.tenthEditText);
        dept = findViewById(R.id.dept);
        skillEditText = findViewById(R.id.skillsEditText);
        phoneEditText = findViewById(R.id.phone);
        Button pdfChooser = findViewById(R.id.choosePdfButton);

        Intent intent = getIntent();
        String userid = intent.getStringExtra("userid");
        String email = intent.getStringExtra("email");
        String name = intent.getStringExtra("name");
        String pdfData = intent.getStringExtra("pdfData");
        String PGdegree = intent.getStringExtra("PGdegree");
        String UGdegree = intent.getStringExtra("UGdegree");
        String plustwo = intent.getStringExtra("plustwo");
        String userdept = intent.getStringExtra("dept");
        String tenth = intent.getStringExtra("tenth");
        String skills = intent.getStringExtra("skills");
        String phone = intent.getStringExtra("phone");


        userId = userid;

        nameEditText.setText(name);
        pgEditText.setText(PGdegree);
        ugEditText.setText(UGdegree);
        plusEditText.setText(plustwo);
        tenthEditText.setText(tenth);
        dept.setText(userdept);
        skillEditText.setText(skills);
        phoneEditText.setText(phone);

        pdfChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });
    }

    private void openFilePicker() {
        Intent new_intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        new_intent.addCategory(Intent.CATEGORY_OPENABLE);
        new_intent.setType("application/pdf");

        startActivityForResult(new_intent, REQUEST_PDF_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PDF_PICK && resultCode == RESULT_OK && data != null) {
            pdfUri = data.getData();
            Toast.makeText(getApplicationContext(), "PDF selected successfully", Toast.LENGTH_LONG).show();
        }
    }

    public void updateProfileButton(View v) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Ensure that the EditText fields are not null before accessing their getText() method
        if (nameEditText != null && pgEditText != null && ugEditText != null && plusEditText != null &&
                tenthEditText != null && dept != null && skillEditText != null) {

            db.collection("users").document(userId)
                    .update(
                            "name", nameEditText.getText().toString(),
                            "PGdegree", pgEditText.getText().toString(),
                            "UGdegree", ugEditText.getText().toString(),
                            "plustwo", plusEditText.getText().toString(),
                            "tenth", tenthEditText.getText().toString(),
                            "dept", dept.getText().toString(),
                            "skills", skillEditText.getText().toString(),
                            "phone", phoneEditText.getText().toString()

                    )
                    .addOnSuccessListener(aVoid -> {
                        if (pdfUri != null) {
                            // If a new PDF file is selected, update the pdfData field
                            // Upload the new PDF file to storage or update the URL as per your implementation
                            // For example, if you're using Firebase Storage:
                            // updatePdfInStorage(pdfUri, userid);
                        }

                        Toast.makeText(getApplicationContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplication(), ProfileActivity.class);
                        finish();
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
