package com.example.facultymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PriorPermission extends AppCompatActivity {

    private String userUid;
    private String selectedStartDate;
    private TextInputLayout FacName, LeaveReason, StartDate, EndDate;
    private FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prior_permission);

        ImageView btnEndDate = findViewById(R.id.btnEndDate);

        btnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        database = FirebaseDatabase.getInstance();
        FacName = findViewById(R.id.fullname);
        LeaveReason = findViewById(R.id.phone);
        StartDate = findViewById(R.id.start);
        EndDate = findViewById(R.id.end);

        FirebaseAuth mAuth3 = FirebaseAuth.getInstance();
        FirebaseUser currentUser3 = mAuth3.getCurrentUser();

        if (currentUser3 != null) {
            String userEmail = currentUser3.getEmail();
            DatabaseReference usersRef = database.getReference("users");

            Query query = usersRef.orderByChild("Email").equalTo(userEmail);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String facultyName = snapshot.child("Professor Name").getValue(String.class);
                            FacName.getEditText().setText(facultyName);
                        }
                    } else {
                        Toast.makeText(PriorPermission.this, "User not found in the database", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(PriorPermission.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(PriorPermission.this, "User not logged in", Toast.LENGTH_SHORT).show();
        }

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            userUid = currentUser.getUid();

            Button submitRequestButton = findViewById(R.id.reqsub);
            submitRequestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    leaveRequest();
                }
            });
        }

        database = FirebaseDatabase.getInstance();
        ImageView btnStartDate = findViewById(R.id.btnStartDate);

        btnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
        btnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog((TextInputEditText) StartDate.getEditText());
            }
        });

        // Initialize TextInputLayouts
        FacName = findViewById(R.id.fullname);
        LeaveReason = findViewById(R.id.phone);
        StartDate = findViewById(R.id.start);
        EndDate = findViewById(R.id.end);
    }
    private void showTimePickerDialog() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Handle selected time
                        String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                        EndDate.getEditText().setText(selectedTime); // Set selected time to EndDate TextInputEditText
                    }
                },
                hour, minute, false);

        timePickerDialog.show();
    }

    private void showDatePickerDialog(final TextInputEditText dateEditText) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(year, monthOfYear, dayOfMonth);

                        if (dateEditText == StartDate.getEditText() && selectedCalendar.before(Calendar.getInstance())) {
                            Toast.makeText(PriorPermission.this, "Cannot choose past dates for Start Date", Toast.LENGTH_SHORT).show();
                        } else if (dateEditText == EndDate.getEditText() && selectedCalendar.before(Calendar.getInstance())) {
                            Toast.makeText(PriorPermission.this, "Cannot choose past dates for End Date", Toast.LENGTH_SHORT).show();
                        } else if (dateEditText == EndDate.getEditText() && selectedCalendar.equals(selectedStartDate)) {
                            Toast.makeText(PriorPermission.this, "End Date cannot be the same as Start Date", Toast.LENGTH_SHORT).show();
                        } else {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            String selectedDate = dateFormat.format(selectedCalendar.getTime());

                            dateEditText.setText(selectedDate);
                            dateEditText.setInputType(InputType.TYPE_NULL);

                            if (dateEditText == StartDate.getEditText()) {
                                selectedStartDate = selectedCalendar.getTime().toString();
                            }
                        }
                    }
                },
                year, month, day);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public void leaveRequest() {
        String name = FacName.getEditText().getText().toString();
        String reason = LeaveReason.getEditText().getText().toString();
        String dateStart = StartDate.getEditText().getText().toString();
        String dateEnd = EndDate.getEditText().getText().toString();
        String leaveType = "Prior Permission";

        if (!validateFacultyName() || !validateLeaveReason() || !validateStart() || !validateEnd()) {
            return;
        } else {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();

            if (currentUser != null) {
                String userEmail = currentUser.getEmail();
                DatabaseReference usersRef = database.getReference("users");

                Query query = usersRef.orderByChild("Email").equalTo(userEmail);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String userUid = snapshot.getKey();

                                // Update permission count
                                int currentPermissionCount = snapshot.child("PERMISSION").getValue(Integer.class);
                                if (currentPermissionCount > 0) {
                                    // Decrease permission count by 1
                                    int newPermissionCount = currentPermissionCount - 1;
                                    snapshot.getRef().child("PERMISSION").setValue(newPermissionCount);

                                    // If permission count goes below 3, decrease casual leave count by 1
                                    if (newPermissionCount < 3) {
                                        int currentCasualLeaveCount = snapshot.child("CASUAL LEAVE").getValue(Integer.class);
                                        if (currentCasualLeaveCount > 0) {
                                            // Decrease casual leave count by 1
                                            int newCasualLeaveCount = currentCasualLeaveCount - 1;
                                            snapshot.getRef().child("CASUAL LEAVE").setValue(newCasualLeaveCount);
                                        }
                                    }

                                    // Save leave request
                                    DatabaseReference leaveReqReferenceAll = database.getReference("leaveRequests");
                                    String leaveReqIdAll = leaveReqReferenceAll.push().getKey();
                                    LeaveRequestForm helpAll = new LeaveRequestForm(name, leaveType, reason, dateStart, dateEnd, "pending", userUid);
                                    leaveReqReferenceAll.child(leaveReqIdAll).setValue(helpAll);

                                    DatabaseReference leaveReqReferenceUser = database.getReference("users").child(userUid).child("requests");
                                    String leaveReqIdUser = leaveReqReferenceUser.push().getKey();
                                    LeaveRequestForm helpUser = new LeaveRequestForm(name, leaveType, reason, dateStart, dateEnd, "pending", userUid);
                                    leaveReqReferenceUser.child(leaveReqIdUser).setValue(helpUser);

                                    Toast.makeText(PriorPermission.this, "Leave Form successfully Submitted", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(PriorPermission.this, MainActivity5.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(PriorPermission.this, "You have exhausted your permission days", Toast.LENGTH_SHORT).show();
                                }
                                return;
                            }
                        } else {
                            Toast.makeText(PriorPermission.this, "User not found in the database", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(PriorPermission.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(PriorPermission.this, "User not logged in", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateFacultyName() {
        String val = FacName.getEditText().getText().toString();

        if (val.isEmpty()) {
            FacName.setError("Faculty Name cannot be Empty");
            return false;
        } else {
            FacName.setError(null);
            return true;
        }
    }

    private boolean validateLeaveReason() {
        String val = LeaveReason.getEditText().getText().toString();

        if (val.isEmpty()) {
            LeaveReason.setError("Reason cannot be Empty");
            return false;
        } else {
            LeaveReason.setError(null);
            return true;
        }
    }

    private boolean validateStart() {
        String val = StartDate.getEditText().getText().toString();

        if (val.isEmpty()) {
            StartDate.setError("Start Date cannot be Empty");
            return false;
        } else {
            StartDate.setError(null);
            return true;
        }
    }

    private boolean validateEnd() {
        String valStart = StartDate.getEditText().getText().toString();
        String valEnd = EndDate.getEditText().getText().toString();

        if (valEnd.isEmpty()) {
            EndDate.setError("End Date cannot be Empty");
            return false;
        } else if (valStart.equals(valEnd)) {
            EndDate.setError("End Date cannot be the same as Start Date");
            return false;
        } else {
            EndDate.setError(null);
            return true;
        }
    }
}