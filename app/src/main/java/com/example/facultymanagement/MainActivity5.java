package com.example.facultymanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity5 extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main5);

        TextView btn = findViewById(R.id.abc);
        TextView abcde = findViewById(R.id.abcde);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity5.this, MainActivity4.class);
                startActivity(intent);
            }
        });

        abcde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity5.this, RetrievingLeaveForm8.class);
                startActivity(intent);
            }
        });

        ImageButton btn1 = findViewById(R.id.menuxml7);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity5.this, MainActivity3.class);
                startActivity(intent);
            }
        });

        ImageButton profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity5.this, MainActivity6.class);
                startActivity(intent);
            }
        });


    }
}