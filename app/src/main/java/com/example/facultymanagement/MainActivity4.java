package com.example.facultymanagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity4 extends AppCompatActivity {
    private String userUid;
    private String selectedStartDate;
    private TextInputLayout FacName, LeaveReason, StartDate, EndDate;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        FacName = findViewById(R.id.fullname);
        LeaveReason = findViewById(R.id.phone);
        StartDate = findViewById(R.id.start);
        EndDate = findViewById(R.id.end);

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

        Spinner leaveTypeSpinner = findViewById(R.id.spinnerLeaveType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.leave_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leaveTypeSpinner.setAdapter(adapter);
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
                            Toast.makeText(MainActivity4.this, "Cannot choose past dates for Start Date", Toast.LENGTH_SHORT).show();
                        } else if (dateEditText == EndDate.getEditText() && selectedCalendar.before(Calendar.getInstance())) {
                            Toast.makeText(MainActivity4.this, "Cannot choose past dates for End Date", Toast.LENGTH_SHORT).show();
                        } else if (dateEditText == EndDate.getEditText() && selectedCalendar.equals(selectedStartDate)) {
                            Toast.makeText(MainActivity4.this, "End Date cannot be the same as Start Date", Toast.LENGTH_SHORT).show();
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
        Spinner leaveTypeSpinner = findViewById(R.id.spinnerLeaveType);
        String name = FacName.getEditText().getText().toString();
        String reason = LeaveReason.getEditText().getText().toString();
        String dateStart = StartDate.getEditText().getText().toString();
        String dateEnd = EndDate.getEditText().getText().toString();
        String leaveType = leaveTypeSpinner.getSelectedItem().toString();

        if (!validateFacultyName() || !validateLeaveReason() || !validateStart() || !validateEnd()) {
            return;
        } else {
            // Deduct leave days from the respective category
            deductLeaveDays(leaveType, dateStart, dateEnd);

            // Update Firebase Database
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference leaveReqReferenceAll = database.getReference("leaveRequests");
            String leaveReqIdAll = leaveReqReferenceAll.push().getKey();
            LeaveRequestForm helpAll = new LeaveRequestForm(name, leaveType, reason, dateStart, dateEnd, "pending", userUid);
            leaveReqReferenceAll.child(leaveReqIdAll).setValue(helpAll);

            DatabaseReference leaveReqReferenceUser = database.getReference("users").child(userUid).child("requests");
            String leaveReqIdUser = leaveReqReferenceUser.push().getKey();
            LeaveRequestForm helpUser = new LeaveRequestForm(name, leaveType, reason, dateStart, dateEnd, "pending", userUid);
            leaveReqReferenceUser.child(leaveReqIdUser).setValue(helpUser);

            Toast.makeText(MainActivity4.this, "Leave Form successfully Submitted", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity4.this, MainActivity5.class);
            startActivity(intent);
        }
    }

    private void deductLeaveDays(String leaveType, String dateStart, String dateEnd) {
        // Retrieve the user's leave days data from the Firebase Database
        DatabaseReference leaveDaysReference = database.getReference("users").child(userUid).child("leaveDays");
        leaveDaysReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get the current leave days for the specified leave type
                    int currentLeaveDays = dataSnapshot.child(leaveType).getValue(Integer.class);

                    // Calculate the number of days between start and end dates
                    long daysDifference = calculateDaysDifference(dateStart, dateEnd);

                    // Deduct the leave days
                    currentLeaveDays -= daysDifference;

                    // Update the leave days in the database
                    leaveDaysReference.child(leaveType).setValue(currentLeaveDays);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors if needed
            }
        });
    }

    private long calculateDaysDifference(String dateStart, String dateEnd) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date startDate = sdf.parse(dateStart);
            Date endDate = sdf.parse(dateEnd);

            // Calculate the difference in days
            long diff = endDate.getTime() - startDate.getTime();
            return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1; // Include the end date
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
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
        String val = EndDate.getEditText().getText().toString();

        if (val.isEmpty()) {
            EndDate.setError("End Date cannot be Empty");
            return false;
        } else {
            EndDate.setError(null);
            return true;
        }
    }
}
