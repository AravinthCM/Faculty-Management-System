package com.example.facultymanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ListsOfLeaveActivity extends AppCompatActivity {

    CardView CASUAL;
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
    }
}