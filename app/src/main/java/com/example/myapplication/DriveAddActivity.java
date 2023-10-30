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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DriveAddActivity extends AppCompatActivity {

    private TextInputEditText cnameInput;
    private TextInputEditText descInput;
    private TextInputEditText cemailInput;
    private TextInputEditText passInput;
//    private TextInputEditText endDateInput;
    private TextInputEditText salInput;
    private TextInputEditText imgInput;

    private TextInputEditText locationInput;


    private Button datePickerButton;
    String selectedJobType = "";
    String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_add);

        datePickerButton = findViewById(R.id.endDate);

        // Set a click listener for the button
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

        // Initialize Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Initialize input fields
        cnameInput = findViewById(R.id.cname);
        descInput = findViewById(R.id.desc);
        cemailInput = findViewById(R.id.cemail);
        passInput = findViewById(R.id.pass);
//        endDateInput = findViewById(R.id.endDate);
        salInput = findViewById(R.id.sal);
        imgInput = findViewById(R.id.img);
        locationInput = findViewById(R.id.locationField);


        // Initialize "Add Drive" button and set its click listener
        Button addDriveButton = findViewById(R.id.registerButton);
        addDriveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get data from form fields
                String companyName = cnameInput.getText().toString();
                String description = descInput.getText().toString();
                String supportEmail = cemailInput.getText().toString();
                String requirements = passInput.getText().toString();
//                String endDate = endDateInput.getText().toString();
                String salary = salInput.getText().toString();
                String imageUrl = imgInput.getText().toString();
                String location = locationInput.getText().toString();

                // Create a Drive object
                Drive drive = new Drive();
                drive.cname = companyName;
                drive.cdesc = description;
                drive.cemail = supportEmail;
                drive.req = requirements;
                drive.date = selectedDate;
                drive.salary = salary;
                drive.image = imageUrl;
                drive.location = location;
                drive.jtime = selectedJobType;

                // Add the Drive object to the "drives" collection with an auto-generated document ID
                DocumentReference documentReference = db.collection("drives").document();
                documentReference.set(drive)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent intent = new Intent(DriveAddActivity.this,AdminHomeActivity.class);

                                startActivity(intent);

                                // Drive added successfully
                                Toast.makeText(DriveAddActivity.this, "Drive added successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Error occurred while adding the drive
                                Toast.makeText(DriveAddActivity.this, "Error adding drive: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
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