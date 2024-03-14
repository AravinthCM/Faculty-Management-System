package com.example.facultymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CasualLeaveActivity extends AppCompatActivity {

    private TextInputLayout FacName, LeaveReason, StartDate, EndDate, SubstitutionStaff, SubjectClassHour;
    private FirebaseDatabase database;
    private String selectedStartDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.casual_leave);

        // Initialize views
        FacName = findViewById(R.id.fullname);
        LeaveReason = findViewById(R.id.phone);
        StartDate = findViewById(R.id.start);
        EndDate = findViewById(R.id.end);
        SubstitutionStaff = findViewById(R.id.Substitutionstaff);
        SubjectClassHour = findViewById(R.id.subclasshour);

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();

        // Fetch current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            // Fetch user details
            fetchUserDetails(currentUser);

            // Set onClickListeners
            setOnClickListeners();
        } else {
            Toast.makeText(CasualLeaveActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchUserDetails(FirebaseUser currentUser) {
        String userEmail = currentUser.getEmail();
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
    }

    private void setOnClickListeners() {
        // Set onClickListeners for date pickers
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


        // Set onClickListener for submit button
        Button submitRequestButton = findViewById(R.id.reqsub);
        submitRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveRequest();
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

    private void leaveRequest() {
        String name = FacName.getEditText().getText().toString();
        String reason = LeaveReason.getEditText().getText().toString();
        String dateStart = StartDate.getEditText().getText().toString();
        String dateEnd = EndDate.getEditText().getText().toString();
        String substitutionStaff = SubstitutionStaff.getEditText().getText().toString();
        String subjectClassHour = SubjectClassHour.getEditText().getText().toString();
        String leaveType = "Casual Leave";

        if (!validateFacultyName() || !validateLeaveReason() || !validateStart() || !validateEnd() || !validateSubstitutionStaff() || !validateSubjectClassHour()) {
            return;
        } else {
            String submissionDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime());

            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                String userUid = currentUser.getUid();
                String userEmail = currentUser.getEmail();
                DatabaseReference usersRef = database.getReference("users");

                Query query = usersRef.orderByChild("Email").equalTo(userEmail);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String userUid = snapshot.getKey();

                                // Calculate number of leave days
                                long leaveDays = calculateLeaveDays(dateStart, dateEnd);

                                // Fetch current leave balance
                                DatabaseReference casualLeaveRef = database.getReference("users").child(userUid).child("CASUAL LEAVE");
                                casualLeaveRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            String leaveBalanceString = dataSnapshot.getValue(String.class);
                                            if (leaveBalanceString != null) {
                                                long currentLeaveBalance = Long.parseLong(leaveBalanceString);
                                                long updatedLeaveBalance = currentLeaveBalance - leaveDays;
                                                if (updatedLeaveBalance < 0) {
                                                    Toast.makeText(CasualLeaveActivity.this, "Insufficient leave balance", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }

                                                // Update leave balance
                                                casualLeaveRef.setValue(String.valueOf(updatedLeaveBalance));

                                                // Proceed to submit leave request
                                                DatabaseReference leaveReqReferenceAll = database.getReference("leaveRequests");
                                                String leaveReqIdAll = leaveReqReferenceAll.push().getKey();
                                                CasualLeaveFormModel helpAll = new CasualLeaveFormModel(name, leaveType, reason, dateStart, dateEnd, substitutionStaff, subjectClassHour, "Pending", submissionDate, userUid);
                                                leaveReqReferenceAll.child(leaveReqIdAll).setValue(helpAll);

                                                DatabaseReference leaveReqReferenceUser = database.getReference("users").child(userUid).child("requests");
                                                String leaveReqIdUser = leaveReqReferenceUser.push().getKey();
                                                CasualLeaveFormModel helpUser = new CasualLeaveFormModel(name, leaveType, reason, dateStart, dateEnd, substitutionStaff, subjectClassHour, "Pending", submissionDate, userUid);
                                                leaveReqReferenceUser.child(leaveReqIdUser).setValue(helpUser);

                                                Toast.makeText(CasualLeaveActivity.this, "Leave Form successfully Submitted", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(CasualLeaveActivity.this, MainActivity5.class);
                                                startActivity(intent);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(CasualLeaveActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
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

    private long calculateLeaveDays(String startDate, String endDate) {
        // Parse start and end dates
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date dateStart, dateEnd;
        try {
            dateStart = sdf.parse(startDate);
            dateEnd = sdf.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }

        // Calculate number of days between start and end dates
        long diff = dateEnd.getTime() - dateStart.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1; // Adding 1 to include the end date
    }


    private boolean validateSubstitutionStaff() {
        String val = SubstitutionStaff.getEditText().getText().toString();

        if (val.isEmpty()) {
            SubstitutionStaff.setError("Substitution Staff cannot be empty");
            return false;
        } else {
            SubstitutionStaff.setError(null);
            return true;
        }
    }

    // Method to validate Subject and Class Hour field
    private boolean validateSubjectClassHour() {
        String val = SubjectClassHour.getEditText().getText().toString();

        if (val.isEmpty()) {
            SubjectClassHour.setError("Subject and Class Hour cannot be empty");
            return false;
        } else {
            SubjectClassHour.setError(null);
            return true;
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

