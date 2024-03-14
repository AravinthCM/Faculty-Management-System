package com.example.facultymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class CasualLeaveActivity extends AppCompatActivity {

    private String userUid;
    private String selectedStartDate;
    private TextInputLayout FacName, LeaveReason, StartDate, EndDate;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.casual_leave);

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
                        Toast.makeText(CasualLeaveActivity.this, "User not found in the database", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(CasualLeaveActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(CasualLeaveActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
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
        ImageView btnEndDate = findViewById(R.id.btnEndDate);

        btnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog((TextInputEditText) StartDate.getEditText());
            }
        });

        btnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog((TextInputEditText) EndDate.getEditText());
            }
        });
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
                            Toast.makeText(CasualLeaveActivity.this, "Cannot choose past dates for Start Date", Toast.LENGTH_SHORT).show();
                        } else if (dateEditText == EndDate.getEditText() && selectedCalendar.before(Calendar.getInstance())) {
                            Toast.makeText(CasualLeaveActivity.this, "Cannot choose past dates for End Date", Toast.LENGTH_SHORT).show();
                        } else if (dateEditText == EndDate.getEditText() && selectedCalendar.equals(selectedStartDate)) {
                            Toast.makeText(CasualLeaveActivity.this, "End Date cannot be the same as Start Date", Toast.LENGTH_SHORT).show();
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
        String leaveType = "Casual Leave";

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

                                // Retrieve current Casual Leave balance from Firebase
                                int currentCasualLeaveBalance = Integer.parseInt(snapshot.child("CASUAL LEAVE").getValue(String.class));

                                // Calculate the number of days between start date and end date
                                long numberOfDays = calculateNumberOfDays(dateStart, dateEnd);

                                // Deduct the number of days from the current balance
                                int updatedCasualLeaveBalance = currentCasualLeaveBalance - (int) numberOfDays;

                                if (updatedCasualLeaveBalance < 0) {
                                    Toast.makeText(CasualLeaveActivity.this, "Insufficient Casual Leave balance", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                // Update Casual Leave balance in Firebase
                                usersRef.child(userUid).child("CASUAL LEAVE").setValue(String.valueOf(updatedCasualLeaveBalance));

                                // Proceed with submitting the leave request
                                DatabaseReference leaveReqReferenceAll = database.getReference("leaveRequests");
                                String leaveReqIdAll = leaveReqReferenceAll.push().getKey();
                                LeaveRequestForm helpAll = new LeaveRequestForm(name, leaveType, reason, dateStart, dateEnd, "pending", userUid);
                                leaveReqReferenceAll.child(leaveReqIdAll).setValue(helpAll);

                                DatabaseReference leaveReqReferenceUser = database.getReference("users").child(userUid).child("requests");
                                String leaveReqIdUser = leaveReqReferenceUser.push().getKey();
                                LeaveRequestForm helpUser = new LeaveRequestForm(name, leaveType, reason, dateStart, dateEnd, "pending", userUid);
                                leaveReqReferenceUser.child(leaveReqIdUser).setValue(helpUser);

                                Toast.makeText(CasualLeaveActivity.this, "Leave Form successfully Submitted", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CasualLeaveActivity.this, MainActivity5.class);
                                startActivity(intent);
                                return;
                            }
                        } else {
                            Toast.makeText(CasualLeaveActivity.this, "User not found in the database", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(CasualLeaveActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(CasualLeaveActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Calculate the number of days between start date and end date
    // Calculate the number of days between start date and end date (inclusive)
    private long calculateNumberOfDays(String startDate, String endDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Calendar startCalendar = Calendar.getInstance();
            Calendar endCalendar = Calendar.getInstance();
            startCalendar.setTime(sdf.parse(startDate));
            endCalendar.setTime(sdf.parse(endDate));
            long diffMillis = endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis();
            return (diffMillis / (24 * 60 * 60 * 1000)) + 1; // Add 1 to include both start and end dates
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
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
