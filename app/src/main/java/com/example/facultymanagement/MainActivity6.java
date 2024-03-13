package com.example.facultymanagement;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity6 extends AppCompatActivity {

    private TextView profileName, profileEmail, profileUid, cas, earn, medic, vel, onduty, specialonduty;
    private FirebaseAuth mAuth;
    private Query mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profileUid = findViewById(R.id.profileUid);
        cas = findViewById(R.id.cas);
        earn = findViewById(R.id.earn);
        medic = findViewById(R.id.medic);
        vel = findViewById(R.id.vel);
        onduty = findViewById(R.id.onduty);
        specialonduty = findViewById(R.id.specialonduty);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userEmail = currentUser.getEmail();

        mQuery = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("Email").equalTo(userEmail).limitToFirst(1);
        mQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String name = userSnapshot.child("Professor Name").getValue(String.class);
                        String email = userSnapshot.child("Email").getValue(String.class);
                        String uid = userSnapshot.child("User Id").getValue(String.class);
                        String casualLeave = userSnapshot.child("CASUAL LEAVE").getValue(String.class);
                        String earnedLeave = userSnapshot.child("EARNED LEAVE").getValue(String.class);
                        String medicalLeave = userSnapshot.child("MEDICAL LEAVE").getValue(String.class);
                        String velLeave = userSnapshot.child("VEL").getValue(String.class);
                        String onDuty = userSnapshot.child("ON DUTY").getValue(String.class);
                        String specialOnDuty = userSnapshot.child("SPECIAL ON DUTY").getValue(String.class);

                        profileName.setText(name);
                        profileEmail.setText(email);
                        profileUid.setText(uid);
                        cas.setText(casualLeave);
                        earn.setText(earnedLeave);
                        medic.setText(medicalLeave);
                        vel.setText(velLeave);
                        onduty.setText(onDuty);
                        specialonduty.setText(specialOnDuty);
                    }
                } else {
                    Toast.makeText(MainActivity6.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity6.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}