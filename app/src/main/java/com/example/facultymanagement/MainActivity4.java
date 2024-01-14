package com.example.facultymanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity4 extends AppCompatActivity {
    private String adapterView;
    TextInputLayout FacName,LeaveType,LeaveReason,StartDate,EndDate;
    Button SubmitRequest;
    FirebaseDatabase rootnode2;
    DatabaseReference reference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        Button btn = findViewById(R.id.reqsub);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity4.this, MainActivity5.class);
                startActivity(intent);
            }
        });


        FacName=findViewById(R.id.fullname);
        LeaveType=findViewById(R.id.leaveType);
        LeaveReason=findViewById(R.id.phone);
        StartDate=findViewById(R.id.start);
        EndDate=findViewById(R.id.end);
        SubmitRequest=findViewById(R.id.reqsub);

        SubmitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaverequest();
            }
        });

    }

    public void leaverequest(){
        rootnode2=FirebaseDatabase.getInstance();
        reference2= rootnode2.getReference("Request Forms");

        String name = FacName.getEditText().getText().toString();
        String reason = LeaveReason.getEditText().getText().toString();
        String type = LeaveType.getEditText().getText().toString();
        String dateStart = StartDate.getEditText().getText().toString();
        String dateEnd = EndDate.getEditText().getText().toString();

        if(!ValidateFacultyName() | !ValidateLeaveType() | !ValidateLeaveReason() | !ValidateStart() |!ValidateEnd()){
            return;
        }else {
            LeaveRequestForm help = new LeaveRequestForm(name, reason, type, dateStart, dateEnd);
            reference2.child(name).setValue(help);
        }
    }

    private Boolean ValidateFacultyName(){
        String val=FacName.getEditText().getText().toString();

        if (val.isEmpty()){
            FacName.setError("Faculty Name cannot be Empty");
            return false;
        }
        else{
            FacName.setError(null);
            return true;
        }
    }
    private Boolean ValidateLeaveType(){
        String val=LeaveType.getEditText().getText().toString();

        if (val.isEmpty()){
            LeaveType.setError("Leave Type cannot be Empty");
            return false;
        }
        else{
            LeaveType.setError(null);
            return true;
        }
    }

    private Boolean ValidateLeaveReason(){
        String val=LeaveReason.getEditText().getText().toString();

        if (val.isEmpty()){
            LeaveReason.setError("Reason cannot be Empty");
            return false;
        }
        else{
            LeaveReason.setError(null);
            return true;
        }
    }

    private Boolean ValidateStart(){
        String val=StartDate.getEditText().getText().toString();

        if (val.isEmpty()){
            StartDate.setError("Start Date cannot be Empty");
            return false;
        }
        else{
            StartDate.setError(null);
            return true;
        }
    }

    private Boolean ValidateEnd(){
        String val=EndDate.getEditText().getText().toString();

        if (val.isEmpty()){
            EndDate.setError("End Date cannot be Empty");
            return false;
        }
        else{
            EndDate.setError(null);
            return true;
        }
    }
}