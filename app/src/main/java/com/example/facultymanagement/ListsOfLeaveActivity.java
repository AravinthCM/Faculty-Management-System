package com.example.facultymanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ListsOfLeaveActivity extends AppCompatActivity {

    CardView CASUAL,MEDICAL,cardViewEARNEDLeave,onduty,prior;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lists_of_leave);

        CASUAL=findViewById(R.id.CASUAL);
        CASUAL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ListsOfLeaveActivity.this, CasualLeaveActivity.class);
                startActivity(intent);
            }
        });

        MEDICAL=findViewById(R.id.MEDICAL);
        MEDICAL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ListsOfLeaveActivity.this, MedicalLeave.class);
                startActivity(intent);
            }
        });

        cardViewEARNEDLeave=findViewById(R.id.cardViewEARNEDLeave);
        cardViewEARNEDLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ListsOfLeaveActivity.this, EarnedLeave.class);
                startActivity(intent);
            }
        });

        onduty=findViewById(R.id.onduty);
        onduty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ListsOfLeaveActivity.this, OnDutyActivity.class);
                startActivity(intent);
            }
        });

        prior=findViewById(R.id.prior);
        prior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ListsOfLeaveActivity.this, PriorPermission.class);
                startActivity(intent);
            }
        });
    }
}