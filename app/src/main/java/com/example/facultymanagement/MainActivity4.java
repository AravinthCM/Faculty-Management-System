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
import com.google.firebase.database.Query;
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
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();

            if (currentUser != null) {
                String userEmail = currentUser.getEmail();
                DatabaseReference usersRef = database.getReference("users");

                Query query = usersRef.orderByChild("email").equalTo(userEmail);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String userUid = snapshot.getKey();

                                DatabaseReference currentUserRef = database.getReference("users").child(userUid);
                                DatabaseReference leaveReqReferenceUser = currentUserRef.child("requests");
                                String leaveReqIdUser = leaveReqReferenceUser.push().getKey();

                                LeaveRequestForm helpUser = new LeaveRequestForm(name, leaveType, reason, dateStart, dateEnd, "pending", userUid);
                                leaveReqReferenceUser.child(leaveReqIdUser).setValue(helpUser);

                                Toast.makeText(MainActivity4.this, "Leave Form successfully Submitted", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity4.this, MainActivity5.class);
                                startActivity(intent);
                                return;
                            }
                        } else {
                            Toast.makeText(MainActivity4.this, "User not found in the database", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle database error
                        Toast.makeText(MainActivity4.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(MainActivity4.this, "User not logged in", Toast.LENGTH_SHORT).show();
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
