package com.example.facultymanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity4 extends AppCompatActivity {
    private String adapterView;
    TextInputLayout FacName,LeaveType,LeaveReason,StartDate,EndDate;
    Button SubmitRequest;

    ImageView imageView;
    FirebaseDatabase database;
    DatabaseReference leaveReqReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        database = FirebaseDatabase.getInstance();

        Button btn = findViewById(R.id.reqsub);
        ImageView img = findViewById(R.id.previous);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity4.this, MainActivity5.class);
                startActivity(intent);
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity4.this, MainActivity5.class);
                startActivity(intent);
            }
        });


        FacName=findViewById(R.id.fullname);
       // LeaveType=findViewById(R.id.leaveType);
        LeaveReason=findViewById(R.id.phone);
        StartDate=findViewById(R.id.start);
        EndDate=findViewById(R.id.end);
        SubmitRequest=findViewById(R.id.reqsub);

        SubmitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveRequest();
            }
        });

    }

    public void leaveRequest(){

        String name = FacName.getEditText().getText().toString();
        String reason = LeaveReason.getEditText().getText().toString();
        //String type = LeaveType.getEditText().getText().toString();
        String dateStart = StartDate.getEditText().getText().toString();
        String dateEnd = EndDate.getEditText().getText().toString();

        if(!ValidateFacultyName() | !ValidateLeaveReason() | !ValidateStart() |!ValidateEnd()){
            return;
        }else {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            leaveReqReference = database.getReference("leaveRequests");

            String leaveReqId = leaveReqReference.push().getKey();

            LeaveRequestForm help = new LeaveRequestForm(name, reason, dateStart, dateEnd, "pending");
            leaveReqReference.child(leaveReqId).setValue(help);

            Toast.makeText(MainActivity4.this,"Leave Form successfully Submitted",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(MainActivity4.this,MainActivity5.class);
            startActivity(intent);
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
    /*private Boolean ValidateLeaveType(){
        String val=LeaveType.getEditText().getText().toString();

        if (val.isEmpty()){
            LeaveType.setError("Leave Type cannot be Empty");
            return false;
        }
        else{
            LeaveType.setError(null);
            return true;
        }
    }*/

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
        String StartDateVal = "^" +
                "(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                //"(?=.*[a-zA-Z])" +      //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{10,}" +               //at least 10 characters
                "$";

        if (val.isEmpty()){
            StartDate.setError("Start Date cannot be Empty");
            return false;
        }/*else if (!val.matches(StartDateVal)) {
            StartDate.setError("Password is too weak");
            return false;
        }*/
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