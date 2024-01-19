package com.example.facultymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.navigation_home) {
                    startActivity(new Intent(MainActivity5.this, MainActivity5.class));
                    return true;
                }

                if (itemId == R.id.navigation_dashboard) {
                    startActivity(new Intent(MainActivity5.this, MainActivity6.class));
                    return true;
                }
                return false;
            }
        });


    }

    /*@Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.navigation_home) {

            Intent intent =new Intent(MainActivity5.this, MainActivity6.class);
            startActivity(intent);
            return true;
        }

        if (itemId == R.id.navigation_dashboard) {
            // Handle dashboard click
            // Example: Start a new activity or fragment for the dashboard
            startActivity(new Intent(this, MainActivity4.class));
            return true;
        }

        if (itemId == R.id.navigation_notifications) {
            // Handle notifications click
            // Example: Start a new activity or fragment for notifications
            startActivity(new Intent(this, MainActivity6.class));
            return true;
        }

        return false;
    }*/
}