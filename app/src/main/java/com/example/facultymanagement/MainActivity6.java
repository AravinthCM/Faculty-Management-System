package com.example.facultymanagement;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity6 extends AppCompatActivity {

    TextView profileName, profileEmail, profilePhone;
    DatabaseReference usersReference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        profileName = findViewById(R.id.name);
        profileEmail = findViewById(R.id.email);
        profilePhone = findViewById(R.id.phoneNo);


        retrieveUserData();
    }

    private void retrieveUserData() {
        // Get the currently logged-in user
        Log.d("UserProfile", "Retrieving user data");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();


    }
}
