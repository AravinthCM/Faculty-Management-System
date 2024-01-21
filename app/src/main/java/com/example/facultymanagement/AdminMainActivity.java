package com.example.facultymanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main_activity);

        TextView btn = findViewById(R.id.abc);
        TextView abcde = findViewById(R.id.abcde);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(AdminMainActivity.this, MainActivity4.class);
                startActivity(intent);
            }
        });
        abcde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(AdminMainActivity.this, TypesOfReqActivityActivity.class);
                startActivity(intent);
            }
        });
    }
}