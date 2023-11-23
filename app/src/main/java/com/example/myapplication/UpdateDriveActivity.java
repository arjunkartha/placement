package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateDriveActivity extends AppCompatActivity {
    private Button datePickerButton;

    String selectedJobType = "";
    String selectedDate = "";

    EditText roleInput, minmarkInput,locationInput, imgInput,   salInput, reqInput,cemailInput, descInput,  cnameInput;

    Spinner jtimeInput;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String docId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_drive);

        Intent intent = getIntent();
        String documentId = intent.getStringExtra("documentId");
        docId = documentId;
        datePickerButton = findViewById(R.id.endDate);
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        Spinner jobTypeSpinner = findViewById(R.id.jobTypeSpinner);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.job_types, android.R.layout.simple_spinner_item);

// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Apply the adapter to the spinner
        jobTypeSpinner.setAdapter(adapter);


        jobTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected job type
                selectedJobType = (String) jobTypeSpinner.getSelectedItem();


                // You can use the selectedJobType in your code
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here or provide a default selection
            }
        });



        cnameInput = findViewById(R.id.cname);
        descInput = findViewById(R.id.desc);
        cemailInput = findViewById(R.id.cemail);
        reqInput = findViewById(R.id.req);
//        endDateInput = findViewById(R.id.endDate);
        salInput = findViewById(R.id.sal);
        imgInput = findViewById(R.id.img);
        locationInput = findViewById(R.id.locationField);
        minmarkInput = findViewById(R.id.mark);
        roleInput = findViewById(R.id.role);
        jtimeInput = findViewById(R.id.jobTypeSpinner);




        // Reference to the specific document in Firestore
        db.collection("drives") // Replace with your Firestore collection name
                .document(docId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String title = document.getString("cname");
                                String date = document.getString("date");
                                String description = document.getString("cdesc");
                                String imageUrl = document.getString("image"); // Assuming "image" is the field for image links
                                String salary = document.getString("salary");
                                String cemail = document.getString("cemail");

                                String time = document.getString("jtime");
                                String location = document.getString("location");
                                String requirements = document.getString("req");
                                String role = document.getString("role");
                                String minmark = document.getString("minmark");



                                cnameInput.setText(title);
                                descInput.setText(description);
                                cemailInput.setText(cemail);
                                reqInput.setText(requirements);
                                salInput.setText(salary);
                                imgInput.setText(imageUrl);
                                locationInput.setText(location);
                                minmarkInput.setText(minmark);
                                roleInput.setText(role);
                                datePickerButton.setText(date);
                                selectedDate = date;


                                if (time.equals("Full Time")) {

                                    jtimeInput.setSelection(0);
                                } else if (time.equals("Part Time")) {
                                    jtimeInput.setSelection(1);
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


    public void updateDriveButton(View v){

        db.collection("drives").document(docId)
                .update(
                        "cname",cnameInput.getText().toString(),
                        "cdesc",descInput.getText().toString(),
                        "cemail", cemailInput.getText().toString(),
                        "req",  reqInput.getText().toString(),
                        "salary",  salInput.getText().toString(),
                        "image",  imgInput.getText().toString(),
                        "location",   locationInput.getText().toString(),
                        "minmark" ,   minmarkInput.getText().toString(),
                         "role",  roleInput.getText().toString(),
                        "jtime",selectedJobType,
                        "date",  selectedDate
                )
                .addOnSuccessListener(aVoid -> {

                    Toast.makeText(getApplicationContext(), "Drive updated successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplication(), AdminDetailActivity.class);
                    intent.putExtra("documentId", docId);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Failed to update Drive", Toast.LENGTH_SHORT).show();
                });
    }









    private void showDatePickerDialog() {
        // Get the current date
        int year = 2023; // Initial year
        int month = 0;   // Initial month (January is 0)
        int day = 1;     // Initial day

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Handle the selected date here
                selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                datePickerButton.setText(selectedDate);

                // Display or use the selected date as needed
            }
        }, year, month, day);

        datePickerDialog.show();
    }
}