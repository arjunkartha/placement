package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class AdminDetailActivity extends AppCompatActivity {
    String docId = "";
    private FirebaseFirestore db;
    Button openMenuButton ;

    List<ApplicantData> applicantDataList = new ArrayList<>();

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
                        } else if (menuItem.getItemId() == R.id.action_update_drive) {
                                Intent new_intent = new Intent(AdminDetailActivity.this,UpdateDriveActivity.class);
                                new_intent.putExtra("documentId", docId);
                                startActivity(new_intent);

                            return true;
                            }else if (menuItem.getItemId() == R.id.action_export_drive) {
                            WritableWorkbook workbook = null;
                            try {
                                // Change the file path to save in the external files directory
                                File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                                File file = new File(directory, "applicant_data.xls");

                                workbook = Workbook.createWorkbook(file);
                                // Create a new sheet
                                WritableSheet sheet = workbook.createSheet("Applicant Data", 0);

                                // Add headers
                                sheet.addCell(new Label(0, 0, "Name"));
                                sheet.addCell(new Label(1, 0, "Email"));
                                sheet.addCell(new Label(2, 0, "Role"));
                                sheet.addCell(new Label(3, 0, "Applicant ID"));
                                sheet.addCell(new Label(4, 0, "DOB"));
                                sheet.addCell(new Label(5, 0, "Phone"));
                                sheet.addCell(new Label(6, 0, "College"));
                                sheet.addCell(new Label(7, 0, "Department"));
                                sheet.addCell(new Label(8, 0, "PG Marks"));
                                sheet.addCell(new Label(9, 0, "UG Marks"));
                                sheet.addCell(new Label(10, 0, "Plus Two Marks"));
                                sheet.addCell(new Label(11, 0, "Tenth Marks"));
                                sheet.addCell(new Label(12, 0, "Resume"));
                                sheet.addCell(new Label(13, 0, "Skills"));

// Add data
                                int rowNum = 1;
                                for (ApplicantData applicantData : applicantDataList) {
                                    sheet.addCell(new Label(0, rowNum, applicantData.getName()));
                                    sheet.addCell(new Label(1, rowNum, applicantData.getEmail()));
                                    sheet.addCell(new Label(2, rowNum, applicantData.getRole()));
                                    sheet.addCell(new Label(3, rowNum, applicantData.getApplicantId()));
                                    sheet.addCell(new Label(4, rowNum, applicantData.getUserDob()));
                                    sheet.addCell(new Label(5, rowNum, applicantData.getUserPhone()));
                                    sheet.addCell(new Label(6, rowNum, applicantData.getUserCollege()));
                                    sheet.addCell(new Label(7, rowNum, applicantData.getUserDept()));
                                    sheet.addCell(new Label(8, rowNum, applicantData.getUserPgMark()));
                                    sheet.addCell(new Label(9, rowNum, applicantData.getUserUgMark()));
                                    sheet.addCell(new Label(10, rowNum, applicantData.getUserPlusTwoMark()));
                                    sheet.addCell(new Label(11, rowNum, applicantData.getUserTenthMark()));
                                    sheet.addCell(new Label(12, rowNum, applicantData.getUserResume()));
                                    sheet.addCell(new Label(13, rowNum, applicantData.getUserSkills()));
                                    rowNum++;
                                }

                                // Show a toast message if export is successful
                                Toast.makeText(AdminDetailActivity.this, "Excel file exported successfully to: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
                                e.printStackTrace();
                                // Show a toast message if there is an error
                                Toast.makeText(AdminDetailActivity.this, "Error exporting Excel file", Toast.LENGTH_SHORT).show();

                            } finally {
                                if (workbook != null) {
                                    try {
                                        workbook.write();
                                        workbook.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            return true;
                        }else {
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
                                String minmark = document.getString("minmark");
                                String role = document.getString("role");

                                TextView textViewTitle = findViewById(R.id.textViewTitleDetail);
                                TextView textViewDate = findViewById(R.id.textViewDate);
                                TextView textViewDescription = findViewById(R.id.textViewDescriptionDetail);
                                ImageView imageView = findViewById(R.id.imageView);
                                TextView t1 = (TextView) findViewById(R.id.t1);
                                TextView t2 = (TextView) findViewById(R.id.t2);
                                TextView t3 = (TextView) findViewById(R.id.t3);
                                TextView req = (TextView) findViewById(R.id.requirements);
                                TextView markReq = (TextView) findViewById(R.id.markReq);
                                TextView roleInput = (TextView) findViewById(R.id.textViewRoleDetail);
                                roleInput.setText(role);
                                textViewTitle.setText(title);
                                textViewDate.setText(date);
                                textViewDescription.setText(description);
                                t1.setText(salary);
                                t2.setText(time);
                                t3.setText(location);
                                req.setText(requirements);
                                markReq.setText("A Cutt-off of " + minmark + "% can only apply");

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
                                                    String userPgMark = userDocumentSnapshot.getString("PGdegree");
                                                    String userUgMark = userDocumentSnapshot.getString("UGdegree");
                                                    String userPlusTwoMark = userDocumentSnapshot.getString("plustwo");
                                                    String userTenthMark = userDocumentSnapshot.getString("tenth");
                                                    String userPhone = userDocumentSnapshot.getString("phone");
                                                    String userResume = userDocumentSnapshot.getString("pdfData");
                                                    String userDob = userDocumentSnapshot.getString("dob");
                                                    String userDept = userDocumentSnapshot.getString("dept");
                                                    String userSkills = userDocumentSnapshot.getString("skills");
                                                    String userCollege = userDocumentSnapshot.getString("college");


//
                                                    ApplicantData applicantData = new ApplicantData(userName, userEmail, userRole, applicantId, userDob, userPhone, userCollege, userDept, userPgMark, userUgMark, userPlusTwoMark, userTenthMark, userResume, userSkills);
                                                    applicantDataList.add(applicantData);

                                                    if (userName != null && userEmail != null) {
                                                        TableRow row = new TableRow(AdminDetailActivity.this);

                                                        // Set layout parameters to add space between columns
                                                        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                                                        params.setMargins(10, 0, 2, 30); // Adjust margins as needed

                                                        TextView numberTextView = new TextView(AdminDetailActivity.this);
                                                        numberTextView.setText(String.valueOf(currentRow));
                                                        numberTextView.setLayoutParams(params);
                                                        numberTextView.setTextSize(16);

                                                        TextView nameTextView = new TextView(AdminDetailActivity.this);
                                                        nameTextView.setText(userName);
                                                        nameTextView.setLayoutParams(params);
                                                        nameTextView.setTextSize(16);

                                                        TextView emailTextView = new TextView(AdminDetailActivity.this);
                                                        emailTextView.setText(userEmail);
                                                        emailTextView.setLayoutParams(params);
                                                        emailTextView.setTextSize(16);
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
                                                                Intent userDetailIntent = new Intent(AdminDetailActivity.this, AdminUserDetailActivity.class);
                                                                // Pass user details to the UserDetailActivity
                                                                userDetailIntent.putExtra("userName", userName);
                                                                userDetailIntent.putExtra("userEmail", userEmail);
                                                                userDetailIntent.putExtra("userRole", userRole);
                                                                userDetailIntent.putExtra("userId", applicantId);
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
